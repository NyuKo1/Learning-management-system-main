import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CourseCatalog } from '@core/models/course-catalog.model';
import { CourseLesson } from '@core/models/course-lesson.model';
import { PaymentRequest, PaymentResponse } from '@core/models/payment.model';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class CrmService {
  private coursesUrl = `${environment.baseUrl}/crm-service/courses`;
  private paymentsUrl = `${environment.baseUrl}/crm-service/payments`;
  private lessonsUrl = `${environment.baseUrl}/crm-service/lessons`;

  constructor(private http: HttpClient) {}

  getAvailableCourses(): Observable<CourseCatalog[]> {
    return this.http.get<CourseCatalog[]>(`${this.coursesUrl}/available`).pipe(
      map(courses => courses.map(c => ({ ...c, tags: c.tags || '' })))
    );
  }

  getCourseById(id: number): Observable<CourseCatalog[]> {
    return this.http.get<CourseCatalog[]>(`${this.coursesUrl}/${id}`);
  }

  createPayment(payment: PaymentRequest): Observable<PaymentResponse> {
    return this.http.post<PaymentResponse>(this.paymentsUrl, payment);
  }

  getMyPurchases(userId: string): Observable<PaymentResponse[]> {
    return this.http.get<PaymentResponse[]>(`${this.paymentsUrl}/user/${userId}`);
  }

  checkPurchased(courseId: number, userId: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.paymentsUrl}/check`, {
      params: { courseId: courseId.toString(), userId },
    });
  }

  getLessons(courseId: number): Observable<CourseLesson[]> {
    return this.http.get<CourseLesson[]>(`${this.lessonsUrl}/course/${courseId}`);
  }

  createLesson(lesson: CourseLesson): Observable<CourseLesson> {
    return this.http.post<CourseLesson>(this.lessonsUrl, lesson);
  }

  updateLesson(id: number, lesson: CourseLesson): Observable<CourseLesson> {
    return this.http.put<CourseLesson>(`${this.lessonsUrl}/${id}`, lesson);
  }

  deleteLesson(id: number): Observable<void> {
    return this.http.delete<void>(`${this.lessonsUrl}/${id}`);
  }

  getCourseDetail(courseId: number): Observable<CourseCatalog> {
    return this.http.get<CourseCatalog>(`${this.coursesUrl}/${courseId}`);
  }

  getCategories(): Observable<string[]> {
    return this.http.get<string[]>(`${this.coursesUrl}/categories`);
  }

  // Admin CRUD — uses /all endpoint that returns List<CourseDTO>
  getAllCourses(): Observable<CourseCatalog[]> {
    return this.http.get<CourseCatalog[]>(`${this.coursesUrl}/all`);
  }

  createCourse(course: Partial<CourseCatalog>): Observable<CourseCatalog> {
    return this.http.post<CourseCatalog>(this.coursesUrl, course);
  }

  updateCourse(id: number, course: Partial<CourseCatalog>): Observable<CourseCatalog> {
    return this.http.put<CourseCatalog>(`${this.coursesUrl}/${id}`, course);
  }

  deleteCourse(id: number): Observable<void> {
    return this.http.delete<void>(`${this.coursesUrl}/${id}`);
  }

  syncFromSubjects(): Observable<number> {
    return this.http.post<number>(`${this.coursesUrl}/sync-from-subjects`, {});
  }

  createCourseFromSubject(subject: { id?: number; name: string; syllabus?: string; ects?: number }): Observable<CourseCatalog> {
    const colors = ['#3b82f6', '#8b5cf6', '#10b981', '#6366f1', '#0ea5e9', '#f59e0b', '#ec4899'];
    const icons  = ['book', 'school', 'science', 'functions', 'code', 'bar_chart', 'palette'];
    const idx = subject.id ? subject.id % colors.length : 0;
    const course: Partial<CourseCatalog> = {
      title: subject.name,
      description: subject.syllabus || subject.name,
      instructor: 'Faculty Staff',
      category: 'Academic',
      level: 'INTERMEDIATE',
      duration: subject.ects ? `${subject.ects * 10}h` : '30h',
      lessons: subject.ects ? subject.ects * 5 : 20,
      price: 49,
      originalPrice: 89,
      rating: 0,
      studentsCount: 0,
      tags: subject.name,
      color: colors[idx],
      icon: icons[idx],
      available: true,
      subjectId: subject.id,
    };
    return this.http.post<CourseCatalog>(this.coursesUrl, course);
  }

  createCourseFromStudyProgram(program: { id?: number; name: string; description?: string }): Observable<CourseCatalog> {
    const course: Partial<CourseCatalog> = {
      title: program.name,
      description: program.description || `Academic study program: ${program.name}`,
      instructor: 'Faculty Staff',
      category: 'Academic Program',
      level: 'BEGINNER',
      duration: '1 year',
      lessons: 0,
      price: 0,
      rating: 0,
      studentsCount: 0,
      tags: program.name,
      color: '#6366f1',
      icon: 'school',
      available: false,
      studyProgramId: program.id,
    };
    return this.http.post<CourseCatalog>(this.coursesUrl, course);
  }

  getTagsArray(tags: string): string[] {
    return tags ? tags.split(',').map(t => t.trim()).filter(t => t.length > 0) : [];
  }
}
