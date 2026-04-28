import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CrmApiService } from '../../../core/services/crm-api.service';
import { Client } from '../../../core/models/client.model';

@Component({
  selector: 'crm-create-lms-account-dialog',
  templateUrl: './create-lms-account-dialog.component.html',
  styleUrls: ['./create-lms-account-dialog.component.scss']
})
export class CreateLmsAccountDialogComponent {
  form: FormGroup;
  loading = false;
  error = '';
  showPassword = false;

  constructor(
    private fb: FormBuilder,
    private api: CrmApiService,
    public dialogRef: MatDialogRef<CreateLmsAccountDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public client: Client
  ) {
    this.form = this.fb.group({
      username: [client.email || '', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.error = '';
    const { username, password } = this.form.value;
    this.api.createLmsStudent(username, password).subscribe({
      next: (res) => {
        this.loading = false;
        this.dialogRef.close({ success: true, lmsUserId: res.id, username });
      },
      error: (err) => {
        this.loading = false;
        this.error = err.status === 409
          ? 'Пользователь с таким email уже существует'
          : 'Ошибка создания аккаунта. Попробуйте снова.';
      }
    });
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
}
