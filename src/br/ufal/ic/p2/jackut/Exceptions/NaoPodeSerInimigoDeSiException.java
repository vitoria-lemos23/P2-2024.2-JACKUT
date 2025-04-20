package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando um usuário tenta se adicionar como próprio inimigo.
 * <p>
 * Representa uma violação das regras de relacionamento social que impedem auto-inimizade.
 *
 * <p><b>Cenários comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar próprio login na lista de inimigos</li>
 *   <li>Erro de lógica na seleção do usuário alvo</li>
 *   <li>Falha na validação de entrada em interfaces</li>
 * </ul>
 *
 * <p><b>Ações recomendadas:</b>
 * <ul>
 *   <li>Validar identidade do usuário alvo antes da operação</li>
 *   <li>Implementar checagem de auto-referência no fluxo de inimizades</li>
 *   <li>Exibir mensagem clara em formulários de relacionamento</li>
 * </ul>
 *
 * @see IGerenciadorAmizades#adicionarInimigo(String, String)
 * @see NaoPodeSerFaDeSiException Exceção similar para auto-idolatria
 * @see NaoPodePaquerarASiMesmoException Exceção similar para auto-paquera
 */
public class NaoPodeSerInimigoDeSiException extends Exception {

    /**
     * Constrói a exceção com mensagem padrão que descreve a restrição de auto-inimizade.
     * <p>
     * Mensagem fixa: "Usuário não pode ser inimigo de si mesmo."
     */
    public NaoPodeSerInimigoDeSiException() {
        super("Usuário não pode ser inimigo de si mesmo.");
    }
}