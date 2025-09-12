export interface Theme {
    idTheme: number;
    name: string;
    description: string;
}
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
@Injectable({
    providedIn: 'root'
})
export class ThemeService {
    private apiUrl = '/api/theme';
    private apiUrlSub = '/api/theme/subscribe/';
    constructor(private http: HttpClient) {}
    getAllThemes(): Observable<Theme[]> {
        return this.http.get<{themes:Theme[]}>(this.apiUrl).pipe(
            map(response => response.themes)
        );
    }
    subscribeOrUnsubscribe(themeId: number) {
        return this.http.put(this.apiUrlSub + themeId, {});
    }
}