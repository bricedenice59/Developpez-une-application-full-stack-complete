import { NgModule } from '@angular/core';
import { RentalRoutingModule } from './posts-routing.module';
import {PostsListComponent} from "./post-list/posts-list.component";
import {PostDetailComponent} from "./post-detail/post-detail.component";

@NgModule({
  imports: [
    RentalRoutingModule,
    PostDetailComponent,
    PostsListComponent
  ]
})
export class PostsModule { }
