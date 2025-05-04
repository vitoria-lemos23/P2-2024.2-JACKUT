/**
 * Pacote contendo todas as exce��es personalizadas do sistema Jackut.
 * Estas exce��es representam situa��es espec�ficas da l�gica de neg�cio
 * da rede social Jackut.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma solicita��o de amizade j� est� pendente de aceita��o.
 *
 * <p>Indica que um usu�rio j� enviou uma solicita��o de amizade que ainda n�o foi
 * respondida pelo destinat�rio.</p>
 *
 * <p>Situa��es que geram esta exce��o:</p>
 * <ul>
 *   <li>Tentativa de enviar nova solicita��o para um usu�rio que j� possui uma solicita��o pendente</li>
 *   <li>Opera��es que verificam o estado de solicita��es de amizade</li>
 * </ul>
 *
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#adicionarAmigo
 * @see br.ufal.ic.p2.jackut.Users#temSolicitacaoPendente
 */
public class AmigoPendenteException extends Exception
{

    /**
     * Constr�i uma nova exce��o com a mensagem padr�o.
     * A mensagem padr�o �: "Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite."
     */
    public AmigoPendenteException() {
        super("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
    }





}