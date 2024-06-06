import { Component } from '@angular/core';
import {FlexModule} from "@angular/flex-layout";
import {MatCard, MatCardHeader} from "@angular/material/card";
import {MatButton} from "@angular/material/button";

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

}
