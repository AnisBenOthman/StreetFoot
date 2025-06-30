import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Match, Round } from '../models/additional-models';

@Injectable({
  providedIn: 'root',
})
export class SchedulingService {
  roundUrl = 'http://localhost:8084/sceduling-service/round-schedules/';
  matchUrl = 'http://localhost:8084/sceduling-service/match-schedules/';
  predictUrl = 'http://localhost:8084/sceduling-service/prediction/';

  constructor(private http: HttpClient) {}

  getAllRoundByTournamentId(tournamentId: number): Observable<Round[]> {
    return this.http.get<Round[]>(
      `${this.roundUrl}getallroundbytournament/${tournamentId}`
    );
  }

  getAllMatchByRoundId(roundId: string): Observable<Match[]> {
    return this.http.get<Match[]>(
      `${this.matchUrl}getallmatchbyround/${roundId}`
    );
  }

  updateMatch(
    matchId: string,
    homeScore: number,
    awayScore: number
  ): Observable<Match> {
    return this.http.put<Match>(
      `${this.matchUrl}updatescore/${matchId}/${homeScore}/${awayScore}`,
      {}
    );
  }

  getPrediction(matchId: string): Observable<{
    predictedLabel: string;
    probabilites: { [key: string]: number };
  }> {
    return this.http.get<{
      predictedLabel: string;
      probabilites: { [key: string]: number };
    }>(`${this.predictUrl}predict/${matchId}`);
  }
}
