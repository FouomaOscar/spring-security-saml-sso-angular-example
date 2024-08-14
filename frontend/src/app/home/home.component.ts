import { Component } from '@angular/core';
import { UserService } from '../core/services/user.service';
import { User } from '../core/models/user.model';
import { BrowserModule } from '@angular/platform-browser';
import { NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { TOKEN_NAME } from '../core/constants';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NgIf],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  providers: [UserService, Router]
})
export class HomeComponent {

  user!: User;

  constructor(private userService: UserService, private router: Router) {}

  getUser() {
    this.userService.getUser().subscribe(
      {
        next: (res) => {
          console.log(res);
          this.user = res;
        },
        error: (err) => {
          console.error('something wrong occurred: ' + err);
        }
      }
    );
  }

  logout() {
    localStorage.removeItem(TOKEN_NAME);
    this.router.navigate(['/auth'])
  }

  ngOnInit() {
    this.getUser();
  }

}
