import { ActivatedRoute, Router } from "@angular/router";
import { Chart } from "chart.js/auto";
import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { switchMap } from "rxjs";
import { Title } from '@angular/platform-browser';

import { ElectionService } from "@/app/services/election.service";
import { ElectionResult } from "@/app/model/election_result";
import { ShareDialogComponent } from "@/app/components/share-dialog/share-dialog.component";


/** Supported chart types. String values are used as event signals. */
export enum ChartType {
  HBAR = 'hbar',
  VBAR = 'vbar',
  PIE = 'pie',
}


/**
 * Choose from a set of colors in random order (note: the order is random not
 * the colors). These values should not change after user clicks around though.
 */
const COLORS = randomizedColors();


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
    private shareDialog: ShareDialogComponent,
    private snackBar: MatSnackBar,
    private titleService: Title) {
  }

  chart?: Chart<any, any, any>;
  chartType = ChartType.HBAR;
  electionResult?: ElectionResult;
  isLoading = false;
  roundNumber = 0;

  get numberOfRounds() {
    return (this.electionResult?.rounds || []).length;
  }

  get roundLabel(): string {
    if (!this.electionResult?.rounds) {
      return "";
    }
    return `${this.roundNumber + 1} of ${this.electionResult?.rounds.length}`;
  }

  ngOnInit(): void {
    this.fetchResults();
  }

  onChangeChartType() {
    this.loadChart();
  }

  onNextRoundClick() {
    this.roundNumber++;
    this.loadChart();
  }

  onPreviousRoundClick() {
    this.roundNumber--;
    this.loadChart();
  }

  refresh() {
    this.fetchResults();
  }

  showShareDialog() {
    this.shareDialog.open(this.electionResult?.election?.id || '');
  }

  vote() {
    const electionId = this.electionResult?.election?.id || '';
    this.router.navigate([`/vote/${electionId}`]);
  }

  private fetchResults() {
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
        this.titleService
          .setTitle(`${this.electionResult.election.question} - Results - @gaber.dev`);
        this.loadChart();
        this.isLoading = false;
      },
      error: err => {
        this.showErrorMessage(err);
        this.isLoading = false;
      }
    });
  }

  private loadChart() {
    if (!this.electionResult || !this.electionResult.rounds) {
      return;
    }

    const round = this.electionResult.rounds[this.roundNumber];
    const data = round.counts.map(c => c.votesCounted);
    const maxVotes = Math.max(...data);

    if (this.chart) {
      this.chart.destroy();
    }

    this.chart = new Chart('resultsCanvas', {
      type: this.chartType === ChartType.PIE ? 'pie' : 'bar',
      data: {
        labels: round.counts.map(c => c.text),
        datasets: [{
          label: `Number of votes in round ${this.roundLabel}`,
          data: round.counts.map(c => c.votesCounted),
          backgroundColor: COLORS,
          borderWidth: this.chartType === ChartType.PIE ? 0.5 : 0,
        }],
      },
      options: {
        indexAxis: this.chartType === ChartType.HBAR ? 'y' : 'x',
        plugins: {
          legend: { position: 'bottom' },
          tooltip: {
            callbacks: {
              label: (item) => `${item.formattedValue} votes`,
            },
          },
        },
        responsive: true,
        scales: {
          x: {
            display: this.chartType !== ChartType.PIE,
            grid: { display: false },
            beginAtZero: true,
            ticks: {
              stepSize: maxVotes < 10 ? 1 : undefined,
            }
          },
          y: {
            display: this.chartType !== ChartType.PIE,
            grid: { display: false },
            ticks: {
              stepSize: maxVotes < 10 ? 1 : undefined,
            }
          },
        },
      },
    });
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
    'rgb(255, 204, 204)',
    'rgb(255, 0, 127)',
  ];
  return colors.sort((a, b) => 0.5 - Math.random());
}
