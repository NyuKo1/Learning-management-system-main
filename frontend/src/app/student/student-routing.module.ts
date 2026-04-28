import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StudentServicesComponent } from './pages/student-services.component';
import { SubjectComponent } from './pages/subject/subject.component';
import { SubjectEnrollmentComponent } from './pages/subject-enrollment/subject-enrollment.component';
import { ExamTermComponent } from './pages/exam-term/exam-term.component';
import { ExamRealizationComponent } from './pages/exam-realization/exam-realization.component';
import { InfoComponent } from './pages/info/info.component';
import { ThesisComponent } from './pages/thesis/thesis.component';
import { CoursesComponent } from './pages/courses/courses.component';
import { MyCoursesComponent } from './pages/my-courses/my-courses.component';
import { CoursePlayerComponent } from './pages/course-player/course-player.component';

const routes: Routes = [
  {
    path: '',
    component: StudentServicesComponent,
    children: [
      {
        path: '',
        redirectTo: '/student-services/my-subjects',
        pathMatch: 'full',
      },
      {
        path: 'my-subjects',
        component: SubjectComponent,
      },
      {
        path: 'subject-enrollments',
        component: SubjectEnrollmentComponent,
      },
      {
        path: 'exam-terms-registration',
        component: ExamTermComponent,
      },
      {
        path: 'exam-realizations',
        component: ExamRealizationComponent,
      },
      {
        path: 'my-info',
        component: InfoComponent,
      },
      {
        path: 'my-thesis',
        component: ThesisComponent,
      },
      {
        path: 'courses',
        component: CoursesComponent,
      },
      {
        path: 'my-courses',
        component: MyCoursesComponent,
      },
      {
        path: 'course/:id',
        component: CoursePlayerComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class StudentRoutingModule {}
