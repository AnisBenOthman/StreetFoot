export interface Standings {
  tournamentId: number;
  teamId: number;
  matchesPlayed: number;
  wins: number;
  draws: number;
  losses: number;
  goalScored: number;
  goalConceded: number;
  points: number;
}
