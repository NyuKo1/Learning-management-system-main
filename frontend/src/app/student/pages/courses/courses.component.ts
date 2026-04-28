import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CourseCatalog } from '@core/models/course-catalog.model';
import { AuthService } from '@core/services/auth.service';
import { CrmService } from '@core/services/crm.service';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.scss'],
})
export class CoursesComponent implements OnInit {
  allCourses: CourseCatalog[] = [];
  purchasedCourseIds = new Set<number>();
  loading = true;
  error = false;

  searchQuery = '';
  selectedCategory = 'All';
  selectedLevel = 'All';

  purchaseInProgress: number | null = null;
  purchaseSuccess: number | null = null;

  categories: string[] = ['All'];
  levels = ['All', 'BEGINNER', 'INTERMEDIATE', 'ADVANCED'];

  constructor(
    private crmService: CrmService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadCourses();
  }

  private loadCourses(): void {
    forkJoin({
      courses: this.crmService.getAvailableCourses().pipe(catchError(() => of([]))),
      categories: this.crmService.getCategories().pipe(catchError(() => of([]))),
    }).subscribe(({ courses, categories }) => {
      this.allCourses = courses;
      this.categories = ['All', ...categories];
      this.loading = false;
      if (this.authService.loggedIn()) this.checkPurchased();
    });
  }

  private checkPurchased(): void {
    const username = this.authService.getUsername();
    this.crmService.getMyPurchases(username).subscribe({
      next: (payments) => {
        this.purchasedCourseIds = new Set(payments.map(p => p.courseId));
      },
    });
  }

  isPurchased(courseId: number): boolean {
    return this.purchasedCourseIds.has(courseId);
  }

  get filteredCourses(): CourseCatalog[] {
    return this.allCourses.filter(c => {
      const q = this.searchQuery.toLowerCase();
      const matchSearch =
        !q ||
        c.title.toLowerCase().includes(q) ||
        c.description.toLowerCase().includes(q) ||
        (c.tags || '').toLowerCase().includes(q);
      const matchCat = this.selectedCategory === 'All' || c.category === this.selectedCategory;
      const matchLvl = this.selectedLevel === 'All' || c.level === this.selectedLevel;
      return matchSearch && matchCat && matchLvl;
    });
  }

  get enrolledCount(): number {
    return this.allCourses.filter(c => this.isPurchased(c.id!)).length;
  }

  selectCategory(cat: string): void { this.selectedCategory = cat; }
  selectLevel(level: string): void { this.selectedLevel = level; }

  getLevelLabel(level: string): string {
    const map: Record<string, string> = {
      All: 'All Levels',
      BEGINNER: 'Beginner',
      INTERMEDIATE: 'Intermediate',
      ADVANCED: 'Advanced',
    };
    return map[level] || level;
  }

  getLevelColor(level: string): string {
    const map: Record<string, string> = { BEGINNER: '#10b981', INTERMEDIATE: '#f59e0b', ADVANCED: '#ef4444' };
    return map[level] || '#6b7280';
  }

  getTags(tags: string): string[] {
    return this.crmService.getTagsArray(tags);
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

  purchaseCourse(course: CourseCatalog, event: Event): void {
    event.stopPropagation();
    if (this.isPurchased(course.id!) || this.purchaseInProgress !== null) return;
    this.router.navigate(['/payment'], {
      queryParams: { courseId: course.id, title: course.title, price: course.price },
    });
  }
}
