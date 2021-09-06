import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ServicesUsuarioService } from '../services-usuario.service';

@Component({
  selector: 'app-autenticar-usuario',
  templateUrl: './autenticar-usuario.component.html',
  styleUrls: ['./autenticar-usuario.component.css']
})
export class AutenticarUsuarioComponent implements OnInit {

  form_autenticar: FormGroup;

  constructor(private back:ServicesUsuarioService,private router: Router) {

    this.form_autenticar= new FormGroup({
      email: new FormControl(null,[Validators.required,Validators.email]),
      senha: new FormControl(null,Validators.required)
    });
 

   }

  ngOnInit(): void {
  }

  onSubmit(){
    console.log(this.form_autenticar);
    console.log("logando...");
    this.back.autenticarUsuario(this.form_autenticar.value);
  }

}
