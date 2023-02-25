import { ActivatedRoute, Router } from "@angular/router";
import { Chart } from "chart.js/auto";
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { switchMap } from "rxjs";

import { Election } from "@/app/model/election";
import { ElectionService } from "@/app/services/election.service";
import { ElectionResult, Choice, Round } from "@/app/model/election_result";

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

  chart?: Chart;
  electionResult?: ElectionResult;
  isLoading = false;
  roundNumber = 0;

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
        // Initially display the final round.
        this.roundNumber = electionResult?.rounds!.length - 1;
        this.loadChart();
        this.isLoading = false;
      },
      error: err => {
        this.showErrorMessage(err);
        this.isLoading = false;
      }
    });
  }

  get roundName(): string {
    if (!this.electionResult?.rounds) {
      return "0";
    }
    const isFinal = this.roundNumber == this.electionResult.rounds.length - 1;
    return `${this.roundNumber + 1}` + (isFinal ? ' (Final)' : '');
  }

  private loadChart() {
    if (!this.electionResult || !this.electionResult.rounds) {
      return;
    }

    // const round = this.electionResult.rounds[this.roundNumber];
    const round: Round = { counts: [] };
    for (let i = 0; i < 11; i++) {
      round.counts.push({
        isEliminated: false,
        text: maketext(Math.random() * 10),
        votesCounted: Math.floor(Math.random() * 4) + 1,
      });
    }

    const data = round.counts.map(c => c.votesCounted);
    const maxVotes = Math.max(...data);
    const barChart = new Chart('resultsCanvas', {
      type: 'bar',
      data: {
        labels: round.counts.map(c => c.text),
        datasets: [{
          label: `Number of votes in round ${this.roundName}`,
          data: round.counts.map(c => c.votesCounted),
          backgroundColor: randomizedColors(),
          borderWidth: 0,
        }],
      },
      options: {
        indexAxis: 'y',
        responsive: true,
        plugins: {
          legend: { position: 'bottom' },
          tooltip: {
            callbacks: {
              label: (item) => `${item.formattedValue} votes`,
            },
          },
        },
        scales: {
          x: {
            beginAtZero: true,
            ticks: {
              stepSize: maxVotes < 10 ? 1 : undefined,
            }
          },
        },
      },
    });

    // const x = new Chart('resultsCanvas', {
    //   type: 'pie',
    //   data: {
    //     labels: round.counts.map(c => c.text),
    //     datasets: [{
    //       label: `Number of votes in round ${this.roundName}`,
    //       data: round.counts.map(c => c.votesCounted),
    //       backgroundColor: randomizedColors(),
    //       borderWidth: 0.5,
    //     }],
    //   },
    //   options: {
    //     responsive: true,
    //     plugins: {
    //       legend: { position: 'bottom' },
    //       tooltip: {
    //         callbacks: {
    //           label: (item) => `${item.formattedValue} votes`,
    //         },
    //       },
    //     },
    //   },
    // });
  }

  private showErrorMessage(err: any) {
    const message =
      `Oops! There was an unexpected error. Please try again later.\nError: "${err.statusText}"`;
    this.snackBar.open(message, 'Dismiss', { panelClass: 'snackbar-text' });
  }
}

function randomizedColors() {
  const colors = [
    'rgb(255, 51, 51)',
    'rgb(255, 153, 51)',
    'rgb(204, 204, 0)',
    'rgb(128, 255, 0)',
    'rgb(0, 204, 204)',
    'rgb(0, 128, 255)',
    'rgb(0, 0, 153)',
    'rgb(192, 192, 192)',
    'rgb(102, 51, 0)',
    'rgb(255, 0, 127)',
  ];
  return colors.sort((a, b) => 0.5 - Math.random());
}



function maketext(length: number) {
  let result = '';
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  const charactersLength = characters.length;
  for (let i = 0; i < length; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }
  return result;
}
