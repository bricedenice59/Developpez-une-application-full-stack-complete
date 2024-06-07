import { Routes } from '@angular/router';
import {LoginComponent} from "./components/auth/login/login.component";
import {SignupComponent} from "./components/auth/signup/signup.component";
import {DashboardComponent} from "./components/dashboard/dashboard.component";
import {HomeComponent} from "./pages/home/home.component";
import {authGuard} from "./core/guards/auth.guard";
import {unAuthGuard} from "./core/guards/unauth.guard";

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
  {
    path: 'home',
    component: HomeComponent ,
  },
  {
    canActivate: [unAuthGuard],
    path: 'login',
    title: 'Log in',
    component: LoginComponent ,
  },
  {
    canActivate: [unAuthGuard],
    path: 'signup',
    title: 'Sign up',
    component: SignupComponent ,
  },
  {
    canActivate: [authGuard],
    path: 'dashboard',
    component: DashboardComponent ,
  }
];
