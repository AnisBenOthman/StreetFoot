import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TournamentCreationComponent } from './features/tournament/tournament-creation/tournament-creation.component';
import { TournamentListComponent } from './features/tournament/tournament-list/tournament-list.component';
import { TournamentDetailsComponent } from './features/tournament/tournament-details/tournament-details.component';

const routes: Routes = [
  { path: 'tournaments', component: TournamentListComponent },
  { path: 'tournaments/new', component: TournamentCreationComponent },
  { path: 'tournaments/:id', component: TournamentDetailsComponent },
  { path: '', redirectTo: 'tournaments', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
