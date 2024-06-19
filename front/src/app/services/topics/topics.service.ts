import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ITopicResponse} from "./interfaces/topic.response.interface";
import {FetchService} from "../../core/services/fetch.service";

@Injectable({
  providedIn: 'root'
})
export class TopicsService extends FetchService {
  private pathService = '/api/topics';

  public getAll(): Observable<ITopicResponse[]> {
    return this.fetch<ITopicResponse[]>(this.pathService);
  }

  public getSubscribed(): Observable<ITopicResponse[]> {
    return this.fetch<ITopicResponse[]>(`${this.pathService}/subscribed`);
  }

  public subscribeToTopic(topicId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/subscribe?topicId=${topicId}&subscribe=true`, null);
  }

  public unSubscribeToTopic(topicId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/subscribe?topicId=${topicId}&subscribe=false`, null);
  }
}
