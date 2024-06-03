package com.openclassrooms.mddapi.integration;

import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
public abstract class BaseIT {

    @Autowired
    protected MockMvc mockMvc;

    protected final UserRepository userRepository;

    protected User user;
    protected final String userName = "brice";
    protected final String userEmail = "brice@denice.com";
    protected final String password = "paSSword1!";

    @Autowired
    public BaseIT(UserRepository userRepository) {
        this.userRepository = userRepository;

        user = userRepository.findByEmail(userEmail).orElse(null);
        if(user != null) {
            return;
        }

        //create the default user for authentication purpose used in all here-below tests of this class
        var newDefaultuser = User.builder()
                .name(userName)
                .email(userEmail)
                .password(password)
                .build();
        user = userRepository.save(newDefaultuser);
    }
}
