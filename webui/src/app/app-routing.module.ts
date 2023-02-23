import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateElectionComponent } from './components/create-election/create-election.component';
import { VoteComponentComponent } from './components/vote/vote-component.component';

const routes: Routes = [
  { path: '', component: CreateElectionComponent },
  { path: 'create', component: CreateElectionComponent },
  { path: 'vote/:electionId', component: VoteComponentComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
