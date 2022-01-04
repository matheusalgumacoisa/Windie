import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ServicesAuxService {
 
 // public se_desenvolvedor : string = 'P';

  url : string = 'http://localhost:4200/api/jogos';



  constructor(private http: HttpClient) { }

  getGeneros(){
    return this.http.get<Rest>(this.url+"/generos");
  }

}


class Rest {
  constructor(
      public id: string,
      public title: string,
      public body: string
  ) { }
}
