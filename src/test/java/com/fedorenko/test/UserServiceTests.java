package com.fedorenko.test;

import com.fedorenko.test.domain.User;
import com.fedorenko.test.repository.UserRepository;
import com.fedorenko.test.service.UserService;
import com.fedorenko.test.util.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testCreateUser() {
        User user = User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("555-123-4567")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.create(user);

        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getFirstName(), savedUser.getFirstName());
    }

    @Test
    public void testGetAllUsers() {
        User user1 = User.builder().build();
        User user2 = User.builder().build();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAll();

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void testGetUserById() {
        User user = User.builder().id(1).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User foundUser = userService.getById(1);

        assertNotNull(foundUser);
        assertEquals(1, foundUser.getId());
    }

    @Test
    public void testGetUserByIdNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getById(1));
    }

    @Test
    public void testUpdateUser() {
        User existingUser = User.builder()
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("555-123-4567")
                .build();
        User updatedUser = User.builder()
                .email("new@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1991, 2, 2))
                .address("456 Elm St")
                .phoneNumber("555-987-6543")
                .build();

        when(userRepository.findById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateById(1, updatedUser);

        assertNotNull(result);
        assertEquals("new@example.com", result.getEmail());
        assertEquals("Jane", result.getFirstName());
    }

    @Test
    public void testUpdateUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateById(1, new User()));
    }

    @Test
    public void testDeleteUser() {
        User user = User.builder().id(1).build();

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        userService.deleteById(1);
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    public void testDeleteUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.deleteById(1));
    }

    @Test
    public void testGetUsersByBirthDateRange() {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2000, 1, 1);
        User user1 = User.builder().dateOfBirth(LocalDate.of(1995, 5, 5)).build();
        User user2 = User.builder().dateOfBirth(LocalDate.of(1997, 7, 7)).build();

        when(userRepository.findAllByDateOfBirthBetween(from, to)).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getByBirthDateRange(from, to);

        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void testGetUsersByBirthDateRangeNoMatch() {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(2000, 1, 1);

        when(userRepository.findAllByDateOfBirthBetween(from, to)).thenReturn(List.of());

        assertThrows(UserNotFoundException.class, () -> userService.getByBirthDateRange(from, to));
    }

    @Test
    public void testGetUsersByBirthDateRangeInvalidDates() {
        LocalDate from = LocalDate.of(2000, 1, 1);
        LocalDate to = LocalDate.of(1990, 1, 1);

        assertThrows(IllegalArgumentException.class, () -> userService.getByBirthDateRange(from, to));
    }
}
