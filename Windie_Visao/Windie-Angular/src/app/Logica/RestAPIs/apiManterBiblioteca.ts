import { DatePipe } from '@angular/common';
import { HttpClient} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestObject } from 'src/app/Modelos/RestObject';
import { Debug } from '../Debug';
import { ApiAutenticacaoService } from './api-autenticacao.service';
import { saveAs } from "file-saver";
import { map,} from 'rxjs/operators';
import { createWriteStream } from 'streamsaver';
import { ApiManterCatalogo } from './apiManterCatalogo';



@Injectable({
  providedIn: 'root'
})
export class ApiManterBiblioteca {
  
  url : string = 'http://localhost:4200/api/biblioteca';
  downloads :[Download] = [{jogo_id:-1,progresso:0,seConcluido:true,state:''}];

  
  downloadAux : Download|null = null;
  porcentagem : number = -1;
  
  constructor(private http: HttpClient,private autentica : ApiAutenticacaoService,private catalogoApi : ApiManterCatalogo) {
    
    Ler_gravarHoras(this).then((data) => { console.log('escutando horas..')});


  }

  public listarJogos(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    Debug.logOutput(JSON.stringify(rest));
    return this.http.post<RestObject>(this.url+"/jogos",rest);
  }

  public gravarHoras(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    Debug.logOutput(JSON.stringify(rest));
    return this.http.post<RestObject>(this.url+"/horas",rest);
  }

  // public baixarArquivos(body:any,jogo_id:number)/*:Observable<HttpEvent<RestObject>>*/{

    

  //  let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
  //  /* return */this.http.post(this.url+"/download",rest,{ responseType: 'blob' as 'json'}).subscribe( retorno =>{
  //    Debug.logDetalhe('resposta: '+JSON.stringify(retorno));
  //       var response = retorno;
  //       let dataType = 'application/x-zip-compressed';
  //           let binaryData : any = [];
  //           binaryData.push(response);
  //           let downloadLink = document.createElement('a');
  //           downloadLink.href = window.URL.createObjectURL(new Blob(binaryData, {type: dataType}));
  //           if ('arquivos_jogo_'+jogo_id+'.zip')
  //               downloadLink.setAttribute('download', 'arquivos_jogo_'+jogo_id+'.zip');
  //           document.body.appendChild(downloadLink);
  //           downloadLink.click();
  //   },erro =>{
     
  //   });
  // }
    

  public async baixarArquivos(body:any,jogo_id:number,exec_caminho:string){ // stream
    
    var url = this.url+'/download?token='+this.autentica.getToken()+'&jogo_id='+jogo_id;
    var fileName = 'arquivos_jogo_'+jogo_id+'.zip';
    Debug.logDetalhe('ponto 1');
    this.atualizarDownloadStatus(jogo_id,exec_caminho);
    Debug.logDetalhe('ponto 2');
    return fetch(url).then(res => {
      Debug.logDetalhe('ponto 3');
      const contentLength = res.headers.get('Content-Length')!;
      sessionStorage.setItem('bytesTotal_jogo_'+jogo_id, contentLength);
      const fileStream = createWriteStream(fileName);
      const writer = fileStream.getWriter();
      if (res.body?.pipeTo) {
        Debug.logDetalhe('ponto 4');
        writer.releaseLock();
        return res.body!.pipeTo(fileStream);
      }
      Debug.logDetalhe('ponto 5');
      const reader = res.body!.getReader();
      const pump : any = () => Debug.logDetalhe('ponto 6');
        reader
          .read()
          .then(({ value, done }) => (done ? writer.close() : writer.write(value).then(pump)));
      Debug.logDetalhe('ponto 7');
      return pump();
    });
    
    // Step 1: start the fetch and obtain a reader
      // let response = await fetch(url);

      // const reader = response.body!.getReader();

      // // Step 2: get total length
      // const contentLength = +response.headers.get('Content-Length')!;

      // // Step 3: read the data
      // let receivedLength = 0; 
      // while(true) {
      //   const {done, value} = await reader.read();

      //   if (done) {
      //     break;
      //   }

      //   receivedLength += value!.length;

      //   const fileStream = createWriteStream(fileName);
      //   const writer = fileStream.getWriter();


      //   const pump : any = () => reader.read().then(({ value, done }) => (done ?  this.registrarDownloadFim(jogo_id,writer)/*writer.close()*/ : writer.write(value).then(pump), receivedLength += value!.length,this.registrarDownloadProgresso(jogo_id,receivedLength,contentLength)//,console.log(`Received ${receivedLength} of ${contentLength}`)
      //   ));
      //   return pump();



      // }



   }

