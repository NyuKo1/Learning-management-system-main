import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDividerModule } from '@angular/material/divider';

import { ShellComponent } from './shell/shell.component';
import { SidenavComponent } from './sidenav/sidenav.component';
import { TopbarComponent } from './topbar/topbar.component';

const routes: Routes = [
  {
    path: '',
    component: ShellComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      {
        path: 'dashboard',
        loadChildren: () => import('../dashboard/dashboard.module').then(m => m.DashboardModule)
      },
      {
        path: 'leads',
        loadChildren: () => import('../leads/leads.module').then(m => m.LeadsModule)
      },
      {
        path: 'clients',
        loadChildren: () => import('../clients/clients.module').then(m => m.ClientsModule)
      },
      {
        path: 'payments',
        loadChildren: () => import('../payments/payments.module').then(m => m.PaymentsModule)
      },
      {
        path: 'courses',
        loadChildren: () => import('../courses/courses.module').then(m => m.CoursesModule)
      }
    ]
  }
];

@NgModule({
  declarations: [ShellComponent, SidenavComponent, TopbarComponent],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatMenuModule,
    MatBadgeModule,
    MatDividerModule
  ]
})
export class LayoutModule {}
