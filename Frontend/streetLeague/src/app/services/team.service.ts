import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Team } from '../models/team';

@Injectable({
  providedIn: 'root',
})
export class TeamService {
  private baseUrl = 'http://127.0.0.1:8084/tournament-service/team/';

  constructor(private http: HttpClient) {}

  getTeamById(teamId: number): Observable<Team> {
    return this.http.get<Team>(`${this.baseUrl}getteambyid/${teamId}`);
  }
}
