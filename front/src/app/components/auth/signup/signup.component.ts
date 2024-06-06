import { Component } from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";

@Component({
  selector: 'app-signup',
  standalone: true,
    imports: [
        MatButton,
        MatInput,
        NgIf,
        ReactiveFormsModule
    ],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss'
})
export class SignupComponent {
  public onError = false;

  public form: FormGroup<{ name: FormControl<string | null>; email: FormControl<string | null>; password: FormControl<string | null>; }>

  constructor(private Router: Router,
              private fb: FormBuilder) {
    this.form = this.fb.group({
      name: [
        '',
        [
          Validators.required,
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

  }
}
