package com.uca.parcialfinalncapas.dto.response;

import com.uca.parcialfinalncapas.utils.enums.Rol;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private Long idUsuario;
    private String nombre;
    private String correo;
    private Rol rol;
}
