import { ElectionPageComponent } from '@/app/components/election-page/election-page.component';
import { NgModule } from '@angular/core';
import { ResultsPageComponent } from '@/app/components/results-page/results-page.component';
import { RouterModule, Routes } from '@angular/router';
import { VotePageComponent } from '@/app/components/vote-page/vote-page.component';

const routes: Routes = [
  { path: '', component: ElectionPageComponent },
  { path: 'election', component: ElectionPageComponent },
  { path: 'election/:electionId', component: ResultsPageComponent },
  { path: 'vote/:electionId', component: VotePageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
