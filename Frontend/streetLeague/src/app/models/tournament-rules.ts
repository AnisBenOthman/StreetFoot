import { ChampionshipMode } from './championship-mode';
import { TournamentType } from './tournament-type';

export interface TournamentRules {
  id?: number;
  roundFrequency: number;
  tournamentType: TournamentType;
  championshipMode?: ChampionshipMode;
  numberOfGroups?: number;
  teamsPerGroup?: number;
  numberOfTeams: number;
  mainRoster: number;
  matchLineup: number;
  substitutes: number;
  isOverTimeRequired: boolean;
  overTimeDuration?: number;
  matchDuration: number;
  halftime: number;
  videoReview: boolean;
}
