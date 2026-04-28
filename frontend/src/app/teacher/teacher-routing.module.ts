import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TeacherServicesComponent } from './pages/teacher-services.component';
import { SubjectComponent } from './pages/subject/subject.component';
import { SubjectMaterialComponent } from './pages/subject-material/subject-material.component';
import { SubjectNotificationComponent } from './pages/subject-notification/subject-notification.component';
import { SubjectTermComponent } from './pages/subject-term/subject-term.component';
import { ExamComponent } from './pages/exam/exam.component';
import { ExamRealizationComponent } from './pages/exam-realization/exam-realization.component';
import { StudentComponent } from './pages/student/student.component';
import { ThesisComponent } from './pages/thesis/thesis.component';
import { SubjectEnrollmentComponent } from './pages/subject-enrollment/subject-enrollment.component';
import { TeacherCoursesComponent } from './pages/courses/courses.component';
import { TeacherMyCoursesComponent } from './pages/my-courses/my-courses.component';
import { TeacherCoursePlayerComponent } from './pages/course-player/course-player.component';

const routes: Routes = [
  {
    path: '',
    component: TeacherServicesComponent,
    children: [
      {
        path: '',
        redirectTo: '/teacher-services/my-subjects',
        pathMatch: 'full',
      },
      {
        path: 'courses',
        component: TeacherCoursesComponent,
      },
      {
        path: 'my-courses',
        component: TeacherMyCoursesComponent,
      },
      {
        path: 'course/:id',
        component: TeacherCoursePlayerComponent,
      },
      {
        path: 'my-subjects',
        component: SubjectComponent,
      },
      {
        path: 'subject-materials',
        component: SubjectMaterialComponent,
      },
      {
        path: 'subject-notifications',
        component: SubjectNotificationComponent,
      },
      {
        path: 'subject-terms',
        component: SubjectTermComponent,
      },
      {
        path: 'subject-enrollments',
        component: SubjectEnrollmentComponent,
      },
      {
        path: 'exams',
        component: ExamComponent,
      },
      {
        path: 'exam-realizations',
        component: ExamRealizationComponent,
      },
      {
        path: 'students',
        component: StudentComponent,
      },
      {
        path: 'theses',
        component: ThesisComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class TeacherRoutingModule {}
