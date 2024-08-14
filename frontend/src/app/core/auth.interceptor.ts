import { HttpInterceptorFn } from '@angular/common/http';
import { BASE_URL, TOKEN_NAME } from './constants';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token: string = String(localStorage.getItem(TOKEN_NAME));

  if (token) {
    // Clone the request to add the new header.
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`),
      url: BASE_URL + req.url
    });
    // console.log('Sending request with new header now ...');
    // send the newly created request
    return next(authReq);
  }
  return next(req);
};
