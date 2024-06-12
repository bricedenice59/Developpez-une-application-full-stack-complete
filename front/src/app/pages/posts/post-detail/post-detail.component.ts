import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {PostsService} from "../../../services/posts/posts.service";
import {IPostResponse} from "../../../services/posts/interfaces/post.response.interface";
import {Subscription} from "rxjs";
import {ICommentResponse} from "../../../services/posts/interfaces/comment.response.interface";
import { Router } from '@angular/router';
import {PostContainerComponent} from "../../../components/posts/post-container/post-container.component";
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [
    PostContainerComponent,
    FormsModule
  ],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.scss'
})
export class PostDetailComponent implements OnInit, OnDestroy {
  private readonly postsService = inject(PostsService);
  public commentsArray: ICommentResponse[] = [];
  public commentsSubscription$: Subscription | undefined;
  public postData : IPostResponse | undefined;
  public hasError: boolean = false;

  public commentText: string = '';
  public maxLengthCommentText: number = 2000;
  public charCountCommentText: number = this.maxLengthCommentText;

  constructor(private router: Router) {
    this.loadPostData();
  }

  private loadPostData(): void {
    const navigation = this.router.getCurrentNavigation();
    let navigationData: { data: IPostResponse } | undefined;

    if (navigation && navigation.extras && navigation.extras.state) {
      navigationData = navigation.extras.state as { data: IPostResponse };
      this.postData = navigationData.data;
      sessionStorage.setItem('postData', JSON.stringify(this.postData));
    } else {
      try {
        this.postData = JSON.parse(sessionStorage.getItem('postData') || '{}');
      }
      catch (error) {
        this.hasError = true;
      }
    }

    if(!this.hasError && this.postData?.id === undefined)
      this.hasError = true;
  }

  ngOnDestroy(): void {
    this.commentsSubscription$?.unsubscribe();
  }

  ngOnInit() {
    if (this.hasError) {
      return;
    }

    this.commentsSubscription$ = this.postsService.getAllComments(this.postData!.id).subscribe(
      (values: ICommentResponse[]) => {
        this.commentsArray = values;
      }
    );
  }

  updateCharCount(): void {
    this.charCountCommentText = this.maxLengthCommentText - this.commentText.length;
  }

  addComment(event: Event): void{
    if (!this.commentText.trim()) {
      event.stopPropagation();
      return;
    }

    const saveCommentSubscription$ = this.postsService.saveComment(this.postData!.id, this.commentText).subscribe({
      next: (_: void) => {
        const newComment: ICommentResponse = {
          id: 0,
          username: "test",
          text: this.commentText
        };

        this.commentsArray.push(newComment);
        saveCommentSubscription$.unsubscribe();

        this.commentText = '';
        this.charCountCommentText = this.maxLengthCommentText;
      },
      error: err => {
        console.log(err);
      }
    });
  }

}
