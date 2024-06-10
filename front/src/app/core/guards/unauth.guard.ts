import {ActivatedRouteSnapshot, CanActivateFn, Router, RouterStateSnapshot} from "@angular/router";
import {inject} from "@angular/core";
import {AuthStorageService} from "../auth.storage.service";

export const unAuthGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean => {
  const router = inject(Router);

  const authStorageService = inject(AuthStorageService);

  const cookie: string | null = authStorageService.get();

  if (!cookie) {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};
