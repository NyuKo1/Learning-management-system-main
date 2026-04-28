import { Component, EventEmitter, Output } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'crm-topbar',
  templateUrl: './topbar.component.html',
  styleUrls: ['./topbar.component.scss']
})
export class TopbarComponent {
  @Output() toggleSidenav = new EventEmitter<void>();
  username = this.auth.getUsername();
  hasLms = this.auth.hasLmsAccess();
  lmsUrl = environment.lmsUrl;
  darkMode = false;

  constructor(private auth: AuthService) {
    this.darkMode = localStorage.getItem('crm-dark') === 'true';
    this.applyTheme();
  }

  toggleDark(): void {
    this.darkMode = !this.darkMode;
    localStorage.setItem('crm-dark', String(this.darkMode));
    this.applyTheme();
  }

  private applyTheme(): void {
    document.documentElement.classList.toggle('dark', this.darkMode);
  }

  logout(): void {
    this.auth.logout();
  }
}
