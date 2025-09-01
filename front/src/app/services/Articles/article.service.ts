import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, Observable } from 'rxjs';

export interface Article {
    id_article: number;
    nom_theme:string;
    author:string;
    title: string;
    content: string;
    created_at: Date;
}
interface ApiResponse {
    articles: Article[];
}
@Injectable({
    providedIn: 'root'
})
export class ArticleService {
    private apiUrl = '/api/article';
    constructor(private http: HttpClient) {}

getAllArticles(): Observable<Article[]> {
    return this.http.get<ApiResponse>(this.apiUrl).pipe(
        map(response => response.articles)
    );
}
}