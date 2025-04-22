import { Status } from './status';

export interface Round {
  tournamentId: string;
  roundNumber: number;
  roundDate: Date;
  status: Status;
}

export interface Match {
  matchId: string;
  roundId: string;
  homeTeamId?: number;
  awayTeamId?: number;
  StaduimId?: string;
}
