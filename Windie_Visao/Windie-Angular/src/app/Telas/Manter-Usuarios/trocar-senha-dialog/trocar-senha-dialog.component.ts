import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { ApiAutenticacaoService } from 'src/app/Logica/RestAPIs/api-autenticacao.service';
import { ApiManterUsuario } from 'src/app/Logica/RestAPIs/apiManterUsuario';

@Component({
  selector: 'app-trocar-senha-dialog',
  templateUrl: './trocar-senha-dialog.component.html',
  styleUrls: ['./trocar-senha-dialog.component.css']
})
export class TrocarSenhaDialogComponent implements OnInit {

  form_senha!: FormGroup;
  erro: string = '';
  constructor(private apiUsuario: ApiManterUsuario, private apiAutenticacao : ApiAutenticacaoService, private dialogRef: MatDialogRef<TrocarSenhaDialogComponent>) {
    this.form_senha = new FormGroup({

      antiga: new FormControl(null,Validators.required),
      nova: new FormControl(null,Validators.required),
      confirmar:  new FormControl(null,Validators.required)

    });  

   }

  ngOnInit(): void {

  }

  onSubmit(){
    if(this.seValid()){
      this.apiUsuario.trocarSenha(this.form_senha.value).subscribe(retorno =>{
        if(!retorno.sucesso){
          if(retorno.erro!.cod == 1){
            this.apiAutenticacao.logOut();
          }else{
            this.apiAutenticacao.setToken(retorno.token!);
            this.erro = retorno.erro!.mensagem;
          }
        }else{
          this.apiAutenticacao.setToken(retorno.token!);
          this.dialogRef.close();
        }
      },erro =>{
        this.erro = 'Erro ao alterar senha';
      })
    }
  }

  seValid():boolean{
    return this.form_senha.valid;
  }

}
