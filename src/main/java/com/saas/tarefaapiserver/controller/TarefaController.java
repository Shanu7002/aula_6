package com.saas.tarefaapiserver.controller;

import com.saas.tarefaapiserver.model.Tarefa;
import com.saas.tarefaapiserver.service.TarefaService;
// Importacoes do Swagger/OpenAPI para documentacao dos endpoints
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER REST - Camada de apresentacao (Presentation Layer).
 *
 * O Controller e o ponto de entrada da aplicacao para requisicoes HTTP.
 * Ele recebe as requisicoes, delega o processamento ao Service,
 * e retorna as respostas (geralmente em formato JSON).
 *
 * Fluxo completo de uma requisicao:
 *   Cliente HTTP (browser/Postman) -> Controller -> Service -> Repository -> Banco
 *   Banco -> Repository -> Service -> Controller -> Resposta JSON -> Cliente
 */

// @RestController = Combinacao de @Controller + @ResponseBody.
// - @Controller: marca a classe como um controller Spring MVC
// - @ResponseBody: indica que o retorno dos metodos sera serializado
//   diretamente no corpo da resposta HTTP (em JSON, por padrao).
// Ou seja, quando um metodo retorna um objeto Tarefa, o Spring
// converte automaticamente para JSON usando a biblioteca Jackson.
@RestController

// @RequestMapping("/api/tarefas") = Define o caminho BASE para todos os endpoints deste controller.
// Todos os metodos desta classe terao URLs que comecam com "/api/tarefas".
// Exemplo: GET http://localhost:8080/api/tarefas
@RequestMapping("/api/tarefas")

// @Tag = Anotacao do Swagger que agrupa os endpoints sob um nome e descricao.
// No Swagger UI, todos os endpoints deste controller aparecerao sob o grupo "Tarefas".
@Tag(name = "Tarefas", description = "Endpoints para gerenciamento de tarefas (CRUD completo)")
public class TarefaController {

    // Injecao de dependencia do Service (mesmo padrao explicado no TarefaService).
    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    // ========== ENDPOINTS GET (Leitura de dados) ==========

    // @Operation = Anotacao do Swagger que documenta um endpoint especifico.
    // - summary: titulo curto exibido na lista de endpoints
    // - description: texto detalhado exibido ao expandir o endpoint
    //
    // @ApiResponse = Documenta os possiveis codigos HTTP de resposta.
    // - responseCode: codigo HTTP (200, 201, 204, 404, etc.)
    // - description: explica quando esse codigo e retornado

    // @GetMapping = Mapeia requisicoes HTTP GET para este metodo.
    // Como nao tem valor entre parenteses, responde no caminho base: GET /api/tarefas
    // Retorna a lista de todas as tarefas em formato JSON.
    //
    // Exemplo de resposta JSON:
    // [{"id":1,"titulo":"Estudar Spring","descricao":"...","concluida":false,"dataCriacao":"..."}]
    @Operation(
            summary = "Listar todas as tarefas",
            description = "Retorna uma lista com todas as tarefas cadastradas no banco de dados, " +
                    "independente do status (concluida ou pendente). " +
                    "A lista pode estar vazia se nao houver tarefas cadastradas."
    )
    @ApiResponse(responseCode = "200", description = "Lista de tarefas retornada com sucesso")
    @GetMapping
    public List<Tarefa> listarTodas() {
        return service.listarTodas();
    }

