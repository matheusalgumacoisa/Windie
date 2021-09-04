import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastrarDesenvolvedorComponent } from './cadastrar-desenvolvedor.component';

describe('CadastrarDesenvolvedorComponent', () => {
  let component: CadastrarDesenvolvedorComponent;
  let fixture: ComponentFixture<CadastrarDesenvolvedorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CadastrarDesenvolvedorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CadastrarDesenvolvedorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
