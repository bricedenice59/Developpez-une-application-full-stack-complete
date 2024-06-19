import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap, catchError, finalize } from 'rxjs/operators';
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: 'root',
})
export abstract class FetchService {
  protected isFetchingData$ = new BehaviorSubject<boolean>(false);

  constructor(protected httpClient: HttpClient) {}

  protected fetch<T>(endpoint: string): Observable<T> {
    this.isFetchingData$.next(true);
    return this.httpClient.get<T>(endpoint).pipe(
      tap(() => this.isFetchingData$.next(false)),
      catchError(error => {
        this.isFetchingData$.next(false);
        throw error;
      }),
      finalize(() => this.isFetchingData$.next(false))
    );
  }

  public get isFetching(): Observable<boolean> {
    return this.isFetchingData$.asObservable();
  }
}
