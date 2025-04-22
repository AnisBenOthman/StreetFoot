import { Component } from '@angular/core';
import { Tournament } from 'src/app/models/tournament';

@Component({
  selector: 'app-tournament-details',
  templateUrl: './tournament-details.component.html',
  styleUrls: ['./tournament-details.component.css'],
})
export class TournamentDetailsComponent {
  tournament: Tournament | undefined;
  teams: String[] = [];
  rounds: Round[] = [];
}
