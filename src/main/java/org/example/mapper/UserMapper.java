package org.example.mapper;

import org.example.dto.*;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "budgets", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    User toEntity(RegisterRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "budgets", ignore = true)
    @Mapping(target = "transactions", ignore = true)
    @Mapping(target = "email", ignore = true)
    User toEntity(LoginRequest request);

    @Mapping(target = "token", ignore = true)
    RegisterResponse toResponse(User user);

    UserDto toDto(User user);
}
