import { ActivatedRoute, Router } from "@angular/router";
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { switchMap } from "rxjs";
import { Title } from '@angular/platform-browser';

import { Election } from "@/app/model/election";
import { ElectionService } from "@/app/services/election.service";
import { environment as env } from "@/environments/environment";
import { ShareDialogComponent } from "@/app/components/share-dialog/share-dialog.component";

@Component({
  selector: 'app-vote-component',
  styleUrls: ['vote-page.component.scss'],
  templateUrl: 'vote-page.component.html',
})
export class VotePageComponent implements OnInit {
  constructor(
    private electionService: ElectionService,
    private route: ActivatedRoute,
    private router: Router,
    private shareDialog: ShareDialogComponent,
    private snackBar: MatSnackBar,
    private titleService: Title) {
  }

  choices: string[] = [];
  election?: Election;
  isLoadingElection = false;
  isSubmitting = false;
  options: string[] = [];
  question?: string;

  get isValid() {
    return !!this.choices.length;
  }

  ngOnInit(): void {
    this.isLoadingElection = true;
    const getElection = this.route.paramMap.pipe(
      switchMap(params => {
        let electionId = params.get('electionId');

        // Randomly choose from a set of pre-approved elections if no ID present.
        // TODO: Random should probably be part of a mockable service.
        if (!electionId) {
          console.log('ok');
          const randIdx = Math.floor(Math.random() * env.demoElectionIds.length);
          console.log(randIdx);
          electionId = env.demoElectionIds[randIdx];
          console.log(electionId);
        }

        return this.electionService.getById(electionId!);
      })
    );
    getElection.subscribe({
      next: election => {
        this.choices = [];
        this.election = election;
        this.options = [...election.options || []];
        this.question = election.question;
        this.titleService.setTitle(`${election.question!} - Vote - @gaber.dev`);
        this.isLoadingElection = false;
      },
      error: err => {
        this.showErrorMessage(err);
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

  reset() {
    this.choices = []
    this.options = [...this.election?.options || []];
  }

  showShareDialog() {
    this.shareDialog.open(this.election?.id || '');
  }

  vote() {
    if (!this.isValid) {
      return;
    }

    this.isSubmitting = true;
    this.electionService.vote(this.election?.id!, this.choices).subscribe({
      next: vote => {
        this.router.navigate([`${vote.electionId}/results`]);
        this.reset();
        this.isSubmitting = false;
      },
      error: err => {
        this.showErrorMessage(err);
        this.isSubmitting = false;
      }
    });
  }

  private showErrorMessage(err: any) {
    const message =
      `Oops! There was an unexpected error. Please try again later.\nError: "${err.statusText}"`;
    this.snackBar.open(message, 'Dismiss', { panelClass: 'snackbar-text' });
  }
}
