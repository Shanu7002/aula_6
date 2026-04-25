package com.saas.tarefaapiserver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // nullable=false → a coluna não aceita null no banco
    // unique=true   → dois usuários não podem ter o mesmo username
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // A senha aqui já virá criptografada com BCrypt.
    // BCrypt gera strings de 60 caracteres — deixamos 100 para folga.
    @Column(nullable = false, length = 100)
    private String senha;

    // Papel do usuário. Exemplos: "ADMIN", "USUARIO".
    // Deixamos como String para simplificar (poderia ser um enum).
    @Column(nullable = false, length = 20)
    private String role;

    public Usuario() {
    }

    public Usuario(String username, String senha, String role) {
        this.username = username;
        this.senha = senha;
        this.role = role;
    }

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}