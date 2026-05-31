import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { SsoEntryComponent } from './pages/sso/sso-entry.component';

const routes: Routes = [
  { path: '', component: SsoEntryComponent },
];

@NgModule({
  declarations: [SsoEntryComponent],
  imports: [CommonModule, RouterModule.forChild(routes)],
})
export class SsoEntryModule {}
