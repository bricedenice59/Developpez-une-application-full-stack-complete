package com.openclassrooms.mddapi.unit.repository;

import com.openclassrooms.mddapi.models.Role;
import com.openclassrooms.mddapi.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Tag("UserRepositoryTests")
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("Create a role in database and lookup role by its name")
    public void RoleRepositoryTests_findByName_ReturnsRole() {
        var role = Role.builder()
                .id(1)
                .name("TEST")
                .createdAt(LocalDateTime.now())
                .build();

        roleRepository.save(role);
        Optional<Role> userOptional = roleRepository.findByName("TEST");

        Assertions.assertTrue(userOptional.isPresent());
    }
}
