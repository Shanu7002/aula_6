package com.saas.tarefaapiserver.repository;

import com.saas.tarefaapiserver.model.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITORY - Camada de acesso a dados (Data Access Layer).
 *
 * O Repository e responsavel por toda comunicacao com o banco de dados.
 * Com o Spring Data JPA, NAO precisamos escrever SQL manualmente para operacoes basicas.
 * O Spring gera automaticamente as implementacoes em tempo de execucao.
 *
 * IMPORTANTE: Isso e uma INTERFACE, nao uma classe!
 * O Spring Data JPA cria a implementacao automaticamente em tempo de execucao.
 * Voce nunca precisa escrever "class TarefaRepositoryImpl implements TarefaRepository".
 */

// @Repository = Marca esta interface como um componente Spring da camada de persistencia.
// Na pratica, o Spring Data JPA ja detecta automaticamente interfaces que estendem JpaRepository,
// entao essa anotacao e opcional aqui, mas e uma boa pratica para deixar claro o proposito.
@Repository

// JpaRepository<Tarefa, Long> = Interface generica do Spring Data JPA.
// Primeiro parametro: a ENTIDADE que este repository gerencia (Tarefa)
// Segundo parametro: o TIPO do ID da entidade (Long)
//
// Ao estender JpaRepository, voce GANHA DE GRACA todos esses metodos:
// - findAll()        -> SELECT * FROM tarefas
// - findById(id)     -> SELECT * FROM tarefas WHERE id = ?
// - save(tarefa)     -> INSERT INTO tarefas (...) ou UPDATE tarefas SET ...
// - delete(tarefa)   -> DELETE FROM tarefas WHERE id = ?
// - count()          -> SELECT COUNT(*) FROM tarefas
// - existsById(id)   -> SELECT COUNT(*) FROM tarefas WHERE id = ?
// E muitos outros! Tudo sem escrever uma unica linha de SQL.
public interface TarefaRepository extends JpaRepository<Tarefa, Long> {

    // ========== QUERY METHODS (Metodos de Consulta Derivados) ==========
    // O Spring Data JPA analisa o NOME do metodo e gera o SQL automaticamente.
    // Isso se chama "Query Derivation" (derivacao de consulta).

    // findByConcluidaFalse() -> O Spring analisa o nome e gera:
    // SELECT * FROM tarefas WHERE concluida = false
    //
    // Regras de nomenclatura:
    // - "findBy" = inicia uma busca (SELECT)
    // - "Concluida" = nome do atributo da entidade
    // - "False" = valor do filtro (WHERE concluida = false)
    List<Tarefa> findByConcluidaFalse();

    // findByTituloContainingIgnoreCase(String titulo) -> O Spring gera:
    // SELECT * FROM tarefas WHERE LOWER(titulo) LIKE LOWER('%titulo%')
    //
    // Decomposicao do nome:
    // - "findBy" = inicia uma busca
    // - "Titulo" = nome do atributo
    // - "Containing" = operador LIKE com % nos dois lados (busca parcial)
    // - "IgnoreCase" = ignora maiusculas/minusculas (case-insensitive)
    //
    // Exemplo: pesquisar("spring") encontra "Estudar Spring Boot", "SPRING", "spring data"
    List<Tarefa> findByTituloContainingIgnoreCase(String titulo);

    // ========== @Query - Consulta JPQL Customizada ==========
    // Quando o nome do metodo ficaria muito longo ou complexo,
    // podemos escrever a consulta manualmente com @Query.
    //
    // JPQL (Java Persistence Query Language) e parecido com SQL,
    // mas usa os nomes das CLASSES e ATRIBUTOS Java em vez de tabelas e colunas.
    // Aqui "Tarefa" e o nome da classe (nao da tabela), e "t.concluida" e o atributo Java.
    //
    // Esta query busca tarefas pendentes (concluida = false)
    // e ordena pela data de criacao mais recente primeiro (DESC = descendente).
    @Query("SELECT t FROM Tarefa t WHERE t.concluida = false ORDER BY t.dataCriacao DESC")
    List<Tarefa> buscarPendentesOrdenadas();
}
