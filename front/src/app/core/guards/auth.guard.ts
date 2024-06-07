import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from "@angular/router";

import {inject} from "@angular/core";
import {AuthCookieService} from "../cookies.services";

export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  const router = inject(Router);

  const authCookiesService = inject(AuthCookieService);

  const cookie: string | null = authCookiesService.get();

  if (!cookie) {
    router.navigate(['/login']);
    return false;
  }
  return true;
};
