import { TestBed } from '@angular/core/testing';

import { ServicesAuxService } from './services-aux.service';

describe('ServicesAuxService', () => {
  let service: ServicesAuxService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicesAuxService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
