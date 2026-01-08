import { Component, inject, isDevMode } from '@angular/core';
import { AuthService } from '../auth.service';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { User } from '../../../core/types/user.type';
import { finalize } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  authService = inject(AuthService);

  //controll UI
  isLoading = false
  router = inject(Router);

  user_form = new FormGroup({
    completeName: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
  })

  register() {
    this.isLoading = true;
    this.authService.register(this.user_form.value as User)
    .pipe(finalize(() => this.isLoading = false))
    .subscribe({
      next: (resp) => {
        if(isDevMode()) console.log(resp);
        alert('register succesfully');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        if(isDevMode()) console.log(err);
        const error = err.error
        alert(typeof(error) === 'string' ? error : "Hubo un error inesperado");
      }
    })
  }

}
