import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { Router } from '@angular/router';
import { TOKEN_NAME } from '../constants';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private httpClient: HttpClient, private router: Router) { }

  getUser(): Observable<User> {
    return this.httpClient.get<User>('/api/authUser');
  }
}
