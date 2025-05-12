import { Component } from '@angular/core';
import { Status } from 'src/app/models/status';
import { Tournament } from 'src/app/models/tournament';
import { Sport } from 'src/app/models/sport';
import { TournamentService } from 'src/app/services/tournament.service';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
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
  currentSportFilter: Sport | 'all' = 'all';
  sports = Sport;

  constructor(
    private tournamentService: TournamentService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.getTournaments();
  }

  getTournaments(): void {
    this.tournamentService.getTournaments().subscribe({
      next: (data) => {
        this.allTournaments = data;
        this.applyFilters();
        console.log('Tournaments fetched successfully:', this.tournaments);
      },
      error: (error) => {
        console.error('Error fetching tournaments:', error);
        this.showErrorMessage('Erreur lors de la récupération des tournois');
      },
    });
  }

  filterTournaments(status: string): void {
    this.currentFilter = status;
    this.applyFilters();
  }

  filterTournamentsBySport(sport: Sport | 'all'): void {
    this.currentSportFilter = sport;
    this.applyFilters();
  }

  private applyFilters(): void {
    let filteredTournaments = [...this.allTournaments];

    if (this.currentFilter !== 'all') {
      filteredTournaments = filteredTournaments.filter(
        (tournament) => tournament.status === this.currentFilter
      );
    }

    if (this.currentSportFilter !== 'all') {
      filteredTournaments = filteredTournaments.filter(
        (tournament) => tournament.sport === this.currentSportFilter
      );
    }

    this.tournaments = filteredTournaments;
  }

  participateInTournament(event: Event, tournamentId: number): void {
    event.stopPropagation();

    const tournament = this.tournaments.find((t) => t.id === tournamentId);
    if (!tournament) {
      this.showErrorMessage('Tournoi non trouvé');
      return;
    }

    const teamId = 22; // TODO: Utiliser l'ID de l'équipe connectée
    if (tournament.participatingTeamIds.includes(teamId)) {
      this.showErrorMessage('Votre équipe est déjà inscrite à ce tournoi');
      return;
    }

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'Confirmer la participation',
        message: 'Êtes-vous sûr de vouloir participer à ce tournoi ?',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        this.tournamentService
          .participateInTournament(tournamentId, teamId)
          .subscribe({
            next: () => {
              console.log(tournamentId, teamId);
              this.showSuccessMessage('Inscription au tournoi réussie !');
              this.getTournaments();
            },
            error: (error) => {
              console.log(tournamentId, teamId);
              console.error('Error joining tournament:', error);
              let errorMessage = "Erreur lors de l'inscription au tournoi";
              if (error.error?.message) {
                errorMessage = error.error.message;
              }
              this.showErrorMessage(errorMessage);
            },
          });
      }
    });
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
            this.allTournaments = this.allTournaments.filter(
              (t) => t.id !== tournamentId
            );
            this.showSuccessMessage('Tournoi supprimé avec succès');
            this.getTournaments();
          },
          error: (error) => {
            console.error('Error deleting tournament:', error);
            this.showErrorMessage('Erreur lors de la suppression du tournoi');
          },
        });
      }
    });
  }

  private showSuccessMessage(message: string): void {
    this.snackBar.open(message, 'Fermer', {
      duration: 3000,
      panelClass: ['success-snackbar'],
      horizontalPosition: 'end',
      verticalPosition: 'top',
    });
  }

  private showErrorMessage(message: string): void {
    this.snackBar.open(message, 'Fermer', {
      duration: 5000,
      panelClass: ['error-snackbar'],
      horizontalPosition: 'end',
      verticalPosition: 'top',
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

  getSportLabel(sport: Sport): string {
    switch (sport) {
      case Sport.FOOTBALL:
        return 'Football';
      case Sport.HANDBALL:
        return 'Handball';
      case Sport.BASKETBALL:
        return 'Basketball';
      case Sport.TENNIS:
        return 'Tennis';
      case Sport.VOLLEYBALL:
        return 'Volleyball';
      case Sport.OTHER:
        return 'Autre';
      default:
        return 'Inconnu';
    }
  }

  isTournamentFull(tournament: Tournament): boolean {
    if (tournament.tournamentRules.tournamentType === 'GROUP_STAGE') {
      const maxTeams =
        tournament.tournamentRules.numberOfGroups! *
        tournament.tournamentRules.teamsPerGroup!;
      return tournament.participatingTeamIds.length >= maxTeams;
    } else {
      return (
        tournament.participatingTeamIds.length >=
        tournament.tournamentRules.numberOfTeams
      );
    }
  }
}
