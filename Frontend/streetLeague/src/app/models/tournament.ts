import { Status } from "./status";
import { TournamentRules } from "./tournament-rules";

export interface Tournament {
    id?: number;
  name: string;
  description?: string;
  startDate: Date;
  endDate: Date;
  teamRegistrationDeadline: Date;
  status: Status;
  tournamentRules: TournamentRules;
  participatingTeamIds: number[];
  awards?: string;
  userId: number;
}
