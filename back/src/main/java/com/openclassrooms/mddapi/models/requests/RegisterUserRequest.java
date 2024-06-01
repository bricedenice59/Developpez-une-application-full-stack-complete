package com.openclassrooms.mddapi.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

//Here’s a breakdown of the regex for password constraint:
//        ^ asserts the start of a line.
//        (?=.*[0-9]) ensures at least one digit is present.
//        (?=.*[a-z]) ensures at least one lowercase letter is present.
//        (?=.*[A-Z]) ensures at least one uppercase letter is present.
//        (?=.*\W) ensures at least one special character is present. \W matches any non-word character.
//        .{8,} ensures the total number of characters is 8 or more.
//$ asserts the end of a line.
//This regex uses positive lookaheads (?=) to confirm the presence of at least one of each required character type without consuming characters in the string, allowing for the checks to be independent of each other’s position in the string. The .{8,} at the end then matches any character sequence of length 8 or more, ensuring the overall length requirement is met.

@Data
public class RegisterUserRequest {

    //https://medium.com/@arunkumarmeenakshisundaram/email-id-validation-in-spring-boot-8c0fceebca79
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotBlank(message = "Email is required!")
    private String email;

    @NotBlank(message = "Name is required!")
    private String name;

    @NotBlank(message = "Password is required!")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$", message = "Invalid password format, at least one digit, one lowercase letter, one uppercase letter and one special character are required!")
    private String password;
}
