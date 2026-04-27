import { Component, OnInit } from '@angular/core';
import { CrmApiService } from '../../../core/services/crm-api.service';
import { CrmCourse } from '../../../core/models/course.model';

@Component({
  selector: 'crm-courses-list',
  templateUrl: './courses-list.component.html',
  styleUrls: ['./courses-list.component.scss']
})
export class CoursesListComponent implements OnInit {
  courses: CrmCourse[] = [];
  loading = true;

  constructor(private api: CrmApiService) {}

  ngOnInit(): void {
    this.api.getCourses().subscribe({
      next: data => { this.courses = data; this.loading = false; },
      error: () => { this.courses = this.mock(); this.loading = false; }
    });
  }

  private mock(): CrmCourse[] {
    return [
      { id: 1, title: 'Python для начинающих', description: 'Основы программирования на Python', price: 15000, currency: '₸', duration: 60, category: 'Программирование', enrolledCount: 47, isAvailable: true, createdAt: '2024-01-15' },
      { id: 2, title: 'Веб-разработка (Angular)', description: 'Создание современных веб-приложений', price: 25000, currency: '₸', duration: 90, category: 'Программирование', enrolledCount: 32, isAvailable: true, createdAt: '2024-02-01' },
      { id: 3, title: 'UI/UX Design', description: 'Дизайн интерфейсов в Figma', price: 20000, currency: '₸', duration: 45, category: 'Дизайн', enrolledCount: 28, isAvailable: true, createdAt: '2024-02-15' },
      { id: 4, title: 'Data Science', description: 'Анализ данных и машинное обучение', price: 35000, currency: '₸', duration: 120, category: 'Аналитика', enrolledCount: 19, isAvailable: false, createdAt: '2024-03-01' }
    ];
  }
}
