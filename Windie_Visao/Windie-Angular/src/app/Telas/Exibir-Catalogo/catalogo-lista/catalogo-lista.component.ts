import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Debug } from 'src/app/Logica/Debug';
import { ApiAprovarJogosService } from 'src/app/Logica/RestAPIs/api-aprovar-jogos.service';
import { ApiAutenticacaoService } from 'src/app/Logica/RestAPIs/api-autenticacao.service';
import { JogoClassificacao } from 'src/app/Modelos/JogoClassificacao';
import { ApiManterCatalogo } from '../../../Logica/RestAPIs/apiManterCatalogo';

@Component({
  selector: 'app-catalogo-lista',
  templateUrl: './catalogo-lista.component.html',
  styleUrls: ['./catalogo-lista.component.css']
})
export class CatalogoListaComponent implements OnInit {

    jogos! : JogoClassificacao[];
    jogos_paginados! : jogo[];
    changeText: boolean = false;
    estrelas: number[] = [1,2,3,4,5];
    pag: pagina = {num_itens: 25, 
      page: 1};
    paginas_numero : number = 0;
    termo_busca: string = '';
    telaCatalogo:boolean = true;
    telaAprovacao:boolean = false; 
    
    constructor(private jogoService: ApiManterCatalogo, private sanitizer: DomSanitizer,private router: Router,
      private apiAprovar : ApiAprovarJogosService,private autenticacao :ApiAutenticacaoService ) { 
      Debug.logDetalhe('url= '+router.url);
      if(router.url=='/greenLight'){
        this.telaCatalogo = false;
        this.telaAprovacao = true;
        this.getJogosEmAprovacao();
      }else{
        this.telaCatalogo = true;
        this.telaAprovacao = false;
        this.getJogosCatalogo();
      }
    }

    ngOnInit(): void {
      
    }

    


  getJogosEmAprovacao(){

    this.apiAprovar.listarJogos(this.pag).subscribe(
      retorno =>{
        if(retorno.sucesso){
          this.autenticacao.setToken(retorno.token!);
          this.jogos = JSON.parse(retorno.body);
          Debug.logInput("jogos: "+this.jogos); 
          this.setUpImages();
          this.calcularEstrelas();
          this.calcularNumeroPaginas();
        }else{
          this.autenticacao.logOut();
        }
      },
      err =>{
        console.log("erro get jogos lista: "+JSON.stringify(err))
      });

  }

  getJogosCatalogo(){

    this.jogoService.listarJogos(this.pag).subscribe(
      success =>{
        this.jogos = JSON.parse(success.body);
        Debug.logInput("jogos: "+this.jogos); 
        this.setUpImages();
        this.calcularEstrelas();
        this.calcularNumeroPaginas();
      },
      err =>{
        console.log("erro get jogos lista: "+JSON.stringify(err))
      });

  }


  setUpImages(){

    this.jogos.forEach(element => {
      element.imagem_capa = this.sanitizer.bypassSecurityTrustUrl('data:image/jpeg;base64,' + element.imagem_capa);
    });
  }

  calcularEstrelas(){ //arredonda o valor das notas para serem de 0.5 em 0.5 de acordo com os ícones de estrela
    this.jogos.forEach(element => {
      element.nota = Math.round(element.nota/0.5 )* 0.5;
    });
  }

  seExibeEstrela(estrela_numero: number,nota: number): boolean{

    if(estrela_numero<= nota){
      return true;
    }
    return false;
  }

  verJogo(jg:JogoClassificacao){

    console.log("vendo: "+jg.titulo);
    if(this.telaCatalogo)
    this.router.navigate(['detalhes'],{ queryParams: { jogo: jg.jogo_id } });
    if(this.telaAprovacao)
    this.router.navigate(['greenLight/detalhes'],{ queryParams: { jogo: jg.jogo_id } });
  }

  calcularNumeroPaginas(){
   // this.paginas_numero = Math.round((this.jogos[0].jogos_numero/this.pag.num_itens)); 
  }

  onChangePage(pagina: number) {
    this.pag.page = pagina;
    if(this.telaCatalogo)
    this.getJogosCatalogo();
    if(this.telaAprovacao)
    this.getJogosEmAprovacao();
  }

  onPreviousPage() {

    this.pag.page = this.pag.page - 1;
    if(this.pag.page<=0){
      this.pag.page = 1;
    }
    if(this.telaCatalogo)
    this.getJogosCatalogo();
    if(this.telaAprovacao)
    this.getJogosEmAprovacao();
  }

  onNextPage() {
    this.pag.page = this.pag.page + 1;
    if(this.pag.page > this.paginas_numero){
      this.pag.page = this.pag.num_itens;
    }
    if(this.telaCatalogo)
    this.getJogosCatalogo();
    if(this.telaAprovacao)
    this.getJogosEmAprovacao();
  }

  seMostrarPaginatorItem(item_numero:number):boolean {
    let numero_de_paginadores : number = 4;
    if(item_numero < this.pag.page + numero_de_paginadores && item_numero > this.pag.page - numero_de_paginadores  )
    {
      return true;
    }else{
      return false;
    }
  }

  buscar(){
    if(this.telaCatalogo){
      this.jogoService.pesquisaJogos({num_itens: this.pag.num_itens, page: this.pag.page,termo: this.termo_busca}).subscribe(
        success =>{
          this.jogos = JSON.parse(success.body); 
          console.log("jogos: "+this.jogos);
          this.setUpImages();
          this.calcularEstrelas();
          this.calcularNumeroPaginas();
        },err =>{
          console.log("erro get jogos lista: "+JSON.stringify(err))
        });
    }

    if(this.telaAprovacao){
      this.apiAprovar.pesquisaJogos({num_itens: this.pag.num_itens, page: this.pag.page,termo: this.termo_busca}).subscribe(
        retorno =>{
          if(retorno.sucesso){
            this.jogos = JSON.parse(retorno.body); 
            console.log("jogos: "+this.jogos);
            this.setUpImages();
            this.calcularEstrelas();
            this.calcularNumeroPaginas();
          }
        },err =>{
          console.log("erro get jogos lista: "+JSON.stringify(err))
        });
    }
  }

}

class jogo {
  constructor(
    public jogo_id: number,
    public titulo: string,
    public descricao: string,
    public arquivos: string,
    public caminho_executavel: string,
    public detalhes: string,
    public tags: string,
    public visibilidade: string,
    public imagem_capa: any,
    public genero: number,
    public nota : number,
    public avaliacoes_numero : number,
    public jogos_numero : number
  ) { }
}

class pagina {
  constructor(
    public num_itens: number , 
    public page: number
  ) { }

}


