import {Component, OnDestroy} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatInput} from "@angular/material/input";
import {Router} from "@angular/router";
import {NgIf} from "@angular/common";
import {LoginRequest} from "../../../core/payloads/auth/loginRequest.interface";
import {AuthService} from "../../../core/services/auth/auth.service";
import {SessionInformation} from "../../../core/models/auth/sessionInformation.interface";
import {SessionService} from "../../../core/services/auth/auth.session.service";
import {HeaderComponent} from "../header/header.component";
import {Subscription} from "rxjs";

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
export class LoginComponent implements OnDestroy {
  public loginSubscription$: Subscription | undefined;
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

  ngOnDestroy(): void {
    this.loginSubscription$?.unsubscribe();
  }

  public submit(): void {
    const loginRequest = this.form.value as LoginRequest;
    this.loginSubscription$ = this.authService.login(loginRequest).subscribe({
      next: (response: SessionInformation): void => {
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
