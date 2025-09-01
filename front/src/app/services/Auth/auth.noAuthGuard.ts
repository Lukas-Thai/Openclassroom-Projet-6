import { Injectable } from "@angular/core";
import { CanActivate, Router } from "@angular/router";
import { AuthService } from "./auth.service";
import { catchError, map, Observable, of } from "rxjs";

@Injectable({ providedIn: 'root' })
export class NoAuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): Observable<boolean> {
    const token = this.authService.getToken();

    if (!token) return of(true);

    return this.authService.checkSession().pipe(
      map(isValid => {
        if (isValid) {
          this.router.navigate(['/accueil']); // ⚡ redirige un loggué
          return false;
        }
        this.authService.logout();
        return true;
      }),
      catchError(() => {
        this.authService.logout();
        return of(true);
      })
    );
  }
}