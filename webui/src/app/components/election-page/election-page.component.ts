import { Component } from '@angular/core';
import { FormArray, FormBuilder, Validators } from '@angular/forms'
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

import { Election } from '@/app/model/election';
import { ElectionService } from '@/app/services/election.service';

@Component({
  selector: 'app-create-election',
  styleUrls: ['election-page.component.scss'],
  templateUrl: 'election-page.component.html',
})
export class ElectionPageComponent {
  constructor(
    private electionService: ElectionService,
    private snackBar: MatSnackBar,
    private fb: FormBuilder,
    private router: Router) { }

  form = this.fb.group({
    question: ['', Validators.required],
    options: this.fb.array([
      this.fb.control('', Validators.required),
      this.fb.control('', Validators.required),
    ]),
  });

  isLoading = false;

  get options(): FormArray {
    return this.form.controls['options'];
  }

  setLoading(isLoading: boolean) {
    this.isLoading = isLoading;
    if (isLoading) {
      this.form.disable();
    } else {
      this.form.enable();
    }
  }

  onAddOption() {
    this.options.push(this.fb.control('', Validators.required));
  }

  onRemoveOption(index: number) {
    this.options.removeAt(index);
  }

  onSubmit() {
    if (!this.form.valid) {
      return;
    }

    const election: Election = {
      question: this.form.controls.question.value || '',
      options: this.form.controls.options.value.filter(v => !!v) as string[],
    };

    this.setLoading(true);
    this.electionService.create(election).subscribe({
      next: savedElection => {
        this.router.navigate([`/vote/${savedElection.id}`]);
        this.setLoading(false);
        this.form.reset();
      },
      error: error => {
        const message =
          `Oops! There was an unexpected error. Please try again later.\nError: "${error.statusText}"`;
        this.snackBar.open(message, 'Dismiss', { panelClass: 'snackbar-text' });
        this.setLoading(false);
      }
    });
  }
}
