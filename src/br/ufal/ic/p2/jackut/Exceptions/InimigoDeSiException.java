package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades;

/**
 * Exceção lançada quando um usuário tenta se adicionar como próprio inimigo.
 * <p>
 * Representa uma violação das regras de relacionamento social do sistema que impedem
 * auto-inimizade.
 *
 * <p><b>Cenários comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar o próprio login na lista de inimigos</li>
 *   <li>Erro lógico na seleção do usuário alvo</li>
 *   <li>Falha na validação de entrada de dados</li>
 * </ul>
 *
 * <p><b>Ações recomendadas:</b>
 * <ul>
 *   <li>Verificar a lógica de seleção de usuário alvo</li>
 *   <li>Implementar validação de identidade antes da operação</li>
 *   <li>Exibir mensagem clara de erro ao usuário final</li>
 * </ul>
 *
 */
public class InimigoDeSiException extends Exception
{

    /**
     * Constrói a exceção com mensagem padrão que descreve a restrição de auto-inimizade.
     * <p>
     * Mensagem fixa: "Usuário não pode ser inimigo de si mesmo."
     */
    public InimigoDeSiException() {
        super("Usuário não pode ser inimigo de si mesmo.");
    }
}