import { Component, OnInit } from '@angular/core';
import { SidenavItem } from '@core/models/sidenav-item.model';

@Component({
  selector: 'app-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss'],
})
export class AdminPanelComponent implements OnInit {
  items: (SidenavItem | '-')[] = [
    {
      text: 'Направления',
      link: '/admin-panel/faculties',
    },
    {
      text: 'Программы',
      link: '/admin-panel/study-programs',
    },
    {
      text: 'Дисциплины',
      link: '/admin-panel/subjects',
    },
    {
      text: 'Итоговые проекты',
      link: '/admin-panel/theses',
    },
    '-',
    {
      text: 'Учащиеся',
      link: '/admin-panel/students',
    },
    {
      text: 'Преподаватели',
      link: '/admin-panel/teachers',
    },
    {
      text: 'Администраторы',
      link: '/admin-panel/administrators',
    },
    '-',
    {
      text: 'Периоды аттестации',
      link: '/admin-panel/exam-periods',
    },
    {
      text: 'Расписание аттестаций',
      link: '/admin-panel/exam-terms',
    },
    {
      text: 'Типы аттестаций',
      link: '/admin-panel/exam-types',
    },
    '-',
    {
      text: 'Страны',
      link: '/admin-panel/countries',
    },
    {
      text: 'Города',
      link: '/admin-panel/cities',
    },
    {
      text: 'Адреса',
      link: '/admin-panel/addresses',
    },
  ];

  constructor() {}

  ngOnInit(): void {}
}
