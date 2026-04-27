import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CrmApiService } from '../../../core/services/crm-api.service';
import { Lead, LeadStatus, LEAD_STATUS_LABELS, LEAD_SOURCE_LABELS } from '../../../core/models/lead.model';
import { LeadFormDialogComponent } from '../../components/lead-form-dialog/lead-form-dialog.component';

@Component({
  selector: 'crm-leads-list',
  templateUrl: './leads-list.component.html',
  styleUrls: ['./leads-list.component.scss']
})
export class LeadsListComponent implements OnInit {
  leads: Lead[] = [];
  filteredLeads: Lead[] = [];
  loading = true;
  filterStatus = '';

  displayedColumns = ['fullName', 'phone', 'source', 'status', 'course', 'manager', 'createdAt', 'actions'];

  statusLabels = LEAD_STATUS_LABELS;
  sourceLabels = LEAD_SOURCE_LABELS;

  statuses: { value: string; label: string }[] = [
    { value: '', label: 'Все статусы' },
    ...Object.entries(LEAD_STATUS_LABELS).map(([value, label]) => ({ value, label }))
  ];

  constructor(
    private api: CrmApiService,
    private dialog: MatDialog,
    private snack: MatSnackBar
  ) {}

  ngOnInit(): void { this.loadLeads(); }

  loadLeads(): void {
    this.loading = true;
    this.api.getLeads().subscribe({
      next: data => {
        this.leads = data;
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.leads = this.mockLeads();
        this.applyFilter();
        this.loading = false;
      }
    });
  }

  applyFilter(): void {
    this.filteredLeads = this.filterStatus
      ? this.leads.filter(l => l.status === this.filterStatus)
      : [...this.leads];
  }

  openCreateDialog(): void {
    const ref = this.dialog.open(LeadFormDialogComponent, { width: '520px' });
    ref.afterClosed().subscribe(result => {
      if (result) { this.loadLeads(); this.snack.open('Лид добавлен', 'OK', { duration: 3000 }); }
    });
  }

  updateStatus(lead: Lead, status: LeadStatus): void {
    this.api.updateLeadStatus(lead.id, status).subscribe({
      next: () => { lead.status = status; this.snack.open('Статус обновлён', 'OK', { duration: 2000 }); },
      error: () => this.snack.open('Ошибка обновления', 'OK', { duration: 2000 })
    });
  }

  deleteLead(id: number): void {
    if (!confirm('Удалить лид?')) return;
    this.api.deleteLead(id).subscribe({
      next: () => { this.leads = this.leads.filter(l => l.id !== id); this.applyFilter(); },
      error: () => this.snack.open('Ошибка удаления', 'OK', { duration: 2000 })
    });
  }

  getStatusClass(status: LeadStatus): string {
    const map: Record<LeadStatus, string> = {
      NEW: 'badge-new', CONTACTED: 'badge-pending', QUALIFIED: 'badge-pending',
      PROPOSAL: 'badge-pending', WON: 'badge-active', LOST: 'badge-lost'
    };
    return map[status];
  }

  private mockLeads(): Lead[] {
    return [
      { id: 1, fullName: 'Арман Сейтказы', phone: '+7 701 123 4567', email: 'arman@mail.ru', status: 'NEW', source: 'WEBSITE', interestedCourseTitle: 'Python для начинающих', createdAt: '2024-04-25T10:00:00', updatedAt: '2024-04-25T10:00:00' },
      { id: 2, fullName: 'Айгерим Нурланова', phone: '+7 702 234 5678', status: 'CONTACTED', source: 'SOCIAL', interestedCourseTitle: 'Веб-разработка', createdAt: '2024-04-24T14:30:00', updatedAt: '2024-04-25T09:00:00' },
      { id: 3, fullName: 'Даниар Жаксыбеков', phone: '+7 705 345 6789', status: 'WON', source: 'REFERRAL', interestedCourseTitle: 'UI/UX Design', createdAt: '2024-04-20T11:00:00', updatedAt: '2024-04-23T16:00:00' },
      { id: 4, fullName: 'Гульназ Ахметова', phone: '+7 707 456 7890', status: 'LOST', source: 'AD', createdAt: '2024-04-18T09:00:00', updatedAt: '2024-04-22T12:00:00' }
    ];
  }
}
