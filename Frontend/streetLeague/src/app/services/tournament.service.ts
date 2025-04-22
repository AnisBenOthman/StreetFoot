import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Tournament } from '../models/tournament';
import { TournamentRules } from '../models/tournament-rules';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TournamentService {
  apiUrl = 'http://127.0.0.1:8084/tournament-service/tournament/';
  rulesApiUrl = 'http://127.0.0.1:8084/tournament-service/tournamentrules/';
  constructor(private http: HttpClient) {}

  getTournaments() {
    return this.http.get<Tournament[]>(this.apiUrl + 'getall');
  }
  addTournament(tournament: Tournament) {
    return this.http.post<Tournament>(
      this.apiUrl + 'addtournament',
      tournament
    );
  }
  createTournamentRules(rules: TournamentRules): Observable<TournamentRules> {
    return this.http.post<TournamentRules>(this.rulesApiUrl + 'add', rules);
  }
  gettournamentbystatus(status: string): Observable<Tournament[]> {
    return this.http.get<Tournament[]>(
      this.apiUrl + 'gettournamentbystatus/' + status
    );
  }
  getTournamentById(id: number): Observable<Tournament> {
    return this.http.get<Tournament>(this.apiUrl + 'gettournamentbyid/' + id);
  }
}
