import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { SsoCallbackComponent } from './sso-callback.component';

const routes: Routes = [
  { path: '', component: SsoCallbackComponent },
];

@NgModule({
  declarations: [SsoCallbackComponent],
  imports: [CommonModule, RouterModule.forChild(routes), MatProgressSpinnerModule],
})
export class SsoCallbackModule {}
