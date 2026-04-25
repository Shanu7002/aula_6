package com.saas.tarefaapiserver.model;

// Importa as anotacoes do JPA (Jakarta Persistence API)
// O JPA e a especificacao Java para mapeamento objeto-relacional (ORM),
// ou seja, ele permite mapear classes Java para tabelas no banco de dados.
import jakarta.persistence.*;

// Importa a anotacao @Schema do Swagger/OpenAPI para documentar o modelo de dados.
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * ENTIDADE JPA - Representa a tabela "tarefas" no banco de dados.
 *
 * Cada instancia desta classe corresponde a uma LINHA na tabela.
 * Cada atributo da classe corresponde a uma COLUNA na tabela.
 *
 * O JPA/Hibernate traduz automaticamente operacoes nesta classe
 * em comandos SQL (INSERT, SELECT, UPDATE, DELETE).
 */

// @Schema = Anotacao do Swagger/OpenAPI que documenta este modelo de dados.
// No Swagger UI, esta descricao aparece na secao "Schemas" no final da pagina,
// e tambem nos exemplos de requisicao/resposta dos endpoints.
@Schema(description = "Entidade que representa uma tarefa no sistema")

// @Entity = Marca esta classe como uma entidade JPA.
// Isso diz ao Hibernate: "esta classe representa uma tabela no banco de dados".
// Sem essa anotacao, o JPA ignora completamente a classe.
@Entity

// @Table(name = "tarefas") = Define o nome da tabela no banco de dados.
// Se nao colocar @Table, o Hibernate usa o nome da classe ("Tarefa") como nome da tabela.
// Aqui estamos explicitamente dizendo que a tabela se chama "tarefas".
@Table(name = "tarefas")
public class Tarefa {

    // @Id = Marca este campo como a CHAVE PRIMARIA (PRIMARY KEY) da tabela.
    // Toda entidade JPA OBRIGATORIAMENTE precisa ter um @Id.
    @Id

    // @GeneratedValue(strategy = GenerationType.IDENTITY) = O banco de dados
    // gera o valor do ID automaticamente (auto-incremento).
    // IDENTITY significa que o proprio banco controla a sequencia (1, 2, 3...).
    // Outros tipos: AUTO (JPA decide), SEQUENCE (usa sequence do banco), TABLE (usa tabela auxiliar).
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    // @Schema para documentar este campo no Swagger.
    // - description: texto explicativo do campo
    // - example: valor de exemplo exibido no Swagger UI
    // - accessMode = READ_ONLY: indica que este campo so aparece nas RESPOSTAS,
    //   nao deve ser enviado pelo usuario nas requisicoes (o banco gera automaticamente)
    @Schema(description = "ID unico da tarefa (gerado automaticamente)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    // @Column = Configura como este campo sera mapeado para a coluna no banco.
    // nullable = false -> a coluna NAO pode ser NULL (equivale a NOT NULL no SQL)
    // length = 150 -> tamanho maximo de 150 caracteres (equivale a VARCHAR(150))
    // Se nao colocar @Column, o JPA mapeia com as configuracoes padrao (nullable, VARCHAR(255)).
    @Column(nullable = false, length = 150)
    @Schema(description = "Titulo da tarefa (obrigatorio, maximo 150 caracteres)", example = "Estudar Spring Boot")
    private String titulo;

    // columnDefinition = "TEXT" -> forca o tipo da coluna para TEXT no banco,
    // que permite armazenar textos muito grandes (sem limite de 255 caracteres).
    @Column(columnDefinition = "TEXT")
    @Schema(description = "Descricao detalhada da tarefa (opcional)", example = "Revisar os conceitos de Spring Data JPA e Hibernate")
    private String descricao;

    // nullable = false -> nao pode ser NULL no banco.
    // O "= false" no Java define o valor padrao quando criamos um objeto novo.
    @Column(nullable = false)
    @Schema(description = "Indica se a tarefa foi concluida (padrao: false)", example = "false")
    private Boolean concluida = false;

    // updatable = false -> este campo NAO pode ser alterado depois de inserido.
    // Ou seja, o Hibernate nunca inclui esta coluna em um UPDATE SQL.
    // Isso faz sentido para dataCriacao, que so e definida uma vez.
    @Column(nullable = false, updatable = false)
    @Schema(description = "Data e hora de criacao da tarefa (preenchida automaticamente)", example = "2026-03-25T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dataCriacao;

    // CONSTRUTOR VAZIO (padrao) - OBRIGATORIO para o JPA!
    // O Hibernate precisa dele para criar instancias da classe via reflexao
    // quando busca dados do banco de dados.
    public Tarefa() {
    }

    // Construtor de conveniencia para facilitar a criacao de tarefas no codigo.
    // Usado no CommandLineRunner para inserir dados de exemplo.
    public Tarefa(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    // @PrePersist = Metodo de CALLBACK do ciclo de vida da entidade.
    // E executado AUTOMATICAMENTE pelo JPA ANTES de inserir (persist) no banco.
    // Aqui usamos para preencher a dataCriacao com a data/hora atual.
    // Outros callbacks disponiveis: @PreUpdate, @PostPersist, @PostUpdate, @PreRemove, etc.
    //
    // POR QUE inicializar "concluida" aqui?
    // Quando o JSON enviado pelo cliente nao tem o campo "concluida" (ou envia null),
    // o Jackson chama setConcluida(null), sobrescrevendo o "= false" do campo.
    // O @PrePersist garante que o valor sera "false" no banco MESMO que chegue null,
    // porque e executado pelo JPA logo antes de gerar o INSERT.
    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        if (this.concluida == null) {
            this.concluida = false;
        }
    }

    // ========== GETTERS E SETTERS ==========
    // O JPA e frameworks como Jackson (conversao JSON) precisam dos getters/setters
    // para acessar e modificar os atributos da classe.
    // Jackson usa os getters para converter o objeto em JSON (serializacao)
    // e os setters para converter JSON em objeto (desserializacao).

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
