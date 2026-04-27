import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatChipsModule } from '@angular/material/chips';

import { CoursesListComponent } from './pages/courses-list/courses-list.component';

@NgModule({
  declarations: [CoursesListComponent],
  imports: [
    CommonModule,
    RouterModule.forChild([{ path: '', component: CoursesListComponent }]),
    MatButtonModule, MatIconModule, MatProgressSpinnerModule, MatChipsModule
  ]
})
export class CoursesModule {}
