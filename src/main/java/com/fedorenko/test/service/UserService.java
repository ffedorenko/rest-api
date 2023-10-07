package com.fedorenko.test.service;

import com.fedorenko.test.domain.User;
import com.fedorenko.test.repository.UserRepository;
import com.fedorenko.test.util.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id = %s not found", id)));
    }

    public User updateById(Integer id, User user) {
        return userRepository.findById(id).map(entity -> {
            if (isFieldNew(user.getFirstName(), entity.getFirstName())) {
                entity.setFirstName(user.getFirstName());
            }
            if (isFieldNew(user.getAddress(), entity.getAddress())) {
                entity.setAddress(user.getAddress());
            }
            if (isFieldNew(user.getEmail(), entity.getEmail())) {
                entity.setEmail(user.getEmail());
            }
            if (isFieldNew(user.getLastName(), entity.getLastName())) {
                entity.setLastName(user.getLastName());
            }
            if (isFieldNew(user.getDateOfBirth(), entity.getDateOfBirth())) {
                entity.setDateOfBirth(user.getDateOfBirth());
            }
            if (isFieldNew(user.getPhoneNumber(), entity.getPhoneNumber())) {
                entity.setPhoneNumber(user.getPhoneNumber());
            }
            return userRepository.save(entity);
        }).orElseThrow(() -> new UserNotFoundException(String.format("User with id = %s not found", id)));
    }

    public void deleteById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id = %s not found", id)));
        userRepository.delete(user);
    }

    public List<User> getByBirthDateRange(LocalDate date1, LocalDate date2) {
        return userRepository.findAllByDateOfBirthBetween(date1, date2);
    }

    private <T> boolean isFieldNew(T newField, T existField) {
        return newField != null && !newField.equals(existField);
    }
}
