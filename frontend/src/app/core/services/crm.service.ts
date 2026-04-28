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

  getCourseDetail(courseId: number): Observable<CourseCatalog> {
    return this.http.get<CourseCatalog>(`${this.coursesUrl}/${courseId}`);
  }

  getCategories(): Observable<string[]> {
    return this.http.get<string[]>(`${this.coursesUrl}/categories`);
  }

  getTagsArray(tags: string): string[] {
    return tags ? tags.split(',').map(t => t.trim()).filter(t => t.length > 0) : [];
  }
}
