import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private authService: AuthService, private router: Router) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    let authReq = req;
    if (token) {
        authReq = req.clone({
        setHeaders: {
            Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(authReq).pipe(
        catchError((error: HttpErrorResponse) => {
            if (error.status === 401) {
            // Supprimer le token
            this.authService.logout();

            // Message à l'utilisateur
            alert('Session expirée. Veuillez vous reconnecter.');

            // Redirection vers le menu principal
            this.router.navigate(['/']);
        }

        return throwError(() => error);
        }));
    }
}