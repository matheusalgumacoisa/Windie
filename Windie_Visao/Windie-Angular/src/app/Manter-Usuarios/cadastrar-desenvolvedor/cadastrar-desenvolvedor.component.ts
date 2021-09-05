import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { ServicesUsuarioService } from '../services-usuario.service';

@Component({
  selector: 'app-cadastrar-desenvolvedor',
  templateUrl: './cadastrar-desenvolvedor.component.html',
  styleUrls: ['./cadastrar-desenvolvedor.component.css']
})
export class CadastrarDesenvolvedorComponent implements OnInit {

  form_cadastrar: FormGroup;

  constructor(private usuarioService: ServicesUsuarioService,private router: Router) { 
    this.form_cadastrar = new FormGroup({

      nome_de_desenvolvedor: new FormControl(''),
      agencia_bancaria: new FormControl(''),
      conta_bancaria: new FormControl(''),
      token: new FormControl('')

    });  
  }

  ngOnInit(): void {



  }

  onSubmit(){

    this.usuarioService.cadastrarDesenvolvedor(this.form_cadastrar.value).subscribe(success => {this.router.navigate([''])});
  }
}
