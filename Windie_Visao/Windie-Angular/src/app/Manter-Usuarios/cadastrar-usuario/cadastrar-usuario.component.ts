import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup} from '@angular/forms';
import { ServicesUsuarioService } from '../services-usuario.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cadastrar-usuario',
  templateUrl: './cadastrar-usuario.component.html',
  styleUrls: ['./cadastrar-usuario.component.css']
})
export class CadastrarUsuarioComponent implements OnInit {

  form_cadastrar: FormGroup;

  constructor(private back:ServicesUsuarioService,private router: Router) { 

    this.form_cadastrar = new FormGroup({

      email: new FormControl(null),
      email_confirm: new FormControl(null),
      senha: new FormControl(null),
      senha_confirm: new FormControl(null),
      apelido: new FormControl(null)

    });

  }

  ngOnInit(): void {

    
  }

  onSubmit(){
   // console.log(this.form_cadastrar);
   console.log("aaa"+localStorage.getItem('currentUser')!);
    this.back.cadastrarUsuario(this.form_cadastrar.value).subscribe(sucesso =>{ this.router.navigate(['/login'])}, fracasso =>{});
  }

}

