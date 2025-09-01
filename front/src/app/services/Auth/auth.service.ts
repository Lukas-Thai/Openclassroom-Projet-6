import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
    private registerUrl = '/api/auth/register';
    private loginUrl = '/api/auth/login';
    constructor(private http: HttpClient) {}

    register(data: any) {
        return this.http.post<{ token: string }>(this.registerUrl, data).pipe(
            tap(response => {
                if (response.token) {
                    localStorage.setItem('authToken', response.token);
                }
            })
        );
    }
    login(data:any){
        return this.http.post<{ token: string }>(this.loginUrl, data).pipe(
            tap(response => {
                if (response.token) {
                    localStorage.setItem('authToken', response.token);
                }
            })
        );
    }
    logout() {
        localStorage.removeItem('authToken');
    }

    getToken(): string | null {
        return localStorage.getItem('authToken');
    }
    
    checkSession(): Observable<boolean> {
  return this.http.get<{ valid: boolean }>('/api/auth/check').pipe(
    map(response => response.valid)
  );
}
}