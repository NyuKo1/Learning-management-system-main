import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
} from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, filter, switchMap, take, finalize } from 'rxjs/operators';
import { AuthService } from '../services/auth.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  private refreshing = false;
  private refreshDone$ = new BehaviorSubject<string | null>(null);

  constructor(private auth: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Skip login/refresh endpoints to avoid infinite loops
    if (req.url.includes('/auth-service/login') || req.url.includes('/auth-service/refresh')) {
      return next.handle(req);
    }

    const cloned = this.addToken(req, this.auth.getToken());

    return next.handle(cloned).pipe(
      catchError((err: HttpErrorResponse) => {
        if (err.status !== 401) return throwError(() => err);

        if (this.refreshing) {
          return this.refreshDone$.pipe(
            filter((token) => token !== null),
            take(1),
            switchMap((token) => next.handle(this.addToken(req, token!)))
          );
        }

        this.refreshing = true;
        this.refreshDone$.next(null);

        return this.auth.refreshTokens().pipe(
          switchMap((tokens) => {
            this.refreshDone$.next(tokens.accessToken);
            return next.handle(this.addToken(req, tokens.accessToken));
          }),
          catchError((refreshErr) => {
            this.auth.logout();
            return throwError(() => refreshErr);
          }),
          finalize(() => {
            this.refreshing = false;
          })
        );
      })
    );
  }

  private addToken(req: HttpRequest<any>, token: string | null): HttpRequest<any> {
    if (!token) return req;
    return req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
}
