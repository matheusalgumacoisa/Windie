import { HttpClient, HttpEvent } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { RestObject } from 'src/app/Modelos/RestObject';
import { ApiAutenticacaoService } from './api-autenticacao.service';


@Injectable({
  providedIn: 'root'
})
export class ApiManterJogos {

  url : string = 'http://localhost:4200/api/jogos';
  uploadProgress:number = -1;

  constructor(private http: HttpClient,private autentica : ApiAutenticacaoService) { }


  public novoJogo(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+'/novo',rest);
  }

  public atualizarJogo(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+'/atualizar',rest);
  }

  public getScreenshots(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+'/screenshots',rest);
  }

  public getJogosByDesenvolvedor():Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),'');
    return this.http.post<RestObject>(this.url+'/jogosByDesenvolvedor', rest);
  }

  public getGeneros():Observable<RestObject>{
    return this.http.get<RestObject>(this.url+"/generos");
  }

  public getJogo(body:any):Observable<RestObject> {
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/jogo",rest);
  }

  public salvarArquivos(body:any):Observable<HttpEvent<RestObject>>{
    //let uploadSub: Subscription;

    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/salvarArquivos",rest,{
      reportProgress: true,
      observe: 'events'
    }).pipe(
      //finalize(() => this.reset()
    );
  }

  public getFilesInfo(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/filesInfo",rest);
  }

  public excluirRascunho(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+'/excluir',rest);
  }
 
}



