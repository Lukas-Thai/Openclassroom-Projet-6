import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from './auth.service';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router,private snackBar: MatSnackBar) {}

    canActivate(): Observable<boolean> {
        const token = this.authService.getToken();
        if (!token) {
            this.showToast("Vous devez d'abord vous connecter.");
            this.router.navigate(['/']);
            return of(false);
        }
        // Vérifie sur l'API si le token est valide
        return this.authService.checkSession().pipe(
            map((isValid) => {
            if (!isValid) {
                this.showToast('Session expirée. Veuillez vous reconnecter.');
                this.authService.logout();
                this.router.navigate(['/']);
                return false;
            }
            return true;
            }),
            catchError(() => {
            this.showToast('Session expirée. Veuillez vous reconnecter.');
            this.authService.logout();
            this.router.navigate(['/']);
            return of(false);
            })
        );
    }

    private showToast(message: string) {
        this.snackBar.open(message, 'Fermer', {
            duration: 10000,
            horizontalPosition: 'center',
            verticalPosition: 'top'
        });
    }
}