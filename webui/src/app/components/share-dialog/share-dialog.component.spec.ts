import { AppModule } from '@/app/app.module';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material/dialog';

import { ShareDialogComponent, ShareDialogData } from './share-dialog.component';

var SHARED_DIALOG_DATA: ShareDialogData = {
  shareUrl: 'http://cool.com',
}

describe('ShareDialogComponent', () => {
  let component: ShareDialogComponent;
  let fixture: ComponentFixture<ShareDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppModule],
      declarations: [ShareDialogComponent],
      providers: [{
        provide: MAT_DIALOG_DATA,
        useValue: SHARED_DIALOG_DATA,
      }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(ShareDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
