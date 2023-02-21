import { Component } from '@angular/core';
import { FormArray, FormBuilder, Validators } from '@angular/forms'

@Component({
  selector: 'app-create-election',
  styleUrls: ['create-election.component.scss'],
  templateUrl: 'create-election.component.html',
})
export class CreateElectionComponent {
  constructor(private fb: FormBuilder) { }

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
    if (this.form.valid) {
      console.log('onSubmit');
      console.log(this.form);
    }
  }
}