   async atualizarDownloadStatus(jogo_id:number,exec_caminho:string){
    Debug.logDetalhe('scanneando atualização no download status');
     
     while(true){
      await new Promise(f => setTimeout(f, 1000));
      //Debug.logDetalhe('download status scan...');
      if(sessionStorage.getItem('baixandoStatus_jogo_'+jogo_id) == undefined || sessionStorage.getItem('baixandoStatus_jogo_'+jogo_id)!='progressing'){
        if(sessionStorage.getItem('baixandoStatus_jogo_'+jogo_id)=='completed'){
          this.catalogoApi.seNaBiblioteca({jogo_id : jogo_id}).subscribe(
            success=>{ 
              
              if(!JSON.parse(success.body)){//se não estiver na biblioteca
                this.catalogoApi.adicionarBiblioteca({jogo_id : jogo_id}).subscribe(
                  retorno => {
                    this.autentica.setToken(retorno.token!); 
                  },
                  erro => {
            
                  }
                );
              } 

            },err=>{
  
            });

          this.registrarDownloadFim(jogo_id);
          sessionStorage.removeItem('baixandoStatus_jogo_'+jogo_id);
          sessionStorage.removeItem('bytesRecebidos_jogo_'+jogo_id);
          sessionStorage.removeItem('bytesTotal_jogo_'+jogo_id);
          sessionStorage.setItem('caminhoInst_jogo_'+jogo_id,sessionStorage.getItem('caminhoInst_jogo_'+jogo_id)+exec_caminho);
          Debug.logDetalhe('download e instalação concluidos!');
          break;
        }
        if(sessionStorage.getItem('baixandoStatus_jogo_'+jogo_id)=='installing'){
          
        }else{ // interrompido ou cancelado
          this.registrarDownloadFim(jogo_id);
          Debug.logDetalhe('download interrompido ou cancelado');
          break;
        }
      }

      this.registrarDownloadProgresso(jogo_id,Number(sessionStorage.getItem('bytesRecebidos_jogo_'+jogo_id)),Number(sessionStorage.getItem('bytesTotal_jogo_'+jogo_id)),sessionStorage.getItem('baixandoStatus_jogo_'+jogo_id)!);

      
     }
   }

  private registrarDownloadProgresso(jogo_id:number,bytesRecebidos:number,bytesTotal:number, state :string){
    this.downloadAux  = this.getDownload(jogo_id);
    if(this.downloadAux == null){
      this.downloadAux = this.criarDownload(jogo_id);
    }

    this.porcentagem = (100*bytesRecebidos)/bytesTotal;
    //this.downloads[this.downloads.indexOf(download)].progresso = porcentagem;
    //Debug.logDetalhe('download: '+JSON.stringify(download)+'this.download: '+JSON.stringify(this.downloads));
    if(this.downloadAux != null){
     this.downloads.find(x => x.jogo_id == this.downloadAux!.jogo_id)!.progresso =  this.porcentagem;
     this.downloads.find(x => x.jogo_id == this.downloadAux!.jogo_id)!.state =  state;
    }
 
  }
  public resetDownload(jogo_id:number){
    this.downloadAux  = this.getDownload(jogo_id);
    if(this.downloadAux == null){
      return;
    }else{
      this.downloads.splice(this.downloads.indexOf(this.downloads.find(x => x.jogo_id == this.downloadAux!.jogo_id)!));
    }
  }

  private registrarDownloadFim(jogo_id:number/*,writer: WritableStreamDefaultWriter<any>*/){
    //writer.close();
    this.downloadAux  = this.getDownload(jogo_id);
    this.downloadAux!.seConcluido = true;
  }

  private criarDownload(jogo_id:number): Download{
    Debug.logDetalhe('criando download object');
    let novo : Download = {jogo_id:jogo_id,progresso:0,seConcluido:false,state:'progressing'};
    this.downloads.push(novo);
    return novo;
  }

  public getDownload(jogo_id:number): Download|null{
    this.downloadAux = null; 
    this.downloads.forEach(element => {
      if(element.jogo_id==jogo_id){
        this.downloadAux = element;
      }  
    });

    return this.downloadAux;
  }

  public initInstalacaoMetadados(caminho_executavel:string,jogo_id:number){ //adiciona o caminho do executavel quando o usuario abre a pagina de detalhes de um jogo que está com o caminho incompleto
    if(sessionStorage.getItem('caminhoInst_jogo_'+jogo_id)!=undefined&&sessionStorage.getItem('caminhoInst_jogo_'+jogo_id)!=null){
      let ultimo_caractere = sessionStorage.getItem('caminhoInst_jogo_'+jogo_id)!.charAt(sessionStorage.getItem('caminhoInst_jogo_'+jogo_id)!.length-1);
      if(ultimo_caractere=='/'||ultimo_caractere=='\\'){
        sessionStorage.setItem('caminhoInst_jogo_'+jogo_id,sessionStorage.getItem('caminhoInst_jogo_'+jogo_id)+caminho_executavel);
      }

    }
  }

}

 async function Ler_gravarHoras(thiss: ApiManterBiblioteca){ //procura por horas jogadas pendentes para serem adicionadas
  while(true){
    await new Promise(f => setTimeout(f, 5000));
      if(localStorage.getItem('horas_pendentes')!=null&&localStorage.getItem('horas_pendentes')!=undefined){
        Debug.logDetalhe('gravando horas: '+localStorage.getItem('horas_pendentes'));
        let jogo_id = JSON.parse(localStorage.getItem('horas_pendentes')!).jogo_id;
        let horas = JSON.parse(localStorage.getItem('horas_pendentes')!).tempo /60 /60;
        thiss.gravarHoras({jogo_id:jogo_id,horas:horas}).subscribe(
          retorno =>{
            localStorage.removeItem('horas_pendentes');
          }, erro =>{

          }
        );
      }
  }
 }

export class Download{
  public constructor(
    public jogo_id:number,
    public progresso:number,
    public seConcluido:boolean,
    public state: string
  ){

  }
}
