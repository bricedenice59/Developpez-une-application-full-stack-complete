import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {PostsService} from "../../../core/services/posts/posts.service";
import {IPost} from "../../../core/models/posts/post.interface";
import {Subscription} from "rxjs";
import {IComment} from "../../../core/models/posts/comment.interface";
import {Router, RouterLink} from '@angular/router';
import {PostContainerComponent} from "../../../components/posts/post-container/post-container.component";
import {FormsModule} from "@angular/forms";
import {CommentsContainerComponent} from "../../../components/posts/comments-container/comments-container.component";
import {CommentSubmitterComponent} from "../../../components/posts/comment-submitter/comment-submitter.component";
import {ICommentContentEmitter} from "../../../core/EventEmitters/comments-content.emitter";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-post-detail',
  standalone: true,
  imports: [
    PostContainerComponent,
    FormsModule,
    RouterLink,
    CommentsContainerComponent,
    CommentSubmitterComponent
  ],
  templateUrl: './post-detail.component.html',
  styleUrl: './post-detail.component.scss'
})
export class PostDetailComponent implements OnInit, OnDestroy {
  private readonly postsService = inject(PostsService);
  private readonly router = inject(Router);
  private readonly snackBar = inject(MatSnackBar);
  public commentsArray: IComment[] = [];
  public commentsSubscription$: Subscription | undefined;
  public postData : IPost | undefined;
  public hasError: boolean = false;

  constructor() {
    this.loadPostData();
  }

  private loadPostData(): void {
    const navigation = this.router.getCurrentNavigation();
    let navigationData: { data: IPost } | undefined;

    if (navigation && navigation.extras && navigation.extras.state) {
      navigationData = navigation.extras.state as { data: IPost };
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
    sessionStorage.removeItem('postData');
  }

  ngOnInit(): void {
    if (this.hasError) {
      return;
    }

    this.commentsSubscription$ = this.postsService.getAllComments(this.postData!.id).subscribe(
      (values: IComment[]) => {
        this.commentsArray = values;
      }
    );
  }

  public saveComment(commentData: { emitterParams: ICommentContentEmitter }) : void {
    const saveCommentSubscription$ = this.postsService.saveComment(this.postData!.id, commentData.emitterParams.comment).subscribe({
      next: (_: void) => {
        const newComment: IComment = {
          id: 0, //we are not using it later so id can be any number
          username: "me",
          text: commentData.emitterParams.comment
        };

        this.commentsArray.unshift(newComment);
        commentData.emitterParams.onFnSuccessCallback(true);
        this.snackBar.open("Comment successfully created!", "Close", { duration: 2000 });

        saveCommentSubscription$.unsubscribe();
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
