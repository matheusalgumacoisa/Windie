import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-exluir-jogo-dialog',
  templateUrl: './exluir-jogo-dialog.component.html',
  styleUrls: ['./exluir-jogo-dialog.component.css']
})
export class ExluirJogoDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<ExluirJogoDialogComponent>) { }

  ngOnInit(): void {
  }


  confirmar(){
    this.dialogRef.close('Y');
  }

  cancelar(){
    this.dialogRef.close('N');
  }

}
