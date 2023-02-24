import { AfterViewInit, ChangeDetectorRef, Component, ContentChild, ElementRef, OnInit, ViewChild } from '@angular/core';

@Component({
  selector: 'app-help-icon-button',
  templateUrl: './help-icon-button.component.html',
  styleUrls: ['./help-icon-button.component.scss']
})
export class HelpIconButtonComponent implements AfterViewInit {
  constructor(private cd: ChangeDetectorRef) { }

  @ViewChild('content')
  content?: ElementRef;

  text: string = '';

  ngAfterViewInit() {
    if (this.content) {
      this.text = this.content.nativeElement.innerHTML.trim();
      this.cd.detectChanges();
    }
  }
}
