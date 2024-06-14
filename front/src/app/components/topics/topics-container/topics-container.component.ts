import {Component, inject, Input} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {TopicsService} from "../../../services/topics/topics.service";

@Component({
  selector: 'app-topics-container',
  standalone: true,
  imports: [
    MatButton
  ],
  templateUrl: './topics-container.component.html',
  styleUrl: './topics-container.component.scss'
})
export class TopicsContainerComponent {
  @Input() id! : number;
  @Input() title : string = '';
  @Input() description : string = '';

  private readonly topicsService = inject(TopicsService);
  public isAlreadySubscribed: boolean = false;

  public subscribe() : void {
    const topicsSubscription$ = this.topicsService.subscribeToTopic(this.id).subscribe({
      next: (_: void) => {
        this.isAlreadySubscribed = true;
        topicsSubscription$.unsubscribe();
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
