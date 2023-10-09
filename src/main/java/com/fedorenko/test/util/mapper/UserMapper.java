package com.fedorenko.test.util.mapper;

import com.fedorenko.test.domain.User;
import com.fedorenko.test.dto.UserDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User dtoToUser(UserDto userDto);
    UserDto toUserDto(User user);
    List<UserDto> toListUserDto(List<User> userList);
}
