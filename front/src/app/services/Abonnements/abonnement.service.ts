import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class AbonnementService {
    private apiUrl = '/api/abonnement';
    constructor(private http: HttpClient) {}

    getAbonnements(): Observable<number[]> {
        return this.http.get<{ subscriptions: number[] }>(this.apiUrl).pipe(
            map(response => response.subscriptions)
        );
    }
}