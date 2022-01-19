import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { RestObject } from 'src/app/Modelos/RestObject';
import { UsuarioSessao } from 'src/app/Modelos/UsuarioSessao';
import { DataSharingService } from '../data-sharing.service';
import { Debug } from '../Debug';

@Injectable({
  providedIn: 'root'
})
export class ApiAutenticacaoService {

  url : string = 'http://localhost:4200/api/autentica';
  usuario! : UsuarioSessao | null;
  erro : string = '';

  constructor(private http: HttpClient,private router: Router,private dataSharing: DataSharingService) {
    if(this.getToken!=null && this.getToken()!=''){ 
      this.carregarUsuario();
    }
  }

  getToken():string | null{
    return  localStorage.getItem('token')!;//.token;
  }

  setToken(token:string){
    localStorage.setItem('token',token);
  }

  getUser():UsuarioSessao | null{
    return this.usuario;
  }

  seUsuarioDesenvolvedor():boolean{
    if(this.usuario != null){
      if(this.usuario.desenvolvedor_id != null &&this.usuario.desenvolvedor_id > 0){
        return true;
      }
    }else{
      if(localStorage.getItem('d')=='true'){
        return true;
      }
    }
    return false;
  }

  logOut(){

    this.erro = "Erro ao sair";
    localStorage.setItem('token','');

    this.http.post(this.url+"/logout",this.getToken()).subscribe(retorno => {
      Debug.logDetalhe('Logout feito com sucesso');
      this.router.navigate(['']);
    },
    erro =>{
      this.usuario = null;
      Debug.logErro(JSON.stringify(erro.error.message));
      this.router.navigate([''])
    });
  }

  logIn(email:string,senha:string){
    let params : RestObject = { token :null,
                                body: {email: email, senha: senha},
                                sucesso:true,
                                erro:undefined };
    Debug.logDetalhe('logando...');
    this.http.post<RestObject>(this.url+"/login",params).subscribe(
      retorno =>{
        if(retorno.sucesso){
          Debug.logDetalhe('login callback: '+JSON.stringify(retorno))
          this.setToken(retorno.token!);
          this.carregarUsuario();
          this.router.navigate(['']);
        }else{
          this.dataSharing.erroLogin.next(retorno.erro!.mensagem);
          Debug.logErro(retorno.erro!.mensagem);
        }
      },
      erro =>{
        this.dataSharing.erroLogin.next("Erro ao entrar");
        Debug.logErro(JSON.stringify(erro.error.message));
      }
    );
  }

  public seAssinante():boolean{
    if(this.usuario != null){
      return this.usuario.se_assinante;
    }
    return false;
  }

  public seAutenticado(): boolean{
    return this.getToken!=null && this.getToken()!='' && this.getToken!= undefined;
  }

  carregarUsuario(){
    Debug.logDetalhe('carregando usuario');
    this.http.post<RestObject>(this.url+"/dados",new RestObject(this.getToken(),'')).subscribe(
      retorno =>{
        let resposta : RestObject = RestObject.assign(retorno);
        this.usuario = JSON.parse(resposta.body);
        this.dataSharing.usuarioCarregado.next(true);
        this.setToken(retorno.token!);
        if(this.usuario!.desenvolvedor_id != null && this.usuario!.desenvolvedor_id > 0){
          localStorage.setItem('d','true');
        }else{
          localStorage.setItem('d','false');
        }
        
        Debug.logOutput('usuario carregado: '+ JSON.stringify(this.usuario));
      },
      erro =>
      {
        Debug.logErro('falha ao carregar usuario');
      }
    )
  }
}
