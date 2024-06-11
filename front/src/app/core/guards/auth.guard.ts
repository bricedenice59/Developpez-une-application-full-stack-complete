import {CanActivateFn, Router} from "@angular/router";

import {inject} from "@angular/core";
import {AuthStorageService} from "../auth.storage.service";

export const authGuard: CanActivateFn = (): boolean => {
  const router = inject(Router);
  const authStorageService = inject(AuthStorageService);

  const cookie: string | null = authStorageService.get();

  if (!cookie) {
    router.navigate(['/login']);
    return false;
  }
  return true;
};
