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

@Injectable({
    providedIn: 'root'
})
export class ArticleService {
    private apiUrl = '/api/article';
    constructor(private http: HttpClient) {}

getAllArticles(): Observable<Article[]> {
    return this.http.get<{articles: Article[];}>(this.apiUrl).pipe(
        map(response => response.articles)
    );
}
getArticleById(id:number): Observable<Article> {
      return this.http.get<{ article: Article }>(`${this.apiUrl}/${id}`).pipe(
    map(response => response.article));
}
createArticle(id_theme:number,title: string, content: string) {
    return this.http.put(this.apiUrl, {
        id_theme: id_theme,
        title: title,
        contenu: content
    });
}
}