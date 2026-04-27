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

  constructor(private auth: AuthService) {}

  logout(): void {
    this.auth.logout();
  }
}
