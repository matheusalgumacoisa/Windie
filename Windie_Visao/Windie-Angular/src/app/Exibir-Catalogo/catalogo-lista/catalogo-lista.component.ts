import { Component, OnInit } from '@angular/core';
import { ServicesCatalogoService } from '../services-catalogo.service';

@Component({
  selector: 'app-catalogo-lista',
  templateUrl: './catalogo-lista.component.html',
  styleUrls: ['./catalogo-lista.component.css']
})
export class CatalogoListaComponent implements OnInit {

  jogos! : jogo[];

  constructor(private jogoService: ServicesCatalogoService ) { }

  ngOnInit(): void {
    this.jogoService.getJogos().subscribe(success =>{this.jogos = JSON.parse(JSON.stringify(success.body)) },err =>{console.log("erro get jogos lista: "+JSON.stringify(err))});
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
    public imagem_capa: string,
    public genero: number
  ) { }
}
