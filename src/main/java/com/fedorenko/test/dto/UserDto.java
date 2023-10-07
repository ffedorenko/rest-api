package com.fedorenko.test.dto;

import com.fedorenko.test.util.validation.AgeConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class UserDto {
    @Email
    @NotNull
    public String email;

    @NotNull
    public String firstName;

    @NotNull
    public String lastName;

    @AgeConstraint
    @NotNull
    public LocalDate dateOfBirth;

    public String address;
    public String phoneNumber;
}
