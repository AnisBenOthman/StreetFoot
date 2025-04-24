import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Match, Round } from '../models/additional-models';

@Injectable({
  providedIn: 'root',
})
export class SchedulingService {
  roundUrl = 'http://127.0.0.1:8084/sceduling-service/round-schedules/';
  matchUrl = 'http://127.0.0.1:8084/sceduling-service/match-schedules/';

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
}
