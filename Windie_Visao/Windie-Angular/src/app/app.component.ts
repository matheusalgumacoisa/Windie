import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DataSharingService } from './Logica/data-sharing.service';
import { ApiAutenticacaoService } from './Logica/RestAPIs/api-autenticacao.service';
import { ApiManterUsuario } from './Logica/RestAPIs/apiManterUsuario';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Windie-Angular';
  label_usuario: string = "Usuário";

  constructor(private router: Router, private usuario : ApiManterUsuario, private autentica:ApiAutenticacaoService, private autenticacao : ApiAutenticacaoService,private dataSharing: DataSharingService) {
    
    dataSharing.usuarioCarregado.subscribe(retorno =>{
      this.label_usuario = autenticacao.usuario?.apelido!;
    });
   
  }

  ngOnInit(){
    
    
  }

  CadastrarUsuario(){ this.router.navigate(['/usuario/cadastrar']);     }
  TelaLogin(){ this.router.navigate(['/login']);     }
  EditarInformacoes(){ this.router.navigate(['/usuario/editar']);     }
  PublicarJogo(){ 
    this.router.navigate(['/jogos/publicar']
    );     }

  seAutenticado(): boolean{
    
    return this.autenticacao.seAutenticado();

  }

  Logout(){
    this.autentica.logOut();
  }

  Painel(){
    this.router.navigate(['/painel']
    );    
  }

}
