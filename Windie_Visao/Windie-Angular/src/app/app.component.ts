import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { ServicesUsuarioService } from './Manter-Usuarios/services-usuario.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Windie-Angular';

  constructor(private router: Router, private usuario : ServicesUsuarioService) {
  }

  CadastrarUsuario(){ this.router.navigate(['/usuario/cadastrar']);     }
  TelaLogin(){ this.router.navigate(['/login']);     }
  EditarInformacoes(){ this.router.navigate(['/usuario/editar']);     }
  PublicarJogo(){ this.router.navigate(['/jogos/publicar']);     }

  seAutenticado(): boolean{
    return this.usuario.seUsuarioAutenticado();
  }

  Logout(){

    console.log("logout");
    this.usuario.sairUsuario();
    this.router.navigate(['']);
  }

}
