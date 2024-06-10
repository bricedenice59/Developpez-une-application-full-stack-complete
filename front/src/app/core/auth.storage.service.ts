import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class AuthStorageService {

  private TOKEN_NAME: string = 'token';

  public get(): string | null {
    return sessionStorage.getItem(this.TOKEN_NAME);
  }

  public set(token: string): void {
    sessionStorage.setItem(this.TOKEN_NAME, token);
  }

  public delete(): void {
    sessionStorage.removeItem(this.TOKEN_NAME);
  }
}
