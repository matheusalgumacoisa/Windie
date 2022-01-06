import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { ServicesCatalogoService } from '../services-catalogo.service';

@Component({
  selector: 'app-catalogo-lista',
  templateUrl: './catalogo-lista.component.html',
  styleUrls: ['./catalogo-lista.component.css']
})
export class CatalogoListaComponent implements OnInit {

    jogos! : jogo[];
    jogos_paginados! : jogo[];
    changeText: boolean = false;
    estrelas: number[] = [1,2,3,4,5];
    pag: pagina = {num_itens: 25, 
      page: 1};
    paginas_numero : number = 0;
    termo_busca: string = ''; 
    
    constructor(private jogoService: ServicesCatalogoService, private sanitizer: DomSanitizer,private router: Router ) { }

    ngOnInit(): void {
      this.getJogos();
    }

    


  getJogos(){

    this.jogoService.getJogos(JSON.stringify(this.pag)).subscribe(success =>{this.jogos = JSON.parse(JSON.stringify(success.body)); 
      console.log("jogos: "+this.jogos);
      this.setUpImages();
      this.calcularEstrelas();
      this.calcularNumeroPaginas();
    },err =>{console.log("erro get jogos lista: "+JSON.stringify(err))});

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

  verJogo(jg:jogo){

    console.log("vendo: "+jg.titulo);
    this.router.navigate(['detalhes'],{ queryParams: { jogo: jg.jogo_id } });
  }

  calcularNumeroPaginas(){
    this.paginas_numero = Math.round((this.jogos[0].jogos_numero/this.pag.num_itens)); 
  }

  onChangePage(pagina: number) {
    this.pag.page = pagina;
    this.getJogos();
  }

  onPreviousPage() {

    this.pag.page = this.pag.page - 1;
    if(this.pag.page<=0){
      this.pag.page = 1;
    }
    this.getJogos();
  }

  onNextPage() {
    this.pag.page = this.pag.page + 1;
    if(this.pag.page > this.paginas_numero){
      this.pag.page = this.pag.num_itens;
    }
    this.getJogos();
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
    this.jogoService.buscaJogos(JSON.stringify({num_itens: this.pag.num_itens, 
      page: this.pag.page,
      termo: this.termo_busca})).subscribe(success =>{this.jogos = JSON.parse(JSON.stringify(success.body)); 
      console.log("jogos: "+this.jogos);
      this.setUpImages();
      this.calcularEstrelas();
      this.calcularNumeroPaginas();
    },err =>{console.log("erro get jogos lista: "+JSON.stringify(err))});
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


