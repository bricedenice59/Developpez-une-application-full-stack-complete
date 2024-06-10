import {Component, OnInit} from '@angular/core';
import {SessionService} from "../auth/services/auth.session.service";
import {ToolbarComponent} from "../toolbar/toolbar.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    ToolbarComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  constructor(private sessionService: SessionService) {
  }

  ngOnInit(): void {

  }
}
