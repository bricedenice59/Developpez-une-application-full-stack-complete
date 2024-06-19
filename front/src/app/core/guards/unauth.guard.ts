import {CanActivateFn, Router} from "@angular/router";
import {inject} from "@angular/core";
import {AuthStorageService} from "../services/auth.storage.service";

export const unAuthGuard: CanActivateFn = (): boolean => {
  const router = inject(Router);
  const authStorageService = inject(AuthStorageService);

  const cookie: string | null = authStorageService.get();

  if (!cookie) {
    return true;
  }

  router.navigate(['/posts']);
  return false;
};
