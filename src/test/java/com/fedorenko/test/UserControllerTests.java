package com.fedorenko.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fedorenko.test.controller.UserController;
import com.fedorenko.test.domain.User;
import com.fedorenko.test.dto.UserDto;
import com.fedorenko.test.service.UserService;
import com.fedorenko.test.util.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testSaveEmployee() throws Exception {
        UserDto userDto = getUserDto();
        User user = getUser();
        Mockito.when(userMapper.dtoToUser(any(UserDto.class))).thenReturn(user);
        Mockito.when(userService.create(user)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(getUser(), getUser());
        List<UserDto> userDtos = Arrays.asList(getUserDto(), getUserDto());
        Mockito.when(userService.getAll()).thenReturn(users);
        Mockito.when(userMapper.toListUserDto(users)).thenReturn(userDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        Integer userId = 1;
        User user = new User();
        UserDto userDto = new UserDto();
        Mockito.when(userService.getById(userId)).thenReturn(user);
        Mockito.when(userMapper.toUserDto(user)).thenReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testRefreshEmployee() throws Exception {
        Integer userId = 1;
        UserDto userDto = getUserDto();
        User user = getUser();

        // Stub the userMapper.dtoToUser method with the correct argument
        Mockito.when(userMapper.dtoToUser(any(UserDto.class))).thenReturn(user);

        // Stub the userService method
        Mockito.when(userService.updateById(userId, user)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testRemoveEmployeeById() throws Exception {
        Integer userId = 1;
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testGetUsersByBirthDatePeriod() throws Exception {
        LocalDate from = LocalDate.now();
        LocalDate to = LocalDate.now().plusDays(7);
        List<User> users = Arrays.asList(new User(), new User());
        List<UserDto> userDtos = Arrays.asList(new UserDto(), new UserDto());
        Mockito.when(userService.getByBirthDateRange(from, to)).thenReturn(users);
        Mockito.when(userMapper.toListUserDto(users)).thenReturn(userDtos);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/dates")
                        .param("from", from.toString())
                        .param("to", to.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Helper method to convert objects to JSON
    private String asJsonString(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(object);
    }

    private UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.dateOfBirth = LocalDate.of(1999, 1, 1);
        userDto.email = "ffedorenko21@gmail.com";
        userDto.firstName = "Fedir";
        userDto.lastName = "Fedorenko";
        return userDto;
    }

    private User getUser() {
        return User
                .builder()
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .email("ffedorenko21@gmail.com")
                .firstName("Fedir")
                .lastName("Fedorenko")
                .build();
    }
}
