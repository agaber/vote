import { ActivatedRoute, Router } from "@angular/router";
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { switchMap } from "rxjs";

import { Election } from "@/app/model/election";
import { ElectionService } from "@/app/services/election.service";

@Component({
  selector: 'app-results',
  templateUrl: 'results-page.component.html',
  styleUrls: ['results-page.component.scss']
})
export class ResultsPageComponent {
  constructor(
    private electionService: ElectionService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar) {
  }
}
