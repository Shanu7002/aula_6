package com.saas.tarefaapiserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * Bean responsável por criptografar senhas e validar hashes.
     * Usamos BCrypt — padrão da indústria.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define as regras de segurança da aplicação.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF para simplificar.
            // Em uma API REST o cliente controla os tokens/cookies, então não precisamos
            // da proteção CSRF (que é importante em apps com formulários HTML tradicionais).
            // Desabilitar aqui permite que o Swagger consiga mandar POST/PUT/DELETE sem
            // precisar de token CSRF.
            .csrf(csrf -> csrf.disable())

            // Regras de autorização — A ORDEM IMPORTA.
            .authorizeHttpRequests(auth -> auth
                // A tela de login e os arquivos estáticos (CSS) precisam ser públicos
                .requestMatchers("/login", "/css/**").permitAll()
                // Qualquer outra rota (Swagger, /api/tarefas, etc.) exige login
                .anyRequest().authenticated()
            )

            // Habilita o login via formulário HTML, apontando para NOSSA página
            .formLogin(form -> form
                .loginPage("/login")                    // URL da nossa página de login
                .defaultSuccessUrl("/swagger-ui.html", true)  // para onde ir após login OK
                .permitAll()
            )

            // Habilita o logout (POST /logout) e redireciona para /login?logout
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}