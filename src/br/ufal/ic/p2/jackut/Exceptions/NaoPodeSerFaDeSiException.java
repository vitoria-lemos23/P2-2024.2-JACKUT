package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando um usuário tenta se adicionar como próprio fã/ídolo.
 * <p>
 * Representa uma violação das regras de relacionamento social que impedem auto-idolatria.
 *
 * <p><b>Cenários comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar próprio login na lista de fãs/ídolos</li>
 *   <li>Erro de lógica na seleção do usuário alvo</li>
 *   <li>Falha na validação de entrada em interfaces de usuário</li>
 * </ul>
 *
 * <p><b>Ações recomendadas:</b>
 * <ul>
 *   <li>Validar identidade do usuário alvo antes da operação</li>
 *   <li>Implementar checagem de auto-referência no fluxo de adição de ídolos</li>
 *   <li>Exibir alerta claro em formulários de relacionamento</li>
 * </ul>
 *
 * @see IGerenciadorAmizades#adicionarIdolo(String, String)
 * @see InimigoDeSiException Exceção similar para auto-inimizade
 */
public class NaoPodeSerFaDeSiException extends Exception {

    /**
     * Constrói a exceção com mensagem padrão que descreve a restrição de auto-idolatria.
     * <p>
     * Mensagem fixa: "Usuário não pode ser fã de si mesmo."
     */
    public NaoPodeSerFaDeSiException() {
        super("Usuário não pode ser fã de si mesmo.");
    }
}