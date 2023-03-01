import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from '@/app/components/app/app.component';
import { AppMaterialModule } from '@/app/app-material.module';
import { AppRoutingModule } from '@/app/app-routing.module';
import { BannerComponent } from './components/banner/banner.component';
import { ElectionPageComponent } from '@/app/components/election-page/election-page.component';
import { HelpIconButtonComponent } from './components/help-icon-button/help-icon-button.component';
import { PagerComponent } from './components/pager/pager.component';
import { ResultsPageComponent } from './components/results-page/results-page.component';
import { ShareDialogComponent } from './components/share-dialog/share-dialog.component';
import { VotePageComponent } from './components/vote-page/vote-page.component';
import { AboutPageComponent } from './components/about-page/about-page.component';

@NgModule({
  declarations: [
    AboutPageComponent,
    AppComponent,
    BannerComponent,
    ElectionPageComponent,
    HelpIconButtonComponent,
    PagerComponent,
    ResultsPageComponent,
    ShareDialogComponent,
    VotePageComponent,
  ],
  imports: [
    AppMaterialModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    BrowserModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
