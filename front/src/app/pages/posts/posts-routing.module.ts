import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {PostsListComponent} from "./post-list/posts-list.component";
import {PostDetailComponent} from "./post-detail/post-detail.component";

const routes: Routes = [
  { title: 'Posts', path: '', component: PostsListComponent },
  { title: 'Post - detail', path: 'post-detail', component: PostDetailComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RentalRoutingModule { }
