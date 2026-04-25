package com.saas.tarefaapiserver.service;

import com.saas.tarefaapiserver.model.Tarefa;
import com.saas.tarefaapiserver.repository.TarefaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SERVICE - Camada de logica de negocios (Business Logic Layer).
 *
 * O Service fica ENTRE o Controller e o Repository:
 *   Controller -> Service -> Repository -> Banco de Dados
 *
 * Responsabilidades do Service:
 * - Conter a logica de negocios (regras, validacoes, calculos)
 * - Coordenar chamadas ao Repository
 * - Tratar erros e excecoes
 *
 * POR QUE separar Controller e Service?
 * - O Controller so cuida de receber requisicoes HTTP e devolver respostas
 * - O Service contem a logica que pode ser REUTILIZADA em varios lugares
 * - Facilita os testes: voce pode testar a logica sem precisar de HTTP
 */

// @Service = Marca esta classe como um componente Spring da camada de servico.
// E uma especializacao de @Component. O Spring cria automaticamente uma instancia
// (bean) desta classe e a gerencia no container de inversao de controle (IoC).
@Service
public class TarefaService {

    // INJECAO DE DEPENDENCIA via construtor.
    // O Spring injeta automaticamente a implementacao do TarefaRepository aqui.
    // "final" garante que o repository nao pode ser alterado depois da criacao.
    //
    // POR QUE Injecao de Dependencia?
    // - A classe TarefaService NAO cria o repository (nao faz "new TarefaRepository()")
    // - Quem fornece (injeta) a dependencia e o Spring
    // - Isso facilita testes e desacopla as camadas
    private final TarefaRepository repository;

    // Quando uma classe tem UM UNICO construtor, o Spring automaticamente
    // injeta as dependencias sem precisar da anotacao @Autowired.
    // (Com mais de um construtor, voce precisa marcar um deles com @Autowired)
    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    // Retorna todas as tarefas do banco de dados.
    // Internamente, o findAll() executa: SELECT * FROM tarefas
    public List<Tarefa> listarTodas() {
        return repository.findAll();
    }

    // Busca uma tarefa pelo ID.
    // findById() retorna um Optional<Tarefa> (pode ou nao ter resultado).
    // .orElseThrow() lanca uma excecao se a tarefa nao for encontrada.
    // Isso evita retornar null, que poderia causar NullPointerException.
    public Tarefa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa nao encontrada com id: " + id));
    }

    // Salva uma nova tarefa no banco de dados.
    // O metodo save() do JPA faz INSERT quando o objeto nao tem ID,
    // ou UPDATE quando o objeto ja tem um ID existente.
    public Tarefa criar(Tarefa tarefa) {
        return repository.save(tarefa);
    }

    // Atualiza uma tarefa existente.
    // Primeiro busca a tarefa pelo ID (se nao encontrar, lanca excecao).
    // Depois atualiza os campos e salva novamente.
    // Como a tarefa ja tem ID, o save() executa um UPDATE (nao INSERT).
    public Tarefa atualizar(Long id, Tarefa tarefaAtualizada) {
        Tarefa tarefa = buscarPorId(id);
        tarefa.setTitulo(tarefaAtualizada.getTitulo());
        tarefa.setDescricao(tarefaAtualizada.getDescricao());
        tarefa.setConcluida(tarefaAtualizada.getConcluida());
        return repository.save(tarefa);
    }

    // Deleta uma tarefa pelo ID.
    // Primeiro verifica se a tarefa existe (buscarPorId lanca excecao se nao existir).
    // Depois remove do banco de dados.
    public void deletar(Long id) {
        Tarefa tarefa = buscarPorId(id);
        repository.delete(tarefa);
    }

    // Pesquisa tarefas por titulo usando busca parcial e case-insensitive.
    // Usa o Query Method do Repository (o Spring gera o SQL automaticamente).
    public List<Tarefa> pesquisarPorTitulo(String titulo) {
        return repository.findByTituloContainingIgnoreCase(titulo);
    }

    // Lista tarefas pendentes ordenadas por data de criacao (mais recentes primeiro).
    // Usa a @Query JPQL customizada definida no Repository.
    public List<Tarefa> listarPendentes() {
        return repository.buscarPendentesOrdenadas();
    }
}
