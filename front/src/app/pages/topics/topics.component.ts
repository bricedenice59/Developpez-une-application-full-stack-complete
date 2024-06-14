import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {TopicsService} from "../../services/topics/topics.service";
import {ITopicResponse} from "../../services/topics/interfaces/topic.response.interface";
import {CollectionSort} from "../../core/utils/collection.sort";
import {MatButton} from "@angular/material/button";
import {TopicsContainerComponent} from "../../components/topics/topics-container/topics-container.component";

@Component({
  selector: 'app-topics',
  standalone: true,
  imports: [
    MatButton,
    TopicsContainerComponent
  ],
  templateUrl: './topics.component.html',
  styleUrl: './topics.component.scss'
})
export class TopicsComponent implements OnInit, OnDestroy {
  public topicsService = inject(TopicsService);
  public topicsArray: ITopicResponse[] = [];
  public topicsSubscription$: Subscription | undefined;
  public hasData: boolean = false;

  ngOnDestroy(): void {
    this.topicsSubscription$?.unsubscribe();
  }


  ngOnInit() : void {
    this.topicsSubscription$ = this.topicsService.getAll().subscribe(
      (values: ITopicResponse[]) => {
        this.hasData = values.length != 0;
        this.topicsArray = CollectionSort.sortByCreationDateDescending(values);
      }
    );
  }

}
