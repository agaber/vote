import { Component } from '@angular/core';
import { FormArray, FormBuilder, Validators } from '@angular/forms'
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

import { Election } from '@/app/model/election';
import { ElectionService } from '@/app/services/election.service';

@Component({
  selector: 'app-create-election',
  styleUrls: ['create-election.component.scss'],
  templateUrl: 'create-election.component.html',
})
export class CreateElectionComponent {
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
    this.electionService.create(election).subscribe(
      savedElection => {
        this.router.navigate([`/vote/${savedElection.id}`]);
      },
      error => {
        console.log(error);
        const message = `Oops! There was an unexpected error: ${error.statusText}`;
        this.snackBar.open(message);
        this.setLoading(false);
      },
      () => {
        this.setLoading(false);
        this.form.reset();
      });
  }
}
