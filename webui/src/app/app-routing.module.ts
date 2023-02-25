import { ElectionPageComponent } from '@/app/components/election-page/election-page.component';
import { NgModule } from '@angular/core';
import { ResultsPageComponent } from '@/app/components/results-page/results-page.component';
import { RouterModule, Routes } from '@angular/router';
import { VotePageComponent } from '@/app/components/vote-page/vote-page.component';

const routes: Routes = [
  // TODO: change this.
  // 1. need new landing page. Idea: direct to the voting page of random election.
  // 2. root url is not supported. agaber.dev is for other stuff,
  //    agaber.dev/vote is the base url --> can maybe fix this with k8s but rather not anyway
  { path: '', redirectTo: '/vote/election/create', pathMatch: 'full' },

  { path: 'vote/election/create', component: ElectionPageComponent },
  { path: 'vote/:electionId/results', component: ResultsPageComponent },
  { path: 'vote/:electionId', component: VotePageComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
