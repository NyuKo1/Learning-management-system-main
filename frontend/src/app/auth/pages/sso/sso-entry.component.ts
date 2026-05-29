import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SsoService } from '@core/services/sso.service';

@Component({
  selector: 'app-sso-entry',
  template: `
    <div
      style="display:flex;align-items:center;justify-content:center;height:100vh"
    >
      <p>Redirecting to {{ clientId }}...</p>
    </div>
  `,
})
export class SsoEntryComponent implements OnInit {
  clientId: string = '';

  constructor(
    private route: ActivatedRoute,
    private ssoService: SsoService
  ) {}

  ngOnInit(): void {
    const clientId = this.route.snapshot.queryParams['client_id'];
    const redirectUri = this.route.snapshot.queryParams['redirect_uri'];
    this.clientId = clientId || 'external system';

    if (clientId && redirectUri) {
      this.ssoService.initiateLogin(clientId, redirectUri);
    }
  }
}
