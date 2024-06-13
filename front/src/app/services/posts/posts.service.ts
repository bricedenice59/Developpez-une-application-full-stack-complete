import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import {IPostResponse} from "./interfaces/post.response.interface";
import {ICommentResponse} from "./interfaces/comment.response.interface";
import {IPostRequest} from "./interfaces/post.request.interface";

@Injectable({
  providedIn: 'root'
})
export class PostsService {

  private pathService = '/api/posts';

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<IPostResponse[]> {
    return this.httpClient.get<IPostResponse[]>(this.pathService);
  }

  public getAllComments(id: number): Observable<ICommentResponse[]> {
    return this.httpClient.get<ICommentResponse[]>(`${this.pathService}/${id}/comments`);
  }

  public savePost(topicId: number, postRequest: IPostRequest): Observable<void> {
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
