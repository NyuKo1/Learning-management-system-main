import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { catchError, of } from 'rxjs';
import { CrmApiService, DashboardStats } from '../../core/services/crm-api.service';
import { AuthService } from '../../core/services/auth.service';
import { environment } from '../../../environments/environment';

interface FunnelStage { label: string; count: number; pct: number; color: string; }

const FUNNEL_META: Record<string, { label: string; color: string }> = {
  NEW:       { label: 'Новые лиды',    color: '#3b82f6' },
  CONTACTED: { label: 'Контакт',       color: '#0ea5e9' },
  QUALIFIED: { label: 'Квалификация',  color: '#7c3aed' },
  PROPOSAL:  { label: 'Предложение',   color: '#f59e0b' },
  WON:       { label: 'Клиент (WON)',  color: '#16a34a' },
  LOST:      { label: 'Отказ',         color: '#dc2626' },
};

@Component({
  selector: 'crm-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  stats: DashboardStats | null = null;
  funnelStages: FunnelStage[] = [];
  loading = true;
  loadError = false;
  username = this.auth.getUsername();
  hasLms = this.auth.hasLmsAccess();
  lmsUrl = environment.lmsUrl;

  statCards = [
    { key: 'totalLeads',       label: 'Всего лидов',   icon: 'person_add',  color: 'blue',   changeKey: 'newLeadsToday',       changeLabel: 'новых сегодня' },
    { key: 'totalClients',     label: 'Клиентов',       icon: 'group',       color: 'green',  changeKey: 'newClientsThisMonth', changeLabel: 'за месяц' },
    { key: 'revenueThisMonth', label: 'Доход за месяц', icon: 'payments',    color: 'purple', changeKey: null,                  changeLabel: '' },
    { key: 'conversionRate',   label: 'Конверсия',      icon: 'trending_up', color: 'orange', changeKey: null,                  changeLabel: '' }
  ];

  constructor(private api: CrmApiService, private auth: AuthService) {}

  ngOnInit(): void {
    forkJoin({
      stats:     this.api.getDashboardStats().pipe(catchError(() => of(null))),
      analytics: this.api.getDashboardAnalytics().pipe(catchError(() => of(null)))
    }).subscribe(({ stats, analytics }) => {
      if (!stats) {
        this.loadError = true;
        this.loading = false;
        return;
      }

      this.stats = stats;

      if (analytics?.funnel) {
        const counts = Object.values(analytics.funnel) as number[];
        const max = Math.max(...counts, 1);
        this.funnelStages = Object.entries(analytics.funnel)
          .filter(([k]) => k !== 'LOST')
          .map(([key, count]) => ({
            label: FUNNEL_META[key]?.label || key,
            count: count as number,
            pct: Math.round((count as number) / max * 100),
            color: FUNNEL_META[key]?.color || '#64748b'
          }));
      }

      this.loading = false;
    });
  }

  getStatValue(key: string): string {
    if (!this.stats) return '—';
    const val = (this.stats as unknown as Record<string, number>)[key];
    if (key === 'revenueThisMonth' || key === 'totalRevenue') {
      return '₸' + Number(val).toLocaleString('ru');
    }
    if (key === 'conversionRate') return val + '%';
    return String(val);
  }
}
