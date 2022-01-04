import { TestBed } from '@angular/core/testing';

import { ServiceJogosService } from './service-jogos.service';

describe('ServiceJogosService', () => {
  let service: ServiceJogosService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServiceJogosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
