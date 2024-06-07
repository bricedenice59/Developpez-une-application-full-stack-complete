import { Component, OnInit } from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatCard, MatCardHeader} from "@angular/material/card";
import {Router, RouterLink} from "@angular/router";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss'],
    standalone: true,
    imports: [
      MatCard,
      MatCardHeader,
      MatButton,
      RouterLink
    ],
})
export class HomeComponent  {

  constructor(private router: Router) {
  }

  public goToPage(page: string): void {
    this.router.navigate([page]);
  }
}
