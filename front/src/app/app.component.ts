import {Component, inject} from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import {SessionService} from "./components/auth/services/auth.session.service";
import {MatToolbar} from "@angular/material/toolbar";
import {AsyncPipe, NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MatToolbar, NgIf, AsyncPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {

}
