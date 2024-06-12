import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {PostsService} from "../../services/posts/posts.service";
import {IPostResponse} from "../../services/posts/interfaces/post.response.interface";
import {map, Subscription} from 'rxjs';
import {PostContainerComponent} from "../../components/posts/post-container/post-container.component";
import {RouterLink} from "@angular/router";
import {MatButton} from "@angular/material/button";
import {NgClass} from "@angular/common";

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [
    PostContainerComponent,
    RouterLink,
    MatButton,
    NgClass,
  ],
  templateUrl: './posts.component.html',
  styleUrl: './posts.component.scss'
})
export class PostsComponent implements OnInit, OnDestroy {

  private readonly postsService = inject(PostsService);
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
          const dateObj = new Date(post.createdAt);

          const formattedDate = `${dateObj.getDate().toString().padStart(2, '0')}/${(dateObj.getMonth() + 1).toString().padStart(2, '0')}/${dateObj.getFullYear()} ${dateObj.getHours().toString().padStart(2, '0')}:${dateObj.getMinutes().toString().padStart(2, '0')}`;
          return {...post, createdAt: formattedDate};
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

  }
}
