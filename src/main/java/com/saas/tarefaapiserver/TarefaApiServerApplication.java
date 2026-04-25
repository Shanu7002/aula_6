package com.saas.tarefaapiserver;

import com.saas.tarefaapiserver.model.Tarefa;
import com.saas.tarefaapiserver.repository.TarefaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * CLASSE PRINCIPAL da aplicacao Spring Boot.
 *
 * Esta e a classe que inicia toda a aplicacao. Ao executar o metodo main(),
 * o Spring Boot automaticamente:
 * 1. Cria o container IoC (Inversao de Controle)
 * 2. Escaneia o pacote atual e sub-pacotes buscando @Component, @Service, @Repository, @Controller
 * 3. Configura automaticamente o banco de dados, servidor web, JPA, etc.
 * 4. Inicia o servidor Tomcat embutido na porta configurada (8080)
 */

// @SpringBootApplication = Anotacao principal que combina 3 anotacoes:
// 1. @Configuration - permite definir beans com @Bean nesta classe
// 2. @EnableAutoConfiguration - ativa a configuracao automatica do Spring Boot
//    (detecta as dependencias no pom.xml e configura tudo automaticamente)
// 3. @ComponentScan - escaneia este pacote e sub-pacotes em busca de componentes Spring
//    (encontra automaticamente @Controller, @Service, @Repository, @Component, etc.)
@SpringBootApplication
public class TarefaApiServerApplication {

    // Metodo main() - ponto de entrada da aplicacao Java.
    // SpringApplication.run() inicializa todo o framework Spring Boot.
    public static void main(String[] args) {
        SpringApplication.run(TarefaApiServerApplication.class, args);
    }

    // @Bean = Registra o retorno deste metodo como um bean gerenciado pelo Spring.
    //
    // CommandLineRunner = Interface funcional do Spring Boot.
    // Qualquer bean do tipo CommandLineRunner e executado automaticamente
    // APOS a aplicacao inicializar completamente.
    //
    // Uso aqui: inserir dados de exemplo no banco de dados ao iniciar.
    // Como usamos H2 em memoria com ddl-auto=create-drop, o banco e criado
    // vazio a cada inicializacao, entao esses dados sao inseridos toda vez.
    //
    // O Spring injeta automaticamente o TarefaRepository como parametro
    // porque ele e um bean gerenciado (Injecao de Dependencia).
    @Bean
    CommandLineRunner initDatabase(TarefaRepository repository) {
        // Expressao lambda que implementa o metodo run() do CommandLineRunner.
        // O parametro "args" sao os argumentos da linha de comando (geralmente vazio).
        return args -> {
            // repository.save() insere cada tarefa no banco de dados.
            // Como as tarefas nao tem ID, o JPA executa INSERT (nao UPDATE).
            // O @PrePersist na entidade Tarefa preenche a dataCriacao automaticamente.
            repository.save(new Tarefa("Estudar Spring Boot", "Revisar os conceitos de Spring Boot e Spring Data JPA"));
            repository.save(new Tarefa("Fazer exercicio de JPA", "Completar o exercicio pratico sobre mapeamento de entidades"));
            repository.save(new Tarefa("Configurar banco H2", "Testar a configuracao do banco de dados em memoria H2"));
            repository.save(new Tarefa("Criar API REST", "Implementar endpoints REST para o CRUD de tarefas"));
            repository.save(new Tarefa("Testar endpoints", "Usar Postman ou curl para testar todos os endpoints da API"));

            System.out.println("=== 5 tarefas de exemplo inseridas com sucesso! ===");
        };
    }
}
