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
  recuperandoURL_assinatura : boolean = false;
  //checkout_url:string = 'https://sandbox.pagseguro.uol.com.br/v2/checkout/payment.html?code=';
  //navegador : string = 'desconhecido';

  constructor(private router: Router, private usuario : ApiManterUsuario, private autentica:ApiAutenticacaoService, private autenticacao : ApiAutenticacaoService,private dataSharing: DataSharingService,private apiAssinatura : ApiManterAssinaturaService) {
    
    dataSharing.usuarioCarregado.subscribe(retorno =>{
      this.label_usuario = autenticacao.usuario?.apelido!;
    });
   
    if(localStorage.getItem('plataforma')!='desktop'){
      localStorage.setItem('plataforma','navegador');
    }
    
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

  seProcessandoAssinatura():boolean{
    let retorno :boolean  = this.apiAssinatura.seProcessandoAssinatura();
    return retorno;
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

  Biblioteca(){
    this.router.navigate(['/biblioteca']);    
  }

  Assinar(){
    this.recuperandoURL_assinatura = true;
    this.apiAssinatura.getCheckoutURL().subscribe(
      retorno =>{
        this.recuperandoURL_assinatura = false;
        if(retorno.sucesso){
          //this.router.navigate([this.checkout_url+retorno.body]);
          Debug.logDetalhe('encaminhando para: '+retorno.body);
          this.AbirTelaDePagamento(retorno.body).then((data) => { console.log('abrindo tela de pagamento..')}); 
        }else{
          
        }
      }, erro =>{
        this.recuperandoURL_assinatura = false;
      }

    )
  }

  async AbirTelaDePagamento(checkout_url:string){
    //await new Promise(f => setTimeout(f, 1000));//delay evita que janela seja aberta antes de estar
    window.location.href = checkout_url;
  }

  seDesktop():boolean{ //retorna true se estiver no app desktop
    if(localStorage.getItem('plataforma')=='desktop'){
      return true;
    }else{
      return false;
    }

  }

}
