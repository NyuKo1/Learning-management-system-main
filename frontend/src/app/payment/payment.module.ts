import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '@shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PaymentPageComponent } from './payment-page.component';

const routes: Routes = [{ path: '', component: PaymentPageComponent }];

@NgModule({
  declarations: [PaymentPageComponent],
  imports: [SharedModule, FormsModule, ReactiveFormsModule, RouterModule.forChild(routes)],
})
export class PaymentModule {}
