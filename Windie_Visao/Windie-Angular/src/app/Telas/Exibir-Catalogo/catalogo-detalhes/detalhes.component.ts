import { HttpEventType } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Debug } from 'src/app/Logica/Debug';
import { ApiAprovarJogosService } from 'src/app/Logica/RestAPIs/api-aprovar-jogos.service';
import { ApiAutenticacaoService } from 'src/app/Logica/RestAPIs/api-autenticacao.service';
import { ApiAvaliarJogosService } from 'src/app/Logica/RestAPIs/api-avaliar-jogos.service';
import { ApiManterBiblioteca } from 'src/app/Logica/RestAPIs/apiManterBiblioteca';
import { ApiManterCatalogo } from 'src/app/Logica/RestAPIs/apiManterCatalogo';
import { Eventos } from 'src/app/Modelos/Eventos';
import { JogoClassificacao } from 'src/app/Modelos/JogoClassificacao';
import { ListaIdString } from 'src/app/Modelos/ListaIdString';
import { RestObject } from 'src/app/Modelos/RestObject';
import {MatDialog, MatDialogModule} from '@angular/material/dialog';
import { ExluirJogoDialogComponent } from '../exluir-jogo-dialog/exluir-jogo-dialog.component';



@Component({
  selector: 'app-detalhes',
  templateUrl: './detalhes.component.html',
  styleUrls: ['./detalhes.component.css']
})
export class DetalhesComponent implements OnInit {
 
   generos  : ListaIdString[] = [];
   estrelas: number[] = [1,2,3,4,5];

   jogo! : JogoClassificacao;
   seJogoBiblioteca: boolean = false;
   telaCatalogo: boolean = true;
   telaAprovar: boolean = false;
   votoAprovar!:boolean; //atribuido caso o usuario ja tenha votado
   avaliarHoveredStar : number = 0;
   notaUsuario : number = 0;
   horas_jogadas: number = 0;


   downloadProgress :number = -1;
   //avaliacao: number = 0;

  constructor(private catalogoApi: ApiManterCatalogo,private route: ActivatedRoute,private apiAprovar : ApiAprovarJogosService, private apiBiblioteca : ApiManterBiblioteca,
    private sanitizer: DomSanitizer, private autenticacao : ApiAutenticacaoService,private  router : Router, private avaliarApi : ApiAvaliarJogosService,private dialog : MatDialog) {

    
  }


  ngOnInit(): void {
    let url_jogo_id! : number;
    this.route.queryParams.subscribe(params => {
      url_jogo_id = params['jogo'];
      this.carregarJogo(url_jogo_id);  
    });
  }
  

  setarAvaliaacoes(){
    if(this.seAssinante()){
      this.avaliarApi.getHorasJogo({jogo_id:this.jogo.jogo_id}).subscribe(
        retorno =>{
          if(retorno.sucesso){
            
            this.horas_jogadas = retorno.body;
            this.autenticacao.setToken(retorno.token!);
            Debug.logDetalhe('get horas: '+retorno.body);
            if(this.horas_jogadas>0){
              this.avaliarApi.getAvaliacao({jogo_id:this.jogo.jogo_id}).subscribe(
                retorno =>{
                  if(retorno.sucesso){
                    this.autenticacao.setToken(retorno.token!);
                    this.notaUsuario = JSON.parse(retorno.body).nota;

                  }else{
                    if(retorno.erro!.cod ==1){
                      this.router.navigate(['']);
                      this.autenticacao.logOut();
                    }
                  }
                },error =>{
                  this.router.navigate(['']);
                  this.autenticacao.logOut();
                }
              );
            }
          }else{
            if(retorno.erro!.cod ==1){
              this.router.navigate(['']);
              this.autenticacao.logOut();
            }
          }
        },erro =>{
          this.router.navigate(['']);
          this.autenticacao.logOut();
        }
      );
    }  
  }

  setarTipoPagina(){
    if(this.router.url.split('?')[0]=='/greenLight/detalhes'){

      this.telaCatalogo= false;
      this.telaAprovar = true;
      this.apiAprovar.seVotou({jogo_id:this.jogo.jogo_id}).subscribe(
        retorno =>{
          Debug.logOutput('se votou: '+JSON.stringify(retorno))
          let valores : {se_votou:boolean;voto:boolean};
          valores = JSON.parse(retorno.body);
          if(retorno.sucesso){
            if(valores.se_votou){
              this.votoAprovar = valores.voto;
            }  
            this.autenticacao.setToken(retorno.token!);
          }else{
            if(retorno.erro!.cod==1){
              this.autenticacao.logOut();
              this.router.navigate(['']);
            }else{
              this.autenticacao.setToken(retorno.token!);
            }
          }
        },erro => {
          this.autenticacao.logOut();
          this.router.navigate(['']);
        }
      );
    }else{
      this.telaCatalogo = true;
      this.telaAprovar= false;
    }
  }



