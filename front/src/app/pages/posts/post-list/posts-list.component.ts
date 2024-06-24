import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {PostsService} from "../../../core/services/posts/posts.service";
import {IPost} from "../../../core/models/posts/post.interface";
import {catchError, map, of, Subscription} from 'rxjs';
import {PostContainerComponent} from "../../../components/posts/post-container/post-container.component";
import {Router, RouterLink} from "@angular/router";
import {MatButton} from "@angular/material/button";
import {NgClass} from "@angular/common";
import {DateTimeFormatter} from "../../../core/utils/date.formatter";
import {CollectionSort} from "../../../core/utils/collection.sort";
import {SpinLoaderComponent} from "../../../core/components/spin-loader/spin-loader.component";

@Component({
  selector: 'app-posts-list',
  standalone: true,
  imports: [
    PostContainerComponent,
    RouterLink,
    MatButton,
    NgClass,
    SpinLoaderComponent,
  ],
  templateUrl: './posts-list.component.html',
  styleUrl: './posts-list.component.scss'
})
export class PostsListComponent implements OnInit, OnDestroy {

  private readonly postsService: PostsService = inject(PostsService);
  private readonly router: Router = inject(Router);
  public postsArray: IPost[] = [];
  public postsSubscription$: Subscription | undefined;
  public hasError: boolean = false;
  public hasData: boolean = false;
  public isAscending: boolean = true;
  public isLoading: boolean = false;

  ngOnDestroy(): void {
    this.postsSubscription$?.unsubscribe();
  }

  ngOnInit() : void {
    this.postsService.isFetching.subscribe(isFetching => {
      this.isLoading = isFetching;
    });

    this.postsSubscription$ = this.postsService.getFeed().pipe(
      map((values: IPost[]) => {
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
      (values: IPost[]) => {
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

  public navigateWithData(post: IPost) : void {
    this.router.navigate(['posts/post-detail'], { state: { data: post } });
  }
}
