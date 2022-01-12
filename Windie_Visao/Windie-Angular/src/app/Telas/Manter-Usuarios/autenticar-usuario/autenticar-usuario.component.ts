import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DataSharingService } from 'src/app/Logica/data-sharing.service';
import { ApiAutenticacaoService } from 'src/app/Logica/RestAPIs/api-autenticacao.service';



@Component({
  selector: 'app-autenticar-usuario',
  templateUrl: './autenticar-usuario.component.html',
  styleUrls: ['./autenticar-usuario.component.css']
})
export class AutenticarUsuarioComponent implements OnInit {

  form_autenticar: FormGroup;
  erro:string = '';

  constructor(private router: Router, private autenticacao: ApiAutenticacaoService, dataSharing : DataSharingService) {

    this.form_autenticar= new FormGroup({
      email: new FormControl(null,[Validators.required,Validators.email]),
      senha: new FormControl(null,Validators.required)
    });
    
    dataSharing.erroLogin.subscribe( mensagem =>{
      this.erroNoLogin(mensagem);
    });
   }

  ngOnInit(): void {
  }

  onSubmit(){
    this.erro = '';
    this.autenticacao.logIn(this.form_autenticar.get('email')?.value,this.form_autenticar.get('senha')?.value);
  }

  getErroAutencicacao():string{
    return this.erro; 
  }

  erroNoLogin(mensagem : string){ //chamado quando a api de autenticação emite um evento de erro
    this.erro = mensagem;
  }

}
