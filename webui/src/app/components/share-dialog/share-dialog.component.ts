import { Clipboard } from '@angular/cdk/clipboard';
import { Component, Inject, Injectable, Optional } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-share-dialog',
  templateUrl: 'share-dialog.component.html',
  styleUrls: ['share-dialog.component.scss']
})
@Injectable({ providedIn: 'root' })
export class ShareDialogComponent {
  constructor(
    @Optional() @Inject(MAT_DIALOG_DATA) public data: ShareDialogData,
    private clipboard: Clipboard,
    private dialog: MatDialog,
    private snackBar: MatSnackBar) { }

  open(shareUrl: string) {
    this.dialog.open(ShareDialogComponent, { data: { shareUrl } });
  }

  copyToClipboard(shareUrl: string) {
    this.clipboard.copy(shareUrl);
    this.snackBar.open('Successfully copied!', 'Dismiss', { duration: 400 });
  }
}

/** The properties to be used in the HTML template. */
export interface ShareDialogData {
  shareUrl: string;
}
