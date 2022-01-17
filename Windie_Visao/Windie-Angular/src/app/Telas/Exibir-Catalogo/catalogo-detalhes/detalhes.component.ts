import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';
import { Debug } from 'src/app/Logica/Debug';
import { ApiAprovarJogosService } from 'src/app/Logica/RestAPIs/api-aprovar-jogos.service';
import { ApiAutenticacaoService } from 'src/app/Logica/RestAPIs/api-autenticacao.service';
import { ApiManterCatalogo } from 'src/app/Logica/RestAPIs/apiManterCatalogo';
import { JogoClassificacao } from 'src/app/Modelos/JogoClassificacao';
import { ListaIdString } from 'src/app/Modelos/ListaIdString';


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

  constructor(private catalogoApi: ApiManterCatalogo,private route: ActivatedRoute,private apiAprovar : ApiAprovarJogosService, 
    private sanitizer: DomSanitizer, private autenticacao : ApiAutenticacaoService,private  router : Router) {

    
  }


  ngOnInit(): void {
    let url_jogo_id! : number;
    this.route.queryParams.subscribe(params => {
      url_jogo_id = params['jogo'];
      this.carregarJogo(url_jogo_id);  
    });
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



  //=================================Ação no back end=================================

  carregarJogo(jogo_id:number){ //carrega o jogo e  dados complementarre
    this.catalogoApi.getJogo({jogo_id : jogo_id}).subscribe(//carrega os jogos
      retorno => { 
        this.jogo = JSON.parse(retorno.body);
        this.catalogoApi.getGeneros().subscribe(// carrega os generos
          retorno =>{
            let gen = JSON.parse(retorno.body);
            gen.forEach((element: { genero_id: number, genero_nome: string; }) => {
              this.generos.push(new ListaIdString(element.genero_id,element.genero_nome));
            });
            JogoClassificacao.Init(this.getGenero(this.jogo.genero),null,this.jogo,this.sanitizer); //feito antes para não precisar esperar o carregamento das screenshots
            this.catalogoApi.getScreenshots({jogo_id : this.jogo.jogo_id}).subscribe(//carrega as screenshots
              retorno=>{
                let screenshots : any[] = JSON.parse(retorno.body);
                screenshots.forEach(element => {
                  screenshots[screenshots.indexOf(element)] = this.sanitizer.bypassSecurityTrustUrl('data:image/jpeg;base64,' + element);
                });
                
                JogoClassificacao.Init(this.getGenero(this.jogo.genero),screenshots,this.jogo,this.sanitizer); //coloca generos e screenshots no objeto jogo
                this.setarTipoPagina();
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

  //=================================Consulta na Visão=================================
    
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
    Debug.logDetalhe('avaliar '+str);
  }

  sePodeAvaliar():boolean{
    return this.autenticacao.seAutenticado();
  }


}


