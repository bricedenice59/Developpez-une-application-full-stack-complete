import {HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import {IPostResponse} from "./interfaces/post.response.interface";
import {ICommentResponse} from "./interfaces/comment.response.interface";
import {IPostRequest} from "./interfaces/post.request.interface";
import {FetchService} from "../../core/services/fetch.service";

@Injectable({
  providedIn: 'root'
})
export class PostsService extends FetchService{

  private pathService = '/api/posts';

  public getFeed(): Observable<IPostResponse[]> {
    return this.fetch<IPostResponse[]>(`${this.pathService}/feed`);
  }

  public getAllComments(id: number): Observable<ICommentResponse[]> {
    return this.fetch<ICommentResponse[]>(`${this.pathService}/${id}/comments`);
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
