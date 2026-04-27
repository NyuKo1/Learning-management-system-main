import { Component, OnInit } from '@angular/core';
import { CrmApiService } from '../../../core/services/crm-api.service';
import { Payment, PAYMENT_STATUS_LABELS } from '../../../core/models/payment.model';

@Component({
  selector: 'crm-payments-list',
  templateUrl: './payments-list.component.html',
  styleUrls: ['./payments-list.component.scss']
})
export class PaymentsListComponent implements OnInit {
  payments: Payment[] = [];
  loading = true;
  statusLabels = PAYMENT_STATUS_LABELS;

  get totalRevenue(): number {
    return this.payments.filter(p => p.status === 'SUCCESS').reduce((s, p) => s + p.amount, 0);
  }

  get successCount(): number {
    return this.payments.filter(p => p.status === 'SUCCESS').length;
  }

  constructor(private api: CrmApiService) {}

  ngOnInit(): void {
    this.api.getPayments().subscribe({
      next: data => { this.payments = data; this.loading = false; },
      error: () => { this.payments = this.mock(); this.loading = false; }
    });
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      SUCCESS: 'badge-success', PENDING: 'badge-pending', FAILED: 'badge-failed', REFUNDED: 'badge-pending'
    };
    return map[status] || 'badge-pending';
  }

  private mock(): Payment[] {
    return [
      { id: 1, clientId: 1, clientName: 'Арман Сейтказы',   courseId: 1, courseTitle: 'Python для начинающих', amount: 15000, currency: '₸', status: 'SUCCESS',  method: 'CARD',     createdAt: '2024-04-25T10:00:00' },
      { id: 2, clientId: 2, clientName: 'Айгерим Нурланова', courseId: 2, courseTitle: 'Веб-разработка',        amount: 25000, currency: '₸', status: 'SUCCESS',  method: 'TRANSFER', createdAt: '2024-04-24T14:00:00' },
      { id: 3, clientId: 3, clientName: 'Гульназ Ахметова',  courseId: 3, courseTitle: 'UI/UX Design',          amount: 20000, currency: '₸', status: 'PENDING',  method: 'CARD',     createdAt: '2024-04-26T09:00:00' },
      { id: 4, clientId: 1, clientName: 'Арман Сейтказы',   courseId: 4, courseTitle: 'Data Science',          amount: 30000, currency: '₸', status: 'FAILED',   method: 'CARD',     createdAt: '2024-04-23T16:00:00' }
    ];
  }
}
