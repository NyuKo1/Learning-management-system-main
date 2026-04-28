import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CourseCatalog } from '@core/models/course-catalog.model';
import { AuthService } from '@core/services/auth.service';
import { CrmService } from '@core/services/crm.service';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-courses-public',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.scss'],
})
export class CoursesPublicComponent implements OnInit {
  courses: CourseCatalog[] = [];
  loading = true;
  error = false;

  searchQuery = '';
  selectedCategory = 'Все';
  selectedLevel = 'Все';

  categories: string[] = ['Все'];
  levels = ['Все', 'BEGINNER', 'INTERMEDIATE', 'ADVANCED'];

  constructor(
    private crmService: CrmService,
    public authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    forkJoin({
      courses: this.crmService.getAvailableCourses().pipe(catchError(() => of([]))),
      categories: this.crmService.getCategories().pipe(catchError(() => of([]))),
    }).subscribe(({ courses, categories }) => {
      this.courses = courses;
      this.categories = ['Все', ...categories];
      this.loading = false;
      if (!courses.length && !categories.length) this.error = true;
    });
  }

  get filteredCourses(): CourseCatalog[] {
    return this.courses.filter(c => {
      const q = this.searchQuery.toLowerCase();
      const matchSearch =
        !q ||
        c.title.toLowerCase().includes(q) ||
        c.description.toLowerCase().includes(q) ||
        c.instructor.toLowerCase().includes(q) ||
        (c.tags || '').toLowerCase().includes(q);
      const matchCat = this.selectedCategory === 'Все' || c.category === this.selectedCategory;
      const matchLvl = this.selectedLevel === 'Все' || c.level === this.selectedLevel;
      return matchSearch && matchCat && matchLvl;
    });
  }

  getTags(tags: string): string[] {
    return this.crmService.getTagsArray(tags);
  }

  getLevelLabel(level: string): string {
    const map: Record<string, string> = {
      'Все': 'Все уровни',
      BEGINNER: 'Начинающий',
      INTERMEDIATE: 'Средний',
      ADVANCED: 'Продвинутый',
    };
    return map[level] || level;
  }

  getLevelColor(level: string): string {
    const map: Record<string, string> = {
      BEGINNER: '#10b981',
      INTERMEDIATE: '#f59e0b',
      ADVANCED: '#ef4444',
    };
    return map[level] || '#6b7280';
  }

  formatStudents(count: number): string {
    return count >= 1000 ? (count / 1000).toFixed(1) + 'k' : count.toString();
  }

  formatPrice(price: number): string {
    return '₸' + Number(price).toFixed(0);
  }

  getDiscount(price: number, original: number): number {
    return Math.round(((original - price) / original) * 100);
  }

  renderStars(rating: number): string[] {
    return Array.from({ length: 5 }, (_, i) => {
      const pos = i + 1;
      if (rating >= pos) return 'star';
      if (rating >= pos - 0.5) return 'star_half';
      return 'star_border';
    });
  }

  buyCourse(course: CourseCatalog, event: Event): void {
    event.stopPropagation();
    if (!this.authService.loggedIn()) {
      this.authService.redirectUrl = `/payment?courseId=${course.id}&price=${course.price}`;
      this.router.navigate(['/auth/login']);
      return;
    }
    this.router.navigate(['/payment'], {
      queryParams: { courseId: course.id, title: course.title, price: course.price },
    });
  }

  reset(): void {
    this.searchQuery = '';
    this.selectedCategory = 'Все';
    this.selectedLevel = 'Все';
  }
}
