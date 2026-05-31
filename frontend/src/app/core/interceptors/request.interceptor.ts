import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '@core/services/auth.service';

@Injectable()
export class RequestInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (request.url.includes('/auth-service/refresh')) {
      return next.handle(request);
    }

    if (!this.isOwnApi(request.url) || !this.authService.accessToken) {
      return next.handle(request);
    }

    const newRequest = request.clone({
      headers: request.headers.set(
        'Authorization',
        `Bearer ${this.authService.accessToken}`
      ),
    });

    return next.handle(newRequest);
  }

  private isOwnApi(url: string): boolean {
    // Relative URLs always count as same-origin.
    if (url.startsWith('/')) return true;
    try {
      const origin = new URL(url).origin;
      return (
        origin === window.location.origin ||
        origin.includes('localhost:808') ||
        origin.includes('localhost:4200') ||
        origin.includes('localhost:4201')
      );
    } catch {
      return false;
    }
  }
}
