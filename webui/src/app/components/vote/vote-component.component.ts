import { ActivatedRoute, Router } from "@angular/router";
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { switchMap } from "rxjs";

import { ElectionService } from "@/app/services/election.service";
import { Election } from "@/app/model/election";

@Component({
  selector: 'app-vote-component',
  styleUrls: ['vote-component.component.scss'],
  templateUrl: 'vote-component.component.html',
})
export class VoteComponentComponent implements OnInit {
  choices: string[] = [];
  election?: Election;
  isLoadingElection = false;
  isSubmitting = false;
  options: string[] = [];
  question?: string;

  constructor(
    private electionService: ElectionService,
    private route: ActivatedRoute,
    private router: Router,
    private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.isLoadingElection = true;
    const getElection = this.route.paramMap.pipe(
      switchMap(params => {
        const electionId = params.get('electionId')!;
        // TODO: Figure out what to do if the electionId isn't in the URL.
        // Maybe show a random election? Could be a good homepage use case?
        return this.electionService.getById(electionId!);
      })
    );
    getElection.subscribe({
      next: election => {
        this.choices = [];
        this.election = election;
        this.options = election.options || [];
        this.question = election.question;
        this.isLoadingElection = false;
      },
      error: err => {
        const message =
          `Oops! There was an unexpected error. Please try again later.\nError: "${err.statusText}"`;
        this.snackBar.open(message, 'Dismiss', { panelClass: 'snackbar-text' });
        this.isLoadingElection = false;
      }
    });
  }

  choiceDropped(event: CdkDragDrop<string[]>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,
      );
    }
  }

  vote() {
    console.log('+++ vote');
    this.isSubmitting = true;
  }
}
