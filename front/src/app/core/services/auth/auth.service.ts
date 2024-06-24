import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../../payloads/auth/loginRequest.interface';
import { RegisterRequest } from '../../payloads/auth/registerRequest.interface';
import {SessionInformation} from "../../models/auth/sessionInformation.interface";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private pathService: string = '/api/auth';

  constructor(private httpClient: HttpClient) { }

  public register(registerRequest: RegisterRequest): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/register`, registerRequest);
  }

  public login(loginRequest: LoginRequest): Observable<SessionInformation> {
    return this.httpClient.post<SessionInformation>(`${this.pathService}/login`, loginRequest);
  }
}
