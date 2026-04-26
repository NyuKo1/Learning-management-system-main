import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '@core/guards/auth.guard';

import { HomeComponent } from './pages/home/home.component';
import { AboutComponent } from './pages/about/about.component';
import { FacultiesComponent } from './pages/faculties/faculties.component';
import { FacultyComponent } from './pages/faculty/faculty.component';
import { StudyProgramComponent } from './pages/study-program/study-program.component';
import { SubjectComponent } from './pages/subject/subject.component';
import { CoursesPublicComponent } from './pages/courses/courses.component';
import { PaymentComponent } from './pages/payment/payment.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'about',
    component: AboutComponent,
  },
  {
    path: 'faculties',
    component: FacultiesComponent,
  },
  {
    path: 'faculties/:id',
    component: FacultyComponent,
  },
  {
    path: 'study-programs/:id',
    component: StudyProgramComponent,
  },
  {
    path: 'subjects/:id',
    component: SubjectComponent,
  },
  {
    path: 'courses',
    component: CoursesPublicComponent,
  },
  {
    path: 'payment',
    component: PaymentComponent,
    canActivate: [AuthGuard],
    data: {
      allowedRoles: ['ROLE_STUDENT', 'ROLE_TEACHER', 'ROLE_ADMIN'],
      validationMethod: 'any',
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class HomeRoutingModule {}
