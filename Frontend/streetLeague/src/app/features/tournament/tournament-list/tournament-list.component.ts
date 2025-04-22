import { Component } from '@angular/core';
import { Status } from 'src/app/models/status';
import { Tournament } from 'src/app/models/tournament';
import { TournamentService } from 'src/app/services/tournament.service';

@Component({
  selector: 'app-tournament-list',
  templateUrl: './tournament-list.component.html',
  styleUrls: ['./tournament-list.component.css'],
})
export class TournamentListComponent {
  tournaments: Tournament[] = [];
  allTournaments: Tournament[] = [];

  constructor(private tournamentService: TournamentService) {}

  ngOnInit(): void {
    this.getTournaments();
  }

  getTournaments(): void {
    this.tournamentService.getTournaments().subscribe({
      next: (data) => {
        this.allTournaments = data;
        this.tournaments = [...this.allTournaments];
        console.log('Tournaments fetched successfully:', this.tournaments);
      },
      error: (error) => {
        console.error('Error fetching tournaments:', error);
      },
    });
  }

  filterTournaments(status: string): void {
    if (status === 'all') {
      this.getTournaments();
    } else {
      this.tournamentService.gettournamentbystatus(status).subscribe({
        next: (data) => {
          this.tournaments = data;
          console.log('Filtered tournaments:', this.tournaments);
        },
        error: (error) => {
          console.error('Error filtering tournaments:', error);
        },
      });
    }
  }

  getStatusLabel(status: Status): string {
    switch (status) {
      case Status.PENDING:
        return 'En attente';
      case Status.ONGOING:
        return 'En cours';
      case Status.FINISHED:
        return 'Terminé';
      case Status.PLANNED:
        return 'Prévu';
      default:
        return 'Inconnu';
    }
  }

  getStatusClass(status: Status): string {
    switch (status) {
      case Status.PENDING:
        return 'status-pending';
      case Status.ONGOING:
        return 'status-active';
      case Status.FINISHED:
        return 'status-completed';
      case Status.PLANNED:
        return 'status-planned';
      default:
        return '';
    }
  }
}
