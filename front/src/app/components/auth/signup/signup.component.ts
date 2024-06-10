import { Component } from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {RegisterRequest} from "../interfaces/registerRequest.interface";
import {AuthService} from "../services/auth.service";
import {SessionInformation} from "../../../interfaces/sessionInformation.interface";
import {HeaderComponent} from "../header/header.component";

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
              private fb: FormBuilder) {
    this.form = this.fb.group({
      name: [
        '',
        [
          Validators.required,
          Validators.max(255)
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
        this.router.navigate(['/login']);
      },
      error: _ => this.onError = true
    })
  }
}
