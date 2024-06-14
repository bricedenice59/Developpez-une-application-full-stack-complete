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

  public getSubscribed(): Observable<ITopicResponse[]> {
    return this.httpClient.get<ITopicResponse[]>(`${this.pathService}/subscribed`);
  }

  public subscribeToTopic(topicId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/subscribe?topicId=${topicId}&subscribe=true`, null);
  }

  public unSubscribeToTopic(topicId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/subscribe?topicId=${topicId}&subscribe=false`, null);
  }
}
