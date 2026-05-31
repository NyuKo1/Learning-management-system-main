import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'environments/environment';
import { AuthService } from './auth.service';

@Injectable({ providedIn: 'root' })
export class SsoService {
  private ssoUrl = `${environment.baseUrl}/auth-service/sso`;

  constructor(private authService: AuthService, private http: HttpClient) {}

  initiateLogin(clientId: string, redirectUri: string): void {
    if (!this.authService.loggedIn()) {
      sessionStorage.setItem(
        'sso_pending',
        JSON.stringify({ clientId, redirectUri })
      );
      window.location.href = '/auth/login';
      return;
    }

    const token = this.authService.accessToken;
    const url = `${this.ssoUrl}/authorize?client_id=${encodeURIComponent(
      clientId
    )}&redirect_uri=${encodeURIComponent(redirectUri)}&response_mode=json`;

    this.http
      .get<{ redirectUri: string; code: string }>(url, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .subscribe({
        next: (data) => {
          if (data?.redirectUri) {
            window.location.href = data.redirectUri;
          } else {
            console.error('SSO: no redirectUri in response', data);
          }
        },
        error: (err) => {
          if (err.status === 401) {
            sessionStorage.setItem(
              'sso_pending',
              JSON.stringify({ clientId, redirectUri })
            );
            window.location.href = '/auth/login';
          } else {
            window.alert('SSO request failed. Please try again.');
          }
        },
      });
  }

  checkPendingSso(): boolean {
    const pending = sessionStorage.getItem('sso_pending');
    if (!pending) return false;

    sessionStorage.removeItem('sso_pending');
    const { clientId, redirectUri } = JSON.parse(pending);
    this.initiateLogin(clientId, redirectUri);
    return true;
  }
}
