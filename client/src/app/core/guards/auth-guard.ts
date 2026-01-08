import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../features/auth/auth.service';
import { map, take } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  
  if (authService.isTokenExpired()){
      authService.logout()
  }
  
  return authService.isLoggedIn$.pipe(
    take(1),
    map(isLogged=>{
      if(isLogged) return true;
      return router.createUrlTree(['/login'], { queryParams: { returnUrl: state.url } });
    })
  )
  
};
