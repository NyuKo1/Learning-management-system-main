import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CrmApiService } from '../../../core/services/crm-api.service';
import { Client } from '../../../core/models/client.model';
import { CreateLmsAccountDialogComponent } from '../../components/create-lms-account-dialog/create-lms-account-dialog.component';
import { environment } from '../../../../environments/environment';

@Component({
  selector: 'crm-clients-list',
  templateUrl: './clients-list.component.html',
  styleUrls: ['./clients-list.component.scss']
})
export class ClientsListComponent implements OnInit {
  clients: Client[] = [];
  filtered: Client[] = [];
  loading = true;
  loadError = false;
  search = '';
  lmsUrl = environment.lmsUrl;

  constructor(
    private api: CrmApiService,
    private dialog: MatDialog,
    private snack: MatSnackBar
  ) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.loadError = false;
    this.api.getClients().subscribe({
      next: data => { this.clients = data; this.applySearch(); this.loading = false; },
      error: () => { this.loadError = true; this.clients = []; this.filtered = []; this.loading = false; }
    });
  }

  applySearch(): void {
    const q = this.search.toLowerCase();
    this.filtered = q
      ? this.clients.filter(c => c.fullName.toLowerCase().includes(q) || c.email.toLowerCase().includes(q))
      : [...this.clients];
  }

  goToLms(lmsUserId: number): void {
    window.open(`${this.lmsUrl}/admin/students/${lmsUserId}`, '_blank');
  }

  openCreateLmsDialog(client: Client): void {
    const ref = this.dialog.open(CreateLmsAccountDialogComponent, {
      data: client,
      width: '460px',
      disableClose: true
    });
    ref.afterClosed().subscribe(result => {
      if (!result?.success) return;
      const target = this.clients.find(c => c.id === client.id);
      if (target) {
        target.hasLmsAccount = true;
        target.lmsUserId = result.lmsUserId;
      }
      this.applySearch();
      this.snack.open(`Аккаунт создан: ${result.username}`, 'OK', { duration: 4000 });
    });
  }

}
