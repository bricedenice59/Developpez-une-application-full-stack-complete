import {Component, Input} from '@angular/core';
import {IComment} from "../../../core/models/posts/comment.interface";

@Component({
  selector: 'post-comments-container',
  standalone: true,
  imports: [],
  templateUrl: './comments-container.component.html',
  styleUrl: './comments-container.component.scss'
})
export class CommentsContainerComponent {
  @Input() comments: IComment[] = new Array<IComment>();
}
