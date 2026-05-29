import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'environments/environment';

export interface TelegramSubscriptionStatus {
  active: boolean;
  telegramUsername?: string;
  createdAt?: string;
}

export interface LinkResult {
  linked: boolean;
  reason?: string;
}

@Injectable({ providedIn: 'root' })
export class NotifyService {
  private url = `${environment.baseUrl}/notify-service/notify`;

  constructor(private http: HttpClient) {}

  getSubscription(userId: number): Observable<TelegramSubscriptionStatus> {
    return this.http.get<TelegramSubscriptionStatus>(
      `${this.url}/subscription/${userId}`
    );
  }

  linkUser(userId: number, username: string): Observable<LinkResult> {
    return this.http.post<LinkResult>(`${this.url}/link`, {
      userId,
      username,
    });
  }
}
