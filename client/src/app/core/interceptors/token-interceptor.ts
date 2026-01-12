import { HttpInterceptorFn } from '@angular/common/http';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const payload_storage = localStorage.getItem('token') as any;
  const payload = JSON.parse(payload_storage);
  if (payload) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${payload.token}`
      }
    });
  }
  return next(req);
};
