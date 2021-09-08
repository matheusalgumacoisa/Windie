import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ServicesUsuarioService } from '../services-usuario.service';

@Component({
  selector: 'app-cadastrar-desenvolvedor',
  templateUrl: './cadastrar-desenvolvedor.component.html',
  styleUrls: ['./cadastrar-desenvolvedor.component.css']
})
export class CadastrarDesenvolvedorComponent implements OnInit {

  form_cadastrar: FormGroup;
  erro:string = '';

  constructor(private usuarioService: ServicesUsuarioService,private router: Router) { 
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
      success => {
        this.router.navigate([''])
      },err => {
        if(JSON.stringify(err.error.status=="500")){
          this.erro = JSON.stringify(err.error.message);
        }else{
            this.erro = "Erro ao cadastrar";
        }
        console.log("erro: "+JSON.stringify(err.error));
      });
  }


}
