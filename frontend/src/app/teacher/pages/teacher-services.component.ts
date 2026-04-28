import { Component, OnInit } from '@angular/core';
import { SidenavItem } from '@core/models/sidenav-item.model';

@Component({
  selector: 'app-teacher-services',
  templateUrl: './teacher-services.component.html',
  styleUrls: ['./teacher-services.component.scss'],
})
export class TeacherServicesComponent implements OnInit {
  items: (SidenavItem | '-')[] = [
    {
      text: 'Мои дисциплины',
      link: '/teacher-services/my-subjects',
    },
    {
      text: 'Материалы',
      link: '/teacher-services/subject-materials',
    },
    {
      text: 'Объявления',
      link: '/teacher-services/subject-notifications',
    },
    {
      text: 'Занятия',
      link: '/teacher-services/subject-terms',
    },
    {
      text: 'Учащиеся в дисциплине',
      link: '/teacher-services/subject-enrollments',
    },
    '-',
    {
      text: 'Аттестации',
      link: '/teacher-services/exams',
    },
    {
      text: 'Результаты',
      link: '/teacher-services/exam-realizations',
    },
    '-',
    {
      text: 'Учащиеся',
      link: '/teacher-services/students',
    },
    {
      text: 'Итоговые проекты',
      link: '/teacher-services/theses',
    },
  ];

  constructor() {}

  ngOnInit(): void {}
}
