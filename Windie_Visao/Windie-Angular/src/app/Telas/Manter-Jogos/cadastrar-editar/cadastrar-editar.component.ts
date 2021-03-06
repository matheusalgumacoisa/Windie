import { HttpEventType } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Debug } from 'src/app/Logica/Debug';
import { ApiAutenticacaoService } from 'src/app/Logica/RestAPIs/api-autenticacao.service';
import { ApiManterCatalogo } from 'src/app/Logica/RestAPIs/apiManterCatalogo';
import { ApiManterJogos } from 'src/app/Logica/RestAPIs/apiManterJogos';
import { Jogo } from 'src/app/Modelos/Jogo';
import { ListaIdString } from 'src/app/Modelos/ListaIdString';
import { RestObject } from 'src/app/Modelos/RestObject';


@Component({
  selector: 'app-cadastrar',
  templateUrl: './cadastrar-editar.component.html',
  styleUrls: ['./cadastrar-editar.component.css']
})
export class CadastrarEditarComponent implements OnInit {

  form_cadastrar: FormGroup;
  capaFile : string = '';
  screenshotFiles: string[] = [];
  generos: ListaIdString[] = [];
  jogoEditar!: Jogo;
  erro: string = '';
  enviando: boolean = false;

  //=============arquivos=========
  fileName = '';
  event : any;
  file!: File;
  envioProgresso:number = -1;

  editando_arquivo:string = '';

  jogo_id : number = -1;

