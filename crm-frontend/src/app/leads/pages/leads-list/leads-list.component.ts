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
  loadError = false;
  filterStatus = '';
  search = '';

  statusLabels = LEAD_STATUS_LABELS;
  sourceLabels = LEAD_SOURCE_LABELS;

  statuses: { value: string; label: string }[] = [
    { value: '', label: 'Все статусы' },
    ...Object.entries(LEAD_STATUS_LABELS).map(([value, label]) => ({ value, label }))
  ];

  allStatuses: { value: LeadStatus; label: string; icon: string }[] = [
    { value: 'NEW',       label: 'Новый',           icon: 'fiber_new' },
    { value: 'CONTACTED', label: 'Контакт',          icon: 'phone' },
    { value: 'QUALIFIED', label: 'Квалифицирован',   icon: 'verified' },
    { value: 'PROPOSAL',  label: 'Предложение',      icon: 'description' },
    { value: 'WON',       label: 'Стал клиентом',    icon: 'check_circle' },
    { value: 'LOST',      label: 'Отказ',            icon: 'cancel' },
  ];

  constructor(
    private api: CrmApiService,
    private dialog: MatDialog,
    private snack: MatSnackBar
  ) {}

  ngOnInit(): void { this.loadLeads(); }

  loadLeads(): void {
    this.loading = true;
    this.loadError = false;
    this.api.getLeads().subscribe({
      next: data => {
        this.leads = data;
        this.applyFilter();
        this.loading = false;
      },
      error: () => {
        this.loadError = true;
        this.leads = [];
        this.filteredLeads = [];
        this.loading = false;
      }
    });
  }

  applyFilter(): void {
    let result = [...this.leads];
    if (this.filterStatus) {
      result = result.filter(l => l.status === this.filterStatus);
    }
    if (this.search) {
      const q = this.search.toLowerCase();
      result = result.filter(l =>
        l.fullName.toLowerCase().includes(q) ||
        (l.phone || '').includes(q) ||
        (l.email || '').toLowerCase().includes(q)
      );
    }
    this.filteredLeads = result;
  }

  openCreateDialog(): void {
    const ref = this.dialog.open(LeadFormDialogComponent, {
      width: '540px',
      data: null
    });
    ref.afterClosed().subscribe(result => {
      if (result) {
        this.leads.unshift(result);
        this.applyFilter();
        this.snack.open('Лид добавлен', 'OK', { duration: 3000 });
      }
    });
  }

  editLead(lead: Lead): void {
    const ref = this.dialog.open(LeadFormDialogComponent, {
      width: '540px',
      data: lead
    });
    ref.afterClosed().subscribe((result: Lead | null) => {
      if (!result) return;
      const idx = this.leads.findIndex(l => l.id === lead.id);
      if (idx !== -1) this.leads[idx] = result;
      this.applyFilter();
      this.snack.open('Лид обновлён', 'OK', { duration: 3000 });
    });
  }

  updateStatus(lead: Lead, status: LeadStatus): void {
    this.api.updateLeadStatus(lead.id, status).subscribe({
      next: (updated) => {
        lead.status = updated.status || status;
        this.applyFilter();
        this.snack.open(`Статус: ${this.statusLabels[status]}`, 'OK', { duration: 2000 });
      },
      error: () => this.snack.open('Ошибка обновления', 'OK', { duration: 2000 })
    });
  }

  deleteLead(lead: Lead): void {
    if (!confirm(`Удалить лид "${lead.fullName}"?`)) return;
    this.api.deleteLead(lead.id).subscribe({
      next: () => {
        this.leads = this.leads.filter(l => l.id !== lead.id);
        this.applyFilter();
        this.snack.open('Лид удалён', 'OK', { duration: 2000 });
      },
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
}
