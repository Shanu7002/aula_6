package com.saas.tarefaapiserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CONFIGURACAO DE CORS (Cross-Origin Resource Sharing).
 *
 * O QUE E CORS?
 * Por padrao, navegadores bloqueiam requisicoes feitas de um dominio/porta
 * para outro dominio/porta diferente. Isso e uma medida de seguranca.
 *
 * Exemplo do problema:
 * - Nosso CLIENT roda em http://localhost:8081
 * - Nosso SERVER roda em http://localhost:8080
 * - Como as portas sao diferentes, o navegador BLOQUEIA as requisicoes do client para o server.
 *
 * A configuracao de CORS diz ao servidor: "aceite requisicoes vindas deste(s) endereco(s)".
 * Sem essa configuracao, a aplicacao client NAO conseguiria consumir a API.
 *
 * NOTA: Isso so e necessario quando o client e uma aplicacao web rodando no navegador.
 * Ferramentas como Postman e curl NAO sao afetadas pelo CORS.
 */

// @Configuration = Marca esta classe como uma classe de configuracao do Spring.
// O Spring processa classes @Configuration durante a inicializacao da aplicacao
// e registra os @Bean definidos nelas no container de inversao de controle (IoC).
@Configuration
public class CorsConfig {

    // @Bean = Marca este metodo como um produtor de bean.
    // O objeto retornado pelo metodo sera gerenciado pelo Spring.
    // WebMvcConfigurer permite personalizar a configuracao do Spring MVC.
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        // Classe anonima que implementa WebMvcConfigurer.
        // Sobrescrevemos apenas o metodo addCorsMappings para configurar o CORS.
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    // "/api/**" = aplica CORS para todas as URLs que comecam com /api/
                    // O ** significa "qualquer coisa depois", incluindo sub-caminhos.
                    .addMapping("/api/**")

                    // Permite requisicoes APENAS do endereco do nosso client.
                    // Em producao, voce colocaria o dominio real do seu frontend.
                    .allowedOrigins("http://localhost:8081")

                    // Permite esses metodos HTTP especificos.
                    // Por padrao, so GET e permitido.
                    .allowedMethods("GET", "POST", "PUT", "DELETE")

                    // Permite todos os headers na requisicao.
                    // Headers comuns: Content-Type, Authorization, Accept, etc.
                    .allowedHeaders("*");
            }
        };
    }
}