   constructor( private sanitizer: DomSanitizer,private apiManterJogos : ApiManterJogos,private router: Router ,private route: ActivatedRoute, private catalogoService : ApiManterCatalogo, private autenticacao : ApiAutenticacaoService) {
    this.form_cadastrar = new FormGroup({
      titulo: new FormControl(null,Validators.required),
      tags: new FormControl(null,Validators.required),
      descricao: new FormControl(null,Validators.required),
      executavel: new FormControl(null,Validators.required),
      detalhes: new FormControl(null,Validators.required),
      trailer: new FormControl(null),
      genero: new FormControl(null,Validators.required),
      imagem_capa: new FormControl(null),
      salvarTipo : new FormControl(null,Validators.required)
    });
    this.loadGeneros();
    
   }
 

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.jogo_id = params['jogo'];
      if(this.jogo_id != null && this.jogo_id != -1){
        this.IniciarEdicao(params['jogo']); //caso a url tenha um argumento inicia o modo de edi????o
      }else{
        
      }

    });
    
  }

  IniciarEdicao(jogo_id:number){
    this.catalogoService.getJogo({jogo_id: jogo_id}).subscribe(jogoRetorno =>{
      //this.autenticacao.setToken(success.token);
      this.jogoEditar = JSON.parse(jogoRetorno.body);

      this.apiManterJogos.getScreenshots({jogo_id :jogo_id}).subscribe(screenshotsRetorno =>{
        let screenshots: string[] = JSON.parse(screenshotsRetorno.body);
        screenshots.forEach(element => {
          screenshots[screenshots.indexOf(element)] = 'data:image/jpeg;base64,' + element;//this.sanitizer.bypassSecurityTrustUrl('data:image/jpeg;base64,' + element);
        });
        Jogo.Init(ListaIdString.getValue(this.jogoEditar.genero,this.generos),screenshots,this.jogoEditar,this.sanitizer);

        screenshots.forEach((element: any) => {
          this.screenshotFiles.push(element);
        });

        this.capaFile = 'data:image/jpeg;base64,' +this.jogoEditar.imagem_capa;
        this.form_cadastrar.setValue({
          titulo: this.jogoEditar.titulo,
          tags: this.jogoEditar.tags,
          descricao: this.jogoEditar.descricao,
          executavel: this.jogoEditar.caminho_executavel,
          detalhes: this.jogoEditar.detalhes,
          trailer: null,
          genero: this.jogoEditar.genero,
          imagem_capa: '',//this.jogoEditar.imagem_capa_sanitized, //iVBORw0KGgoAA...
          salvarTipo : this.jogoEditar.visibilidade
        });
        if(this.form_cadastrar.get('salvarTipo')?.value != 'RASCUNHO'){
          this.form_cadastrar.get('salvarTipo')!.disable();
        }
      },err =>{

      });

      this.apiManterJogos.getFilesInfo({jogo_id:jogo_id}).subscribe(retorno =>{
        if(retorno.sucesso){
          this.editando_arquivo = retorno.body;
        }
      },erro =>{

      })
      
    },err =>{

    });
    
  }

  onSubimt(){
    this.enviando = true;
    Debug.logRequest('cadastrar jogo'+ JSON.stringify(this.form_cadastrar.value));
    if(this.jogo_id != null && this.jogo_id != -1){
      Debug.logDetalhe('imagem: '+this.jogoEditar.imagem_capa);
      //let screenshotsJson : string[] =  this.screenshotsToJason();
        let JogoRest =  {
          jogo_id : this.jogo_id,
          titulo: this.form_cadastrar.get('titulo')?.value,
          tags: this.form_cadastrar.get('tags')?.value,
          descricao: this.form_cadastrar.get('descricao')?.value,
          caminho_executavel: this.form_cadastrar.get('executavel')?.value,
          detalhes: this.form_cadastrar.get('detalhes')?.value,
          trailer: this.form_cadastrar.get('trailer')?.value,
          imagem_capa: this.capaFile ,//image/png;base64,iVBORw0KGgoAA...
          visibilidade: this.form_cadastrar.get('salvarTipo')?.value,
          genero: this.form_cadastrar.get('genero')?.value,
          token:  JSON.parse(localStorage.getItem('currentUser')!).token,
          screenshots: this.screenshotFiles}; 
          console.log("formulario :"+JSON.stringify(JogoRest));
        this.apiManterJogos.atualizarJogo(JogoRest).subscribe(
          retorno => {
            if(retorno.sucesso){
              this.autenticacao.setToken(retorno.token!);
              if(this.file!=null){
                this.enviarArquivos(this.jogo_id );
              }else{
                this.router.navigate(['']);
              }
            }else{
              this.enviando = false;
              if(retorno.erro!.cod==1){//erro de autentica????o
                this.autenticacao.logOut();
                this.router.navigate(['']);
              }else{
                this.autenticacao.setToken(retorno.token!); 
                this.erro = retorno.erro!.mensagem;
              }
            }
          }, 
          err => {
            this.enviando = false;
            this.erro = "Erro ao cadastrar";
          });

    }else{
        
        let JogoRest =  {titulo: this.form_cadastrar.get('titulo')?.value,
          tags: this.form_cadastrar.get('tags')?.value,
          descricao: this.form_cadastrar.get('descricao')?.value,
          caminho_executavel: this.form_cadastrar.get('executavel')?.value,
          detalhes: this.form_cadastrar.get('detalhes')?.value,
          trailer: this.form_cadastrar.get('trailer')?.value,
          imagem_capa: this.capaFile ,
          visibilidade: this.form_cadastrar.get('salvarTipo')?.value,
          genero: this.form_cadastrar.get('genero')?.value,
          token:  JSON.parse(localStorage.getItem('currentUser')!).token,
          screenshots: this.screenshotFiles}; 

        this.apiManterJogos.novoJogo(JogoRest).subscribe(
          retorno => {
            if(retorno.sucesso){
              this.autenticacao.setToken(retorno.token!);
              if(this.file!=null){
                this.enviarArquivos(retorno.body);   
              }else{
                this.router.navigate(['']);
              }

            }else{
              this.enviando = false;
              if(retorno.erro!.cod==1){//erro de autentica????o
                this.autenticacao.logOut();
                this.router.navigate(['']);
              }else{
                this.autenticacao.setToken(retorno.token!); 
                this.erro = retorno.erro!.mensagem;
              }
            }
          }, 
          err => {
            this.enviando = false;
            this.erro = "Erro ao cadastrar";
          });
    }

  }

  onExcluir(){
    this.apiManterJogos.excluirRascunho({jogo_id:this.jogoEditar.jogo_id}).subscribe(
      retorno => {
        if(retorno.sucesso){
          this.autenticacao.setToken(retorno.token!);
          this.router.navigate(['/painel']);
        }else{
          if(retorno.erro!.cod == 1){
            this.autenticacao.logOut();
          }else{
            this.autenticacao.setToken(retorno.token!);
            this.erro = 'Erro ao excluir rascunho';
          }
        }
      },erro =>{
        this.erro = 'Erro ao excluir rascunho';
      }
    );
  }

  loadGeneros(){
    this.apiManterJogos.getGeneros().subscribe( 
      retorno =>{ 
        Debug.logRequest("genero json:" +JSON.stringify( retorno.body));
        let gen = JSON.parse(retorno.body);
            gen.forEach((element: { genero_id: number, genero_nome: string; }) => {
              this.generos.push(new ListaIdString(element.genero_id,element.genero_nome));
            });
      }, err =>{
        Debug.logErro("genero erro: "+JSON.stringify(err));
      });

  }

  carregarScreenshotNavegador(event: any){
    if(event.srcElement.files[0].name.split('.')[1]== 'png'||event.srcElement.files[0].name.split('.')[1]== 'jpg'||event.srcElement.files[0].name.split('.')[1]== 'jpeg')
    {
      if (event.target.files && event.target.files[0]) {
        const reader = new FileReader();
        reader.onload = (_event: any) => {
            if(!this.sePossuiScreenshotsCarregadas()) this.screenshotFiles = this.screenshotFiles || [];
            this.screenshotFiles.push(_event.target.result);
        };
        reader.readAsDataURL(event.target.files[0]);
      }
    }else{
      this.erro = 'Insira somente imagens no formato .png ou .jpg';
    }
  }

  getScreenshotLink(index:number):string{
    return 'localhost/image'+index+'.png';
  }

  sePossuiScreenshotsCarregadas():boolean{
    if(this.screenshotFiles != null && this.screenshotFiles.length >0) return true
    else return false;
  }

  removeScreenshot(index:number){
    this.screenshotFiles.splice(index,1);
  }

  carregarImagemNavegador(event: any) {
    if(event.srcElement.files[0].name.split('.')[1]== 'png'||event.srcElement.files[0].name.split('.')[1]== 'jpg'||event.srcElement.files[0].name.split('.')[1]== 'jpeg')
    {
      this.erro = '';
      if (event.target.files && event.target.files[0]) {
          const reader = new FileReader();
          reader.onload = (_event: any) => {
              this.capaFile = _event.target.result;
          };
          reader.readAsDataURL(event.target.files[0]);
      }
    }else{
      this.erro = 'Insira somente imagens no formato .png ou .jpg';
    }
  }

  seFormValid():boolean{
    return (this.form_cadastrar.valid && !this.enviando && this.form_cadastrar.get('salvarTipo')?.value == 'RASCUNHO')  
            || (this.form_cadastrar.valid && !this.enviando && (this.file != null || this.editando_arquivo != '')&& this.capaFile != '')  ;
  }

  onFilesSelected(event : any){
    if(event.srcElement.files[0].name.split('.')[1]== 'zip')
    {
      this.erro = '';
      const file:File = event.target.files[0];
      this.event = event;
      this.file = file;

      if (file) {

          this.fileName = file.name;
          Debug.logDetalhe('file size: '+file.size);
          
      }
    }else{
      this.erro = 'Insira somente arquivos no formato .zip com o conteudo do jogo dentro';
    }
  }

  enviarArquivos(jogo_id:number){
    const formData = new FormData();

    formData.append("arquivo", this.file);

    const reader = new FileReader();
    reader.onload = (_event: any) => {
          //this.capaFile = _event.target.result;
    
    
    // Debug.logDetalhe('enviando: '/*+_event.target.result*/);
    // this.jogoService.salvarArquivos({arquivo:_event.target.result,jogo_id:jogo_id}).subscribe(
    //   retorno=>{
    //     if(retorno.sucesso){
    //       this.autenticacao.setToken(retorno.token!);
    //       Debug.logDetalhe('arquivo enviado');
    //     }else{
    //       if(retorno.erro!.cod==1){

    //       }else{
    //         this.autenticacao.setToken(retorno.token!);
    //       }
    //       Debug.logDetalhe('arquivo n??o enviado');
    //     }
    //   },
    //   erro=>{
    //     Debug.logDetalhe('arquivo n??o enviado');
    //   }).;
      Debug.logDetalhe('enviando: '/*+_event.target.result*/);
      const upload$ = this.apiManterJogos.salvarArquivos({arquivo:_event.target.result,jogo_id:jogo_id});
      //Debug.logDetalhe('file: '+JSON.stringify(_event.target.result)); 
      let uploadSub: Subscription;

      uploadSub = upload$.subscribe(event => {
        if (event.type == HttpEventType.UploadProgress) {
          this.envioProgresso = Math.round(100 * (event.loaded / event.total!));
        }
        if(event.type== HttpEventType.Response){
          let  retorno :RestObject = event.body!;
          if(retorno.sucesso){
              this.autenticacao.setToken(retorno.token!);
              Debug.logDetalhe('arquivo enviado');
              this.enviando = false;
              this.router.navigate(['']);
          }else{
            if(retorno.erro!.cod==1){
        
            }else{
              this.autenticacao.setToken(retorno.token!);
            }
            Debug.logDetalhe('arquivo n??o enviado');
          }
        }
        
      });
    };
    reader.readAsDataURL(this.event.target.files[0]);
    //const upload$ = this.http.post("/api/thumbnail-upload", formData);

    //upload$.subscribe();
  }
}
