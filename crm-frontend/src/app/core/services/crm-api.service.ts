import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Lead, LeadRequest } from '../models/lead.model';
import { Client, ClientRequest } from '../models/client.model';
import { Payment } from '../models/payment.model';
import { CrmCourse } from '../models/course.model';

@Injectable({ providedIn: 'root' })
export class CrmApiService {
  private base = `${environment.apiUrl}/crm-service`;

  constructor(private http: HttpClient) {}

  // ── LEADS ──────────────────────────────────────────────
  getLeads(status?: string): Observable<Lead[]> {
    let params = new HttpParams();
    if (status) params = params.set('status', status);
    return this.http.get<Lead[]>(`${this.base}/leads`, { params });
  }

  createLead(req: LeadRequest): Observable<Lead> {
    return this.http.post<Lead>(`${this.base}/leads`, req);
  }

  updateLead(id: number, req: Partial<LeadRequest>): Observable<Lead> {
    return this.http.put<Lead>(`${this.base}/leads/${id}`, req);
  }

  updateLeadStatus(id: number, status: string): Observable<Lead> {
    return this.http.patch<Lead>(`${this.base}/leads/${id}/status`, { status });
  }

  deleteLead(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/leads/${id}`);
  }

  // ── CLIENTS ────────────────────────────────────────────
  getClients(): Observable<Client[]> {
    return this.http.get<Client[]>(`${this.base}/clients`);
  }

  getClient(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.base}/clients/${id}`);
  }

  createClient(req: ClientRequest): Observable<Client> {
    return this.http.post<Client>(`${this.base}/clients`, req);
  }

  // ── PAYMENTS ───────────────────────────────────────────
  getPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.base}/payments`);
  }

  getPaymentsByClient(clientId: number): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.base}/payments/client/${clientId}`);
  }

  // ── COURSES ────────────────────────────────────────────
  getCourses(): Observable<CrmCourse[]> {
    return this.http.get<CrmCourse[]>(`${this.base}/courses/available`);
  }

  // ── DASHBOARD STATS ────────────────────────────────────
  getDashboardStats(): Observable<DashboardStats> {
    return this.http.get<DashboardStats>(`${this.base}/dashboard/stats`);
  }
}

export interface DashboardStats {
  totalLeads: number;
  newLeadsToday: number;
  totalClients: number;
  newClientsThisMonth: number;
  totalRevenue: number;
  revenueThisMonth: number;
  conversionRate: number;
  totalCourses: number;
}
