import {Component, inject, OnInit} from '@angular/core';
import {TopicsService} from "../../core/services/topics/topics.service";
import {ITopic} from "../../core/models/topics/topic.interface";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NgForOf, NgIf} from "@angular/common";
import {UserService} from "../../core/services/user/user.service";
import {IUserDetails} from "../../core/models/user/user.interface";
import {SessionService} from "../../core/services/auth/auth.session.service";
import {TopicsContainerComponent} from "../../components/topics/topics-container/topics-container.component";
import {ITopicsContainerEmitter} from "../../core/EventEmitters/topics-container.emitter";
import {SpinLoaderComponent} from "../../core/components/spin-loader/spin-loader.component";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [
    MatButton,
    MatInput,
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    TopicsContainerComponent,
    SpinLoaderComponent
  ],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss'
})
export class UserComponent implements OnInit {
  [key: string]: any; // Index signature

  private readonly topicsService: TopicsService = inject(TopicsService);
  private readonly userService: UserService = inject(UserService);
  private readonly sessionService: SessionService = inject(SessionService);
  public form: FormGroup<{ name: FormControl<string | null>; email: FormControl<string | null>;}>

  public nameValidationMessage : string = "";
  public emailValidationMessage : string = "";
  public userSubscribedTopicsArray: ITopic[] = [];
  public currentUserDetails: IUserDetails | undefined;
  public onErrorFetchingUserDetails: boolean = false;
  public onErrorFetchingSubscriptions: boolean = false;
  public onError: boolean = false;
  public isLoading: boolean = false;
  public hasSubscriptions: boolean = false;

  constructor(private fb: FormBuilder) {
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

    this.topicsService.isFetching.subscribe(isFetching => {
      this.isLoading = isFetching;
    });

    this.getUserDetails();
    this.getUserAllSubscription();
  }

  public getUserAllSubscription() : void {
      const userTopicsGetAllSubscription$ = this.topicsService.getSubscribed().subscribe({
      next: (values: ITopic[]) => {
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
      return true;
    }

    // Check if the email is the same as the current one
    if (email.value && email.value.trim().toLowerCase() === this.currentUserDetails!.email.toLowerCase()) {
      this.emailValidationMessage = 'Email address is the same as the current one, please type a different email or leave it blank to not update it';
      return true;
    }

    return !this.form.valid;
  }

  public updateUserProfile(): void{
    const newUserDetails : IUserDetails = {
      name: this.form.controls['name'].value!,
      email: this.form.controls['email'].value!,
    };

    const userUpdateDetailsSubscription$: Subscription = this.userService.updateDetails(newUserDetails).subscribe({
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
      next: (_: void): void => {
        userTopicsUnSubscribeSubscription$.unsubscribe();
        const index: number = this.userSubscribedTopicsArray.findIndex(subscription => subscription.id === topicData.emitterParams.id);
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
