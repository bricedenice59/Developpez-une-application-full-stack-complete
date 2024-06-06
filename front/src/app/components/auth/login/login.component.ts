import { Component } from '@angular/core';
import {MatButton} from "@angular/material/button";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatInput} from "@angular/material/input";
import {Router} from "@angular/router";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    MatButton,
    MatInput,
    NgIf,
    ReactiveFormsModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  public onError = false;

  public form: FormGroup<{ email: FormControl<string | null>; password: FormControl<string | null>; }>

  constructor(private Router: Router,
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

  }
}
