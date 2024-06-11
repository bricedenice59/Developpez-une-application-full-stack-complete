import {HttpHandlerFn, HttpInterceptorFn, HttpRequest} from "@angular/common/http";
import {inject} from "@angular/core";
import {AuthStorageService} from "../auth.storage.service";

export const JwtInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
  const authStorageService = inject(AuthStorageService);

  const token = authStorageService.get();

  if (token != null) {
    const requestWithAuth: HttpRequest<any> = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });

    return next(requestWithAuth);
  }
  return next(req);
};
