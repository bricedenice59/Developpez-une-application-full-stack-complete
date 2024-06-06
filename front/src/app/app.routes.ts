import { Routes } from '@angular/router';
import {LoginComponent} from "./components/auth/login/login.component";
import {WelcomeComponent} from "./components/auth/welcome/welcome.component";
import {SignupComponent} from "./components/auth/signup/signup.component";

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'welcome',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent ,
  },
  {
    path: 'signup',
    component: SignupComponent ,
  },
  {
    path: 'welcome',
    component: WelcomeComponent ,
  }
];
