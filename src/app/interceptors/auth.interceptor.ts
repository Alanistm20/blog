import { inject } from '@angular/core';
import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  const token = auth.token();

 
  const isLogin = req.url.includes('/auth/login');

  
  const isPublicGet =
    req.method === 'GET' &&
    (req.url.includes('/categories') || req.url.includes('/posts'));

  if (!token || isLogin || isPublicGet) {
    return next(req);
  }

  return next(req.clone({
    setHeaders: { Authorization: `Bearer ${token}` }
  }));
};
