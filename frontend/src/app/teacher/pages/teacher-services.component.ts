import { Component, OnInit } from '@angular/core';
import { SidenavItem } from '@core/models/sidenav-item.model';

@Component({
  selector: 'app-teacher-services',
  templateUrl: './teacher-services.component.html',
  styleUrls: ['./teacher-services.component.scss'],
})
export class TeacherServicesComponent implements OnInit {
  items: (SidenavItem | '-')[] = [
    { text: 'Course Catalog', link: '/teacher-services/courses' },
    { text: 'My Courses', link: '/teacher-services/my-courses' },
    '-',
    { text: 'My Subjects', link: '/teacher-services/my-subjects' },
    { text: 'Materials', link: '/teacher-services/subject-materials' },
    { text: 'Announcements', link: '/teacher-services/subject-notifications' },
    { text: 'Classes', link: '/teacher-services/subject-terms' },
    { text: 'Subject Enrollments', link: '/teacher-services/subject-enrollments' },
    '-',
    { text: 'Exams', link: '/teacher-services/exams' },
    { text: 'Exam Results', link: '/teacher-services/exam-realizations' },
    '-',
    { text: 'Students', link: '/teacher-services/students' },
    { text: 'Theses', link: '/teacher-services/theses' },
  ];

  constructor() {}

  ngOnInit(): void {}
}
