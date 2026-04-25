package com.saas.tarefaapiserver;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.saas.tarefaapiserver.model.Tarefa;
import com.saas.tarefaapiserver.model.Usuario;
import com.saas.tarefaapiserver.repository.TarefaRepository;
import com.saas.tarefaapiserver.repository.UsuarioRepository;

@SpringBootApplication
public class TarefaApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TarefaApiServerApplication.class, args);
    }

    /**
     * Cria 5 tarefas de exemplo no banco (já existia antes).
     *
     * O if (repository.count() == 0) garante que os dados só são inseridos
     * na PRIMEIRA execução. Nas próximas, como usamos ddl-auto=update,
     * os dados já existem no Postgres e não precisam ser reinseridos.
     */
    @Bean
    CommandLineRunner initTarefas(TarefaRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Tarefa("Estudar Spring Boot", "Revisar conceitos"));
                repository.save(new Tarefa("Fazer exercício de JPA", "Mapeamento de entidades"));
                repository.save(new Tarefa("Configurar PostgreSQL", "Conectar via Docker"));
                repository.save(new Tarefa("Criar API REST", "Implementar CRUD"));
                repository.save(new Tarefa("Testar endpoints", "Usar Postman ou curl"));
                System.out.println("=== 5 tarefas de exemplo inseridas! ===");
            } else {
                System.out.println("=== Tarefas já existem no banco (" + repository.count() + ") ===");
            }
        };
    }

    /**
     * Cria 2 usuários de exemplo no banco, com senha criptografada em BCrypt.
     *
     * Mesmo padrão: só insere se a tabela estiver vazia.
     * Isso evita erro de username duplicado na segunda execução.
     */
    @Bean
    CommandLineRunner initUsuarios(UsuarioRepository repository, PasswordEncoder encoder) {
        return args -> {
            if (repository.count() == 0) {
                // encoder.encode("senha") → gera um hash BCrypt (~60 caracteres)
                repository.save(new Usuario(
                    "admin",
                    encoder.encode("admin123"),
                    "ADMIN"
                ));

                repository.save(new Usuario(
                    "joao",
                    encoder.encode("joao123"),
                    "USUARIO"
                ));

                System.out.println("=== 2 usuários de exemplo inseridos! ===");
                System.out.println("    admin / admin123");
                System.out.println("    joao  / joao123");
            } else {
                System.out.println("=== Usuários já existem no banco (" + repository.count() + ") ===");
            }
        };
    }
}