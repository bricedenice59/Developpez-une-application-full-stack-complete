import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {ITopic} from "../../models/topics/topic.interface";
import {FetchService} from "../fetch.service";

@Injectable({
  providedIn: 'root'
})
export class TopicsService extends FetchService {
  private pathService = '/api/topics';

  public getAll(): Observable<ITopic[]> {
    return this.fetch<ITopic[]>(this.pathService);
  }

  public getSubscribed(): Observable<ITopic[]> {
    return this.fetch<ITopic[]>(`${this.pathService}/subscribed`);
  }

  public subscribeToTopic(topicId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/subscribe?topicId=${topicId}&subscribe=true`, null);
  }

  public unSubscribeToTopic(topicId: number): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}/subscribe?topicId=${topicId}&subscribe=false`, null);
  }
}
