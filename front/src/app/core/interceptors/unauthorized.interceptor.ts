import {inject} from '@angular/core';
import {
  HttpRequest,
  HttpErrorResponse,
  HttpInterceptorFn,
  HttpHandlerFn,
  HttpStatusCode
} from '@angular/common/http';
import {catchError} from "rxjs/operators";
import {throwError} from "rxjs";
import {SessionService} from "../services/auth/auth.session.service";

export const UnauthorizedInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
  const sessionService = inject(SessionService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === HttpStatusCode.Unauthorized) {
        // If 401 received, log the user out and redirect to login page
        sessionService.logOut('Session has expired! please log in again.');
      }
      return throwError(() => error);
    }));
}
