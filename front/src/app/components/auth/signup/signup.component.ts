import { Component } from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {RegisterRequest} from "../interfaces/registerRequest.interface";
import {AuthService} from "../services/auth.service";
import {HeaderComponent} from "../header/header.component";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-signup',
  standalone: true,
    imports: [
        MatButton,
        MatInput,
        NgIf,
        ReactiveFormsModule,
        HeaderComponent
    ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {
  public onError = false;

  public form: FormGroup<{ name: FormControl<string | null>; email: FormControl<string | null>; password: FormControl<string | null>; }>

  constructor(private router: Router,
              private authService: AuthService,
              private fb: FormBuilder,
              private snackBar: MatSnackBar) {
    this.form = this.fb.group({
      name: [
        '',
        [
          Validators.required,
          Validators.maxLength(255)
        ]
      ],
      email: [
        '',
        [
          Validators.required,
          Validators.email
        ]
      ],
      password: [
        '',
        [
          Validators.required,
          Validators.pattern('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$')
        ]
      ]
    });
  }

  public submit(): void {
    const registerRequest = this.form.value as RegisterRequest;
    this.authService.register(registerRequest).subscribe({
      next: (_: void) => {
        this.snackBar.open("Account successfully created, you will be redirected to the login page.", "Close", { duration: 2000 });
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2_000);
      },
      error: _ => this.onError = true
    })
  }
}
