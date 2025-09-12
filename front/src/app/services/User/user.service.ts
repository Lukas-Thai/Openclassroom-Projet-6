
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, tap } from 'rxjs';
export interface User{
    id:Number;
    username:String;
    email:String;
    created_at:String;
    updated_at:String;
}
@Injectable({
    providedIn: 'root'
})

export class UserService{
    private apiUrl = '/api/user';
    constructor(private http: HttpClient) {}
    getUser(){
        return this.http.get<{user:User}>(this.apiUrl).pipe(map(response => response.user));
    }
    updateUser(email:string,username:string,password:string) {
        return this.http.post<{ token: string }>(this.apiUrl+"/update", {
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