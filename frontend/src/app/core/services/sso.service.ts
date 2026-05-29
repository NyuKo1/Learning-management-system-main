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
    )}&redirect_uri=${encodeURIComponent(redirectUri)}`;

    fetch(url, {
      headers: { Authorization: `Bearer ${token}` },
      redirect: 'manual',
    })
      .then((response) => {
        if (response.type === 'opaqueredirect' || response.status === 302) {
          const location = response.headers.get('Location');
          if (location) {
            window.location.href = location;
          }
        } else if (response.status === 401) {
          sessionStorage.setItem(
            'sso_pending',
            JSON.stringify({ clientId, redirectUri })
          );
          window.location.href = '/auth/login';
        }
      })
      .catch(() => {
        window.alert('SSO request failed. Please try again.');
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
