import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, TokenPayload } from '../models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'crm_access_token';
  private readonly REFRESH_KEY = 'crm_refresh_token';

  private loggedIn$ = new BehaviorSubject<boolean>(this.hasToken());

  constructor(private http: HttpClient, private router: Router) {}

  login(req: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth-service/login`, req).pipe(
      tap(res => {
        localStorage.setItem(this.TOKEN_KEY, res.accessToken);
        localStorage.setItem(this.REFRESH_KEY, res.refreshToken);
        this.loggedIn$.next(true);
      })
    );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_KEY);
    this.loggedIn$.next(false);
    this.router.navigate(['/auth/login']);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn$.asObservable();
  }

  isLoggedInSnapshot(): boolean {
    return this.hasToken();
  }

  getPayload(): TokenPayload | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const base64 = token.split('.')[1];
      return JSON.parse(atob(base64)) as TokenPayload;
    } catch {
      return null;
    }
  }

  getUsername(): string {
    return this.getPayload()?.sub ?? '';
  }

  hasLmsAccess(): boolean {
    const roles = this.getPayload()?.roles ?? [];
    return roles.some(r => ['ROLE_STUDENT', 'ROLE_TEACHER', 'ROLE_ADMIN'].includes(r));
  }

  hasCrmAccess(): boolean {
    const roles = this.getPayload()?.roles ?? [];
    return roles.some(r => ['ROLE_CRM_MANAGER', 'ROLE_CRM_ADMIN', 'ROLE_ADMIN'].includes(r));
  }

  private hasToken(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }
}
