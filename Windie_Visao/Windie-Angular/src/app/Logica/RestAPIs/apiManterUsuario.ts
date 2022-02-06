import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http'
import { Observable } from 'rxjs';
import { RestObject } from 'src/app/Modelos/RestObject';
import { ApiAutenticacaoService } from './api-autenticacao.service';


@Injectable({
  providedIn: 'root'
})
export class ApiManterUsuario {


  url : string = 'http://localhost:4200/api/usuario';


  constructor(private http: HttpClient,private autentica : ApiAutenticacaoService) {

  }

  public cadastrarUsuario(body: any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/cadastrar",rest);
  }

  public cadastrarDesenvolvedor(body: any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/cadastrarDesenvolvedor",rest);

  }

  public atualizarUsuario(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/atualizar",rest);
  }

  public getDesenvolvedor():Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),'');
    return this.http.post<RestObject>(this.url+"/desenvolvedor",rest);
  }

  public trocarSenha(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/mudarSenha",rest);
  }
}



