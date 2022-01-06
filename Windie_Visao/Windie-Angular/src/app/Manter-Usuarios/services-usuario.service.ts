import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http'
import { BehaviorSubject, Observable } from 'rxjs';
import { map, take } from 'rxjs/operators';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class ServicesUsuarioService {

  private currentUserSubject: BehaviorSubject<Token>;
  public currentUser: Observable<Token>;
  public logado : boolean = false;
  assinatura! : string;
 // public se_desenvolvedor : string = 'P';

  url : string = 'http://localhost:4200/api/usuario';


  constructor(private http: HttpClient,private router: Router) {

    this.currentUserSubject = new BehaviorSubject<Token>(JSON.parse(localStorage.getItem('currentUser')!));
    this.currentUser = this.currentUserSubject.asObservable();
    //this.autenticarToken();
   }

  public cadastrarUsuario(body: any):Observable<Post>{

    return this.http.post<Post>(this.url+"/cadastrar",body);

  }

  public cadastrarDesenvolvedor(body: any):Observable<Post>{

    body.token = JSON.parse(localStorage.getItem('currentUser')!).token;
    return this.http.post<Post>(this.url+"/cadastrarDesenvolvedor",body);

  }

  public getNick():Observable<Post>{

    return this.http.post<Post>(this.url+"/nickName",localStorage.getItem('currentUser'));
  }

  public getPapel()/*:Observable<Post>*/{

    this.http.post<Post>(this.url+"/papel",localStorage.getItem('currentUser')).subscribe(success =>{console.log("get papel "+success.body); localStorage.setItem('currentUserPapel', success.body)},err => {console.log("err papel")});;

  }

  public getUsuarioForm():Observable<any>{
    return this.http.post<Post>(this.url+"/usuarioForm",localStorage.getItem('currentUser'))
  }

  public getAssinatura():Observable<Post>{

    return this.http.post<Post>(this.url+"/assinatura",localStorage.getItem('currentUser'));
  }

  public autenticarUsuario(body: any):Observable<Post>{
    return this.http.post<Post>(this.url+"/login",body);/*.subscribe(res => {
      this.logado = true;
      localStorage.setItem('currentUser', JSON.stringify(res));
      this.router.navigate([''])
    },err =>{this.logado = false;console.log('erroooo231'+err)});*/
  }

  public autenticarToken(){

    this.http.post(this.url+"/autorizar",localStorage.getItem('currentUser')).subscribe(res => {
      this.logado = true;
      localStorage.setItem('currentUser', JSON.stringify(res));
      console.log('certo');
      this.getPapel();
      this.getAssinatura().subscribe(success =>{console.log("get ass "+success.body); this.assinatura = success.body},err => {console.log("err ass")});
    },err =>{this.logado = false;console.log('erroooo'+err)});
  }

  public seUsuarioDesenvolvedor(): boolean {
    this.getPapel();
    return localStorage.getItem('currentUserPapel') == 'D';

  }

  public seUsuarioAssinante():boolean{
    if(this.assinatura=='valid'){
      return true;
    }else{
      return false;
    }
  }
  public seUsuarioAutenticado(): boolean {

    //return this.logado;

    var user  =  JSON.parse(localStorage.getItem('currentUser')!)
    this.currentUserSubject.next(user);
    return user!=null && user.token != "";
  }

  public sairUsuario():Observable<Token>{

    return this.currentUser = this.http.post<Token>(this.url+"/logout",JSON.parse(localStorage.getItem('currentUser')!)).pipe(map(tokenBack => {
      // store user details and jwt token in local storage to keep user logged in between page refreshes
      localStorage.setItem('currentUser', '{"token": ""}');
      localStorage.setItem('currentUserPapel','');
      this.currentUserSubject.next(tokenBack);
      return tokenBack ;
    }));
  }

  public atualizarUsuario(body:any):Observable<Post>{
    body.token = JSON.parse(localStorage.getItem('currentUser')!).token;
    return this.http.post<Post>(this.url+"/atualizar",body);
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
      public token: string
  ) { }
}



