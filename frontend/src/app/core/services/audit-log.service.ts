import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'environments/environment';
import { AuditEvent, AuditFilters, AuditPage } from '@core/models/audit-event.model';

@Injectable({ providedIn: 'root' })
export class AuditLogService {
  private base = `${environment.baseUrl}/audit-service/audit/events`;

  constructor(private http: HttpClient) {}

  search(filters: AuditFilters, page = 0, size = 50): Observable<AuditPage> {
    let params = new HttpParams().set('page', page).set('size', size);
    Object.entries(filters).forEach(([k, v]) => {
      if (v !== undefined && v !== null && v !== '') {
        params = params.set(k, String(v));
      }
    });
    return this.http.get<AuditPage>(this.base, { params });
  }

  findOne(id: number): Observable<AuditEvent> {
    return this.http.get<AuditEvent>(`${this.base}/${id}`);
  }
}
