import { Component, OnInit } from '@angular/core';
import { SidenavItem } from '@core/models/sidenav-item.model';

@Component({
  selector: 'app-student-services',
  templateUrl: './student-services.component.html',
  styleUrls: ['./student-services.component.scss'],
})
export class StudentServicesComponent implements OnInit {
  items: (SidenavItem | '-')[] = [
    { text: 'Course Catalog', link: '/student-services/courses' },
    { text: 'My Courses', link: '/student-services/my-courses' },
    '-',
    { text: 'My Subjects', link: '/student-services/my-subjects' },
    { text: 'My Enrollments', link: '/student-services/subject-enrollments' },
    '-',
    { text: 'Register for Exam', link: '/student-services/exam-terms-registration' },
    { text: 'Exam Results', link: '/student-services/exam-realizations' },
    '-',
    { text: 'My Profile', link: '/student-services/my-info' },
    { text: 'My Thesis', link: '/student-services/my-thesis' },
  ];

  constructor() {}

  ngOnInit(): void {}
}
