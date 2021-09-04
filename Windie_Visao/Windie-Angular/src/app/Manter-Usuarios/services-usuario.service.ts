import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http'
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';


@Injectable({
  providedIn: 'root'
})
export class ServicesUsuarioService {

  private currentUserSubject: BehaviorSubject<Token>;
  public currentUser: Observable<Token>;
  //url : string = 'http://localhost:8080/usuario';
  url : string = 'http://localhost:4200/api/usuario';

  constructor(private http: HttpClient) {

    this.currentUserSubject = new BehaviorSubject<Token>(JSON.parse(localStorage.getItem('currentUser')!));
    this.currentUser = this.currentUserSubject.asObservable();


   }

  public cadastrarUsuario(body: any):Observable<Post>{

    return this.http.post<Post>(this.url+"/cadastrar",body);

  }

  public autenticarUsuario(body: any):Observable<Token>{

    return this.http.post<Token>(this.url+"/login",body).pipe(map(user => {
      // store user details and jwt token in local storage to keep user logged in between page refreshes
      localStorage.setItem('currentUser', JSON.stringify(user));
      this.currentUserSubject.next(user);
      return user;
  }));

  }

  public seUsuarioAutenticado(): boolean {
    var user  =  JSON.parse(localStorage.getItem('currentUser')!)
    this.currentUserSubject.next(user);
    return user.token != "";
  }

  public sairUsuario(){
    localStorage.setItem('currentUser', '{"token": "", "email": "" ,"body": ""}');
  }
}

class Post {
  constructor(
      public id: string,
      public title: string,
      public body: string
  ) { }
}

class Token {
  constructor(
      public token: string,
      public email: string,
      public body: string
  ) { }
}



