import {Component, OnDestroy} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {RegisterRequest} from "../../../core/payloads/auth/registerRequest.interface";
import {AuthService} from "../../../core/services/auth/auth.service";
import {HeaderComponent} from "../header/header.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Subscription} from "rxjs";

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
export class SignupComponent implements OnDestroy {
  private signupSubscription$ : Subscription | undefined
  public onError = false;
  public isSigningUp = false;
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

  ngOnDestroy(): void {
    this.signupSubscription$?.unsubscribe();
  }

  public submit(): void {
    if(this.isSigningUp) {
      return;
    }

    this.isSigningUp = true;

    const registerRequest = this.form.value as RegisterRequest;
    this.signupSubscription$ = this.authService.register(registerRequest).subscribe({
      next: (_: void): void => {
        this.snackBar.open("Account successfully created, you will be redirected to the login page.", "Close", { duration: 2000 });
        setTimeout((): void => {
          this.router.navigate(['/login']).then(() => {
            this.isSigningUp = false;
          });
        }, 2000);
      },
      error: _ => {
        this.onError = true;
        this.isSigningUp = false;
      }
    })
  }
}
