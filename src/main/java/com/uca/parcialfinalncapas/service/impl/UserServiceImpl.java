package com.uca.parcialfinalncapas.service.impl;

import com.uca.parcialfinalncapas.dto.request.UserCreateRequest;
import com.uca.parcialfinalncapas.dto.request.UserUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.UserResponse;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.exceptions.UserNotFoundException;
import com.uca.parcialfinalncapas.repository.UserRepository;
import com.uca.parcialfinalncapas.service.UserService;
import com.uca.parcialfinalncapas.utils.enums.Rol;
import com.uca.parcialfinalncapas.utils.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /* ------------------------------------------------------------------
       BUSCAR POR CORREO
       ------------------------------------------------------------------ */
    @Override
    public UserResponse findByCorreo(String correo) {
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuario no encontrado con correo: " + correo));
        return UserMapper.toDTO(user);
    }

    /* ------------------------------------------------------------------
       GUARDAR (CREATE)
       ------------------------------------------------------------------ */
    @Override
    public UserResponse save(UserCreateRequest req) {

        if (userRepository.findByCorreo(req.getCorreo()).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con el correo: " + req.getCorreo());
        }

        User user = User.builder()
                .nombre(req.getNombre())
                .correo(req.getCorreo())
                .password(passwordEncoder.encode(req.getPassword()))
                .rol(req.getRol())     // convertir String -> Enum
                .build();

        return UserMapper.toDTO(userRepository.save(user));
    }

    /* ------------------------------------------------------------------
       ACTUALIZAR
       ------------------------------------------------------------------ */
    @Override
    public UserResponse update(UserUpdateRequest req) {

        User user = userRepository.findById(req.getId())
                .orElseThrow(() ->
                        new UserNotFoundException("No se encontró un usuario con el ID: " + req.getId()));

        // Actualizar campos
        user.setNombre(req.getNombre());
        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        user.setRol(Rol.valueOf(req.getNombreRol()));

        return UserMapper.toDTO(userRepository.save(user));
    }

    /* ------------------------------------------------------------------
       ELIMINAR
       ------------------------------------------------------------------ */
    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("No se encontró un usuario con el ID: " + id);
        }
        userRepository.deleteById(id);
    }

    /* ------------------------------------------------------------------
       LISTAR TODOS
       ------------------------------------------------------------------ */
    @Override
    public List<UserResponse> findAll() {
        return UserMapper.toDTOList(userRepository.findAll());
    }
}