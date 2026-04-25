package com.saas.tarefaapiserver.repository;

import com.saas.tarefaapiserver.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Data JPA gera o SQL automaticamente baseado no nome do método.
    // "findByUsername" → SELECT * FROM usuarios WHERE username = ?
    Optional<Usuario> findByUsername(String username);
}