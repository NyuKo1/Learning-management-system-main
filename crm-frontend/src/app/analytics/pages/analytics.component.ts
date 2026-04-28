import { Component, OnInit } from '@angular/core';
import { CrmApiService, AnalyticsData, DashboardStats } from '../../core/services/crm-api.service';
import { forkJoin } from 'rxjs';
import { catchError, of } from 'rxjs';

interface MonthStat { month: string; revenue: number; leads: number; }
interface FunnelRow  { label: string; count: number; pct: number; color: string; }
interface MethodRow  { label: string; pct: number; color: string; }

const STATUS_LABELS: Record<string, string> = {
  NEW: 'Новые лиды', CONTACTED: 'Контакт', QUALIFIED: 'Квалифицирован',
  PROPOSAL: 'Предложение', WON: 'Стал клиентом', LOST: 'Отказ'
};
const STATUS_COLORS: Record<string, string> = {
  NEW: '#3b82f6', CONTACTED: '#0ea5e9', QUALIFIED: '#7c3aed',
  PROPOSAL: '#f59e0b', WON: '#16a34a', LOST: '#dc2626'
};
const METHOD_LABELS: Record<string, string> = {
  CARD: 'Банковская карта', CASH: 'Наличные', TRANSFER: 'Перевод', ONLINE: 'Онлайн-оплата'
};
const METHOD_COLORS: Record<string, string> = {
  CARD: '#3b82f6', CASH: '#f59e0b', TRANSFER: '#7c3aed', ONLINE: '#16a34a'
};

@Component({
  selector: 'crm-analytics',
  templateUrl: './analytics.component.html',
  styleUrls: ['./analytics.component.scss']
})
export class AnalyticsComponent implements OnInit {
  loading = true;
  loadError = false;

  months: MonthStat[] = [];
  funnel: FunnelRow[] = [];
  paymentMethods: MethodRow[] = [];
  topCourses: { title: string; sales: number; revenue: number }[] = [];

  totalRevenue = 0;
  totalLeads = 0;
  totalClients = 0;
  conversionRate = 0;

  constructor(private api: CrmApiService) {}

  ngOnInit(): void {
    this.loadError = false;
    forkJoin({
      analytics: this.api.getDashboardAnalytics().pipe(catchError(() => of(null))),
      stats:     this.api.getDashboardStats().pipe(catchError(() => of(null)))
    }).subscribe(({ analytics, stats }) => {
      if (!analytics && !stats) {
        this.loadError = true;
        this.loading = false;
        return;
      }
      if (analytics) {
        this.buildFromAnalytics(analytics);
      }
      if (stats) {
        this.totalRevenue    = Number(stats.totalRevenue) || 0;
        this.totalLeads      = stats.totalLeads;
        this.totalClients    = stats.totalClients;
        this.conversionRate  = stats.conversionRate;
      }
      this.loading = false;
    });
  }

  private buildFromAnalytics(data: AnalyticsData): void {
    this.months = data.monthly.map(m => ({
      month: m.month,
      revenue: Number(m.revenue) || 0,
      leads: m.leads
    }));

    const funnelEntries = Object.entries(data.funnel);
    const maxCount = Math.max(...funnelEntries.map(([, v]) => v), 1);
    this.funnel = funnelEntries.map(([key, count]) => ({
      label: STATUS_LABELS[key] || key,
      count,
      pct: Math.round(count / maxCount * 100),
      color: STATUS_COLORS[key] || '#64748b'
    }));

    const methodEntries = Object.entries(data.methods);
    const totalMethods = methodEntries.reduce((s, [, v]) => s + v, 0) || 1;
    this.paymentMethods = methodEntries.map(([key, count]) => ({
      label: METHOD_LABELS[key] || key,
      pct: Math.round(count / totalMethods * 100),
      color: METHOD_COLORS[key] || '#64748b'
    }));

    this.topCourses = (data.topCourses || []).map(c => ({
      title: c.title,
      sales: c.sales,
      revenue: Number(c.revenue) || 0
    }));
  }

  get maxRevenue(): number {
    return Math.max(...this.months.map(m => m.revenue), 1);
  }

  barHeight(revenue: number): string {
    return (revenue / this.maxRevenue * 100) + '%';
  }

  formatRevenue(v: number): string {
    return v >= 1000000
      ? '₸' + (v / 1000000).toFixed(1) + 'M'
      : '₸' + (v / 1000).toFixed(0) + 'K';
  }
}
