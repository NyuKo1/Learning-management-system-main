import { Component, OnInit } from '@angular/core';
import { CrmApiService } from '../../../core/services/crm-api.service';
import { Client } from '../../../core/models/client.model';
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
  search = '';
  lmsUrl = environment.lmsUrl;

  constructor(private api: CrmApiService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.api.getClients().subscribe({
      next: data => { this.clients = data; this.applySearch(); this.loading = false; },
      error: () => { this.clients = this.mock(); this.applySearch(); this.loading = false; }
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

  private mock(): Client[] {
    return [
      { id: 1, fullName: 'Арман Сейтказы',   phone: '+7 701 111 2222', email: 'arman@mail.ru',   totalPurchases: 3, totalSpent: 45000, hasLmsAccount: true,  lmsUserId: 12, createdAt: '2024-03-10' },
      { id: 2, fullName: 'Айгерим Нурланова', phone: '+7 702 222 3333', email: 'aigul@gmail.com', totalPurchases: 1, totalSpent: 15000, hasLmsAccount: true,  lmsUserId: 15, createdAt: '2024-04-01' },
      { id: 3, fullName: 'Гульназ Ахметова',  phone: '+7 707 333 4444', email: 'gulnaz@mail.ru',  totalPurchases: 2, totalSpent: 28000, hasLmsAccount: false, createdAt: '2024-04-15' }
    ];
  }
}
