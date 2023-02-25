import { ActivatedRoute, Router } from "@angular/router";
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { switchMap } from "rxjs";

import { Election } from "@/app/model/election";
import { ElectionService } from "@/app/services/election.service";
import { ElectionResult } from "@/app/model/election_result";

@Component({
  selector: 'app-results',
  templateUrl: 'results-page.component.html',
  styleUrls: ['results-page.component.scss']
})
export class ResultsPageComponent implements OnInit {
  constructor(
    private electionService: ElectionService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar) {
  }

  isLoading = false;
  electionResult?: ElectionResult;

  ngOnInit(): void {
    this.isLoading = true;
    const getElection = this.route.paramMap.pipe(
      switchMap(params => {
        const electionId = params.get('electionId')!;
        return this.electionService.tally(electionId);
      })
    );
    getElection.subscribe({
      next: electionResult => {
        this.electionResult = electionResult;
        this.isLoading = false;
      },
      error: err => {
        this.showErrorMessage(err);
        this.isLoading = false;
      }
    });
  }

  private showErrorMessage(err: any) {
    const message =
      `Oops! There was an unexpected error. Please try again later.\nError: "${err.statusText}"`;
    this.snackBar.open(message, 'Dismiss', { panelClass: 'snackbar-text' });
  }
}
