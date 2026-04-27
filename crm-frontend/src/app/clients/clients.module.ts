import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatInputModule } from '@angular/material/input';
import { FormsModule } from '@angular/forms';
import { MatTooltipModule } from '@angular/material/tooltip';

import { ClientsListComponent } from './pages/clients-list/clients-list.component';

@NgModule({
  declarations: [ClientsListComponent],
  imports: [
    CommonModule, FormsModule,
    RouterModule.forChild([{ path: '', component: ClientsListComponent }]),
    MatButtonModule, MatIconModule, MatProgressSpinnerModule, MatInputModule, MatTooltipModule
  ]
})
export class ClientsModule {}
