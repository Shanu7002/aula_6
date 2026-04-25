package com.saas.tarefaapiserver.service;

import com.saas.tarefaapiserver.model.Usuario;
import com.saas.tarefaapiserver.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Método chamado pelo Spring Security toda vez que alguém tenta fazer login.
     *
     * Recebe o username digitado na tela.
     * Precisa devolver um UserDetails com os dados do usuário.
     * Se não existir, lança UsernameNotFoundException.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Busca no banco pelo username
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado: " + username)
            );

        // 2. Converte o nosso Usuario em UserDetails (formato que o Spring entende)
        return User.builder()
            .username(usuario.getUsername())
            .password(usuario.getSenha())  // senha já está em BCrypt
            .roles(usuario.getRole())      // Spring adiciona o prefixo "ROLE_" sozinho
            .build();
    }
}