import { Component, OnInit } from '@angular/core';
import { CrmApiService, DashboardStats } from '../../core/services/crm-api.service';
import { AuthService } from '../../core/services/auth.service';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'crm-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  loading = true;
  username = this.auth.getUsername();
  hasLms = this.auth.hasLmsAccess();
  lmsUrl = environment.lmsUrl;

  funnelStages = [
    { label: 'Новые лиды',     count: 284, pct: 100, color: '#3b82f6' },
    { label: 'Контакт',        count: 198, pct: 70,  color: '#0ea5e9' },
    { label: 'Квалификация',   count: 132, pct: 46,  color: '#7c3aed' },
    { label: 'Предложение',    count: 87,  pct: 31,  color: '#f59e0b' },
    { label: 'Клиент (WON)',   count: 147, pct: 52,  color: '#16a34a' }
  ];

  statCards = [
    { key: 'totalLeads',          label: 'Всего лидов',   icon: 'person_add', color: 'blue',   changeKey: 'newLeadsToday', changeLabel: 'новых сегодня' },
    { key: 'totalClients',        label: 'Клиентов',       icon: 'group',      color: 'green',  changeKey: 'newClientsThisMonth', changeLabel: 'за месяц' },
    { key: 'revenueThisMonth',    label: 'Доход за месяц', icon: 'payments',   color: 'purple', changeKey: null, changeLabel: '' },
    { key: 'conversionRate',      label: 'Конверсия',      icon: 'trending_up', color: 'orange', changeKey: null, changeLabel: '' }
  ];

  constructor(private api: CrmApiService, private auth: AuthService) {}

  ngOnInit(): void {
    this.api.getDashboardStats().subscribe({
      next: data => { this.stats = data; this.loading = false; },
      error: () => {
        this.stats = this.mockStats();
        this.loading = false;
      }
    });
  }

  getStatValue(key: string): string {
    if (!this.stats) return '—';
    const val = (this.stats as unknown as Record<string, number>)[key];
    if (key === 'revenueThisMonth' || key === 'totalRevenue') {
      return '₸' + val.toLocaleString('ru');
    }
    if (key === 'conversionRate') return val + '%';
    return String(val);
  }

  private mockStats(): DashboardStats {
    return {
      totalLeads: 284,
      newLeadsToday: 12,
      totalClients: 147,
      newClientsThisMonth: 23,
      totalRevenue: 4820000,
      revenueThisMonth: 840000,
      conversionRate: 52,
      totalCourses: 18
    };
  }
}
