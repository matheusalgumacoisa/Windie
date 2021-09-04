import { TestBed } from '@angular/core/testing';

import { ServicesUsuarioService } from './services-usuario.service';

describe('ServicesUsuarioService', () => {
  let service: ServicesUsuarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicesUsuarioService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
