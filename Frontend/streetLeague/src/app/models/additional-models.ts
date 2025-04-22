export interface AdditionalModels {
    export interface Round {
        id?: number;
        roundNumber: number;
        startDate: Date;
        endDate: Date;
        matches: Match[];
        tournamentId: number;
      }
      
      export interface Group {
        id?: number;
        name: string;
        teams: number[];
        tournamentId: number;
      }
}
