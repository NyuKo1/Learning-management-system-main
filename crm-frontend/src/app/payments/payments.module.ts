import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { PaymentsListComponent } from './pages/payments-list/payments-list.component';

@NgModule({
  declarations: [PaymentsListComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: '', component: PaymentsListComponent }]),
    MatButtonModule, MatIconModule, MatProgressSpinnerModule
  ]
})
export class PaymentsModule {}
