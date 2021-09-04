import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
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
      email: new FormControl(null),
      senha: new FormControl(null)
    });


   }

  ngOnInit(): void {
  }

  onSubmit(){
    console.log(this.form_autenticar);
    console.log("logando...");
    this.back.autenticarUsuario(this.form_autenticar.value).subscribe(sucesso =>{ 
      
      if(sucesso.body = "autenticado"){

        localStorage.setItem('user_logged',JSON.stringify(sucesso));

      }
    
    }, fracasso =>{

      console.log("fracasso:");
      console.log(fracasso.error);
      console.log(fracasso);
    });
  }

}
