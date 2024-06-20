import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {provideAnimations} from "@angular/platform-browser/animations";
import {JwtInterceptor} from "./core/interceptors/jwt.interceptor";
import {UnauthorizedInterceptor} from "./core/interceptors/unauthorized.interceptor";

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes),
    provideHttpClient(withInterceptors([JwtInterceptor, UnauthorizedInterceptor])),
    provideAnimations()]
};
