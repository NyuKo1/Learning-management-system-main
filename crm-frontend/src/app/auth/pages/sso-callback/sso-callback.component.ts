import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../../../core/services/auth.service';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'crm-sso-callback',
  template: `
    <div style="display:flex;align-items:center;justify-content:center;height:100vh;flex-direction:column;gap:16px">
      <div *ngIf="loading">
        <mat-spinner diameter="40"></mat-spinner>
        <p style="color:#64748b;margin-top:12px">Completing sign-in...</p>
      </div>
      <div *ngIf="error" style="color:#dc2626;text-align:center">
        <p>SSO login failed: {{ error }}</p>
        <a routerLink="/auth/login">Return to login</a>
      </div>
    </div>
  `,
})
export class SsoCallbackComponent implements OnInit {
  loading = true;
  error = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private auth: AuthService
  ) {}

  ngOnInit(): void {
    const code = this.route.snapshot.queryParams['code'];
    if (!code) {
      this.error = 'No authorization code received.';
      this.loading = false;
      return;
    }

    this.http
      .post<{ accessToken: string; refreshToken: string }>(
        `${environment.apiUrl}/auth-service/sso/token`,
        {
          code,
          client_id: 'crm',
          client_secret: 'crm-secret-change-in-prod',
          redirect_uri: `${environment.crmUrl}/sso/callback`,
        }
      )
      .subscribe({
        next: (tokens) => {
          localStorage.setItem('crm_access_token', tokens.accessToken);
          localStorage.setItem('crm_refresh_token', tokens.refreshToken);
          (this.auth as any)['loggedIn$'].next(true);
          this.loading = false;
          this.router.navigate(['/']);
        },
        error: (err) => {
          this.loading = false;
          this.error = err.error?.message || 'Token exchange failed.';
        },
      });
  }
}
