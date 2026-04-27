import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { CrmApiService } from '../../../core/services/crm-api.service';
import { LEAD_SOURCE_LABELS } from '../../../core/models/lead.model';

@Component({
  selector: 'crm-lead-form-dialog',
  templateUrl: './lead-form-dialog.component.html'
})
export class LeadFormDialogComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  sourceOptions = Object.entries(LEAD_SOURCE_LABELS).map(([value, label]) => ({ value, label }));

  constructor(
    private fb: FormBuilder,
    private api: CrmApiService,
    private ref: MatDialogRef<LeadFormDialogComponent>
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      fullName: ['', Validators.required],
      phone:    ['', Validators.required],
      email:    [''],
      source:   ['WEBSITE', Validators.required],
      notes:    ['']
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.api.createLead({ ...this.form.value, status: 'NEW' }).subscribe({
      next: () => this.ref.close(true),
      error: () => { this.loading = false; }
    });
  }

  cancel(): void { this.ref.close(false); }
}
