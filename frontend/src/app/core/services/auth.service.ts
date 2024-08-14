import { HttpClient, HttpBackend } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { BASE_URL, TOKEN_NAME } from '../constants';
import { User } from '../models/user.model';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private userSubject: BehaviorSubject<User>;
  public user: Observable<User>;

  constructor() {
    this.userSubject = new BehaviorSubject<User>(JSON.parse(String(localStorage.getItem(TOKEN_NAME))));
    this.user = this.userSubject.asObservable();
  }

  authenticate(): void {
    setTimeout(() => window.location.replace(BASE_URL + '/auth'), 1000);
  }
}
