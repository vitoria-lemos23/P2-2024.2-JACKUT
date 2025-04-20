package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando um usuário tenta estabelecer relação de paquera consigo mesmo.
 * <p>
 * Representa uma violação das regras de relacionamento social que impedem auto-paquera.
 *
 * <p><b>Cenários comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar próprio login na lista de paqueras</li>
 *   <li>Erro de seleção de usuário alvo em interfaces</li>
 *   <li>Falha na validação de entrada de dados</li>
 * </ul>
 *
 * <p><b>Ações recomendadas:</b>
 * <ul>
 *   <li>Validar identidade do usuário alvo antes da operação</li>
 *   <li>Implementar checagem de auto-referência na lógica de paqueras</li>
 *   <li>Exibir mensagem preventiva em formulários de relacionamento</li>
 * </ul>
 *
 * @see IGerenciadorAmizades#adicionarPaquera(String, String)
 * @see InimigoDeSiException Exceção similar para auto-inimizade
 * @see NaoPodeSerFaDeSiException Exceção similar para auto-idolatria
 */
public class PaqueraDeSiException extends Exception {

    /**
     * Constrói a exceção com mensagem padrão que descreve a restrição de auto-paquera.
     * <p>
     * Mensagem fixa: "Usuário não pode ser paquera de si mesmo."
     */
    public PaqueraDeSiException() {
        super("Usuário não pode ser paquera de si mesmo.");
    }
}