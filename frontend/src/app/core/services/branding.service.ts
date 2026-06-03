import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { environment } from 'environments/environment';
import { Branding, DEFAULT_BRANDING } from '@core/models/branding.model';

@Injectable({ providedIn: 'root' })
export class BrandingService {
  private branding$ = new BehaviorSubject<Branding>(DEFAULT_BRANDING);

  constructor(private http: HttpClient) {}

  /** Called from APP_INITIALIZER. Failure falls back to defaults — never blocks startup. */
  load(): Observable<Branding> {
    const url = `${environment.baseUrl}/auth-service/branding`;
    return this.http.get<Branding>(url).pipe(
      tap((b) => {
        this.branding$.next(b);
        this.applyToDom(b);
      }),
      catchError(() => {
        this.applyToDom(DEFAULT_BRANDING);
        return of(DEFAULT_BRANDING);
      })
    );
  }

  get current$(): Observable<Branding> {
    return this.branding$.asObservable();
  }

  get snapshot(): Branding {
    return this.branding$.value;
  }

  private applyToDom(b: Branding): void {
    document.documentElement.style.setProperty('--brand-primary', b.primaryColor);
    document.title = b.productName;
    let favicon = document.querySelector<HTMLLinkElement>('link[rel="icon"]');
    if (!favicon) {
      favicon = document.createElement('link');
      favicon.rel = 'icon';
      document.head.appendChild(favicon);
    }
    favicon.href = b.faviconUrl;
  }
}
