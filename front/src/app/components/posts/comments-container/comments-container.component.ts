import {Component, Input} from '@angular/core';
import {ICommentResponse} from "../../../services/posts/interfaces/comment.response.interface";

@Component({
  selector: 'post-comments-container',
  standalone: true,
  imports: [],
  templateUrl: './comments-container.component.html',
  styleUrl: './comments-container.component.scss'
})
export class CommentsContainerComponent {
  @Input() comments: ICommentResponse[] = new Array<ICommentResponse>();
}
