import {Component, Input} from '@angular/core';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-post-container',
  standalone: true,
  imports: [
    RouterLink
  ],
  templateUrl: './post-container.component.html',
  styleUrl: './post-container.component.scss'
})
export class PostContainerComponent {
  @Input() id! : number;
  @Input() title : string = '';
  @Input() date : string = '';
  @Input() author : string = '';
  @Input() description : string = '';
}
