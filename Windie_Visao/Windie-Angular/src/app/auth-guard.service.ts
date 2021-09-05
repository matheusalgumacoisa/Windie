import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { ServicesUsuarioService } from './Manter-Usuarios/services-usuario.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{

  constructor(private usuarioService: ServicesUsuarioService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    
    //console.log("sss"+state.url);
    if(this.usuarioService.seUsuarioAutenticado()){
      this.usuarioService.autenticarToken();
      //console.log("coisa que retorna"+this.usuarioService.seUsuarioAutenticado().subscribe());
      if(state.url == "/login"||state.url == "/usuario/cadastrar"){
       //console.log("login");
        this.router.navigate(['']);
        return false;
      }
      if(state.url == "/jogos/publicar"){
        this.router.navigate(['/usuario/cadastrar-desenvolvedor']);
        return false;
      }
      return true;
    }else{
      
      if(state.url == "/login"||state.url == "/usuario/cadastrar"){
         return true;
       }

      console.log("não autenticado");
      this.router.navigate(['']);
      return false;
    }

    

  }
}
