import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { TestBed } from '@angular/core/testing';

import { AppComponent } from '@/app/components/app/app.component';
import { BannerComponent } from '../banner/banner.component';
import { AppMaterialModule } from '@/app/app-material.module';
import { ElectionPageComponent } from '@/app/components/election-page/election-page.component';
import { HelpIconButtonComponent } from '../help-icon-button/help-icon-button.component';
import { ResultsPageComponent } from '../results-page/results-page.component';
import { ShareDialogComponent } from '../share-dialog/share-dialog.component';
import { VotePageComponent } from '../vote-page/vote-page.component';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        AppMaterialModule,
        HttpClientTestingModule,
        RouterTestingModule,
      ],
      declarations: [
        AppComponent,
        BannerComponent,
        ElectionPageComponent,
        HelpIconButtonComponent,
        ResultsPageComponent,
        ShareDialogComponent,
        VotePageComponent,
      ],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
});
