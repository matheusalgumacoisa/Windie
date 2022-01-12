import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { ApiAutenticacaoService } from './RestAPIs/api-autenticacao.service';


@Injectable({
  providedIn: 'root'
})
export class AuthGuardService implements CanActivate{

  constructor( private router: Router, private autenticacao : ApiAutenticacaoService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {
    
    if(this.autenticacao.seAutenticado()){
      if(state.url == "/login"||state.url == "/usuario/cadastrar"){
        this.router.navigate(['']);
        return false;
      }
      if(state.url == "/jogos/publicar" && !this.autenticacao.seUsuarioDesenvolvedor()){
        
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
