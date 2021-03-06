import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { Debug } from 'src/app/Logica/Debug';
import { ApiManterUsuario } from 'src/app/Logica/RestAPIs/apiManterUsuario';

@Component({
  selector: 'app-cadastrar-usuario',
  templateUrl: './cadastrar-usuario.component.html',
  styleUrls: ['./cadastrar-usuario.component.css']
})
export class CadastrarUsuarioComponent implements OnInit {

  form_cadastrar: FormGroup;
  erro: string = '';

  constructor(private back:ApiManterUsuario,private router: Router) { 

    this.form_cadastrar = new FormGroup({

      email: new FormControl(null,[Validators.required,Validators.email]),
      email_confirm: new FormControl(null,[Validators.required,Validators.email]),
      senha: new FormControl(null,Validators.required),
      senha_confirm: new FormControl(null,Validators.required),
      apelido: new FormControl(null,Validators.required)

    });

  }

  ngOnInit(): void {

    
  }

  onSubmit(){
    if(this.form_cadastrar.valid){
      console.log(this.form_cadastrar);
      console.log("aaa"+localStorage.getItem('currentUser')!);
      this.back.cadastrarUsuario(this.form_cadastrar.value).subscribe(retorno =>{
        if(retorno.sucesso){ 
          this.router.navigate(['/login']);
        }else{
          Debug.logFalhaNaValidacao(retorno.erro!.mensagem);
          this.erro = retorno.erro!.mensagem;
        }
      }, 
      fracasso =>{
        this.erro = "Erro ao cadastrar";
      });
    }
  }

  aplicaCssErro(campo: any){

    return {
      'has-error': this.verificaValidTouched(campo),
      'has-feedback': this.verificaValidTouched(campo)

    }
  }

  verificaValidTouched(campo: any){

    return  !(!this.form_cadastrar.get(campo)!.valid &&  this.form_cadastrar.get(campo)!.touched);
  }

  verificaCampoBate(value: any, value_confirma: any){

    return !(this.form_cadastrar.get(value)!.value != this.form_cadastrar.get(value_confirma)!.value &&  this.form_cadastrar.get(value_confirma)!.touched);
  }

}

