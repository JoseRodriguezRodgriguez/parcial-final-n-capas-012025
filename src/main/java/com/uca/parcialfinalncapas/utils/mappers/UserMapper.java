package com.uca.parcialfinalncapas.utils.mappers;

import com.uca.parcialfinalncapas.dto.request.UserCreateRequest;
import com.uca.parcialfinalncapas.dto.request.UserUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.UserResponse;
import com.uca.parcialfinalncapas.entities.User;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntityCreate(User userRequest) {
        return User.builder()
                .nombre(userRequest.getNombre())
                .correo(userRequest.getCorreo())
                .password(userRequest.getPassword())
                .rol(userRequest.getRol())
                .build();
    }

    public static User toEntityUpdate(User userUpdate) {
        return User.builder()
                .id(userUpdate.getId())
                .nombre(userUpdate.getNombre())
                .password(userUpdate.getPassword())
                .rol(userUpdate.getRol())
                .build();
    }

    public static UserResponse toDTO(User user) {
        return UserResponse.builder()
                .idUsuario(user.getId())
                .nombre(user.getNombre())
                .correo(user.getCorreo())
                .rol(user.getRol())
                .build();
    }

    public static List<UserResponse> toDTOList(List<User> users) {
        return users.stream().map(UserMapper::toDTO).collect(Collectors.toList());
    }
}
