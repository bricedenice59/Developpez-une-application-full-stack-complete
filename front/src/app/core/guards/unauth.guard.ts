import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from "@angular/router";
import {inject} from "@angular/core";
import {AuthCookieService} from "../cookies.services";

export const unAuthGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  const router = inject(Router);

  const authCookiesService = inject(AuthCookieService);

  const cookie: string | null = authCookiesService.get();

  if (!cookie) {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};
