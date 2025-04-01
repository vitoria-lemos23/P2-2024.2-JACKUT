/**
 * Pacote contendo todas as exceções personalizadas do sistema Jackut.
 * Estas exceções representam situações específicas da lógica de negócio
 * da rede social Jackut.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma solicitação de amizade já está pendente de aceitação.
 *
 * <p>Indica que um usuário já enviou uma solicitação de amizade que ainda não foi
 * respondida pelo destinatário.</p>
 *
 * <p>Situações que geram esta exceção:</p>
 * <ul>
 *   <li>Tentativa de enviar nova solicitação para um usuário que já possui uma solicitação pendente</li>
 *   <li>Operações que verificam o estado de solicitações de amizade</li>
 * </ul>
 *
 * @author [Seu Nome ou Equipe]
 * @version 1.0
 * @since 1.0
 * @see br.ufal.ic.p2.jackut.Jackut#adicionarAmigo
 * @see br.ufal.ic.p2.jackut.Users#temSolicitacaoPendente
 */
public class AmigoPendenteException extends Exception {

    /**
     * Constrói uma nova exceção com a mensagem padrão.
     * A mensagem padrão é: "Usuário já está adicionado como amigo, esperando aceitação do convite."
     */
    public AmigoPendenteException() {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }





}