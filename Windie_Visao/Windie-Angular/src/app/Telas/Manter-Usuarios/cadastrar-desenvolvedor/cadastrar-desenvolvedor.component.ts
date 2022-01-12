import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ApiAutenticacaoService } from 'src/app/Logica/RestAPIs/api-autenticacao.service';
import { ApiManterUsuario } from 'src/app/Logica/RestAPIs/apiManterUsuario';


@Component({
  selector: 'app-cadastrar-desenvolvedor',
  templateUrl: './cadastrar-desenvolvedor.component.html',
  styleUrls: ['./cadastrar-desenvolvedor.component.css']
})
export class CadastrarDesenvolvedorComponent implements OnInit {

  form_cadastrar: FormGroup;
  erro:string = '';

  constructor(private usuarioService: ApiManterUsuario,private router: Router,private autenticacao :ApiAutenticacaoService) { 
    this.form_cadastrar = new FormGroup({

      nome_de_desenvolvedor: new FormControl('',Validators.required),
      agencia_bancaria: new FormControl('',Validators.required),
      conta_bancaria: new FormControl('',Validators.required),
      token: new FormControl('')

    });  
  }

  ngOnInit(): void {



  }

  onSubmit(){

    this.usuarioService.cadastrarDesenvolvedor(this.form_cadastrar.value).subscribe(
      retorno => {
        if(retorno.sucesso){
          this.autenticacao.setToken(retorno.token!);
          this.autenticacao.carregarUsuario();
          this.router.navigate([''])
        } else{
          if(retorno.erro!.cod == 1){ //erro de autenticação do token
            this.autenticacao.logOut();
            this.router.navigate(['']);
          }else{
            this.autenticacao.setToken(retorno.token!);
            this.autenticacao.carregarUsuario();
            this.erro = retorno.erro!.mensagem;
          }
        }
      },erro => {
            this.erro = "Erro ao cadastrar";
      });
  }


}
