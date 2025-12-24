import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';

export interface LoginResponse {
  token: string;
  username: string;
  role: string; // ROLE_ADMIN / ROLE_PERIODISTA
  expiresInSeconds: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {}

  login(username: string, password: string) {
    return this.http.post<LoginResponse>(`${this.api}/login`, { username, password })
      .pipe(tap(res => {
        localStorage.setItem('token', res.token);
        localStorage.setItem('role', res.role);
        localStorage.setItem('username', res.username);
      }));
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('username');
  }

  token(): string | null {
    return localStorage.getItem('token');
  }

  isLoggedIn(): boolean {
    return !!this.token();
  }

  isAdmin(): boolean {
    return localStorage.getItem('role') === 'ROLE_ADMIN';
  }
}