    // @GetMapping("/{id}") = Responde em GET /api/tarefas/{id}
    // O {id} e uma VARIAVEL DE CAMINHO (path variable).
    // Exemplo: GET /api/tarefas/3 -> id = 3
    //
    // @PathVariable = Extrai o valor da URL e injeta no parametro do metodo.
    // O nome do parametro "id" deve coincidir com o {id} da URL.
    //
    // @Parameter = Anotacao do Swagger que documenta um parametro do endpoint.
    // Aparece na pagina do Swagger com a descricao e exemplo de uso.
    @Operation(
            summary = "Buscar tarefa por ID",
            description = "Retorna uma unica tarefa com base no ID informado na URL. " +
                    "Se a tarefa nao existir, retorna erro 500 com mensagem descritiva."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Tarefa nao encontrada com o ID informado")
    })
    @GetMapping("/{id}")
    public Tarefa buscarPorId(
            @Parameter(description = "ID da tarefa a ser buscada", example = "1")
            @PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // GET /api/tarefas/pendentes -> lista apenas tarefas nao concluidas.
    @Operation(
            summary = "Listar tarefas pendentes",
            description = "Retorna apenas as tarefas que ainda NAO foram concluidas (concluida = false), " +
                    "ordenadas pela data de criacao mais recente primeiro. " +
                    "Utiliza uma @Query JPQL customizada no Repository."
    )
    @ApiResponse(responseCode = "200", description = "Lista de tarefas pendentes retornada com sucesso")
    @GetMapping("/pendentes")
    public List<Tarefa> listarPendentes() {
        return service.listarPendentes();
    }

    // @RequestParam = Extrai parametros da QUERY STRING da URL.
    // Exemplo: GET /api/tarefas/pesquisa?titulo=spring
    // O valor "spring" sera injetado no parametro "titulo".
    //
    // Diferenca entre @PathVariable e @RequestParam:
    // - @PathVariable: valor faz parte do CAMINHO da URL -> /api/tarefas/3
    // - @RequestParam: valor vem como PARAMETRO apos o "?" -> /api/tarefas/pesquisa?titulo=spring
    @Operation(
            summary = "Pesquisar tarefas por titulo",
            description = "Busca tarefas cujo titulo contenha o texto informado. " +
                    "A busca e parcial (LIKE) e case-insensitive (ignora maiusculas/minusculas). " +
                    "Exemplo: pesquisar 'spring' encontra 'Estudar Spring Boot'."
    )
    @ApiResponse(responseCode = "200", description = "Resultado da pesquisa retornado com sucesso")
    @GetMapping("/pesquisa")
    public List<Tarefa> pesquisar(
            @Parameter(description = "Texto para buscar no titulo das tarefas", example = "Spring")
            @RequestParam String titulo) {
        return service.pesquisarPorTitulo(titulo);
    }

    // ========== ENDPOINT POST (Criacao de dados) ==========

    // @PostMapping = Mapeia requisicoes HTTP POST para este metodo.
    // POST /api/tarefas -> cria uma nova tarefa.
    //
    // @RequestBody = Indica que o corpo da requisicao HTTP (JSON) deve ser
    // convertido automaticamente em um objeto Tarefa (desserializacao).
    // O Jackson le o JSON e preenche os atributos do objeto usando os setters.
    //
    // Exemplo de JSON enviado no corpo do POST:
    // {"titulo": "Nova tarefa", "descricao": "Descricao da tarefa"}
    //
    // ResponseEntity = Permite controlar o codigo de status HTTP da resposta.
    // HttpStatus.CREATED = codigo 201, que indica que um recurso foi criado com sucesso.
    // (O padrao seria 200 OK, mas 201 Created e mais correto para criacao)
    @Operation(
            summary = "Criar nova tarefa",
            description = "Cria uma nova tarefa no banco de dados. " +
                    "Envie apenas 'titulo' e 'descricao' no JSON. " +
                    "Os campos 'id', 'concluida' e 'dataCriacao' sao preenchidos automaticamente. " +
                    "Retorna a tarefa criada com todos os campos preenchidos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos no corpo da requisicao")
    })
    @PostMapping
    public ResponseEntity<Tarefa> criar(@RequestBody Tarefa tarefa) {
        Tarefa novaTarefa = service.criar(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaTarefa);
    }

    // ========== ENDPOINT PUT (Atualizacao de dados) ==========

    // @PutMapping("/{id}") = Mapeia requisicoes HTTP PUT para este metodo.
    // PUT /api/tarefas/3 -> atualiza a tarefa com id = 3.
    //
    // Combina @PathVariable (para pegar o ID da URL)
    // com @RequestBody (para pegar os novos dados do corpo da requisicao).
    @Operation(
            summary = "Atualizar tarefa existente",
            description = "Atualiza os dados de uma tarefa existente. " +
                    "Informe o ID na URL e envie os novos valores no corpo da requisicao (JSON). " +
                    "Campos que podem ser atualizados: 'titulo', 'descricao' e 'concluida'. " +
                    "O campo 'dataCriacao' nao e alterado (definido como updatable=false na entidade)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Tarefa nao encontrada com o ID informado")
    })
    @PutMapping("/{id}")
    public Tarefa atualizar(
            @Parameter(description = "ID da tarefa a ser atualizada", example = "1")
            @PathVariable Long id,
            @RequestBody Tarefa tarefa) {
        return service.atualizar(id, tarefa);
    }

    // ========== ENDPOINT DELETE (Remocao de dados) ==========

    // @DeleteMapping("/{id}") = Mapeia requisicoes HTTP DELETE.
    // DELETE /api/tarefas/3 -> deleta a tarefa com id = 3.
    //
    // ResponseEntity<Void> = Resposta sem corpo (a tarefa foi deletada, nao ha nada para retornar).
    // noContent() = codigo HTTP 204 (No Content), que indica sucesso sem corpo de resposta.
    // Esse e o codigo padrao correto para operacoes de exclusao.
    @Operation(
            summary = "Deletar tarefa",
            description = "Remove permanentemente uma tarefa do banco de dados com base no ID informado. " +
                    "A operacao e irreversivel. Retorna 204 (sem corpo) em caso de sucesso."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Tarefa nao encontrada com o ID informado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da tarefa a ser deletada", example = "1")
            @PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
