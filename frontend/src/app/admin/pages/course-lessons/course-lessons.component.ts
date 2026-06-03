import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseCatalog } from '@core/models/course-catalog.model';
import { CourseLesson } from '@core/models/course-lesson.model';
import { CrmService } from '@core/services/crm.service';

@Component({
  selector: 'app-course-lessons',
  templateUrl: './course-lessons.component.html',
  styleUrls: ['./course-lessons.component.scss'],
})
export class CourseLessonsComponent implements OnInit {
  courses: CourseCatalog[] = [];
  selectedCourseId: number | null = null;
  lessons: CourseLesson[] = [];

  loadingCourses = true;
  loadingLessons = false;
  saving = false;
  errorMsg = '';

  editorOpen = false;
  editing: CourseLesson | null = null;
  draft: CourseLesson = this.emptyDraft();

  constructor(private crmService: CrmService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    const preselectId = Number(this.route.snapshot.queryParamMap.get('courseId')) || null;

    this.crmService.getAllCourses().subscribe({
      next: (data) => {
        this.courses = data;
        this.loadingCourses = false;
        const target = preselectId ? data.find((c) => c.id === preselectId) : null;
        if (target && target.id != null) {
          this.selectedCourseId = target.id;
          this.loadLessons();
        } else if (data.length > 0 && data[0].id != null) {
          this.selectedCourseId = data[0].id;
          this.loadLessons();
        }
      },
      error: () => {
        this.loadingCourses = false;
        this.errorMsg = 'Не удалось загрузить курсы.';
      },
    });
  }

  loadLessons(): void {
    if (!this.selectedCourseId) return;
    this.loadingLessons = true;
    this.errorMsg = '';
    this.crmService.getLessons(this.selectedCourseId).subscribe({
      next: (data) => {
        this.lessons = data.sort((a, b) => a.orderIndex - b.orderIndex);
        this.loadingLessons = false;
      },
      error: () => {
        this.lessons = [];
        this.loadingLessons = false;
        this.errorMsg = 'Не удалось загрузить уроки. Проверьте права доступа.';
      },
    });
  }

  onCourseChange(): void {
    this.cancel();
    this.loadLessons();
  }

  startCreate(): void {
    if (!this.selectedCourseId) return;
    this.editing = null;
    this.draft = {
      ...this.emptyDraft(),
      courseId: this.selectedCourseId,
      orderIndex: this.lessons.length + 1,
    };
    this.editorOpen = true;
    this.errorMsg = '';
  }

  startEdit(lesson: CourseLesson): void {
    this.editing = lesson;
    this.draft = { ...lesson };
    this.editorOpen = true;
    this.errorMsg = '';
  }

  cancel(): void {
    this.editing = null;
    this.draft = this.emptyDraft();
    this.editorOpen = false;
    this.errorMsg = '';
  }

  save(): void {
    if (!this.draft.title || !this.draft.courseId) {
      this.errorMsg = 'Заполните название и выберите курс.';
      return;
    }
    this.saving = true;
    this.errorMsg = '';
    const obs = this.editing && this.editing.id
      ? this.crmService.updateLesson(this.editing.id, this.draft)
      : this.crmService.createLesson(this.draft);

    obs.subscribe({
      next: () => {
        this.saving = false;
        this.cancel();
        this.loadLessons();
      },
      error: (err) => {
        this.saving = false;
        this.errorMsg = err?.error?.message || 'Ошибка сохранения урока.';
      },
    });
  }

  remove(lesson: CourseLesson): void {
    if (!lesson.id) return;
    if (!confirm(`Удалить урок "${lesson.title}"?`)) return;
    this.crmService.deleteLesson(lesson.id).subscribe({
      next: () => {
        if (this.editing?.id === lesson.id) this.cancel();
        this.loadLessons();
      },
      error: () => (this.errorMsg = 'Не удалось удалить урок.'),
    });
  }

  private emptyDraft(): CourseLesson {
    return {
      courseId: this.selectedCourseId ?? 0,
      title: '',
      description: '',
      videoUrl: '',
      content: '',
      orderIndex: 1,
      duration: '',
    };
  }
}
