import { NgModule } from '@angular/core';
import { RentalRoutingModule } from './posts-routing.module';
import {PostsListComponent} from "./post-list/posts-list.component";
import {PostDetailComponent} from "./post-detail/post-detail.component";
import {PostCreateComponent} from "./post-create/post-create.component";

@NgModule({
  imports: [
    RentalRoutingModule,
    PostDetailComponent,
    PostsListComponent,
    PostCreateComponent
  ]
})
export class PostsModule { }
