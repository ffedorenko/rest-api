package com.fedorenko.test.controller;

import com.fedorenko.test.dto.UserDto;
import com.fedorenko.test.service.UserService;
import com.fedorenko.test.util.mapper.UserMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class UserController {
    private final UserMapper userMapper;
    private final UserService userService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveEmployee(@RequestBody @Valid UserDto requestForSave) {
        var user = userMapper.dtoToUser(requestForSave);
        return userMapper.toUserDto(userService.create(user));
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsers() {
        return userMapper.toListUserDto(userService.getAll());
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getEmployeeById(@PathVariable Integer id) {
        return userMapper.toUserDto(userService.getById(id));
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto refreshEmployee(@PathVariable("id") Integer id, @RequestBody UserDto requestForSave) {
        return userMapper.toUserDto(userService.updateById(id, userMapper.dtoToUser(requestForSave)));
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeEmployeeById(@PathVariable Integer id) {
        userService.deleteById(id);
    }

    @GetMapping("/users/dates")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsersByBirthDatePeriod(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        return userMapper.toListUserDto(userService.getByBirthDateRange(from, to));
    }
}
