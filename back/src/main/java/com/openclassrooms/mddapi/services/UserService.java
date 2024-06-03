package com.openclassrooms.mddapi.services;


import com.openclassrooms.mddapi.exceptions.UserAlreadyExistException;
import com.openclassrooms.mddapi.exceptions.UserNotFoundException;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.payloads.requests.UpdateUserDetailsRequest;
import com.openclassrooms.mddapi.repositories.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Data
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Integer id) {
        var user =  userRepository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public void update(int id, boolean updateEmail, UpdateUserDetailsRequest userDetailsRequest) {
        if(updateEmail) {
            //does it exist a user that has the same email?
            var userInDBfromEmail = userRepository.findByEmail(userDetailsRequest.getEmail());
            if (userInDBfromEmail.isPresent()) {
                throw new UserAlreadyExistException(MessageFormat.format("A user already exist with this email {0}", userDetailsRequest.getEmail()));
            }
        }
        var user = getUserById(id);
        if(updateEmail)
            user.setEmail(userDetailsRequest.getEmail());

        user.setName(userDetailsRequest.getName());

        userRepository.save(user);
    }
}
