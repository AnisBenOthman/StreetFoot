import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Tournament } from '../../../models/tournament';
import { TournamentService } from '../../../services/tournament.service';
import { ChampionshipMode } from '../../../models/championship-mode';
import { TournamentType } from '../../../models/tournament-type';
import { Status } from '../../../models/status';
import { Sport } from '../../../models/sport';
import { SchedulingService } from 'src/app/services/scheduling.service';
import { Round, Match } from 'src/app/models/additional-models';
import { forkJoin, of } from 'rxjs';
import { catchError, finalize } from 'rxjs/operators';

@Component({
  selector: 'app-tournament-details',
  templateUrl: './tournament-details.component.html',
  styleUrls: ['./tournament-details.component.css'],
})
export class TournamentDetailsComponent implements OnInit {
  tournament: Tournament | null = null;
  rounds: Round[] = [];
  matchesByRound: { [roundId: string]: Match[] } = {};
  displayedColumns: string[] = ['matchId', 'teams', 'stadium'];
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private tournamentService: TournamentService,
    private schedulingService: SchedulingService
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
}
