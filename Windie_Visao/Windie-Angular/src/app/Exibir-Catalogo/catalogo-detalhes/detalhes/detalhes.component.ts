import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';
import { ServiceJogosService } from 'src/app/Manter-Jogos/service-jogos.service';
import { ServicesUsuarioService } from 'src/app/Manter-Usuarios/services-usuario.service';
import { ServicesAuxService } from 'src/app/services-aux.service';
import { ServicesCatalogoService } from '../../services-catalogo.service';

@Component({
  selector: 'app-detalhes',
  templateUrl: './detalhes.component.html',
  styleUrls: ['./detalhes.component.css']
})
export class DetalhesComponent implements OnInit {

   jogo_id! : number;
   jogo! : jogo;
   tags: string[] =[];
   generos  : Genero[] = [];
   genero! : string;
   estrelas: number[] = [1,2,3,4,5];
   screenshots: any[] = [];

  constructor(private jogoService: ServicesCatalogoService,private route: ActivatedRoute, private sanitizer: DomSanitizer, private auxService:ServicesAuxService, private jogosService : ServiceJogosService, private usuario : ServicesUsuarioService) { 
    
  }


  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.jogo_id = params['jogo'];
    });
    this.jogoService.getJogo('{jogo_id : '+this.jogo_id+' }').subscribe(success => { 
      console.log("get jogo: "+JSON.stringify(success.body));
      this.jogo = JSON.parse(JSON.stringify(success.body));
      this.jogo.imagem_capa = this.sanitizer.bypassSecurityTrustUrl('data:image/jpeg;base64,' + this.jogo.imagem_capa); // configurando capa para exibição no navegador
      this.configurarTags();
      this.configurarGenero(this.jogo.genero);
      this.carregarScreenshots();
      this.jogoService.seNaBiblioteca({token: JSON.parse(localStorage.getItem('currentUser')!).token ,jogo_id : this.jogo_id}).subscribe(success=>{ console.log('se na biblioteca: '+JSON.stringify(success.body));this.jogo.seJogoBiblioteca = JSON.parse(JSON.stringify(success.body)).seJogoBiblioteca },err=>{});

      }, err =>{});
  }

  configurarTags(){
    this.tags = this.jogo.tags.split(',');
  }

  configurarGenero(genero_n:number){
    this.auxService.getGeneros().subscribe(success =>{
      this.generos = JSON.parse(JSON.stringify(success.body));
      this.generos.forEach(element => {
        if(element.genero_id==genero_n){
          this.genero = element.genero_nome;
        }
      });
    },err=>{});
  }

  carregarScreenshots(){
    this.jogosService.getScreenshots(this.jogo_id).subscribe(success=>{
      this.screenshots = JSON.parse(JSON.stringify(success));
      this.screenshots.forEach(element => {
        this.screenshots[this.screenshots.indexOf(element)] = this.sanitizer.bypassSecurityTrustUrl('data:image/jpeg;base64,' + element);
      });
    },err=>{})
  }

  seExibeEstrela(estrela_numero: number,nota: number): boolean{

    if(estrela_numero<= nota){
      return true;
    }
    return false;
  }

  seLogado(){
    
    return this.usuario.seUsuarioAutenticado();

  }

  seAssinante(){
    return this.usuario.seUsuarioAutenticado() && this.usuario.seUsuarioAssinante();
  }

  adicionarBiblioteca(){
    console.log('adicionar a biblioteca');
    this.jogoService.adicionarBiblioteca({token: JSON.parse(localStorage.getItem('currentUser')!).token ,jogo_id : this.jogo_id}).subscribe(
      success => { this.jogoService.seNaBiblioteca({token: JSON.parse(localStorage.getItem('currentUser')!).token ,jogo_id : this.jogo_id}).subscribe(success=>{ console.log('se na biblioteca: '+JSON.stringify(success.body));this.jogo.seJogoBiblioteca = JSON.parse(JSON.stringify(success.body)).seJogoBiblioteca },err=>{});},
      err => {}
    );
  }

  removerBiblioteca(){
    console.log('remover da biblioteca');
    this.jogoService.removerBiblioteca({token: JSON.parse(localStorage.getItem('currentUser')!).token ,jogo_id : this.jogo_id}).subscribe(
      success => { this.jogoService.seNaBiblioteca({token: JSON.parse(localStorage.getItem('currentUser')!).token ,jogo_id : this.jogo_id}).subscribe(success=>{ console.log('se na biblioteca: '+JSON.stringify(success.body));this.jogo.seJogoBiblioteca = JSON.parse(JSON.stringify(success.body)).seJogoBiblioteca },err=>{});},
      err => {});
  }

}

class jogo {
  constructor(
    public jogo_id: number,
    public titulo: string,
    public descricao: string,
    //public arquivos: string,
    public caminho_executavel: string,
    public detalhes: string,
    public tags: string,
    public visibilidade: string,
    public imagem_capa: any,
    public genero: number,
    public nota : number,
    public avaliacoes_numero : number,
    public seJogoBiblioteca : boolean//,
   // public jogos_numero : number
  ) { }
}

class Genero {
  constructor(
      public genero_id: number,
      public genero_nome: string,
  ) { }
}
