import {Component, OnInit} from '@angular/core';
import {SessionService} from "../auth/services/auth.session.service";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  constructor(private sessionService: SessionService) {
  }

  ngOnInit(): void {

  }
}
