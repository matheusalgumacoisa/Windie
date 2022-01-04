import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ServiceJogosService {

  url : string = 'http://localhost:4200/api/mjogos';

  constructor(private http: HttpClient) { }


  novoJogo(body:JogoRest){

    return this.http.post(this.url+'/novo',body);
  }

}

class JogoRest {
  constructor(
    public titulo: string,
    public tags: string,
    public descricao: string,
    public caminho_executavel: string,
    public detalhes: string,
    public trailer: string,
    public visibilidade: string,
    public genero: number,
    public token: string,
    public imagem_capa: any
  ) { }
}

