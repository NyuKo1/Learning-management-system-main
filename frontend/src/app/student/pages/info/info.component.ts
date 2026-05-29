import { Component, OnInit } from '@angular/core';
import { Student } from '@core/models/student.model';
import { getStudyProgramDisplay } from '@core/models/study-program.model';
import { getUserDisplay } from '@core/models/user.model';
import { AuthService } from '@core/services/auth.service';
import {
  NotifyService,
  TelegramSubscriptionStatus,
} from '@core/services/notify.service';
import { StudentService } from '@core/services/student.service';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.scss'],
})
export class InfoComponent implements OnInit {
  student: Student;
  telegramStatus: TelegramSubscriptionStatus | null = null;
  botUsername: string = 'smarteducontrol_notify_bot';

  getUserDisplay = getUserDisplay;
  getStudyProgramDisplay = getStudyProgramDisplay;

  constructor(
    public authService: AuthService,
    public studentService: StudentService,
    private notifyService: NotifyService
  ) {}

  ngOnInit(): void {
    this.getStudent();
    this.getTelegramStatus();
  }

  getStudent() {
    this.studentService
      .getById([this.authService.getStudentId()])
      .subscribe((student: Student[]) => {
        this.student = student[0];
      });
  }

  getTelegramStatus(): void {
    const studentId = this.authService.getStudentId();
    if (!studentId) return;
    this.notifyService.getSubscription(studentId).subscribe({
      next: (status) => (this.telegramStatus = status),
      error: () => {},
    });
  }

  linkTelegram(): void {
    this.notifyService
      .linkUser(
        this.authService.getStudentId(),
        this.authService.getUsername()
      )
      .subscribe({
        next: (res) => {
          if (res.linked) {
            this.getTelegramStatus();
            window.alert('Telegram успешно привязан!');
          } else {
            window.alert(
              'Telegram не найден. Убедитесь, что отправили /start ' +
                this.authService.getUsername() +
                ' боту.'
            );
          }
        },
        error: () => window.alert('Ошибка при привязке. Попробуйте позже.'),
      });
  }
}
