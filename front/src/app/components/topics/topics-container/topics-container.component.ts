import {Component, EventEmitter, Input, Output} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {ITopicsContainerEmitter} from "../../../core/EventEmitters/topics-container.emitter";

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
  @Input() isAlreadySubscribed :  boolean = false;
  @Input() isSubscribeCase : boolean = false;
  @Output() topicContainerEvent = new EventEmitter<{ emitterParams: ITopicsContainerEmitter }>();


  // emit to the parent component the current topic id and its callback
  public fn() {
    this.topicContainerEvent.emit({emitterParams: {id: this.id, onFnSuccessCallback: this.handleSuccess.bind(this)} });
  }

  // Function to handle the success status
  handleSuccess(success: boolean) {
    this.isAlreadySubscribed = success;
  }
}
