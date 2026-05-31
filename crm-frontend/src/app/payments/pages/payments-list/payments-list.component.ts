import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { forkJoin } from 'rxjs';
import { catchError, of } from 'rxjs';
import { CrmApiService } from '../../../core/services/crm-api.service';
import { Payment, PaymentMethod, PAYMENT_STATUS_LABELS, PAYMENT_METHOD_LABELS } from '../../../core/models/payment.model';
import { CrmCourse } from '../../../core/models/course.model';

@Component({
  selector: 'crm-payments-list',
  templateUrl: './payments-list.component.html',
  styleUrls: ['./payments-list.component.scss']
})
export class PaymentsListComponent implements OnInit {
  payments: Payment[] = [];
  courses: CrmCourse[] = [];
  loading = true;
  statusLabels = PAYMENT_STATUS_LABELS;
  methodLabels = PAYMENT_METHOD_LABELS;
  showForm = false;
  processing = false;

  form = {
    customerName: '',
    email: '',
    courseId: null as number | null,
    customCourseTitle: '',
    amount: null as number | null,
    method: 'CARD' as PaymentMethod,
    cardNumber: ''
  };

  methods: PaymentMethod[] = ['CARD', 'CASH', 'TRANSFER', 'ONLINE'];

  get totalRevenue(): number {
    return this.payments
      .filter(p => p.status === 'SUCCESS' || p.status === 'COMPLETED')
      .reduce((s, p) => s + p.amount, 0);
  }

  get successCount(): number {
    return this.payments.filter(p => p.status === 'SUCCESS' || p.status === 'COMPLETED').length;
  }

  get pendingCount(): number {
    return this.payments.filter(p => p.status === 'PENDING').length;
  }

  constructor(private api: CrmApiService, private snack: MatSnackBar) {}

  ngOnInit(): void {
    forkJoin({
      payments: this.api.getPayments().pipe(catchError(() => of([]))),
      courses: this.api.getCourses().pipe(catchError(() => of([])))
    }).subscribe(({ payments, courses }) => {
      this.payments = payments;
      this.courses = courses;
      this.loading = false;
    });
  }

  selectedCourseName(): string {
    if (!this.form.courseId) return '';
    return this.courses.find(c => c.id === this.form.courseId)?.title || '';
  }

  onCourseChange(): void {
    const course = this.courses.find(c => c.id === this.form.courseId);
    if (course && !this.form.amount) {
      this.form.amount = course.price;
    }
  }

  canSubmit(): boolean {
    return !!(this.form.customerName.trim() &&
              (this.form.courseId || this.form.customCourseTitle.trim()) &&
              this.form.amount && this.form.amount > 0);
  }

  processPayment(): void {
    if (!this.canSubmit()) return;
    this.processing = true;

    const courseTitle = this.form.courseId
      ? this.courses.find(c => c.id === this.form.courseId)?.title || this.form.customCourseTitle
      : this.form.customCourseTitle;

    // PCI: never transmit full card number — mask client-side to last 4 digits.
    const cleanedCard = (this.form.cardNumber || '').replace(/\s/g, '');
    const cardLastFour = this.form.method === 'CARD' && cleanedCard.length >= 4
      ? cleanedCard.slice(-4)
      : undefined;

    const req = {
      customerName: this.form.customerName,
      email: this.form.email || undefined,
      courseId: this.form.courseId || undefined,
      courseTitle,
      amount: this.form.amount!,
      currency: '₸',
      method: this.form.method,
      cardLastFour
    };

    this.api.createPayment(req).subscribe({
      next: (payment) => {
        payment.courseTitle = payment.courseTitle || courseTitle;
        this.payments = [payment, ...this.payments];
        this.processing = false;
        this.showForm = false;
        this.resetForm();
        this.snack.open('✓ Платёж записан', 'Закрыть', { duration: 3000 });
      },
      error: () => {
        this.processing = false;
        this.snack.open('Ошибка при записи платежа', 'Закрыть', { duration: 3000 });
      }
    });
  }

  private resetForm(): void {
    this.form = { customerName: '', email: '', courseId: null, customCourseTitle: '', amount: null, method: 'CARD', cardNumber: '' };
  }

  getEffectiveCourseTitle(p: Payment): string {
    return p.courseTitle || p.course?.title || '—';
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      SUCCESS: 'badge-success', COMPLETED: 'badge-success',
      PENDING: 'badge-pending', FAILED: 'badge-failed', REFUNDED: 'badge-pending'
    };
    return map[status] || 'badge-pending';
  }

  getMethodIcon(method?: string): string {
    const icons: Record<string, string> = { CARD: 'credit_card', CASH: 'payments', TRANSFER: 'swap_horiz', ONLINE: 'language' };
    return icons[method || ''] || 'payment';
  }
}
