import {Injectable, OnDestroy} from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import {SessionInformation} from "../../../interfaces/sessionInformation.interface";
import {HttpClient} from "@angular/common/http";
import {AuthCookieService} from "../../../core/cookies.services";

const defaultAuthenticationState: SessionInformation = {
  isAuthenticated: false,
  username: undefined,
  token: undefined
}

@Injectable({
  providedIn: 'root'
})
export class SessionService implements OnDestroy {
  private authenticationSubject = new BehaviorSubject<SessionInformation>(defaultAuthenticationState);
  private auth$ = this.authenticationSubject.asObservable();

  constructor(private authCookieService: AuthCookieService) {
    this.auth$.subscribe((sessionInfo: SessionInformation) => {
      if (sessionInfo.isAuthenticated) {
        this.authCookieService.set(sessionInfo.token!);
      }
    });
  }

  ngOnDestroy(): void {
    this.authenticationSubject.next(defaultAuthenticationState);
    this.authenticationSubject.complete();
    this.authCookieService.delete();
  }

  public logIn(userSession: SessionInformation): void {
    this.authenticationSubject.next(userSession);
  }

  public logOut(): void {
    this.authenticationSubject.next(defaultAuthenticationState);
    this.authCookieService.delete();
  }
}
