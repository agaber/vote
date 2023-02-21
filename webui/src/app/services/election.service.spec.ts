import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { Election } from '@/app/model/election';
import { ElectionService } from '@/app/services/election.service';
import { environment as env } from '@/environments/environment';

describe('ElectionService', () => {
  let httpMock: HttpTestingController;
  let service: ElectionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [],
    });
    httpMock = TestBed.inject(HttpTestingController);
    service = TestBed.inject(ElectionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('test environment variables should be enabled', () => {
    expect(env.apiUrl).toEqual("htpps://notreal.test/vote/api/v1");
  });

  it('should create new elections', () => {
    const newElection: Election = {
      question: 'What is the fish of the day?',
      options: ['Alaskan salmon', 'Cod', 'Sardines', 'Swordfish', 'Herring']
    };

    const expectedElection: Election = { ...newElection, id: '11111' };
    service.create(newElection).subscribe(result => {
      expect(result).toEqual(expectedElection);
    });

    const req = httpMock.expectOne(`${env.apiUrl}/elections`);
    expect(req.request.method).toEqual('POST');
    req.flush(expectedElection);
  });
});
