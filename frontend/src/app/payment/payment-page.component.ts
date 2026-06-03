import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '@core/services/auth.service';
import { CrmService } from '@core/services/crm.service';

@Component({
  selector: 'app-payment-page',
  templateUrl: './payment-page.component.html',
  styleUrls: ['./payment-page.component.scss'],
})
export class PaymentPageComponent implements OnInit {
  form: FormGroup;
  courseId!: number;
  courseTitle = '';
  price = 0;

  submitting = false;
  success = false;
  errorMsg = '';

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private crmService: CrmService,
    private authService: AuthService
  ) {
    // Placeholder payment — accepts any non-empty values; backend always returns SUCCESS.
    this.form = this.fb.group({
      customerName: ['', Validators.required],
      email: ['', Validators.required],
      cardNumber: ['', Validators.required],
      cardExpiry: ['', Validators.required],
      cardCvv: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.courseId = Number(this.route.snapshot.queryParamMap.get('courseId'));
    this.courseTitle = this.route.snapshot.queryParamMap.get('title') || '';
    this.price = Number(this.route.snapshot.queryParamMap.get('price')) || 0;

    // Pre-fill email from token if available
    const username = this.authService.getUsername();
    if (username && username.includes('@')) {
      this.form.patchValue({ email: username });
    }
  }

  formatCardNumber(event: Event): void {
    const input = event.target as HTMLInputElement;
    const val = input.value.replace(/\D/g, '').substring(0, 16);
    input.value = val.replace(/(.{4})/g, '$1 ').trim();
    this.form.get('cardNumber')?.setValue(input.value, { emitEvent: false });
  }

  formatExpiry(event: Event): void {
    const input = event.target as HTMLInputElement;
    let val = input.value.replace(/\D/g, '').substring(0, 4);
    if (val.length >= 2) val = val.substring(0, 2) + '/' + val.substring(2);
    input.value = val;
    this.form.get('cardExpiry')?.setValue(input.value, { emitEvent: false });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting = true;
    this.errorMsg = '';

    const { customerName, email, cardNumber } = this.form.value;
    this.crmService
      .createPayment({
        courseId: this.courseId,
        customerName,
        email,
        cardNumber: cardNumber.replace(/\s/g, ''),
        amount: this.price,
        currency: '₸',
      })
      .subscribe({
        next: () => {
          this.success = true;
          this.submitting = false;
          setTimeout(() => this.router.navigate(['/student-services/my-courses']), 2000);
        },
        error: (err) => {
          this.errorMsg =
            err?.error?.message || 'Ошибка при обработке платежа. Попробуйте ещё раз.';
          this.submitting = false;
        },
      });
  }

  goBack(): void {
    this.router.navigate(['/student-services/courses']);
  }
}
