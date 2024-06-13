import {Component, OnDestroy, OnInit} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NgForOf, NgIf} from "@angular/common";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {TopicsService} from "../../../services/topics/topics.service";
import {ITopicResponse} from "../../../services/topics/interfaces/topic.response.interface";
import {Subscription} from "rxjs";
import {PostsService} from "../../../services/posts/posts.service";
import {IPostRequest} from "../../../services/posts/interfaces/post.request.interface";

@Component({
  selector: 'app-post-create',
  standalone: true,
  imports: [
    MatButton,
    MatInput,
    NgIf,
    ReactiveFormsModule,
    NgForOf
  ],
  templateUrl: './post-create.component.html',
  styleUrl: './post-create.component.scss'
})
export class PostCreateComponent implements OnInit, OnDestroy {
  public topicsArray: ITopicResponse[] = [];
  public topicsSubscription$: Subscription | undefined;
  public onError = false;

  public form: FormGroup<{ topicId: FormControl<string | null>; title: FormControl<string | null>; content: FormControl<string | null>;}>

  constructor(private router: Router,
              private fb: FormBuilder,
              private topicService: TopicsService,
              private postsService: PostsService) {
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
      (values: ITopicResponse[]) => {
        this.topicsArray = values;
      }
    );
  }

  submit() {
    const postRequest : IPostRequest = {
      title: this.form.controls['title'].value!,
      description: this.form.controls['content'].value!,
    };
    const saveNewPostSubscription$ = this.postsService.savePost(this.form.controls['topicId'].value!, postRequest).subscribe({
      next: (_: void) => {
        saveNewPostSubscription$.unsubscribe();

        this.router.navigate(['/posts']);
      },
      error: err => {
        console.log(err);
      }
    });
  }
}