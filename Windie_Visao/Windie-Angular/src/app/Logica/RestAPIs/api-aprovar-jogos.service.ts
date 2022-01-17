import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestObject } from 'src/app/Modelos/RestObject';
import { Debug } from '../Debug';
import { ApiAutenticacaoService } from './api-autenticacao.service';

@Injectable({
  providedIn: 'root'
})
export class ApiAprovarJogosService {

  url : string = 'http://localhost:4200/api/aprovar';

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

  public votar(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/votar",rest);
  }

  public seVotou(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    return this.http.post<RestObject>(this.url+"/seVotou",rest);
  }


}
