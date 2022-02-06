import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-trocar-senha-dialog',
  templateUrl: './trocar-senha-dialog.component.html',
  styleUrls: ['./trocar-senha-dialog.component.css']
})
export class TrocarSenhaDialogComponent implements OnInit {

  form_senha!: FormGroup;
  erro: string = '';
  constructor() {
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
      
    }
  }

  seValid():boolean{
    return this.form_senha.valid;
  }

}
