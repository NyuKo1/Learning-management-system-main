import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatDialogModule } from '@angular/material/dialog';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';

import { LeadsListComponent } from './pages/leads-list/leads-list.component';
import { LeadFormDialogComponent } from './components/lead-form-dialog/lead-form-dialog.component';

@NgModule({
  declarations: [LeadsListComponent, LeadFormDialogComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild([{ path: '', component: LeadsListComponent }]),
    MatTableModule, MatButtonModule, MatIconModule, MatSelectModule,
    MatInputModule, MatDialogModule, MatMenuModule, MatProgressSpinnerModule,
    MatTooltipModule, MatChipsModule, MatSnackBarModule, MatDividerModule
  ]
})
export class LeadsModule {}
