package com.fedorenko.test;

import com.fedorenko.test.domain.User;
import com.fedorenko.test.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    @Order(1)
    public void saveUserTest() {
        var user = User.builder()
                .email("ffedorenko21@gmail.com")
                .firstName("Fedir")
                .lastName("Fedorenko")
                .dateOfBirth(LocalDate.of(1999, 2, 21))
                .build();

        userRepository.save(user);

        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getFirstName()).isEqualTo("Fedir");
    }

    @Test
    @Order(2)
    public void findByIdTest() {
        var newUser = User.builder()
                .email("ffedorenko21@gmail.com")
                .firstName("Fedir")
                .lastName("Fedorenko")
                .dateOfBirth(LocalDate.of(1999, 2, 21))
                .build();

        userRepository.save(newUser);

        var user = userRepository.findById(2);
        assertThat(user.isPresent()).isEqualTo(true);
        user.ifPresent(value -> assertThat(value.getId()).isEqualTo(2));
    }

    @Test
    @Order(3)
    public void deleteByIdTest() {
        userRepository.deleteById(1);
        assertThat(userRepository.findById(1).isEmpty()).isEqualTo(true);
    }
}
