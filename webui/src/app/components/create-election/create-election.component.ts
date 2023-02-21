import { Component } from '@angular/core';
import { FormArray, FormBuilder, Validators } from '@angular/forms'

import { Election } from '@/app/model/election';
import { ElectionService } from '@/app/services/election.service';

@Component({
  selector: 'app-create-election',
  styleUrls: ['create-election.component.scss'],
  templateUrl: 'create-election.component.html',
})
export class CreateElectionComponent {
  constructor(private electionService: ElectionService, private fb: FormBuilder) { }

  form = this.fb.group({
    question: ['', Validators.required],
    options: this.fb.array([
      this.fb.control('', Validators.required),
      this.fb.control('', Validators.required),
    ]),
  });

  get options(): FormArray {
    return this.form.controls['options'];
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

    console.log('+++ onSubmit');
    const election: Election = {
      question: this.form.controls.question.value || '',
      options: this.form.controls.options.value.filter(v => !!v) as string[],
    };
    console.log(`+++ trying to save it`);
    console.log(election);
    this.electionService.create(election).subscribe(savedElection => {
      console.log('+++ we did it!');
      console.log(savedElection);
    });
    // this.form.reset();
  }
}
