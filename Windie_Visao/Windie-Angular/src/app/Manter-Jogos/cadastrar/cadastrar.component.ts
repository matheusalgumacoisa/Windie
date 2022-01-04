import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ServicesCatalogoService } from 'src/app/Exibir-Catalogo/services-catalogo.service';
import { ServicesAuxService } from 'src/app/services-aux.service';
import { ServiceJogosService } from '../service-jogos.service';

@Component({
  selector: 'app-cadastrar',
  templateUrl: './cadastrar.component.html',
  styleUrls: ['./cadastrar.component.css']
})
export class CadastrarComponent implements OnInit {

  form_cadastrar: FormGroup;
  capaFile!: { link: string; file: any; name: string; };
  screenshotFiles!: {link: string; file: any; name: string; }[];//= [{link: 'string', file: 'any', name: 'string' }];
  //screenshotFiles!: Array<{ link: string; file: any; name: string; }> = {link: 'string', file: 'any', name: 'string' };
  generos!: Genero[];

   constructor(private serv : ServicesAuxService,private jogoService : ServiceJogosService ) {
    this.form_cadastrar = new FormGroup({
      titulo: new FormControl(null,Validators.required),
      tags: new FormControl(null,Validators.required),
      descricao: new FormControl(null,Validators.required),
      executavel: new FormControl(null,Validators.required),
      detalhes: new FormControl(null,Validators.required),
      trailer: new FormControl(null,Validators.required),
      genero: new FormControl(null,Validators.required),
      imagem_capa: new FormControl(null,Validators.required),
      salvarTipo : new FormControl()
      //screenshot: new FormControl(null,Validators.required)
    });
    this.loadGeneros();
   }


  ngOnInit(): void {
  }

  onSubimt(){


    let JogoRest =  {titulo: this.form_cadastrar.get('titulo')?.value,
      tags: this.form_cadastrar.get('tags')?.value,
      descricao: this.form_cadastrar.get('descricao')?.value,
      caminho_executavel: this.form_cadastrar.get('executavel')?.value,
      detalhes: this.form_cadastrar.get('detalhes')?.value,
      trailer: this.form_cadastrar.get('trailer')?.value,
      imagem_capa: this.capaFile ? this.capaFile.link : 'localhost/image.png',
      visibilidade: this.form_cadastrar.get('salvarTipo')?.value,
      genero: this.form_cadastrar.get('genero')?.value,
      token:  JSON.parse(localStorage.getItem('currentUser')!).token}; 

      console.log("formulario enviado: " + JSON.stringify(JogoRest/*JogoRest + this.form_cadastrar.value*/));
    this.jogoService.novoJogo(JogoRest).subscribe(succses => { console.log("formulario enviado com sucesso");}, err => {console.log("formulario não enviado com sucesso");});

  }

  loadGeneros(){
    this.serv.getGeneros().subscribe( success =>{ console.log("genero json:" +JSON.stringify( success.body));this.generos = JSON.parse(JSON.stringify( success.body))}, err =>{console.log("genero erro: "+JSON.stringify(err));})
  }

  screenshotPreview(event: any,index: number){
    if (event.target.files && event.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (_event: any) => {
          if(!this.sePossuiScreenshotsCarregadas()) this.screenshotFiles = this.screenshotFiles || [];
          this.screenshotFiles.push({
            link: _event.target.result,
            file: event.srcElement.files[0],
            name: event.srcElement.files[0].name
          });
      /*    this.screenshotFiles[index] = {
              link: _event.target.result,
              file: event.srcElement.files[0],
              name: event.srcElement.files[0].name
          };*/
      };
      reader.readAsDataURL(event.target.files[0]);
    }
  }

  getScreenshotLink(index:number):string{
    return 'localhost/image'+index+'.png';
  }

  sePossuiScreenshotsCarregadas():boolean{
    if(this.screenshotFiles != null && this.screenshotFiles.length >0) return true
    else return false;

  }

  removeScreenshot(index:number){
    this.screenshotFiles.splice(index,1);
  }

  imagesPreview(event: any) {
    if (event.target.files && event.target.files[0]) {
        const reader = new FileReader();

        reader.onload = (_event: any) => {
            this.capaFile = {
                link: _event.target.result,
                file: event.srcElement.files[0],
                name: event.srcElement.files[0].name
            };
        };
        reader.readAsDataURL(event.target.files[0]);
    }
  }

  save(): void {
      const formData = new FormData();
      formData.append('myImageToSend', this.capaFile.file);
      formData.append('title', 'Set your title name here');
      formData.append('description', 'Set your title description here');

  }
}

class Genero {
  constructor(
      public genero_id: string,
      public genero_nome: string,
  ) { }
}
