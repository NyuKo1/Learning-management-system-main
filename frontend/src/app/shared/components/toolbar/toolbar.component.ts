import { Component, OnInit } from '@angular/core';
import { AuthService } from '@core/services/auth.service';
import { SsoService } from '@core/services/sso.service';
import { environment } from 'environments/environment';

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss'],
})
export class ToolbarComponent implements OnInit {
  isDarkMode: boolean = false;
  crmUrl = environment.crmUrl;

  constructor(public authService: AuthService, private sso: SsoService) {}

  ngOnInit(): void {
    this.isDarkMode = localStorage.getItem('theme') === 'dark';
    this.applyTheme();
  }

  toggleTheme(): void {
    this.isDarkMode = !this.isDarkMode;
    localStorage.setItem('theme', this.isDarkMode ? 'dark' : 'light');
    this.applyTheme();
  }

  get hasCrmRole(): boolean {
    return this.authService
      .getRoles()
      .some((r) => ['ROLE_ADMIN', 'ROLE_ROOT'].includes(r));
  }

  openCrm(): void {
    const returnUrl = `${window.location.origin}${this.crmUrl}/sso/callback`;
    this.sso.initiateLogin('crm', returnUrl);
  }

  private applyTheme(): void {
    if (this.isDarkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }
}
