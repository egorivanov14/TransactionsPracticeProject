package org.example.mapper;

import org.example.dto.user.RegisterRequest;
import org.example.dto.user.UserDto;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "budgets", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(RegisterRequest request);

    UserDto toDto(User user);
}
