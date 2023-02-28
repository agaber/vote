import { Component } from '@angular/core';
import { EventEmitter, Input, Output } from '@angular/core';

/**
 * Generates previous and next buttons. Angular Material has a Paginator
 * component but it looked hard to customize the text. This is meant to be a
 * simpler, more specific version of that.
 */
@Component({
  selector: 'app-pager',
  templateUrl: './pager.component.html',
  styleUrls: ['./pager.component.scss']
})
export class PagerComponent {
  @Input() label: string = '';
  @Input() numberOfPages: number = 0;
  @Input() pageNumber: number = 1;
  @Output() nextButtonClick = new EventEmitter();
  @Output() previousButtonClick = new EventEmitter();

  get isNextButtonDisabled() {
    return this.pageNumber >= this.numberOfPages;
  }

  get isPreviousButtonDisabled() {
    return this.pageNumber <= 1;
  }

  onNextButtonClick() {
    this.nextButtonClick.emit();
  }

  onPreviousButtonClick() {
    this.previousButtonClick.emit();
  }
}
