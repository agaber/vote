import { ActivatedRoute, Router } from "@angular/router";
import { Component, OnInit } from '@angular/core';
import { ElectionService } from "@/app/services/election.service";
import { Observable, switchMap } from "rxjs";
import { Election } from "@/app/model/election";

@Component({
  selector: 'app-vote-component',
  styleUrls: ['vote-component.component.scss'],
  templateUrl: 'vote-component.component.html',
})
export class VoteComponentComponent implements OnInit {
  election$?: Observable<Election>;

  constructor(
    private electionService: ElectionService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit(): void {
    this.election$ = this.route.paramMap.pipe(
      switchMap(params => {
        // TODO not found
        const electionId = params.get('electionId') || '';
        return this.electionService.getById(electionId);
      })
    );
  }
}
