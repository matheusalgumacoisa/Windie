import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiManterJogos } from 'src/app/Logica/RestAPIs/apiManterJogos';
import { JogoClassificacao } from 'src/app/Modelos/JogoClassificacao';


@Component({
  selector: 'app-painel',
  templateUrl: './painel.component.html',
  styleUrls: ['./painel.component.css']
})
export class PainelComponent implements OnInit {

  public jogos : JogoClassificacao[] = [];

  constructor(private jogosService:ApiManterJogos,private router: Router ) { }

  ngOnInit(): void {
   this.jogosService.getJogosByDesenvolvedor().subscribe(success =>{
     console.log(JSON.stringify(success));
     this.jogos = JSON.parse(success.body);

   },err =>{});
  }

  editarJogo(jogo_id : number){
    console.log("editar jogo");
    this.router.navigate(['/jogos/publicar'],{ queryParams:{jogo : jogo_id}});
  }

}

