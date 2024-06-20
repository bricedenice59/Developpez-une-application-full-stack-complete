import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {TopicsService} from "../../../core/services/topics/topics.service";
import {ITopic} from "../../../core/models/topics/topic.interface";
import {Subscription} from "rxjs";
import {PostsService} from "../../../core/services/posts/posts.service";
import {IPostRequest} from "../../../core/payloads/posts/post.request.interface";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-post-create',
  standalone: true,
  imports: [
    MatButton,
    MatInput,
    NgIf,
    ReactiveFormsModule,
    NgForOf,
    RouterLink
  ],
  templateUrl: './post-create.component.html',
  styleUrl: './post-create.component.scss'
})
export class PostCreateComponent implements OnInit, OnDestroy {
  public topicsArray: ITopic[] = [];
  public topicsSubscription$: Subscription | undefined;
  public onError = false;
  public isSubmittingComment = false;

  public form: FormGroup<{ topicId: FormControl<string | null>; title: FormControl<string | null>; content: FormControl<string | null>;}>

  constructor(private router: Router,
              private fb: FormBuilder,
              private topicService: TopicsService,
              private postsService: PostsService,
              private snackBar: MatSnackBar) {
    this.form = this.fb.group({
      topicId: [
        '',
        [
          Validators.required
        ]
      ],
      title: [
        '',
        [
          Validators.required,
          Validators.maxLength(255)
        ]
      ],
      content: [
        '',
        [
          Validators.required,
          Validators.maxLength(2000)
        ]
      ]
    });
  }

  ngOnDestroy(): void {
    this.topicsSubscription$?.unsubscribe();
  }

  ngOnInit(): void {
    this.topicsSubscription$ = this.topicService.getAll().subscribe(
      (values: ITopic[]) => {
        this.topicsArray = values;
      }
    );
  }

  submit() {
    if(this.isSubmittingComment){
      return;
    }
    this.isSubmittingComment = true;

    const postRequest : IPostRequest = {
      title: this.form.controls['title'].value!,
      description: this.form.controls['content'].value!,
    };
    const saveNewPostSubscription$ = this.postsService.savePost(this.form.controls['topicId'].value!, postRequest).subscribe({
      next: (_: void) => {
        saveNewPostSubscription$.unsubscribe();
        this.snackBar.open("Post successfully created!, you will be redirected to the post page.", "Close", { duration: 2000 });
        setTimeout(() => {
          this.router.navigate(['/posts']).then(() => {
            this.isSubmittingComment = false;
          });
        }, 2000);
      },
      error: err => {
        console.log(err);
        this.isSubmittingComment = false;
      }
    });
  }
}
