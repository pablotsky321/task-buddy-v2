import { Component, inject, isDevMode } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  authService = inject(AuthService);
  router = inject(Router);

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  });

  //controlUI
  isLoading = false;

  login(){
   if(this.loginForm.valid){
    this.isLoading = true;
    this.authService.login(this.loginForm.value.email!, this.loginForm.value.password!)
    .pipe(
     finalize(() => {
      this.isLoading = false;
     }) 
    )
    .subscribe({
      next: (res) => {
        if(isDevMode()) console.log(res);
        this.setToken(res.token);
        this.authService.isLoggedIn.next(true);
        this.router.navigate(['/tasks']);
      },
      error: (err) => {
        if(isDevMode()) console.log(err);
        const error = err.error;
        alert(typeof(error) === 'string' ? error : "Hubo un error inesperado");
      }
    })
   } 
  }

  setToken(token:string){
    const expires_at = Date.now() + (12 * 60 * 60 * 1000);

    const payload = {
      token: token,
      expires_at: expires_at
    }

    localStorage.setItem('token', JSON.stringify(payload))
  }

}
