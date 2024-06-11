import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {PostsService} from "../../services/posts/posts.service";
import {IPostResponse} from "../../services/posts/interfaces/post.response.interface";
import {map, Subscription} from 'rxjs';
import {PostContainerComponent} from "../../components/posts/post-container/post-container.component";
import {RouterLink} from "@angular/router";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-posts',
  standalone: true,
  imports: [
    PostContainerComponent,
    RouterLink,
    MatButton,
  ],
  templateUrl: './posts.component.html',
  styleUrl: './posts.component.scss'
})
export class PostsComponent implements OnInit, OnDestroy {

  private readonly postsService = inject(PostsService);
  public postsArray: IPostResponse[] = [];
  public postsSubscription$: Subscription | undefined;
  public hasData: boolean = false;

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
        this.postsArray = values;
      }
    );
  }

  sortPosts() {
    return undefined;
  }

  createPost() {

  }
}
