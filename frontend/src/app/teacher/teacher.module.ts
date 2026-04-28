import { NgModule } from '@angular/core';
import { SharedModule } from '@shared/shared.module';

import { TeacherRoutingModule } from './teacher-routing.module';
import { TeacherServicesComponent } from './pages/teacher-services.component';
import { SubjectComponent } from './pages/subject/subject.component';
import { SubjectEnrollmentComponent } from './pages/subject-enrollment/subject-enrollment.component';
import { SubjectMaterialComponent } from './pages/subject-material/subject-material.component';
import { SubjectNotificationComponent } from './pages/subject-notification/subject-notification.component';
import { SubjectTermComponent } from './pages/subject-term/subject-term.component';
import { ExamComponent } from './pages/exam/exam.component';
import { ExamRealizationComponent } from './pages/exam-realization/exam-realization.component';
import { StudentComponent } from './pages/student/student.component';
import { ThesisComponent } from './pages/thesis/thesis.component';
import { TeacherCoursesComponent } from './pages/courses/courses.component';
import { TeacherMyCoursesComponent } from './pages/my-courses/my-courses.component';
import { TeacherCoursePlayerComponent } from './pages/course-player/course-player.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  declarations: [
    TeacherServicesComponent,
    SubjectComponent,
    SubjectEnrollmentComponent,
    SubjectMaterialComponent,
    SubjectNotificationComponent,
    SubjectTermComponent,
    ExamComponent,
    ExamRealizationComponent,
    StudentComponent,
    ThesisComponent,
    TeacherCoursesComponent,
    TeacherMyCoursesComponent,
    TeacherCoursePlayerComponent,
  ],
  imports: [SharedModule, TeacherRoutingModule, FormsModule],
})
export class TeacherModule {}
