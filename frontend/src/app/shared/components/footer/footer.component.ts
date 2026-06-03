import { Component } from '@angular/core';
import { BrandingService } from '@core/services/branding.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent {
  constructor(public branding: BrandingService) {}
}
