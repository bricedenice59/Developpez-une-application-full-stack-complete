import { Component } from '@angular/core';
import {MatButton} from "@angular/material/button";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatInput} from "@angular/material/input";
import {Router} from "@angular/router";
import {NgIf} from "@angular/common";
import {LoginRequest} from "../interfaces/loginRequest.interface";
import {AuthService} from "../services/auth.service";
import {SessionInformation} from "../../../interfaces/sessionInformation.interface";
import {SessionService} from "../services/auth.session.service";
import {HeaderComponent} from "../header/header.component";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    MatButton,
    MatInput,
    NgIf,
    ReactiveFormsModule,
    HeaderComponent,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  public onError = false;

  public form: FormGroup<{ email: FormControl<string | null>; password: FormControl<string | null>; }>

  constructor(private router: Router,
              private authService: AuthService,
              private sessionService: SessionService,
              private fb: FormBuilder) {
    this.form = this.fb.group({
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
        ]
      ]
    });
  }

  public submit(): void {
    const loginRequest = this.form.value as LoginRequest;
    this.authService.login(loginRequest).subscribe({
      next: (response: SessionInformation) => {
        response.isAuthenticated = true;
        this.sessionService.logIn(response);
        this.router.navigate(['/posts']);
      },
      error: err => {
        this.onError = true;
        console.log(err);
      }
    });
  }
}
