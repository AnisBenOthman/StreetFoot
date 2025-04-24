import { Status } from './status';
import { TournamentRules } from './tournament-rules';
import { Sport } from './sport';

export interface Tournament {
  id?: number;
  name: string;
  description?: string;
  startDate: Date;
  endDate: Date;
  teamRegistrationDeadline: Date;
  status: Status;
  sport: Sport;
  tournamentRules: TournamentRules;
  participatingTeamIds: number[];
  awards?: string;
  userId: number;
}
