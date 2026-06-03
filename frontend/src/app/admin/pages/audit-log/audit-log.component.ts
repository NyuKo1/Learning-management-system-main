import { Component, OnInit } from '@angular/core';
import { AuditLogService } from '@core/services/audit-log.service';
import { AuditEvent, AuditFilters, AuditPage } from '@core/models/audit-event.model';

@Component({
  selector: 'app-audit-log',
  templateUrl: './audit-log.component.html',
  styleUrls: ['./audit-log.component.scss'],
})
export class AuditLogComponent implements OnInit {
  page: AuditPage = { content: [], totalElements: 0, totalPages: 0, number: 0, size: 50 };
  loading = false;
  selected: AuditEvent | null = null;

  filters: AuditFilters = {};
  pageIndex = 0;
  pageSize = 50;

  displayedColumns = ['occurredAt', 'username', 'serviceName', 'httpMethod', 'path', 'status', 'durationMs'];

  constructor(private api: AuditLogService) {}

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    this.loading = true;
    this.api.search(this.filters, this.pageIndex, this.pageSize).subscribe({
      next: (p) => {
        this.page = p;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      },
    });
  }

  onFilterChange(): void {
    this.pageIndex = 0;
    this.load();
  }

  onPageChange(event: { pageIndex: number; pageSize: number }): void {
    this.pageIndex = event.pageIndex;
    this.pageSize = event.pageSize;
    this.load();
  }

  open(e: AuditEvent): void {
    this.api.findOne(e.id).subscribe((detail) => (this.selected = detail));
  }

  close(): void {
    this.selected = null;
  }

  exportCsv(): void {
    const headers = ['id', 'occurredAt', 'username', 'serviceName', 'httpMethod', 'path', 'status', 'durationMs'];
    const rows = this.page.content.map((e) =>
      headers.map((h) => JSON.stringify((e as any)[h] ?? '')).join(',')
    );
    const csv = [headers.join(','), ...rows].join('\n');
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `audit-log-page-${this.pageIndex}.csv`;
    a.click();
    URL.revokeObjectURL(url);
  }
}
