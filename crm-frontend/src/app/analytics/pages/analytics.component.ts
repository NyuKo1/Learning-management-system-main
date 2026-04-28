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
    forkJoin({
      analytics: this.api.getDashboardAnalytics().pipe(catchError(() => of(null))),
      stats:     this.api.getDashboardStats().pipe(catchError(() => of(null)))
    }).subscribe(({ analytics, stats }) => {
      if (analytics) {
        this.buildFromAnalytics(analytics);
      } else {
        this.loadFallback();
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

  private loadFallback(): void {
    this.months = [
      { month: 'Ноя', revenue: 320000, leads: 38 },
      { month: 'Дек', revenue: 410000, leads: 45 },
      { month: 'Янв', revenue: 290000, leads: 31 },
      { month: 'Фев', revenue: 560000, leads: 62 },
      { month: 'Мар', revenue: 740000, leads: 74 },
      { month: 'Апр', revenue: 840000, leads: 89 },
    ];
    this.funnel = [
      { label: 'Новые лиды',   count: 284, pct: 100, color: '#3b82f6' },
      { label: 'Контакт',      count: 198, pct: 70,  color: '#0ea5e9' },
      { label: 'Квалификация', count: 132, pct: 46,  color: '#7c3aed' },
      { label: 'Предложение',  count: 87,  pct: 31,  color: '#f59e0b' },
      { label: 'Клиент (WON)', count: 147, pct: 52,  color: '#16a34a' },
    ];
    this.paymentMethods = [
      { label: 'Банковская карта', pct: 54, color: '#3b82f6' },
      { label: 'Перевод',         pct: 28, color: '#7c3aed' },
      { label: 'Наличные',        pct: 11, color: '#f59e0b' },
      { label: 'Онлайн-оплата',  pct: 7,  color: '#16a34a' },
    ];
    this.topCourses = [
      { title: 'Python для начинающих',     sales: 47, revenue: 705000 },
      { title: 'Веб-разработка Full Stack', sales: 34, revenue: 850000 },
      { title: 'UI/UX Design',              sales: 28, revenue: 560000 },
      { title: 'Data Science & ML',         sales: 22, revenue: 660000 },
    ];
    this.totalRevenue = 3160000;
    this.totalLeads   = 339;
    this.totalClients = 147;
    this.conversionRate = 43;
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
