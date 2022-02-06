import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ApiAutenticacaoService } from 'src/app/Logica/RestAPIs/api-autenticacao.service';
import { ApiDevPainelService } from 'src/app/Logica/RestAPIs/api-dev-painel.service';
import { ApiManterJogos } from 'src/app/Logica/RestAPIs/apiManterJogos';
import { JogoClassificacao } from 'src/app/Modelos/JogoClassificacao';


@Component({
  selector: 'app-painel',
  templateUrl: './painel.component.html',
  styleUrls: ['./painel.component.css']
})
export class PainelComponent implements OnInit {

  public jogos : JogoClassificacao[] = [];
  public  meses :string[] = ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
  "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"];
  hoje :Date = new Date();
  public mes_passado :string = '';
  public horas_jogadas:string = '';
  public faturado:string = '';
 
  constructor(private jogosService:ApiManterJogos,private router: Router,private apiPainel : ApiDevPainelService,private autentica : ApiAutenticacaoService ) { }

  ngOnInit(): void {
    let esse_mes :number = this.hoje.getMonth();
    if(esse_mes!=0){
      this.mes_passado = this.meses[esse_mes-1];
    }else{
      this.mes_passado = this.meses[11];
    }
   this.jogosService.getJogosByDesenvolvedor().subscribe(success =>{
     console.log(JSON.stringify(success));
     this.jogos = JSON.parse(success.body);
     this.getJogadoHorasMes();
   },err =>{});
  }

  editarJogo(jogo_id : number){
    console.log("editar jogo");
    this.router.navigate(['/jogos/publicar'],{ queryParams:{jogo : jogo_id}});
  }

  getFaturadoMes(){
    
   this.apiPainel.faturadoUltimoMes().subscribe(retorno => {
    if(retorno.sucesso){
      this.autentica.setToken(retorno.token!);
      let faturado : number = JSON.parse(retorno.body);
      this.faturado = faturado.toFixed(2);
      this.faturado = this.faturado.replace('.',',');
    }else{
      if(retorno.erro!.cod==1){
        this.autentica.logOut();
        this.router.navigate(['']);
      }else{
        this.autentica.setToken(retorno.token!);
      }
    }
   },erro =>{
    this.autentica.logOut();
    this.router.navigate(['']);
   });
  }

  getJogadoHorasMes(){
    this.apiPainel.horasJogadasUltimoMes().subscribe(retorno => {
      if(retorno.sucesso){
        let horas:number = JSON.parse(retorno.body);
        this.horas_jogadas= horas.toFixed(1);
        this.horas_jogadas = this.horas_jogadas.replace('.',',');
        this.autentica.setToken(retorno.token!);
        this.getFaturadoMes();
      }else{
        if(retorno.erro!.cod==1){
          this.autentica.logOut();
          this.router.navigate(['']);
        }else{
          this.autentica.setToken(retorno.token!);
          this.getFaturadoMes();  
        }
      }
    },erro =>{
      this.autentica.logOut();
    this.router.navigate(['']);
    });
  }

}

