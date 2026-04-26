import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseCatalog } from '@core/models/course-catalog.model';
import { AuthService } from '@core/services/auth.service';
import { CrmService } from '@core/services/crm.service';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss'],
})
export class PaymentComponent implements OnInit {
  form: FormGroup;
  courseId: number | null = null;
  courseTitle = '';
  coursePrice = 0;
  course: CourseCatalog | null = null;

  submitting = false;
  success = false;
  errorMsg = '';
  showCvv = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private crmService: CrmService,
    public authService: AuthService
  ) {
    this.form = this.fb.group({
      customerName: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      cardNumber: ['', [Validators.required, Validators.pattern(/^\d{4}[\s-]?\d{4}[\s-]?\d{4}[\s-]?\d{4}$/)]],
      expiryMonth: ['', [Validators.required, Validators.pattern(/^(0[1-9]|1[0-2])$/)]],
      expiryYear: ['', [Validators.required, Validators.pattern(/^\d{2}$/)]],
      cvv: ['', [Validators.required, Validators.pattern(/^\d{3,4}$/)]],
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.courseId = params['courseId'] ? +params['courseId'] : null;
      this.courseTitle = params['title'] || 'Selected Course';
      this.coursePrice = params['price'] ? +params['price'] : 0;
    });

    if (this.authService.loggedIn()) {
      this.form.patchValue({
        customerName: this.authService.getUsername(),
        email: '',
      });
    }
  }

  formatCardInput(event: Event): void {
    const input = event.target as HTMLInputElement;
    let val = input.value.replace(/\D/g, '').substring(0, 16);
    input.value = val.replace(/(.{4})/g, '$1 ').trim();
    this.form.get('cardNumber')!.setValue(input.value, { emitEvent: false });
  }

  submit(): void {
    if (this.form.invalid || !this.courseId) return;

    this.submitting = true;
    this.errorMsg = '';

    const raw = this.form.value;
    const cardNumber = raw.cardNumber.replace(/\s/g, '');

    this.crmService
      .createPayment({
        courseId: this.courseId,
        customerName: raw.customerName,
        email: raw.email,
        cardNumber,
        amount: this.coursePrice,
        currency: 'USD',
      })
      .subscribe({
        next: () => {
          this.submitting = false;
          this.success = true;
          setTimeout(() => {
            if (this.authService.validateRoles(['ROLE_STUDENT'], 'any')) {
              this.router.navigate(['/student-services/my-courses']);
            } else {
              this.router.navigate(['/courses']);
            }
          }, 2500);
        },
        error: () => {
          this.submitting = false;
          this.errorMsg = 'Payment failed. Please check your details and try again.';
        },
      });
  }

  get cardCtrl() { return this.form.get('cardNumber')!; }
  get nameCtrl() { return this.form.get('customerName')!; }
  get emailCtrl() { return this.form.get('email')!; }
}
