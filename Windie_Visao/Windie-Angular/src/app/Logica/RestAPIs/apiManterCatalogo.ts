import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestObject } from 'src/app/Modelos/RestObject';
import { Debug } from '../Debug';
import { ApiAutenticacaoService } from './api-autenticacao.service';

@Injectable({
  providedIn: 'root'
})
export class ApiManterCatalogo {



  
  url : string = 'http://localhost:4200/api/catalogo';

  constructor(private http: HttpClient,private autentica : ApiAutenticacaoService) { }


  public listarJogos(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    Debug.logOutput(JSON.stringify(rest));
    return this.http.post<RestObject>(this.url+"/jogos",rest);
  }

  public pesquisaJogos(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/pesquisar",rest);
  }

  public getJogo(body:any):Observable<RestObject> {
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/jogo",rest);
  }

  public seNaBiblioteca(body:any):Observable<RestObject> {
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/seJogoBiblioteca",rest);
  }

  public adicionarBiblioteca(body:any):Observable<RestObject> {
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/adicionarNaBiblioteca",rest);
  }

  public removerBiblioteca(body:any):Observable<RestObject> {
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/removerDaBiblioteca",rest);
  }

  public getGeneros():Observable<RestObject>{
    return this.http.get<RestObject>(this.url+"/generos");
  }

  public getScreenshots(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+'/screenshots',rest);
  }

  getJogosCatalogoNumero():Observable<RestObject> {
    let rest : RestObject  = new RestObject(this.autentica.getToken(),'CATALOGO');
    return this.http.post<RestObject>(this.url+'/numero',rest);
  }

  getJogosAprovNumero():Observable<RestObject> {
    let rest : RestObject  = new RestObject(this.autentica.getToken(),'APROVACAO');
    return this.http.post<RestObject>(this.url+'/numero',rest);
  }

  getJogosBibliotecaNumero():Observable<RestObject> {
    let rest : RestObject  = new RestObject(this.autentica.getToken(),'BIBLIOTECA');
    return this.http.post<RestObject>(this.url+'/numero',rest);
  }
}
