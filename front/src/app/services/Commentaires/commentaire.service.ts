export interface Commentaire{
    id_commentaire: number;
    id_article: number;
    author: string;
    content: string;
    date: Date;
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class CommentaireService {
    private apiUrl = '/api/commentaire';
    private apiUrlCreate = '/api/commentaire/create';
    private constructor(private http: HttpClient) {}

    getAllCommentaires(id:number): Observable<Commentaire[]> {
        return this.http.get<{commentaires: Commentaire[];}>(this.apiUrl+`/${id}`).pipe(
            map(response => response.commentaires)
        );
    }
    createCommentaire(id_article:number,content:string) {
        const params = new HttpParams().set('contenu', content);
        return this.http.get<{ message: string }>(
        `${this.apiUrl}/create/${id_article}`,
        { params }
    );}

}