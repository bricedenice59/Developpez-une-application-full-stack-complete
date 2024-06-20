import {Component} from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {MatToolbar} from "@angular/material/toolbar";
import {AsyncPipe, NgIf} from "@angular/common";
import {ToolbarComponent} from "./core/components/toolbar/toolbar.component";
import {filter} from "rxjs";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatToolbar, NgIf, AsyncPipe, ToolbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent{

  public showHeader : boolean = false;

  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.showHeader = this.router.url !== '/home' &&
        this.router.url !== '/login' &&
        this.router.url !== '/signup';
    });
  }
}
