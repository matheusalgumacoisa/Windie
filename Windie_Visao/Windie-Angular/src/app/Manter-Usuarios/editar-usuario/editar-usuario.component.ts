import { Component, OnInit } from '@angular/core';
import { ServicesUsuarioService } from '../services-usuario.service';
import { CommonModule } from '@angular/common';  
import { BrowserModule } from '@angular/platform-browser';
import { FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-editar-usuario',
  templateUrl: './editar-usuario.component.html',
  styleUrls: ['./editar-usuario.component.css']
})
export class EditarUsuarioComponent implements OnInit {

  papel! : string;
  assinatura! : string;
 // apelido! : string;

  form_cadastrar: FormGroup;

  constructor(private usuarioService: ServicesUsuarioService,private router: Router) { 
    this.form_cadastrar = new FormGroup({

      apelido: new FormControl(null),
      nome_desenvolvedor: new FormControl('null'),
      agencia: new FormControl('null'),
      conta: new FormControl('null')

    });    
  }

  ngOnInit(): void {
    this.usuarioService.getPapel().subscribe(success =>{console.log("get papel"); this.papel = success.body},err => {console.log("err papel")});
    this.usuarioService.getAssinatura().subscribe(success =>{console.log("get ass "+success.body); this.assinatura = success.body},err => {console.log("err ass")});
    this.usuarioService.getUsuarioForm().subscribe(success =>{this.form_cadastrar.setValue(success);console.log("form"+success);},err => {console.log("err form "+JSON.stringify(err))});

  }

  onSubmit(){

    this.usuarioService.atualizarUsuario(this.form_cadastrar.value).subscribe(success =>{this.router.navigate([''])});
  }
  
}
