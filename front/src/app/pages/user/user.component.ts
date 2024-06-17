import {Component, inject, OnInit} from '@angular/core';
import {TopicsService} from "../../services/topics/topics.service";
import {ITopicResponse} from "../../services/topics/interfaces/topic.response.interface";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NgForOf, NgIf} from "@angular/common";
import {UserService} from "../../services/User/user.service";
import {IUserDetails} from "../../services/User/interfaces/user.interface";
import {SessionService} from "../../components/auth/services/auth.session.service";
import {TopicsContainerComponent} from "../../components/topics/topics-container/topics-container.component";
import {ITopicsContainerEmitter} from "../../core/EventEmitters/topics-container.emitter";

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [
    MatButton,
    MatInput,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    TopicsContainerComponent
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent implements OnInit {
  [key: string]: any; // Index signature

  private readonly topicsService = inject(TopicsService);
  private readonly userService = inject(UserService);
  private readonly sessionService = inject(SessionService);
  public form: FormGroup<{ name: FormControl<string | null>; email: FormControl<string | null>;}>

  public nameValidationMessage : string = "";
  public emailValidationMessage : string = "";
  public userSubscribedTopicsArray: ITopicResponse[] = [];
  public currentUserDetails: IUserDetails | undefined;
  public onErrorFetchingUserDetails: boolean = false;
  public onErrorFetchingSubscriptions: boolean = false;
  public onError: boolean = false;
  public hasSubscriptions: boolean = false;

  constructor(private router: Router,
              private fb: FormBuilder) {
    this.form = this.fb.group({
      name: [
        '',
        [
          Validators.maxLength(255)
        ]
      ],
      email: [
        '',
        [
          Validators.email
        ]
      ]
    });
  }

  ngOnInit(): void {
    this.form.valueChanges.subscribe(_ => {
      this.isUpdateButtonDisabled();
    });

    this.getUserDetails();
    this.getUserAllSubscription();
  }

  public getUserAllSubscription() : void {
      const userTopicsGetAllSubscription$ = this.topicsService.getSubscribed().subscribe({
      next: (values: ITopicResponse[]) => {
        this.hasSubscriptions = values.length > 0;
        this.userSubscribedTopicsArray = values;
        userTopicsGetAllSubscription$.unsubscribe();
      },
      error: err => {
        console.log(err);
        this.onErrorFetchingSubscriptions = true;
      }
    });
  }

  private getUserDetails() : void {
    const getUserDetailsSubscription$ = this.userService.get().subscribe({
      next: (value: IUserDetails) => {
        this.currentUserDetails = value;
        getUserDetailsSubscription$.unsubscribe();
      },
      error: err => {
        console.log(err);
        this.onErrorFetchingUserDetails = true;
      }
    });
  }

  public isUpdateButtonDisabled() : boolean {
    // Early return if there's an error fetching user details
    if(this.onErrorFetchingUserDetails)
      return true;

    // Reset validation messages
    this.nameValidationMessage = "";
    this.emailValidationMessage = "";

    const { name, email } = this.form.controls;

    // Helper function to set validation messages
    const setValidationMessage = (control: FormControl<string | null>, messageKey : string, message : string) => {
      if (control.errors && control.errors[messageKey]) {
        this[messageKey + 'ValidationMessage'] = message;
        return true;
      }
      return false;
    };

    // Check for 'name' field errors
    if (setValidationMessage(name, 'name', 'Name cannot be more than 255 characters.')) return true;

    // Check for 'email' field errors
    if (setValidationMessage(email, 'email', 'Please enter a valid email address.')) return true;

    // Check if both inputs are empty
    if (!name.value && !email.value) return true;

    //finally check if user tries to update same values...it doesn't make sense to update details with same data

    // Check if the name is the same as the current one
    if (name.value && name.value.trim().toLowerCase() === this.currentUserDetails!.name.toLowerCase()) {
      this.nameValidationMessage = 'Username is the same as the current one, please type a different username or leave it blank to not update it';
    }

    // Check if the email is the same as the current one
    if (email.value && email.value.trim().toLowerCase() === this.currentUserDetails!.email.toLowerCase()) {
      this.emailValidationMessage = 'Email address is the same as the current one, please type a different email or leave it blank to not update it';
    }

    return !this.form.valid;
  }

  public updateUserProfile(): void{
    const newUserDetails : IUserDetails = {
      name: this.form.controls['name'].value!,
      email: this.form.controls['email'].value!,
    };

    const userUpdateDetailsSubscription$ = this.userService.updateDetails(newUserDetails).subscribe({
      next: (_: void) => {
        userUpdateDetailsSubscription$.unsubscribe();
        this.logout();
      },
      error: err => {
        console.log(err);
      }
    });
  }

  public logout(): void {
    this.sessionService.logOut();
  }

  public unsubscribeFromTopic(topicData: { emitterParams: ITopicsContainerEmitter }) : void {
     const userTopicsUnSubscribeSubscription$= this.topicsService.unSubscribeToTopic(topicData.emitterParams.id).subscribe({
      next: (_: void) => {
        userTopicsUnSubscribeSubscription$.unsubscribe();
        const index = this.userSubscribedTopicsArray.findIndex(subscription => subscription.id === topicData.emitterParams.id);
        if (index !== -1) {
          this.userSubscribedTopicsArray.splice(index, 1);
          this.hasSubscriptions = this.userSubscribedTopicsArray.length > 0;
        }
      },
      error: err => {
        console.log(err);
      }
    });
  }
}
