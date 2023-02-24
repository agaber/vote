import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Election } from '@/app/model/election';
import { environment as env } from '@/environments/environment';
import { Observable } from 'rxjs';
import { Vote } from '@/app/model/vote';

const HTTP_OPTONS = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
  }),
};

@Injectable({ providedIn: 'root' })
export class ElectionService {

  constructor(private http: HttpClient) { }

  create(election: Election): Observable<Election> {
    const url = `${env.apiUrl}/elections`
    return this.http.post<Election>(url, election, HTTP_OPTONS);
  }

  getById(electionId: string): Observable<Election> {
    const url = `${env.apiUrl}/elections/${electionId}`;
    return this.http.get<Election>(url);
  }

  vote(electionId: string, choices: string[]): Observable<Vote> {
    const url = `${env.apiUrl}/elections/${electionId}:vote`;
    const body = { choices };
    return this.http.post<Vote>(url, body, HTTP_OPTONS);
  }

  tally(electionId: string): Observable<string> {
    const url = `${env.apiUrl}/elections/${electionId}:tally`;
    return this.http.post<string>(url, undefined, HTTP_OPTONS);
  }
}
