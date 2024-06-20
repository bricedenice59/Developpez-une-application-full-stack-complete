import {HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import {IPost} from "../../models/posts/post.interface";
import {IComment} from "../../models/posts/comment.interface";
import {IPostRequest} from "../../payloads/posts/post.request.interface";
import {FetchService} from "../fetch.service";

@Injectable({
  providedIn: 'root'
})
export class PostsService extends FetchService{

  private pathService = '/api/posts';

  public getFeed(): Observable<IPost[]> {
    return this.fetch<IPost[]>(`${this.pathService}/feed`);
  }

  public getAllComments(id: number): Observable<IComment[]> {
    return this.fetch<IComment[]>(`${this.pathService}/${id}/comments`);
  }

  public savePost(topicId: string, postRequest: IPostRequest): Observable<void> {
    return this.httpClient.post<void>(`${this.pathService}?topicId=${topicId}`, postRequest);
  }

  public saveComment(id: number, commentStr: string): Observable<void> {
    const obj = {
      comment: commentStr
    };
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.httpClient.post<void>(`${this.pathService}/${id}/comments`, JSON.stringify(obj), { headers });
  }
}
