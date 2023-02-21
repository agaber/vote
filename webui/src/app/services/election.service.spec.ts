import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';

import { ElectionService } from '@/app/services/election.service';

describe('ElectionService', () => {
  let service: ElectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule],
      providers: [],
    });
    service = TestBed.inject(ElectionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
