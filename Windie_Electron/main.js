// Modules to control application life and create native browser window
const {app, BrowserWindow} = require('electron')
const path = require('path');
const { listenerCount } = require('process');
const windieLibW = 'F:\\WindieLibrary';
const LibTempW = windieLibW+ '\\temp';
const LibBinW = windieLibW+ '\\bin';
const windieLib = 'F:/WindieLibrary';
const LibTemp = windieLib+ '/temp';
const LibBin = windieLib+ '/bin';
const angularURL = 'http://localhost:4200';

var timer_counter = 0; //timer em segundos
var jogo_aberto_id = -1;
var jogo_aberto_time = -1; 


function createWindow () {
  // Create the browser window.
  const mainWindow = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.js')
    }
  });
  mainWindow.maximize();
  mainWindow.webContents
  .executeJavaScript('localStorage.setItem(\'plataforma\',\'desktop\');', true)
  .then(result => {
    console.log(result);
  });//seta a plataforma como desktop no local storage
  //unzip();
  findInstalledGames(mainWindow);
  listen(mainWindow).then((data) => { console.log('escutando..')});
  timer().then((data) => { console.log('timer rodando..')});
  // and load the index.html of the app.
 // mainWindow.loadFile('index.html')
  mainWindow.loadURL(angularURL);
  // Open the DevTools.
  // mainWindow.webContents.openDevTools()
  //const session = mainWindow.webContents.session;
  const { session } = require('electron')
  //mainWindow.webContents.session.setDownloadPath('F:/WindieLibrary');
  mainWindow.webContents.session.on('will-download', (event, item, webContents) =>{ //acionado no ato de download
    //event.preventDefault();
    item.setSavePath(LibTempW+'\\'+item.getFilename());
    item.on('updated',(event, state) =>{
     // console.log(`Received bytes: ${item.getReceivedBytes()}`);
     var jogo_id = item.getFilename().substring(
        item.getFilename().lastIndexOf("_") + 1, 
        item.getFilename().lastIndexOf(".")
      );
      updateDownloadStatus(jogo_id,item.getReceivedBytes(),state,mainWindow);

    })
    
    item.once('done', (event, state) => {
      var jogo_id = item.getFilename().substring(
        item.getFilename().lastIndexOf("_") + 1, 
        item.getFilename().lastIndexOf(".")
      );
      
      if (state === 'completed') {
        console.log('Download successfully')
        updateDownloadStatus(jogo_id,item.getReceivedBytes(),'installing',mainWindow);
        unzip(LibTemp+'/'+item.getFilename(),LibBin+'/'+jogo_id,jogo_id,mainWindow);
      } else {
        console.log(`Download failed: ${state}`)
        updateDownloadStatus(jogo_id,item.getReceivedBytes(),state,mainWindow);
      }
    })

    console.log('will-download : '+item.getState());
  });
  
}

function unzip(zipPath,extractionPath,jogo_id,mainWindow){ //descompacta um arquivo de instalação para uma pasta com o numero do jogo
    // console.log('extraindo: '+zipPath+' para... '+extractionPath);
    // //await sleep(3000);
    // console.log('extraindo...');
    const StreamZip = require('node-stream-zip');
    const zip = new StreamZip({ file: zipPath });
    console.log('extraindo: '+JSON.stringify(jogo_id));
    console.log('extraindo: '+JSON.stringify(zipPath));
    zip.on('ready', () => {
      //fs.mkdirSync('extracted');
      zip.extract(null, extractionPath, (err/*, count*/) => {
          console.log(err ? 'Extract error' : `Extracted entries`);
          zip.close();
          if(err) {
            concluirInstalacao(jogo_id,false,mainWindow,zipPath);
          }else{
            concluirInstalacao(jogo_id,true,mainWindow,zipPath);
          }

      });
    });
}

function concluirInstalacao(jogo_id,sucesso,mainWindow,arquivo_temp_path){ //escreve metadados da instalação (caminho da biblioteca), seta download como completo e exclui o arquivo temporario de download 
  const fs = require('fs');
  fs.unlink(arquivo_temp_path, (err) => { // remove o arquivo temporario em zip
    if (err) {
      console.error(err)
      return
    }
  });


  if(sucesso){
    mainWindow.webContents.executeJavaScript('sessionStorage.setItem(\'baixandoStatus_jogo_'+jogo_id+'\',\'completed\');');
    mainWindow.webContents.executeJavaScript('sessionStorage.setItem(\'caminhoInst_jogo_'+jogo_id+'\',\''+LibBin+'/'+jogo_id+'/\');');
  }else{
    findInstalledGames(mainWindow);
    mainWindow.webContents.executeJavaScript('sessionStorage.setItem(\'baixandoStatus_jogo_'+jogo_id+'\',\'cancelled\');');
  }
  console.log('jogo extraido');
}

function excluirJogo(jogo_id,mainWindow){

  var path = LibBin+'/'+jogo_id;
  deleteFolder(path);

  //mainWindow.webContents.executeJavaScript('sessionStorage.setItem(\'bytesRecebidos_jogo_'+jogo_id+'\',\''+0+'\');');
  var remove_comand = 'sessionStorage.removeItem(\'caminhoInst_jogo_'+jogo_id+'\');';
  console.log(remove_comand);
  mainWindow.webContents.executeJavaScript(remove_comand ); //remove da lista de jogos baixados

}

function deleteFolder (path) { // remove jogo
  const fs = require('fs');
  if( fs.existsSync(path) ) {
      fs.readdirSync(path).forEach(function(file) {
        var curPath = path + "/" + file;
          if(fs.lstatSync(curPath).isDirectory()) { // recurse
              deleteFolder(curPath);
          } else { // delete file
              fs.unlinkSync(curPath);
          }
      });
      fs.rmdirSync(path);
    }
};

