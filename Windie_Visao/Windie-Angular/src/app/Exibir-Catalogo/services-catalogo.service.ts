import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ServicesCatalogoService {


  
  url : string = 'http://localhost:4200/api/catalogo';

  constructor(private http: HttpClient,private router: Router ) { }


  public getJogos(body:any):Observable<Get>{
    return this.http.post<Get>(this.url+"/jogos",body);
  }

  public buscaJogos(body:any):Observable<Get>{
    return this.http.post<Get>(this.url+"/buscar",body);
  }

  getJogo(body:any):Observable<Get> {
    return this.http.post<Get>(this.url+"/jogo",body);
  }

  public seNaBiblioteca(body:any):Observable<Get> {
    return this.http.post<Get>(this.url+"/seJogoBiblioteca",body);
  }

  public adicionarBiblioteca(body:any):Observable<Get> {
    return this.http.post<Get>(this.url+"/adicionarNaBiblioteca",body);
  }

  public removerBiblioteca(body:any):Observable<Get> {
    return this.http.post<Get>(this.url+"/removerDaBiblioteca",body);
  }
}



class Get {
  constructor(
      public id: string,
      public title: string,
      public body: string
  ) { }
}
