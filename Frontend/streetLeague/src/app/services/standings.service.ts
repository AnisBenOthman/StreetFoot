import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Standings } from '../models/standings';

@Injectable({
  providedIn: 'root',
})
export class StandingsService {
  private baseUrl = 'http://127.0.0.1:8084/standing-service/standings';

  constructor(private http: HttpClient) {}

  getStandingsByTournamentId(tournamentId: number): Observable<Standings[]> {
    return this.http.get<Standings[]>(
      `${this.baseUrl}/getstandingoftournament/${tournamentId}`
    );
  }

  getStandingsByTournamentAndTeamId(
    tournamentId: number,
    teamId: number
  ): Observable<Standings> {
    return this.http.get<Standings>(
      `${this.baseUrl}/getstandingfortournamentandteam/${tournamentId}/${teamId}`
    );
  }
}
