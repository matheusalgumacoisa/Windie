import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ServicesUsuarioService } from './Manter-Usuarios/services-usuario.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  title = 'Windie-Angular';
  label_usuario!: string;

  constructor(private router: Router, private usuario : ServicesUsuarioService) {

   
  }

  ngOnInit(){
    
    this.usuario.getNick().subscribe(sucesso => this.label_usuario = sucesso.body);
  }

  CadastrarUsuario(){ this.router.navigate(['/usuario/cadastrar']);     }
  TelaLogin(){ this.router.navigate(['/login']);     }
  EditarInformacoes(){ this.router.navigate(['/usuario/editar']);     }
  PublicarJogo(){ this.router.navigate(['/jogos/publicar']);     }

  seAutenticado(): boolean{
    return this.usuario.seUsuarioAutenticado();
   //return false;
  }

  Logout(){

    console.log("logout");
    this.usuario.sairUsuario().subscribe(sucesso =>{ 
      this.router.navigate(['']);
    }, fracasso =>{
    });

  }

}
