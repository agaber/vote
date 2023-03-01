import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, Validators } from '@angular/forms'
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Title } from '@angular/platform-browser';

import { Election } from '@/app/model/election';
import { ElectionService } from '@/app/services/election.service';

@Component({
  selector: 'app-create-election',
  styleUrls: ['election-page.component.scss'],
  templateUrl: 'election-page.component.html',
})
export class ElectionPageComponent implements OnInit {
  constructor(
    private electionService: ElectionService,
    private fb: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar,
    private titleService: Title) { }

  form = this.fb.group({
    question: ['', [Validators.required, Validators.maxLength(1000)]],
    options: this.fb.array([
      this.fb.control('', this.optionsValidators),
      this.fb.control('', this.optionsValidators),
    ]),
  });

  isLoading = false;

  get options(): FormArray {
    return this.form.controls['options'];
  }

  get optionsValidators() {
    return [Validators.required, Validators.maxLength(1000)];
  }

  ngOnInit(): void {
    this.titleService.setTitle('Create a new election - @gaber.dev');
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
    this.options.push(this.fb.control('', this.optionsValidators));
  }

  onRemoveOption(index: number) {
    this.options.removeAt(index);
  }

  onSubmit() {
    if (!this.form.valid) {
      return;
    }

    const options = this.form.controls.options.value;
    const uniqueOptions
      = new Set(options.map(v => (v || '').trim()).filter(v => !!v));
    const election: Election = {
      question: this.form.controls.question.value || '',
      options: [...uniqueOptions],
    };

    this.setLoading(true);
    this.electionService.create(election).subscribe({
      next: savedElection => {
        this.router.navigate([`${savedElection.id}`]);
        this.setLoading(false);
        this.form.reset();
      },
      error: error => {
        this.showErrorMessage(error);
        this.setLoading(false);
      }
    });
  }

  private showErrorMessage(err: any) {
    const message =
      `Oops! There was an unexpected error. Please try again later.\nError: "${err.statusText}"`;
    this.snackBar.open(message, 'Dismiss', { panelClass: 'snackbar-text' });
  }
}
