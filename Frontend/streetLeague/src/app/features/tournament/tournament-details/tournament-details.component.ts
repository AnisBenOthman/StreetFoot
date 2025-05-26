import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Tournament } from '../../../models/tournament';
import { TournamentService } from '../../../services/tournament.service';
import { ChampionshipMode } from '../../../models/championship-mode';
import { TournamentType } from '../../../models/tournament-type';
import { Status } from '../../../models/status';
import { Sport } from '../../../models/sport';
import { SchedulingService } from 'src/app/services/scheduling.service';
import { StandingsService } from 'src/app/services/standings.service';
import { Round, Match } from 'src/app/models/additional-models';
import { Standings } from 'src/app/models/standings';
import { forkJoin, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatchUpdateDialogComponent } from '../match-update-dialog/match-update-dialog.component';
import { TeamService } from 'src/app/services/team.service';

@Component({
  selector: 'app-tournament-details',
  templateUrl: './tournament-details.component.html',
  styleUrls: ['./tournament-details.component.css'],
})
export class TournamentDetailsComponent implements OnInit {
  tournament: Tournament | null = null;
  rounds: Round[] = [];
  matchesByRound: { [roundId: string]: Match[] } = {};
  standings: Standings[] = [];
  displayedColumns: string[] = [
    'matchId',
    'teams',
    'stadium',
    'score',
    'actions',
  ];
  standingsColumns: string[] = [
    'position',
    'teamId',
    'matchesPlayed',
    'wins',
    'draws',
    'losses',
    'goalScored',
    'goalConceded',
    'goalDifference',
    'points',
  ];
  loading = true;
  teamNames: { [teamId: number]: string } = {};

  constructor(
    private route: ActivatedRoute,
    private tournamentService: TournamentService,
    private schedulingService: SchedulingService,
    private standingsService: StandingsService,
    private teamService: TeamService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadTournament(Number(id));
    }
  }

  private loadTournament(id: number): void {
    this.loading = true;
    this.tournamentService.getTournamentById(id).subscribe({
      next: (tournament) => {
        console.log('Tournament loaded:', tournament);
        this.tournament = tournament;
        this.loadRounds(id.toString());
        this.loadStandings(id);
      },
      error: (error) => {
        console.error('Error loading tournament:', error);
        this.loading = false;
      },
    });
  }

  private loadRounds(tournamentId: string): void {
    console.log('Loading rounds for tournament:', tournamentId);
    this.schedulingService
      .getAllRoundByTournamentId(Number(tournamentId))
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (rounds) => {
          console.log('Rounds loaded:', rounds);
          this.rounds = rounds;
          if (rounds.length > 0) {
            const matchObservables = rounds.map((round) =>
              this.schedulingService.getAllMatchByRoundId(round.id).pipe(
                catchError((error) => {
                  console.error(
                    'Error loading matches for round',
                    round.roundNumber,
                    ':',
                    error
                  );
                  return of([]);
                })
              )
            );

            forkJoin(matchObservables).subscribe({
              next: (matchesArray) => {
                rounds.forEach((round, index) => {
                  console.log(
                    'Matches loaded for round',
                    round.roundNumber,
                    ':',
                    matchesArray[index]
                  );
                  this.matchesByRound[round.id] = matchesArray[index];
                });
              },
              error: (error) => console.error('Error loading matches:', error),
            });
          }
        },
        error: (error) => console.error('Error loading rounds:', error),
      });
  }

  private loadTeamName(teamId: number): void {
    if (!this.teamNames[teamId]) {
      this.teamService.getTeamById(teamId).subscribe({
        next: (team) => {
          this.teamNames[teamId] = team.name;
        },
        error: (error) => {
          console.error(`Error loading team name for ID ${teamId}:`, error);
          this.teamNames[teamId] = `Équipe ${teamId}`; // Fallback
        },
      });
    }
  }

  private loadStandings(tournamentId: number): void {
    console.log('Requesting standings for tournament:', tournamentId);
    this.standingsService.getStandingsByTournamentId(tournamentId).subscribe({
      next: (standings) => {
        console.log('Standings loaded successfully:', standings);
        this.standings = standings.sort((a, b) => b.points - a.points);
        standings.forEach((standing) => {
          this.loadTeamName(standing.teamId);
        });
      },
      error: (error) => {
        console.error('Error loading standings:', error);
        console.error('Error details:', {
          status: error.status,
          message: error.message,
          error: error.error,
        });
      },
    });
  }

  getRoundMatches(roundId: string): Match[] {
    return this.matchesByRound[roundId] || [];
  }

  getStatusLabel(status: Status): string {
    switch (status) {
      case Status.PENDING:
        return 'En attente';
      case Status.PLANNED:
        return 'Planifié';
      case Status.ONGOING:
        return 'En cours';
      case Status.FINISHED:
        return 'Terminé';
      default:
        return 'Inconnu';
    }
  }

  getStatusClass(status: Status): string {
    switch (status) {
      case Status.PENDING:
        return 'status-pending';
      case Status.PLANNED:
        return 'status-planned';
      case Status.ONGOING:
        return 'status-in-progress';
      case Status.FINISHED:
        return 'status-completed';
      default:
        return '';
    }
  }

  getTournamentTypeLabel(type: TournamentType): string {
    switch (type) {
      case TournamentType.CHAMPIONSHIP:
        return 'Championnat';
      case TournamentType.GROUP_STAGE:
        return 'Phase de groupes';
      default:
        return 'Inconnu';
    }
  }

  getChampionshipModeLabel(mode: ChampionshipMode): string {
    switch (mode) {
      case ChampionshipMode.HOME_AWAY:
        return 'Aller-retour';
      case ChampionshipMode.HOME_ONLY:
        return 'Match unique';
      default:
        return 'Inconnu';
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

  getGoalDifference(standing: Standings): number {
    return standing.goalScored - standing.goalConceded;
  }

  updateMatchScore(match: Match): void {
    const dialogRef = this.dialog.open(MatchUpdateDialogComponent, {
      width: '400px',
      data: { match },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        console.log('Updating match score with result:', result);
        this.schedulingService
          .updateMatch(match.id, result.homeScore, result.awayScore)
          .subscribe({
            next: (updatedMatch) => {
              console.log('Match updated successfully:', updatedMatch);
              this.showSuccessMessage('Score mis à jour avec succès');
              // Rafraîchir la page après la mise à jour
              window.location.reload();
            },
            error: (error) => {
              console.error('Error updating match score:', error);
              this.showErrorMessage('Erreur lors de la mise à jour du score');
            },
          });
      } else {
        console.log('Match update dialog closed without result.');
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

  getTeamName(teamId: number): string {
    return this.teamNames[teamId] || `Équipe ${teamId}`;
  }
}
