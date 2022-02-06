import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RestObject } from 'src/app/Modelos/RestObject';
import { Debug } from '../Debug';
import { ApiAutenticacaoService } from './api-autenticacao.service';

@Injectable({
  providedIn: 'root'
})
export class ApiManterAssinaturaService {

  url : string = 'http://localhost:4200/api/assinatura';

  
  processando_ass : boolean = false;

  constructor(private http: HttpClient,private autentica : ApiAutenticacaoService) {
    this.getProcessandoAssinatura();
   }

  public getCheckoutToken():Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),"JSON.stringify(body)");
    Debug.logOutput(JSON.stringify(rest));
    return this.http.post<RestObject>(this.url+"/checkoutCode",rest);
  }

  public getCheckoutURL():Observable<RestObject>{
    let rest : RestObject  = new RestObject(this.autentica.getToken(),"null");
    return this.http.post<RestObject>(this.url+"/getURL",rest);
  }

  seProcessandoAssinatura(): boolean {
    return this.processando_ass;
  }

  private getProcessandoAssinatura(){
    let rest : RestObject  = new RestObject(this.autentica.getToken(),'null');
    this.http.post<RestObject>(this.url+"/pendente",rest).subscribe( retorno =>{
      if(retorno.sucesso){
        if(this.processando_ass !=  JSON.parse(JSON.stringify(retorno.body))){
         // Debug.logDetalhe('pagamento pendente '+retorno.body);
          this.processando_ass =  JSON.parse(retorno.body);
        }
      }else{
        if(this.processando_ass)
        this.processando_ass = false;
      }
    },erro =>{
      if(this.processando_ass)
      this.processando_ass = false;
    });
  }
}