  //=================================A????o no back end=================================

  carregarJogo(jogo_id:number){ //carrega o jogo e  dados complementarre
    this.catalogoApi.getJogo({jogo_id : jogo_id}).subscribe(//carrega os jogos
      retorno => { 
        this.jogo = JSON.parse(retorno.body);
        this.apiBiblioteca.initInstalacaoMetadados(this.jogo.caminho_executavel,this.jogo.jogo_id);
        this.catalogoApi.getGeneros().subscribe(// carrega os generos
          retorno =>{
            let gen = JSON.parse(retorno.body);
            gen.forEach((element: { genero_id: number, genero_nome: string; }) => {
              this.generos.push(new ListaIdString(element.genero_id,element.genero_nome));
            });
            JogoClassificacao.Init(this.getGenero(this.jogo.genero),null,this.jogo,this.sanitizer); //feito antes para n??o precisar esperar o carregamento das screenshots
            this.catalogoApi.getScreenshots({jogo_id : this.jogo.jogo_id}).subscribe(//carrega as screenshots
              retorno=>{
                let screenshots : any[] = JSON.parse(retorno.body);
                screenshots.forEach(element => {
                  screenshots[screenshots.indexOf(element)] = this.sanitizer.bypassSecurityTrustUrl('data:image/jpeg;base64,' + element);
                });
                
                JogoClassificacao.Init(this.getGenero(this.jogo.genero),screenshots,this.jogo,this.sanitizer); //coloca generos e screenshots no objeto jogo

                this.setarTipoPagina();
                if(this.telaCatalogo){
                  this.setarAvaliaacoes();
                }
                
                

              },
              erro=>{
        
              });
            


          },erro=>{

          });
        
        this.catalogoApi.seNaBiblioteca({jogo_id : jogo_id}).subscribe(
          success=>{ 
            
            this.seJogoBiblioteca = JSON.parse(success.body); 
            console.log('se na biblioteca: '+this.seJogoBiblioteca);
          },err=>{

          });
      }, 
      erro =>{

      });
  }

  adicionarBiblioteca(){
    this.catalogoApi.adicionarBiblioteca({jogo_id : this.jogo.jogo_id}).subscribe(
      retorno => {
        this.autenticacao.setToken(retorno.token!); 
        this.catalogoApi.seNaBiblioteca({jogo_id : this.jogo.jogo_id}).subscribe(
          retorno=>{ 
            this.seJogoBiblioteca = JSON.parse(retorno.body)
          },erro=>{

          });
      },
      erro => {

      }
    );
  }

  removerBiblioteca(){
    console.log('remover da biblioteca');
    this.catalogoApi.removerBiblioteca({jogo_id : this.jogo.jogo_id}).subscribe(
      retorno => {
        this.autenticacao.setToken(retorno.token!);  
        this.catalogoApi.seNaBiblioteca({jogo_id : this.jogo.jogo_id}).subscribe(
          retorno=>{ 
            console.log('se na biblioteca: '+JSON.stringify(retorno.body));
            this.seJogoBiblioteca = JSON.parse(retorno.body)
          },erro=>{

          });
      },
      erro => {});
  }

  //=================================Consulta na Vis??o=================================
    
  seLogado(){
    
    return this.autenticacao.seAutenticado();

  }

  seAssinante(){
    return this.autenticacao.seAutenticado() && this.autenticacao.seAssinante();
  }

  
  seExibeEstrela(estrela_numero: number,nota: number): boolean{

    if(estrela_numero<= nota){
      return true;
    }
    return false;
  }

  getGenero(genero_id:number):string | null{
    let genero_nome = null;
    this.generos.forEach(element => {
      if(element.id==genero_id){
        genero_nome = element.valor;
      }
    });

    return genero_nome;
  }

  votarAprovacao(voto:boolean){
    this.votoAprovar = voto;
    this.apiAprovar.votar({voto:voto,jogo_id:this.jogo.jogo_id}).subscribe(retorno=>{
      if(retorno.sucesso){
        this.autenticacao.setToken(retorno.token!);
      }else{
        if(retorno.erro!.cod==1){
          this.autenticacao.logOut();
          this.router.navigate(['']);
        }else{
          this.autenticacao.setToken(retorno.token!);
        }
      }
    }, erro =>{
      this.autenticacao.logOut();
      this.router.navigate(['']);
    }); 
  }

