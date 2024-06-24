import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {
  catchError,
  forkJoin,
  map,
  of,
  Subject,
  takeUntil
} from "rxjs";
import {TopicsService} from "../../core/services/topics/topics.service";
import {CollectionSort} from "../../core/utils/collection.sort";
import {MatButton} from "@angular/material/button";
import {TopicsContainerComponent} from "../../components/topics/topics-container/topics-container.component";
import {ITopicData} from "../../core/models/topics/topic.data.interface";
import {ITopicsContainerEmitter} from "../../core/EventEmitters/topics-container.emitter";
import {SpinLoaderComponent} from "../../core/components/spin-loader/spin-loader.component";

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
  public topicsService: TopicsService = inject(TopicsService);
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
}
