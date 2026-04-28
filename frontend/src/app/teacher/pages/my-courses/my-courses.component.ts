import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PaymentResponse } from '@core/models/payment.model';
import { AuthService } from '@core/services/auth.service';
import { CrmService } from '@core/services/crm.service';

@Component({
  selector: 'app-teacher-my-courses',
  templateUrl: './my-courses.component.html',
  styleUrls: ['./my-courses.component.scss'],
})
export class TeacherMyCoursesComponent implements OnInit {
  payments: PaymentResponse[] = [];
  loading = true;
  error = false;

  constructor(
    private crmService: CrmService,
    public authService: AuthService,
    private router: Router
  ) {}

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
    const map: Record<string, string> = { BEGINNER: 'Beginner', INTERMEDIATE: 'Intermediate', ADVANCED: 'Advanced' };
    return map[level] || level;
  }

  getLevelColor(level: string): string {
    const map: Record<string, string> = { BEGINNER: '#10b981', INTERMEDIATE: '#f59e0b', ADVANCED: '#ef4444' };
    return map[level] || '#6b7280';
  }

  formatPrice(price: number): string {
    return '₸' + Number(price).toFixed(0);
  }

  formatDate(dateStr?: string): string {
    if (!dateStr) return '';
    return new Date(dateStr).toLocaleDateString('ru-RU', { year: 'numeric', month: 'short', day: 'numeric' });
  }

  openCourse(courseId: number): void {
    this.router.navigate(['/teacher-services/course', courseId]);
  }
}
