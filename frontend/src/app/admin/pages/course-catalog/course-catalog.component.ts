import { Component, OnInit } from '@angular/core';
import { CourseCatalog } from '@core/models/course-catalog.model';
import { CrmService } from '@core/services/crm.service';

@Component({
  selector: 'app-admin-course-catalog',
  templateUrl: './course-catalog.component.html',
  styleUrls: ['./course-catalog.component.scss'],
})
export class CourseCatalogComponent implements OnInit {
  courses: CourseCatalog[] = [];
  loading = true;
  error = false;
  syncing = false;

  editing: CourseCatalog | null = null;
  saving = false;
  saveError = '';

  readonly COLOR_PALETTE = [
    '#3b82f6', '#8b5cf6', '#10b981', '#6366f1',
    '#0ea5e9', '#f59e0b', '#ec4899', '#ef4444',
    '#14b8a6', '#f97316', '#64748b', '#a855f7',
  ];

  readonly ICON_OPTIONS = [
    'book', 'school', 'science', 'functions', 'code',
    'bar_chart', 'palette', 'computer', 'psychology', 'biotech',
    'calculate', 'language', 'architecture', 'music_note', 'fitness_center',
    'business', 'account_balance', 'rocket_launch', 'design_services', 'data_object',
  ];

  constructor(private crmService: CrmService) {}

  ngOnInit(): void {
    this.loadCourses();
  }

  loadCourses(): void {
    this.loading = true;
    this.error = false;
    this.crmService.getAllCourses().subscribe({
      next: (data) => { this.courses = data; this.loading = false; },
      error: () => { this.error = true; this.loading = false; },
    });
  }

  startEdit(course: CourseCatalog): void {
    this.editing = { ...course };
    this.saveError = '';
  }

  cancelEdit(): void {
    this.editing = null;
    this.saveError = '';
  }

  saveEdit(): void {
    if (!this.editing?.id) return;
    this.saving = true;
    this.crmService.updateCourse(this.editing.id, this.editing).subscribe({
      next: (updated) => {
        const idx = this.courses.findIndex(c => c.id === updated.id);
        if (idx >= 0) this.courses[idx] = updated;
        this.editing = null;
        this.saving = false;
      },
      error: (err) => {
        this.saveError = err?.error?.message || 'Error saving course';
        this.saving = false;
      },
    });
  }

  syncFromLms(): void {
    this.syncing = true;
    this.crmService.syncFromSubjects().subscribe({
      next: (count) => {
        this.syncing = false;
        if (count > 0) {
          this.loadCourses();
          window.alert(`Synced ${count} new course(s) from LMS subjects.`);
        } else {
          window.alert('All subjects are already synced — no new courses created.');
        }
      },
      error: () => {
        this.syncing = false;
        window.alert('Sync failed. Check that the LMS subject service is running.');
      },
    });
  }

  deleteCourse(id: number): void {
    if (!confirm('Delete this course?')) return;
    this.crmService.deleteCourse(id).subscribe({
      next: () => this.courses = this.courses.filter(c => c.id !== id),
      error: () => window.alert('Failed to delete course'),
    });
  }

  toggleAvailable(course: CourseCatalog): void {
    this.crmService.updateCourse(course.id!, { ...course, available: !course.available }).subscribe({
      next: (updated) => {
        const idx = this.courses.findIndex(c => c.id === updated.id);
        if (idx >= 0) this.courses[idx] = updated;
      },
    });
  }

  getLevelLabel(level: string): string {
    const map: Record<string, string> = {
      BEGINNER: 'Beginner',
      INTERMEDIATE: 'Intermediate',
      ADVANCED: 'Advanced',
    };
    return map[level] || level;
  }
}
