import { Component } from '@angular/core';
import { Status } from 'src/app/models/status';
import { Tournament } from 'src/app/models/tournament';
import { TournamentService } from 'src/app/services/tournament.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from 'src/app/shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-tournament-list',
  templateUrl: './tournament-list.component.html',
  styleUrls: ['./tournament-list.component.css'],
})
export class TournamentListComponent {
  tournaments: Tournament[] = [];
  allTournaments: Tournament[] = [];
  currentFilter: string = 'all';

  constructor(
    private tournamentService: TournamentService,
    private dialog: MatDialog
  ) {}

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
    this.currentFilter = status;
    if (status === 'all') {
      this.tournaments = [...this.allTournaments];
    } else {
      this.tournaments = this.allTournaments.filter(
        (tournament) => tournament.status === status
      );
    }
    console.log('Filtered tournaments:', this.tournaments);
  }

  deleteTournament(event: Event, tournamentId: number): void {
    event.stopPropagation();

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'Confirmer la suppression',
        message:
          'Êtes-vous sûr de vouloir supprimer ce tournoi ? Cette action est irréversible.',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.tournamentService.deleteTournamentbyId(tournamentId).subscribe({
          next: () => {
            this.tournaments = this.tournaments.filter(
              (t) => t.id !== tournamentId
            );
            this.allTournaments = this.allTournaments.filter(
              (t) => t.id !== tournamentId
            );

            this.getTournaments();

            console.log('Tournament deleted successfully');
          },
          error: (error) => {
            console.error('Error deleting tournament:', error);
          },
        });
      }
    });
  }

  participateInTournament(event: Event, tournamentId: number): void {
    event.stopPropagation();

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'Confirmer la participation',
        message: 'Êtes-vous sûr de vouloir participer à ce tournoi ?',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        // Pour l'instant, on utilise un ID utilisateur fixe (1)
        // TODO: Utiliser l'ID de l'utilisateur connecté
        this.tournamentService
          .participateInTournament(tournamentId, 1)
          .subscribe({
            next: () => {
              console.log('Successfully joined tournament');
              this.getTournaments(); // Rafraîchir la liste pour mettre à jour le nombre d'équipes
            },
            error: (error) => {
              console.error('Error joining tournament:', error);
            },
          });
      }
    });
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

  getStatusClass(status: string | Status): string {
    if (typeof status === 'string' && status === 'all') {
      return '';
    }

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

  isTournamentFull(tournament: Tournament): boolean {
    if (tournament.tournamentRules.tournamentType === 'GROUP_STAGE') {
      const maxTeams =
        tournament.tournamentRules.numberOfGroups! *
        tournament.tournamentRules.teamsPerGroup!;
      return tournament.participatingTeamIds.length >= maxTeams;
    } else {
      // Pour le type CHAMPIONSHIP
      return (
        tournament.participatingTeamIds.length >=
        tournament.tournamentRules.numberOfTeams
      );
    }
  }
}
