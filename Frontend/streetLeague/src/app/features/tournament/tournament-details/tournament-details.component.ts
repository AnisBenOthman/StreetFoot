import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Tournament } from '../../../models/tournament';
import { TournamentService } from '../../../services/tournament.service';
import { ChampionshipMode } from '../../../models/championship-mode';
import { TournamentType } from '../../../models/tournament-type';
import { Status } from '../../../models/status';

@Component({
  selector: 'app-tournament-details',
  templateUrl: './tournament-details.component.html',
  styleUrls: ['./tournament-details.component.css'],
})
export class TournamentDetailsComponent implements OnInit {
  tournament: Tournament | null = null;

  constructor(
    private route: ActivatedRoute,
    private tournamentService: TournamentService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadTournament(Number(id));
    }
  }

  private loadTournament(id: number): void {
    this.tournamentService.getTournamentById(id).subscribe({
      next: (tournament) => {
        this.tournament = tournament;
      },
      error: (error) => console.error('Error loading tournament:', error),
    });
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
}
