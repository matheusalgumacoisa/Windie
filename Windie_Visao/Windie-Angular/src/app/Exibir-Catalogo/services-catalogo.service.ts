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


  public getJogos():Observable<Get>{
    return this.http.get<Get>(this.url+"/jogos");
  }
}



class Get {
  constructor(
      public id: string,
      public title: string,
      public body: string
  ) { }
}
