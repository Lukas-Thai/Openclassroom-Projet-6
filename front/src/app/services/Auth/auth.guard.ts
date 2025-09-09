import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ToastService } from '../SnackBar/snackbar.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router,private snackBar: ToastService) {}

    canActivate(): Observable<boolean> {
        const token = this.authService.getToken();
        if (!token) {
            this.snackBar.show("Vous devez d'abord vous connecter.",5000,'top');
            this.router.navigate(['/']);
            return of(false);
        }
        // Vérifie sur l'API si le token est valide
        return this.authService.checkSession().pipe(
            map((isValid) => {
            if (!isValid) {
                this.snackBar.show('Session expirée. Veuillez vous reconnecter.',5000,'top');
                this.authService.logout();
                this.router.navigate(['/']);
                return false;
            }
            return true;
            }),
            catchError(() => {
            this.snackBar.show('Session expirée. Veuillez vous reconnecter.',5000,'top');
            this.authService.logout();
            this.router.navigate(['/']);
            return of(false);
            })
        );
    }
}