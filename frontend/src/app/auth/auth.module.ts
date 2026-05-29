import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';
import { AuthRoutingModule } from './auth-routing.module';

import { LoginComponent } from './pages/login/login.component';
import { SsoEntryComponent } from './pages/sso/sso-entry.component';

@NgModule({
  declarations: [LoginComponent, SsoEntryComponent],
  imports: [SharedModule, AuthRoutingModule],
})
export class AuthModule {}
