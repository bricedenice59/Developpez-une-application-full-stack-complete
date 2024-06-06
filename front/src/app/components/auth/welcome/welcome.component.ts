import { Component } from '@angular/core';
import {FlexModule} from "@angular/flex-layout";
import {MatCard, MatCardHeader} from "@angular/material/card";
import {MatButton} from "@angular/material/button";
import {Router} from "@angular/router";

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [
    FlexModule,
    MatCard,
    MatCardHeader,
    MatButton
  ],
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.scss'
})
export class WelcomeComponent {
  constructor( private router: Router) {
  }
  public goToPage(page: string): void {
    this.router.navigate([page]);
  }
}
