import { Component, OnInit } from '@angular/core';
import { PaymentResponse } from '@core/models/payment.model';
import { AuthService } from '@core/services/auth.service';
import { CrmService } from '@core/services/crm.service';

@Component({
  selector: 'app-my-courses',
  templateUrl: './my-courses.component.html',
  styleUrls: ['./my-courses.component.scss'],
})
export class MyCoursesComponent implements OnInit {
  payments: PaymentResponse[] = [];
  loading = true;
  error = false;

  constructor(private crmService: CrmService, public authService: AuthService) {}

  ngOnInit(): void {
    const username = this.authService.getUsername();
    this.crmService.getMyPurchases(username).subscribe({
      next: (data) => {
        this.payments = data;
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      },
    });
  }

  getTags(tags: string): string[] {
    return this.crmService.getTagsArray(tags);
  }

  getLevelLabel(level: string): string {
    const map: Record<string, string> = {
      BEGINNER: 'Beginner',
      INTERMEDIATE: 'Intermediate',
      ADVANCED: 'Advanced',
    };
    return map[level] || level;
  }

  getLevelColor(level: string): string {
    const map: Record<string, string> = {
      BEGINNER: '#10b981',
      INTERMEDIATE: '#f59e0b',
      ADVANCED: '#ef4444',
    };
    return map[level] || '#6b7280';
  }

  formatPrice(price: number): string {
    return '$' + Number(price).toFixed(2);
  }

  formatDate(dateStr?: string): string {
    if (!dateStr) return '';
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
  }
}
