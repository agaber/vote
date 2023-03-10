import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { Election } from '@/app/model/election';
import { ElectionService } from '@/app/services/election.service';
import { environment as env } from '@/environments/environment';
import { Vote } from '@/app/model/vote';
import { ElectionResult } from '../model/election_result';

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
    expect(req.request.body).toEqual(newElection);
    req.flush(expectedElection);
  });

  it('should get an election by ID', () => {
    const election: Election = {
      id: '12345',
      question: 'Will this test pass?',
      options: ['Yes', 'Yep', 'naturally'],
    };

    service.getById(election.id!).subscribe(result => {
      expect(result).toEqual(election);
    });

    const req = httpMock.expectOne(`${env.apiUrl}/elections/${election.id}`);
    expect(req.request.method).toEqual('GET');
    req.flush(election);
  });

  it('should vote in an election', () => {
    const election: Election = {
      id: '12345',
      question: 'Will this test pass?',
      options: ['Yes', 'Yep', 'naturally'],
    };

    const vote: Vote = {
      id: '54321',
      electionId: election.id,
      choices: ['Yes', 'Yep'],
    }

    service.vote(election.id!, vote.choices).subscribe(result => {
      expect(result).toEqual(vote);
    });

    const req = httpMock.expectOne(`${env.apiUrl}/elections/${election.id}:vote`);
    expect(req.request.method).toEqual('POST');
    expect(req.request.body).toEqual({ choices: vote.choices });
    req.flush(vote);
  });

  it('should tally the results of an election', () => {
    const electionId = '12345';

    const election: Election = {
      id: electionId,
      question: 'Something something millenials?',
      options: ['Yes', 'No', 'IDK'],
    };

    const expectedElectionResult: ElectionResult = {
      election: election,
      rounds: [],
      winner: "Everyone!",
    }

    service.tally(electionId).subscribe(result => {
      expect(result).toEqual(expectedElectionResult);
    });

    const req = httpMock.expectOne(`${env.apiUrl}/elections/${electionId}:tally`);
    expect(req.request.method).toEqual('POST');
    req.flush(expectedElectionResult);
  });
});

describe('test environment', () => {
  it('should make variables available with expected values', () => {
    expect(env.apiUrl).toEqual("https://notreal.test/vote/api/v1");
    expect(env.uiUrl).toEqual("https://ui.notreal.test");
  });
})
