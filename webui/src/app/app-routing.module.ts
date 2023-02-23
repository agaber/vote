import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateElectionComponent } from './components/create-election/create-election.component';

const routes: Routes = [
  { path: '', component: CreateElectionComponent },
  { path: 'create', component: CreateElectionComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
