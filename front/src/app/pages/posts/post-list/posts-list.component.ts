import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {PostsService} from "../../../services/posts/posts.service";
import {IPostResponse} from "../../../services/posts/interfaces/post.response.interface";
import {map, Subscription} from 'rxjs';
import {PostContainerComponent} from "../../../components/posts/post-container/post-container.component";
import {Router, RouterLink} from "@angular/router";
import {MatButton} from "@angular/material/button";
import {NgClass} from "@angular/common";
import {DateTimeFormatter} from "../../../core/utils/date.formatter";

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
  public hasData: boolean = false;
  public isAscending: boolean = true;

  ngOnDestroy(): void {
    this.postsSubscription$?.unsubscribe();
  }

  ngOnInit() {
    this.postsSubscription$ = this.postsService.getAll().pipe(
      map((values: IPostResponse[]) => {
        return values.map(post => {
          return {...post, createdAt: DateTimeFormatter.Format(new Date(post.createdAt))};
        });
      })
    ).subscribe(
      (values: IPostResponse[]) => {
        this.hasData = values.length != 0;
        this.postsArray = this.sortPostsDescending(values);
      }
    );
  }

  sortPostsAscending(posts: IPostResponse[]): IPostResponse[] {
    return posts.sort((a, b) => new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime());
  }

  sortPostsDescending(posts: IPostResponse[]): IPostResponse[] {
    return posts.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime());
  }

  sortPosts() {
    this.isAscending = !this.isAscending;
    if(this.isAscending)
      this.postsArray = this.sortPostsDescending(this.postsArray);
    else
      this.postsArray = this.sortPostsAscending(this.postsArray);
  }

  createPost() {
    this.router.navigate(['posts/post-create']);
  }

  navigateWithData(post: IPostResponse) {
    this.router.navigate(['posts/post-detail'], { state: { data: post } });
  }
}
