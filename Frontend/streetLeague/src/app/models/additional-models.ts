import { Status } from './status';

export interface BaseEntity {
  id: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface Round extends BaseEntity {
  tournamentId: number;
  roundNumber: number;
  roundDate: Date;
  status: Status;
}

export interface Match extends BaseEntity {
  roundId: string;
  stadium: string;
  homeTeamId: number;
  awayTeamId: number;
}
