import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {PostsService} from "../../../services/posts/posts.service";
import {IPostResponse} from "../../../services/posts/interfaces/post.response.interface";
import {catchError, map, of, Subscription} from 'rxjs';
import {PostContainerComponent} from "../../../components/posts/post-container/post-container.component";
import {Router, RouterLink} from "@angular/router";
import {MatButton} from "@angular/material/button";
import {NgClass} from "@angular/common";
import {DateTimeFormatter} from "../../../core/utils/date.formatter";
import {CollectionSort} from "../../../core/utils/collection.sort";

@Component({
  selector: 'app-posts-list',
  standalone: true,
  imports: [
    PostContainerComponent,
    RouterLink,
    MatButton,
    NgClass,
  ],
  templateUrl: './posts-list.component.html',
  styleUrl: './posts-list.component.scss'
})
export class PostsListComponent implements OnInit, OnDestroy {

  private readonly postsService = inject(PostsService);
  private readonly router = inject(Router);
  public postsArray: IPostResponse[] = [];
  public postsSubscription$: Subscription | undefined;
  public hasError: boolean = false;
  public hasData: boolean = false;
  public isAscending: boolean = true;

  ngOnDestroy(): void {
    this.postsSubscription$?.unsubscribe();
  }

  ngOnInit() : void {
    this.postsSubscription$ = this.postsService.getFeed().pipe(
      map((values: IPostResponse[]) => {
        return values.map(post => {
          return {...post, createdAt: DateTimeFormatter.Format(new Date(post.createdAt))};
        });
      }),
      catchError(error => {
        this.hasError = true;
        console.error('An error occurred:', error);
        return of([]);
      })
    ).subscribe(
      (values: IPostResponse[]) => {
        this.hasData = values.length != 0;
        this.postsArray = CollectionSort.sortByCreationDateDescending(values);
      }
    );
  }

  public sortPosts(): void {
    this.isAscending = !this.isAscending;
    if(this.isAscending)
      this.postsArray = CollectionSort.sortByCreationDateDescending(this.postsArray);
    else
      this.postsArray = CollectionSort.sortByCreationDateAscending(this.postsArray);
  }

  public createPost(): void {
    this.router.navigate(['posts/post-create']);
  }

  public navigateWithData(post: IPostResponse) : void {
    this.router.navigate(['posts/post-detail'], { state: { data: post } });
  }
}
