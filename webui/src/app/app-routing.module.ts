import { CreateElectionComponent } from '@/app/components/create-election/create-election.component';
import { NgModule } from '@angular/core';
import { ResultsComponent } from '@/app/components/results/results.component';
import { RouterModule, Routes } from '@angular/router';
import { VoteComponentComponent } from '@/app/components/vote/vote-component.component';

const routes: Routes = [
  { path: '', component: CreateElectionComponent },
  { path: 'election', component: CreateElectionComponent },
  { path: 'election/:electionId', component: ResultsComponent },
  { path: 'vote/:electionId', component: VoteComponentComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
