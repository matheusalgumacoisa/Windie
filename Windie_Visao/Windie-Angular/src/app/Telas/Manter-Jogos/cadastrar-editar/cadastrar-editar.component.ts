import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Debug } from 'src/app/Logica/Debug';
import { ApiAutenticacaoService } from 'src/app/Logica/RestAPIs/api-autenticacao.service';
import { ApiManterCatalogo } from 'src/app/Logica/RestAPIs/apiManterCatalogo';
import { ApiManterJogos } from 'src/app/Logica/RestAPIs/apiManterJogos';
import { Jogo } from 'src/app/Modelos/Jogo';
import { ListaIdString } from 'src/app/Modelos/ListaIdString';


@Component({
  selector: 'app-cadastrar',
  templateUrl: './cadastrar-editar.component.html',
  styleUrls: ['./cadastrar.component.css']
})
export class CadastrarEditarComponent implements OnInit {

  form_cadastrar: FormGroup;
  capaFile : string = '';
  screenshotFiles: string[] = [];
  generos: ListaIdString[] = [];
  jogoEditar!: Jogo;
  erro: string = 'erro teste';

  jogo_id : number = -1;

   constructor( private sanitizer: DomSanitizer,private jogoService : ApiManterJogos,private router: Router ,private route: ActivatedRoute, private catalogoService : ApiManterCatalogo, private autenticacao : ApiAutenticacaoService) {
    this.form_cadastrar = new FormGroup({
      titulo: new FormControl(null,Validators.required),
      tags: new FormControl(null,Validators.required),
      descricao: new FormControl(null,Validators.required),
      executavel: new FormControl(null,Validators.required),
      detalhes: new FormControl(null,Validators.required),
      trailer: new FormControl(null,Validators.required),
      genero: new FormControl(null,Validators.required),
      imagem_capa: new FormControl(null,Validators.required),
      salvarTipo : new FormControl(null,Validators.required)
    });
    this.loadGeneros();
   }
 

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.jogo_id = params['jogo'];
      if(this.jogo_id != null && this.jogo_id != -1){
        this.IniciarEdicao(params['jogo']); //caso a url tenha um argumento inicia o modo de edição
      }

    });

  }

  IniciarEdicao(jogo_id:number){
    this.catalogoService.getJogo({jogo_id: jogo_id}).subscribe(jogoRetorno =>{
      //this.autenticacao.setToken(success.token);
      this.jogoEditar = JSON.parse(jogoRetorno.body);

      this.jogoService.getScreenshots({jogo_id :jogo_id}).subscribe(screenshotsRetorno =>{
        let screenshots: string[] = JSON.parse(screenshotsRetorno.body);
        screenshots.forEach(element => {
          screenshots[screenshots.indexOf(element)] = 'data:image/jpeg;base64,' + element;//this.sanitizer.bypassSecurityTrustUrl('data:image/jpeg;base64,' + element);
        });
        Jogo.Init(ListaIdString.getValue(this.jogoEditar.genero,this.generos),screenshots,this.jogoEditar,this.sanitizer);

        screenshots.forEach((element: any) => {
          this.screenshotFiles.push(element);
        });

        this.capaFile = 'data:image/jpeg;base64,' +this.jogoEditar.imagem_capa;

        this.form_cadastrar = new FormGroup({
          titulo: new FormControl(this.jogoEditar.titulo,Validators.required),
          tags: new FormControl(this.jogoEditar.tags,Validators.required),
          descricao: new FormControl(this.jogoEditar.descricao ,Validators.required),
          executavel: new FormControl(this.jogoEditar.caminho_executavel,Validators.required),
          detalhes: new FormControl(this.jogoEditar.detalhes,Validators.required),
          trailer: new FormControl(null,Validators.required),
          genero: new FormControl(this.jogoEditar.genero,Validators.required),
          imagem_capa: new FormControl(this.jogoEditar.imagem_capa_sanitized,Validators.required), //iVBORw0KGgoAA...
          salvarTipo : new FormControl(this.jogoEditar.visibilidade,Validators.required)
        });

      },err =>{

      });

    },err =>{

    });
  }

  onSubimt(){

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
        this.jogoService.atualizarJogo(JogoRest).subscribe(
          succses => {
            this.autenticacao.setToken(succses.token!); 
            this.router.navigate(['']);
          }, 
          err => {
            console.log("formulario não enviado com sucesso");
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

        this.jogoService.novoJogo(JogoRest).subscribe(
          succses => {
            this.autenticacao.setToken(succses.token!);  
            this.router.navigate(['']);
          }, 
          err => {
            console.log("formulario não enviado com sucesso");
          });
    }

  }

  loadGeneros(){
    this.jogoService.getGeneros().subscribe( 
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
    if (event.target.files && event.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (_event: any) => {
          if(!this.sePossuiScreenshotsCarregadas()) this.screenshotFiles = this.screenshotFiles || [];
          this.screenshotFiles.push(_event.target.result);
      };
      reader.readAsDataURL(event.target.files[0]);
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
    if (event.target.files && event.target.files[0]) {
        const reader = new FileReader();
        reader.onload = (_event: any) => {
            this.capaFile = _event.target.result;
        };
        reader.readAsDataURL(event.target.files[0]);
    }
  }
}
