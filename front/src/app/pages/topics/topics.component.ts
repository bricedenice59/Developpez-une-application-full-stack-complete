import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {
  catchError,
  forkJoin,
  map,
  of,
  Subject,
  takeUntil
} from "rxjs";
import {TopicsService} from "../../services/topics/topics.service";
import {CollectionSort} from "../../core/utils/collection.sort";
import {MatButton} from "@angular/material/button";
import {TopicsContainerComponent} from "../../components/topics/topics-container/topics-container.component";
import {ITopicData} from "../../services/topics/interfaces/topic.data.interface";
import {ITopicsContainerEmitter} from "../../core/EventEmitters/topics-container.emitter";
import {SpinLoaderComponent} from "../../components/common/spin-loader/spin-loader.component";

@Component({
  selector: 'app-topics',
  standalone: true,
  imports: [
    MatButton,
    TopicsContainerComponent,
    SpinLoaderComponent
  ],
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss'
})
export class TopicsComponent implements OnInit, OnDestroy {
  public topicsService = inject(TopicsService);
  public topicsArray: ITopicData[] = [];
  private destroy$ : Subject<void> = new Subject<void>();
  public hasData: boolean = false;
  public hasError: boolean = false;
  public isLoading: boolean = false;

  ngOnInit(): void {
    this.topicsService.isFetching.subscribe(isFetching => {
      this.isLoading = isFetching;
    });

    //I have 2 requests sent in cascade, one that retrieves all topics and one that retrieves all topics' subscription status for the current user
    forkJoin({
      allTopics: this.topicsService.getAll(),
      subscribedTopics: this.topicsService.getSubscribed()
    }).pipe(
      map(({ allTopics, subscribedTopics }) => {
        const subscribedIds = new Set(subscribedTopics.map(topic => topic.id));
        return allTopics.map(topic => ({
          ...topic,
          isSubscribed: subscribedIds.has(topic.id)
        }));
      }),
      catchError(error => {
        this.hasError = true;
        console.error('An error occurred:', error);
        return of([]);
      }),
      takeUntil(this.destroy$)
    ).subscribe(topics => {
      this.topicsArray = CollectionSort.sortByCreationDateDescending(topics);
      this.hasData = this.topicsArray.length > 0;
    });
  }

  public subscribeTopic(topicData: { emitterParams: ITopicsContainerEmitter }) : void {
    const topicsSubscription$ = this.topicsService.subscribeToTopic(topicData.emitterParams.id).subscribe({
      next: (_: void) => {
        topicsSubscription$.unsubscribe();
        topicData.emitterParams.onFnSuccessCallback(true);
      },
      error: err => {
        console.log(err);
      }
    });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ngOnInit() : void {
  //   this.topicsSubscription$ = this.topicsService.getAll().pipe(
  //     switchMap((allTopics: ITopicResponse[]) => {
  //       this.hasData = allTopics.length != 0;
  //
  //       if(this.hasData){
  //         allTopics.forEach((x : ITopicResponse)=> {
  //           const topic : ITopicData = {
  //             id: x.id,
  //             title: x.title,
  //             description: x.description,
  //             createdAt: x.createdAt,
  //             isSubscribed: false
  //           }
  //           this.topicsArray.push(topic);
  //         });
  //
  //         this.topicsArray = CollectionSort.sortByCreationDateDescending(this.topicsArray);
  //       }
  //       return this.hasData ? this.topicsService.getSubscribed() : of([]);
  //     })
  //   ).subscribe((userTopics: ITopicResponse[]) => {
  //     if (userTopics.length) {
  //       userTopics.forEach((x : ITopicResponse)=> {
  //         const topic = this.topicsArray.find((z : ITopicData)=> {
  //           return z.id == x.id;
  //         });
  //         if (topic) {
  //           topic.isSubscribed = true;
  //         }
  //       });
  //     }
  //   });
  // }
}
