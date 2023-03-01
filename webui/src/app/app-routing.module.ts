import { AboutPageComponent } from './components/about-page/about-page.component';
import { ElectionPageComponent } from '@/app/components/election-page/election-page.component';
import { NgModule } from '@angular/core';
import { ResultsPageComponent } from '@/app/components/results-page/results-page.component';
import { RouterModule, Routes } from '@angular/router';
import { VotePageComponent } from '@/app/components/vote-page/vote-page.component';

const routes: Routes = [
  { path: 'about', component: AboutPageComponent },
  { path: 'election/create', component: ElectionPageComponent },
  { path: ':electionId/results', component: ResultsPageComponent },
  { path: ':electionId', component: VotePageComponent },
  { path: '', component: VotePageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
