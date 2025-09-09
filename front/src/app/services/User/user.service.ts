
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { tap } from 'rxjs';


@Injectable({
    providedIn: 'root'
})
export class UserService{
    private apiUrl = '/api/user';
    constructor(private http: HttpClient) {}
    updateUser(email:string,username:string,password:string) {
        return this.http.post<any>(this.apiUrl+"/update", {
            email: email,
            username: username,
            password: password
        }).pipe(tap(response => {
                if (response.token) {
                    localStorage.setItem('authToken', response.token);
                }
        }));
        };
}