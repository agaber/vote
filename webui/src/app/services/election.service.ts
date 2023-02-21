import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment as env } from '@/environments/environment';
import { Election } from '@/app/model/election';
import { Observable } from 'rxjs';

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
}