function updateDownloadStatus(jogo_id,received_bytes,state,mainWindow){ //atualiza o status de download na session storage
  mainWindow.webContents.executeJavaScript('sessionStorage.setItem(\'baixandoStatus_jogo_'+jogo_id+'\',\''+state+'\');');
  // mainWindow.webContents.executeJavaScript('sessionStorage.setItem(\'bytesTotal_jogo_'+jogo_id+'\',\''+item.getTotalBytes()+'\');');
   mainWindow.webContents.executeJavaScript('sessionStorage.setItem(\'bytesRecebidos_jogo_'+jogo_id+'\',\''+received_bytes+'\');');
}

function findInstalledGames(mainWindow){ //busca na biblioteca por jogos instalados e registra os metaDados na session storage
  const fs = require('fs');
  console.log('diretorios na biblioteca');
  fs.readdir(LibBin , { withFileTypes: true }, (error, files) => {
    console.log('files: '+files);
    const directoriesInDIrectory = files
        .filter((item) => item.isDirectory())
        .map((item) => item.name);
    
    directoriesInDIrectory.forEach(element => {
      console.log('element: '+element);
      mainWindow.webContents.executeJavaScript('sessionStorage.setItem(\'caminhoInst_jogo_'+element+'\',\''+LibBin+'/'+element+'/\');');
    });

    console.log('diretorios na biblioteca'+directoriesInDIrectory);
});
}

function getReadyPath(path,separador_desejado){ //retorna um path de separadores mistos com o separador desejado
 let path_ready =  path.replace('/',separador_desejado);
 path_ready =  path.replace('\\',separador_desejado);

 return path_ready;

}


function abrirJogo(caminho,jogo_id,mainWindow){
  var exec = require('child_process').execFile;

  var fun =function(){
    console.log("abriu...");
    jogo_aberto_id = jogo_id;
    console.log('jai: '+jogo_aberto_id+' jat: '+jogo_aberto_time+' tc: '+timer_counter);
    jogo_aberto_time = timer_counter;
    exec(caminho, function(err, data) {  
          //if(err){
            console.log('fechou')
            var tempoDecorrido = timer_counter -  jogo_aberto_time;
            mainWindow.webContents.executeJavaScript('localStorage.setItem(\'horas_pendentes\',\'{"jogo_id": "'+ jogo_aberto_id+'" ,"tempo": "'+tempoDecorrido+'"}\');');
            jogo_aberto_id = -1;
            ogo_aberto_time = -1;
            console.log(err)
         // }
          console.log(data.toString());                       
      });  
  }
  fun();
}

async function listen(mainWindow){ //escuta "eventos" no session storage
  while(true){
    await new Promise(f => setTimeout(f, 3000));  //delay para não prejudicar performance
    let jsonListEvents ;
    await mainWindow.webContents.executeJavaScript('sessionStorage.getItem(\'eventos\');').then((result) =>{
      jsonListEvents = result//JSON.stringify(result);
    });
    // console.log('eventos: '+jsonListEvents);
    //var array = [];
    var array = JSON.parse(jsonListEvents);
    // console.log('array: '+array);
    if(array != null){
    // for (var bit in array.bit) {
    //   for (var key in array.bit[bit]) {
    //     if (array.bit[bit].hasOwnProperty(key)) {
    //       console.log(key + " -> " + array.bit[bit][key]);
    //     }
    //   }
    // }
      array.forEach(function(evento) {
        if(evento.evento_tipo == 'jogar'){
          console.log('evento: '+JSON.stringify(evento));
          var jogo_id = evento.evento_corpo.jogo_id;
          var path;
          mainWindow.webContents.executeJavaScript('sessionStorage.getItem(\'caminhoInst_jogo_'+jogo_id+'\');').then((result) =>{
            path = result//JSON.stringify(result);
            abrirJogo(getReadyPath(path,'/'),jogo_id, mainWindow);
            console.log('abrindo... '+getReadyPath(path,'/'))
          },(err)=>{});
          
          //console.log('abriu... ')
        }
        if(evento.evento_tipo == 'excluir'){
          console.log('evento: '+JSON.stringify(evento));
          var jogo_id = evento.evento_corpo.jogo_id;
          excluirJogo(jogo_id,mainWindow);
        }
      });
      console.log('limpar eventos');
      mainWindow.webContents.executeJavaScript('sessionStorage.removeItem(\'eventos\')');
    }


    // for(var i in JSON.parse(jsonListEvents)){
    //   console.log('[forEach]: ',  [i, jsonListEvents [i]]);
    // }
    // if(array !=null){
    //   array.map(function(nome, i)  {
    //     console.log('[forEach]', nome+' | '+ i);
    //     // if(element != 'null' && JSON.parse(element).evento_tipo =='jogar' ){
    //     //   console.log('abrindo jogo: '+JSON.parse(element).evento_corpo.jogo_id);
    //     // }
    //   });
    // }

  }

}

async function timer(){
  while(true){
    await new Promise(f => setTimeout(f, 1000));
    timer_counter = timer_counter +1;
  }

}
// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.whenReady().then(() => {
  createWindow();
  app.on('activate', function () {
    // On macOS it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (BrowserWindow.getAllWindows().length === 0) createWindow()
  })
})

// Quit when all windows are closed, except on macOS. There, it's common
// for applications and their menu bar to stay active until the user quits
// explicitly with Cmd + Q.
app.on('window-all-closed', function () {
  if (process.platform !== 'darwin') app.quit()
})





// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
