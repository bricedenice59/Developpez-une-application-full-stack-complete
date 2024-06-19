import {Component, EventEmitter, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {ICommentContentEmitter} from "../../../core/EventEmitters/comments-content.emitter";

@Component({
  selector: 'post-comment-submitter',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './comment-submitter.component.html',
  styleUrl: './comment-submitter.component.scss'
})
export class CommentSubmitterComponent {
  @Output() addCommentEvent = new EventEmitter<{ emitterParams: ICommentContentEmitter }>();

  public commentText: string = '';
  public maxLengthCommentText: number = 2000;
  public charCountCommentText: number = this.maxLengthCommentText;

  updateCharCount(): void {
    this.charCountCommentText = this.maxLengthCommentText - this.commentText.length;
  }

  // Function to handle the success status
  handleSuccess(success: boolean) {
    if(success){
      this.commentText = '';
      this.charCountCommentText = this.maxLengthCommentText;
    }
  }

  addComment(event: Event): void{
    if (!this.commentText.trim()) {
      event.stopPropagation();
      return;
    }
    this.addCommentEvent.emit({emitterParams: {comment: this.commentText.trim() , onFnSuccessCallback: this.handleSuccess.bind(this)} });
  }
}
