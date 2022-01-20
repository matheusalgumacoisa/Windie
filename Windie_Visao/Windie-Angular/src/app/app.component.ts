import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DataSharingService } from './Logica/data-sharing.service';
import { Debug } from './Logica/Debug';
import { ApiAutenticacaoService } from './Logica/RestAPIs/api-autenticacao.service';
import { ApiManterAssinaturaService } from './Logica/RestAPIs/api-manter-assinatura.service';
import { ApiManterUsuario } from './Logica/RestAPIs/apiManterUsuario';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Windie-Angular';
  label_usuario: string = "Usuário";
  checkout_url:string = 'https://sandbox.pagseguro.uol.com.br/v2/checkout/payment.html?code=';

  constructor(private router: Router, private usuario : ApiManterUsuario, private autentica:ApiAutenticacaoService, private autenticacao : ApiAutenticacaoService,private dataSharing: DataSharingService,private apiAssinatura : ApiManterAssinaturaService) {
    
    dataSharing.usuarioCarregado.subscribe(retorno =>{
      this.label_usuario = autenticacao.usuario?.apelido!;
    });
   
  }

  ngOnInit(){
    
    
  }

  CadastrarUsuario(){ this.router.navigate(['']).then(()=>this.router.navigate(['/usuario/cadastrar']));     }
  TelaLogin(){ this.router.navigate(['/login']);     }
  EditarInformacoes(){ this.router.navigate(['/usuario/editar']);     }
  PublicarJogo(){ this.router.navigate(['']).then(()=>this.router.navigate(['/jogos/publicar']));    }

  seAutenticado(): boolean{
    
    return this.autenticacao.seAutenticado();

  }

  seAssinante():boolean{
    return this.autenticacao.seAssinante();
  }

  Logout(){
    this.autentica.logOut();
  }

  Painel(){
    this.router.navigate(['/painel']
    );    
  }

  Catalogo(){
    this.router.navigate(['/detalhes']).then(()=>this.router.navigate(['']));    
  }

  seDesenvolvedor():boolean{
    return this.autentica.seUsuarioDesenvolvedor();
  }

  JogosAprovacao(){
    this.router.navigate(['/greenLight']); 
  }

  Assinar(){
    this.apiAssinatura.getCheckoutToken().subscribe(
      retorno =>{
        if(retorno.sucesso){
          //this.router.navigate([this.checkout_url+retorno.body]);
          Debug.logDetalhe('encaminhando para: '+this.checkout_url+retorno.body);
          window.location.href = this.checkout_url+retorno.body;
        }
      }, erro =>{

      }

    )
  }

}
