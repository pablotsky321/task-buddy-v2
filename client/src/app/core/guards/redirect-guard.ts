import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../../features/auth/auth.service';
import { take } from 'rxjs';

export const redirectGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  authService.isLoggedIn$.pipe(take(1)).subscribe({
    next: (isLogged) => {
      if (isLogged) {
        router.navigate(['/tasks'])
      } else {
        router.navigate(['/login'])
      }
    }
  })

  return false;

};
