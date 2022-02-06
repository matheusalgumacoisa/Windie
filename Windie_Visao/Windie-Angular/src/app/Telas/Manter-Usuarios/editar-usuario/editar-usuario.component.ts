import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { DataSharingService } from 'src/app/Logica/data-sharing.service';
import { Debug } from 'src/app/Logica/Debug';
import { ApiAutenticacaoService } from 'src/app/Logica/RestAPIs/api-autenticacao.service';
import { ApiManterUsuario } from 'src/app/Logica/RestAPIs/apiManterUsuario';
import { Desenvolvedor } from 'src/app/Modelos/Desenvolvedor';
import { UsuarioSessao } from 'src/app/Modelos/UsuarioSessao';
import { TrocarSenhaDialogComponent } from '../trocar-senha-dialog/trocar-senha-dialog.component';
 
@Component({
  selector: 'app-editar-usuario',
  templateUrl: './editar-usuario.component.html',
  styleUrls: ['./editar-usuario.component.css']
})
export class EditarUsuarioComponent implements OnInit {

  erro:string = '';
  usuario!: UsuarioSessao;
  desenvolvedor! : Desenvolvedor;

  form_cadastrar!: FormGroup;

  constructor(private usuarioService: ApiManterUsuario,private router: Router,private autenticacao :ApiAutenticacaoService,private dataSharing: DataSharingService, private dialog : MatDialog) {

    this.form_cadastrar = new FormGroup({

      apelido: new FormControl(null,Validators.required),
      nome_desenvolvedor: new FormControl(null,Validators.required),
      //agencia: new FormControl(null,Validators.required),
      //conta: new FormControl(null,Validators.required)
      email_paypal:  new FormControl(null,[Validators.required,Validators.email])

    });   
    dataSharing.usuarioCarregado.subscribe(retorno =>{ //quando usuario carregado carrega-lo para essa pagina
      Debug.logDetalhe('Editando: '+autenticacao.usuario?.apelido!);
      if(this.autenticacao.seUsuarioDesenvolvedor()){

        usuarioService.getDesenvolvedor().subscribe(
          retorno =>{
            let desenvolvedor :Desenvolvedor = JSON.parse(retorno.body);
            Debug.logDetalhe('dev: '+JSON.stringify(retorno.body));
            this.form_cadastrar = new FormGroup({

              apelido: new FormControl(this.autenticacao.usuario?.apelido,Validators.required),
              nome_desenvolvedor: new FormControl(this.autenticacao.usuario?.nome_desenvolvedor,Validators.required),
              //agencia: new FormControl(desenvolvedor.agencia_bancaria,Validators.required),
              //conta: new FormControl(desenvolvedor.conta_bancaria,Validators.required)
              email_paypal:  new FormControl(desenvolvedor.email_paypal,[Validators.required,Validators.email])
    
            });  
          },erro =>{

          }
        );  
      }else{
        this.form_cadastrar = new FormGroup({

          apelido: new FormControl(this.autenticacao.usuario?.apelido,Validators.required),
          nome_desenvolvedor: new FormControl(this.autenticacao.usuario?.nome_desenvolvedor,Validators.required),
          //agencia: new FormControl(null,Validators.required),
          //conta: new FormControl(null,Validators.required)
          email_paypal:  new FormControl('',[Validators.required,Validators.email])

        });
      }
    });
    
  }

  ngOnInit(): void {

  }

  onSubmit(){

    this.usuarioService.atualizarUsuario(this.form_cadastrar.value).subscribe(
      retorno =>{
        Debug.logRequest('atualizar ususario callback: '+JSON.stringify(retorno));
        if(retorno.sucesso){
          this.autenticacao.setToken(retorno.token!);
          this.autenticacao.carregarUsuario();
          this.router.navigate(['/login']);
        }else{
          Debug.logErro(retorno.erro!.mensagem);
          if(retorno.erro!.cod == 1){
            this.autenticacao.logOut();
            this.router.navigate(['']);
          }else{
            this.autenticacao.setToken(retorno.token!);
            this.erro = retorno.erro!.mensagem;
            Debug.logErro(retorno.erro!.mensagem);
            Debug.logErro('stack trace: '+retorno.erro!.stackTrace);
          }
        }
      },
      erro =>{
        this.erro = "Erro ao atualizar";
        console.log("erro: "+JSON.stringify(erro.error));
      });
  }

  seValid():boolean{
    if(this.seDesenvolvedor()){
      return  this.form_cadastrar.valid;
    }else{

      return this.form_cadastrar.get('apelido')!.valid;
      
    }


  }

  seAssinante():boolean{
    return this.autenticacao.seAssinante();
  }

  seDesenvolvedor():boolean{
    return this.autenticacao.seUsuarioDesenvolvedor();
  }

  mudarSenha(){
    let dialogRef = this.dialog.open(TrocarSenhaDialogComponent, {
      height: '600px',
      width: '600px',
    });
  }
  
}
