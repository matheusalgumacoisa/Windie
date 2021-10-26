import { TestBed } from '@angular/core/testing';

import { ServicesCatalogoService } from './services-catalogo.service';

describe('ServicesCatalogoService', () => {
  let service: ServicesCatalogoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicesCatalogoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
