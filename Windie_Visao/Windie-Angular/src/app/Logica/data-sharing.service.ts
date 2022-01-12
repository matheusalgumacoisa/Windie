import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataSharingService { //classe define eventos que podem ser emitidos por uma classe e escutados por outra
  public usuarioCarregado: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  public erroLogin: BehaviorSubject<string> = new BehaviorSubject<string>('');
  constructor() { }
}
