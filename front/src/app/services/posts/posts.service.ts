import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import {IPostResponse} from "./interfaces/post.response.interface";


@Injectable({
  providedIn: 'root'
})
export class PostsService {

  private pathService = '/api/posts';

  constructor(private httpClient: HttpClient) { }

  public getAll(): Observable<IPostResponse[]> {
    return this.httpClient.get<IPostResponse[]>(this.pathService);
  }

  public getDetail(id: number): Observable<IPostResponse> {
    return this.httpClient.get<IPostResponse>(`${this.pathService}/${id}`);
  }
}
