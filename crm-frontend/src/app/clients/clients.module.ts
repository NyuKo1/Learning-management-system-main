import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialogModule } from '@angular/material/dialog';
import { MatDividerModule } from '@angular/material/divider';

import { ClientsListComponent } from './pages/clients-list/clients-list.component';
import { CreateLmsAccountDialogComponent } from './components/create-lms-account-dialog/create-lms-account-dialog.component';

@NgModule({
  declarations: [ClientsListComponent, CreateLmsAccountDialogComponent],
  imports: [
    CommonModule, FormsModule, ReactiveFormsModule,
    RouterModule.forChild([{ path: '', component: ClientsListComponent }]),
    MatButtonModule, MatIconModule, MatProgressSpinnerModule,
    MatInputModule, MatFormFieldModule, MatTooltipModule,
    MatSnackBarModule, MatDialogModule, MatDividerModule
  ]
})
export class ClientsModule {}
