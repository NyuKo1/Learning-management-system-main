import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AnalyticsComponent } from './pages/analytics.component';

@NgModule({
  declarations: [AnalyticsComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: '', component: AnalyticsComponent }]),
    MatIconModule,
    MatProgressSpinnerModule
  ]
})
export class AnalyticsModule {}
