package com.uca.parcialfinalncapas.security;

import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        User user = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));

        return new org.springframework.security.core.userdetails.User(
                user.getCorreo(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRol().name()))
        );
    }
}