import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root',
})
export class AuthCookieService {

  private TOKEN_NAME: string = 'token';

  constructor(private cookieService: CookieService) {}

  public get(): string | null {
    if(this.cookieService.check(this.TOKEN_NAME))
      return this.cookieService.get(this.TOKEN_NAME);
    return null;
  }

  public set(token: string): void {
    this.cookieService.set(this.TOKEN_NAME, token, { secure: false, sameSite: "Lax" });
  }

  public delete(): void {
    this.cookieService.delete(this.TOKEN_NAME);
  }
}
