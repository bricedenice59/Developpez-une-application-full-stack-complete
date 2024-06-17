import { Routes } from '@angular/router';
import {LoginComponent} from "./components/auth/login/login.component";
import {SignupComponent} from "./components/auth/signup/signup.component";
import {HomeComponent} from "./pages/home/home.component";
import {authGuard} from "./core/guards/auth.guard";
import {unAuthGuard} from "./core/guards/unauth.guard";
import {TopicsComponent} from "./pages/topics/topics.component";
import {UserComponent} from "./pages/user/user.component";

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
    path: 'posts',
    loadChildren: () => import('./pages/posts/posts.module').then(m => m.PostsModule)
  },
  {
    canActivate: [authGuard],
    path: 'topics',
    title: 'Topics',
    component: TopicsComponent
  },
  {
    canActivate: [authGuard],
    path: 'user',
    title: 'My Profile',
    component: UserComponent
  }
];