  seVoutouAprovar(){
    return this.votoAprovar != undefined && this.votoAprovar == true;
  }

  seVoutouNaoAprovar(){
    return this.votoAprovar != undefined && this.votoAprovar == false;
  }

  onHoverStar(str:number){
    this.avaliarHoveredStar = str;
    
  }

  onLeaveHoverStar(str:number){
    this.avaliarHoveredStar = 0;
  }

  avaliar(str:number){
    this.avaliarApi.avaliar({jogo_id:this.jogo.jogo_id,nota:str}).subscribe(
      retorno =>{
        if(retorno.sucesso){
          this.autenticacao.setToken(retorno.token!);
          this.avaliarApi.getAvaliacao({jogo_id:this.jogo.jogo_id}).subscribe(
            retorno =>{
              if(retorno.sucesso){
                this.autenticacao.setToken(retorno.token!);
                this.notaUsuario = JSON.parse(retorno.body).nota;

              }else{
                if(retorno.erro!.cod ==1){
                  this.router.navigate(['']);
                  this.autenticacao.logOut();
                }else{
                  this.autenticacao.setToken(retorno.token!);
                }
              }
            },error =>{
              this.router.navigate(['']);
              this.autenticacao.logOut();
            }
          );
        }else{
          if(retorno.erro!.cod==1){
            this.autenticacao.logOut();
            this.router.navigate(['']);
          }else{
            this.autenticacao.setToken(retorno.token!);
          }
        }
      },error=>{
        this.autenticacao.logOut();
        this.router.navigate(['']);
      }
    )
    Debug.logDetalhe('avaliar '+str);
  }

  sePodeAvaliar():boolean{
    return this.autenticacao.seAutenticado()&&this.horas_jogadas >0&&this.telaCatalogo;
  }

  seDesktop():boolean{ //retorna true se estiver no app desktop
    if(localStorage.getItem('plataforma')=='desktop'){
      return true;
    }else{
      return false;
    }

  }
 
  baixar(){
    this.apiBiblioteca.baixarArquivos({jogo_id:this.jogo.jogo_id},this.jogo.jogo_id,this.jogo.caminho_executavel);
  }

  desinstalar(){
    let dialogRef = this.dialog.open(ExluirJogoDialogComponent, {
      height: '200px',
      width: '600px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log(`Dialog result: ${result}`); // Pizza!
      if(result == 'Y'){
        Debug.logDetalhe('desinstalando..');
        let eventos :Eventos[] =[];
        if(sessionStorage.getItem('eventos')!=undefined){
          eventos = JSON.parse(sessionStorage.getItem('eventos')!);
        }   
        eventos.push({evento_tipo: 'excluir' ,evento_corpo:{jogo_id:this.jogo.jogo_id}});
        sessionStorage.setItem('eventos',JSON.stringify(eventos));
        this.apiBiblioteca.resetDownload(this.jogo.jogo_id);
        this.router.navigate(['/biblioteca']);
      }
    });
    

  }

  downloadProgresso(){
    if(!this.seDesktop()||this.downloadProgress==null||this.apiBiblioteca.getDownload(this.jogo.jogo_id)==null){
      return -1;
    }
    this.downloadProgress =this.apiBiblioteca.getDownload(this.jogo.jogo_id)!.progresso;
    return this.downloadProgress ;
  }

  downloadConcluido(){

    if(!this.seDesktop()){
      return false;
    }
    /*if(!this.seJogoBiblioteca){
      this.adicionarBiblioteca();
    }*/
    return this.apiBiblioteca.getDownload(this.jogo.jogo_id)!.seConcluido;

  }

  seInstalando(){
    if(!this.seDesktop()){
      return false;
    }

    return this.apiBiblioteca.getDownload(this.jogo.jogo_id)!.state == 'installing';
  }

  jogar(){
    Debug.logDetalhe('abrindo jogo em: '+sessionStorage.getItem('caminhoInst_jogo_'+this.jogo.jogo_id));
    let eventos :Eventos[] =[];
    if(sessionStorage.getItem('eventos')!=undefined){
      eventos = JSON.parse(sessionStorage.getItem('eventos')!);
    }

    eventos.push({evento_tipo: 'jogar' ,evento_corpo:{jogo_id:this.jogo.jogo_id}});
    sessionStorage.setItem('eventos',JSON.stringify(eventos));

  }

  seInstalado(){
    if(sessionStorage.getItem('caminhoInst_jogo_'+this.jogo.jogo_id)==undefined || sessionStorage.getItem('caminhoInst_jogo_'+this.jogo.jogo_id) == null) return false;
    return true;
  }

  seAberto(){
    return false;
  }

}


