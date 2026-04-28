import { Component, OnInit } from '@angular/core';
import { SidenavItem } from '@core/models/sidenav-item.model';

@Component({
  selector: 'app-student-services',
  templateUrl: './student-services.component.html',
  styleUrls: ['./student-services.component.scss'],
})
export class StudentServicesComponent implements OnInit {
  items: (SidenavItem | '-')[] = [
    {
      text: '🛒 Каталог курсов',
      link: '/student-services/courses',
    },
    {
      text: '📚 Мои курсы',
      link: '/student-services/my-courses',
    },
    '-',
    {
      text: 'Мои дисциплины',
      link: '/student-services/my-subjects',
    },
    {
      text: 'Мои записи',
      link: '/student-services/subject-enrollments',
    },
    '-',
    {
      text: 'Запись на аттестацию',
      link: '/student-services/exam-terms-registration',
    },
    {
      text: 'Результаты аттестаций',
      link: '/student-services/exam-realizations',
    },
    '-',
    {
      text: 'Мой профиль',
      link: '/student-services/my-info',
    },
    {
      text: 'Итоговый проект',
      link: '/student-services/my-thesis',
    },
  ];

  constructor() {}

  ngOnInit(): void {}
}
