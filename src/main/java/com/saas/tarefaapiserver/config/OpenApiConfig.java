package com.saas.tarefaapiserver.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * CONFIGURACAO DO SWAGGER / OPENAPI.
 *
 * O QUE E SWAGGER?
 * Swagger e uma ferramenta que gera DOCUMENTACAO INTERATIVA para APIs REST.
 * Ele cria uma pagina web onde voce pode:
 * - Ver todos os endpoints da API (URLs, metodos HTTP, parametros)
 * - Testar os endpoints diretamente no navegador (sem precisar de Postman/curl)
 * - Ver os modelos de dados (quais campos os objetos JSON possuem)
 *
 * O QUE E OPENAPI?
 * OpenAPI e a ESPECIFICACAO (padrao) que define como documentar APIs REST.
 * Swagger e a FERRAMENTA que implementa essa especificacao.
 * Ou seja: OpenAPI = o padrao, Swagger = a ferramenta que segue o padrao.
 *
 * COMO FUNCIONA?
 * A biblioteca SpringDoc analisa automaticamente os controllers, endpoints,
 * parametros e modelos da aplicacao e gera a documentacao.
 * As anotacoes @Operation, @ApiResponse, @Schema permitem personalizar
 * e enriquecer essa documentacao gerada automaticamente.
 *
 * ACESSE: http://localhost:8080/swagger-ui/index.html
 */

// @Configuration = Classe de configuracao do Spring (mesma explicacao do CorsConfig).
@Configuration
public class OpenApiConfig {

    // @Bean = Cria um bean OpenAPI que o SpringDoc usa para gerar a documentacao.
    // Aqui configuramos as informacoes gerais da API (titulo, descricao, versao, etc.).
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // Info = Informacoes gerais sobre a API exibidas no topo da pagina do Swagger.
                .info(new Info()
                        .title("Tarefa API - Gerenciador de Tarefas")
                        .description("""
                                API REST para gerenciamento de tarefas, desenvolvida como exemplo \
                                didatico para a disciplina de SaaS.

                                Esta API demonstra os conceitos de:
                                - **Spring Data JPA** para persistencia de dados
                                - **Spring Web** para criacao de endpoints REST
                                - **H2 Database** como banco de dados em memoria
                                - **Arquitetura em camadas** (Controller → Service → Repository)

                                ### Como testar
                                Use os endpoints abaixo para interagir com a API. \
                                Clique em "Try it out" para testar cada operacao.""")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Disciplina SaaS")
                                .email("professor@exemplo.com")))
                // Servers = Lista de servidores onde a API esta disponivel.
                // O Swagger usa isso como URL base para as requisicoes de teste.
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento")
                ));
    }
}
