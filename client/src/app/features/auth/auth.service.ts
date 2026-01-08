import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { User } from '../../core/types/user.type';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  
  private httpReq = inject(HttpClient);
  private API_URL = `${environment.API_URL}/auth`;
  private router = inject(Router);

  isLoggedIn = new BehaviorSubject<boolean>(!!localStorage.getItem('token'));
  isLoggedIn$ = this.isLoggedIn.asObservable();

  register(user: User) {
    return this.httpReq.post<User>(`${this.API_URL}/register`, user);
  }

  login(email:string, password:string){
    return this.httpReq.post<{token:string}>(`${this.API_URL}/login`, {email, password});
  }

  logout() {
    localStorage.removeItem('token')
    this.isLoggedIn.next(false)
    this.router.navigate(['/login'])
  }

  isTokenExpired() {
    const token = localStorage.getItem('token')
    if (!token) return true
    const payload = JSON.parse(token)
    const expires_at = payload.expires_at
    return expires_at < Date.now()
  }

}
