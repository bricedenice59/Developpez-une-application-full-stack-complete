import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import {IUserDetails} from "../../models/user/user.interface";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private pathService: string = '/api/user';

  constructor(private httpClient: HttpClient) { }

  public get(): Observable<IUserDetails> {
    return this.httpClient.get<IUserDetails>(this.pathService);
  }

  public updateDetails(newDetails: IUserDetails): Observable<void> {
    return this.httpClient.put<void>(this.pathService, newDetails);
  }
}
