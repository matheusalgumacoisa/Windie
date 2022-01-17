import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestObject } from 'src/app/Modelos/RestObject';
import { Debug } from '../Debug';
import { ApiAutenticacaoService } from './api-autenticacao.service';
 
@Injectable({
  providedIn: 'root'
})
export class ApiAvaliarJogosService {
 
  url : string = 'http://localhost:4200/api/avaliar';

  constructor(private http: HttpClient,private autentica : ApiAutenticacaoService) { }

  public avaliar(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    Debug.logOutput(JSON.stringify(rest));
    return this.http.post<RestObject>(this.url+"/nota",rest);
  }

  public getAvaliacao(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    Debug.logOutput(JSON.stringify(rest));
    return this.http.post<RestObject>(this.url+"/getAvaliacao",rest);
  }

  public getHorasJogo(body:any):Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),JSON.stringify(body));
    Debug.logOutput(JSON.stringify(rest));
    return this.http.post<RestObject>(this.url+"/getHoras",rest);
  }
}
