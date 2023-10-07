package com.fedorenko.test.repository;

import com.fedorenko.test.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAllByDateOfBirthBetween(LocalDate dateOfBirth, LocalDate dateOfBirth2);
}
