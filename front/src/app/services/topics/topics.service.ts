import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import {ITopicResponse} from "./interfaces/topic.response.interface";

@Injectable({
  providedIn: 'root'
})
export class TopicsService {

  private pathService = '/api/topics';

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<ITopicResponse[]> {
    return this.httpClient.get<ITopicResponse[]>(this.pathService);
  }
}
