import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { forkJoin } from 'rxjs';
import { catchError, of } from 'rxjs';
import { CrmApiService } from '../../../core/services/crm-api.service';
import { Lead, LEAD_SOURCE_LABELS, LEAD_STATUS_LABELS } from '../../../core/models/lead.model';
import { CrmCourse } from '../../../core/models/course.model';

@Component({
  selector: 'crm-lead-form-dialog',
  templateUrl: './lead-form-dialog.component.html'
})
export class LeadFormDialogComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  loadingOptions = true;
  courses: CrmCourse[] = [];
  isEdit: boolean;

  sourceOptions = Object.entries(LEAD_SOURCE_LABELS).map(([value, label]) => ({ value, label }));
  statusOptions = Object.entries(LEAD_STATUS_LABELS).map(([value, label]) => ({ value, label }));

  constructor(
    private fb: FormBuilder,
    private api: CrmApiService,
    private ref: MatDialogRef<LeadFormDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public lead: Lead | null
  ) {
    this.isEdit = !!lead;
  }

  ngOnInit(): void {
    this.form = this.fb.group({
      fullName:             [this.lead?.fullName || '',       Validators.required],
      phone:                [this.lead?.phone || '',           Validators.required],
      email:                [this.lead?.email || ''],
      source:               [this.lead?.source || 'WEBSITE',  Validators.required],
      status:               [this.lead?.status || 'NEW',      Validators.required],
      interestedCourseId:   [this.lead?.interestedCourseId || null],
      notes:                [this.lead?.notes || '']
    });

    this.api.getCourses().pipe(catchError(() => of([]))).subscribe(courses => {
      this.courses = courses;
      this.loadingOptions = false;
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;

    const courseId = this.form.value.interestedCourseId;
    const course = courseId ? this.courses.find(c => c.id === courseId) : null;
    const payload = {
      ...this.form.value,
      interestedCourseTitle: course?.title || (this.isEdit ? this.lead?.interestedCourseTitle : undefined)
    };

    const obs$ = this.isEdit
      ? this.api.updateLead(this.lead!.id, payload)
      : this.api.createLead(payload);

    obs$.subscribe({
      next: (result) => this.ref.close(result),
      error: () => { this.loading = false; }
    });
  }

  cancel(): void { this.ref.close(null); }
}
