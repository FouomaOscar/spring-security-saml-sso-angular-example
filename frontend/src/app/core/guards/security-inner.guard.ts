import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const securityInnerGuard: CanActivateFn = (route, state) => {

  const router: Router = inject(Router);
  if (!localStorage.getItem('samlDemoAuthToken')) {
    return true;
  } else {
    router.navigate(['home']);
    return false;
  }
};
