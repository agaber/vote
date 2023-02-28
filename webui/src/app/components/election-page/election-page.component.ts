import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, ValidationErrors, ValidatorFn, Validators } from '@angular/forms'
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
    return [Validators.required, Validators.maxLength(1000), this.noDuplicateOptionValidator()];
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

  noDuplicateOptionValidator(): ValidatorFn {
    const validator: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
      if (this.form?.controls) {
        // There's other validations for empty input (required).
        if (control.value.trim() === '') {
          return null;
        }
        // FYI: Form options will not yet contain the full user entered value until after this
        // function has finished executing.
        const options = this.form.controls.options.value.map(v => v?.trim());
        if (options.filter(v => v === control.value.trim()).length > 0) {
          return { duplicate: { value: control.value } };
        };
      }
      return null;
    };
    return validator;
  }
}
