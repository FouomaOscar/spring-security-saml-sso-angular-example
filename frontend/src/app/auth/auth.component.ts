import { Component } from '@angular/core';
import { AuthService } from '../core/services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TOKEN_NAME } from '../core/constants';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.scss',
  providers: [AuthService, Router]
})
export class AuthComponent {

  constructor(private authService: AuthService, private router: Router, route: ActivatedRoute) {

    route.queryParams.subscribe(params => {
      const authToken = params['token'];
      if (authToken) {
        localStorage.setItem(TOKEN_NAME, authToken)
        this.router.navigate(['/home'])
      }
    });
  }

  samlLogin() {

    this.authService.authenticate();
  }
}
