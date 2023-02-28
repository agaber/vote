import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ShareDialogComponent } from '../share-dialog/share-dialog.component';

@Component({
  selector: 'app-banner',
  templateUrl: './banner.component.html',
  styleUrls: ['./banner.component.scss']
})
export class BannerComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private shareDialog: ShareDialogComponent) { }

  private electionId: string = ''

  ngOnInit(): void {
    this.electionId = this.route.snapshot.paramMap.get("electionId") || '';
  }

  share() {
    this.shareDialog.open(this.electionId);
  }
}
