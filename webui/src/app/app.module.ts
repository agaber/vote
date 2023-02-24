import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from '@/app/components/app/app.component';
import { AppMaterialModule } from '@/app/app-material.module';
import { AppRoutingModule } from '@/app/app-routing.module';
import { CreateElectionComponent } from '@/app/components/create-election/create-election.component';
import { VoteComponentComponent } from './components/vote/vote-component.component';
import { HelpIconButtonComponent } from './components/help-icon-button/help-icon-button.component';
import { ShareDialogComponent } from './components/share-dialog/share-dialog.component';


@NgModule({
  declarations: [
    AppComponent,
    CreateElectionComponent,
    VoteComponentComponent,
    HelpIconButtonComponent,
    ShareDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    AppMaterialModule,
    ReactiveFormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule { }
