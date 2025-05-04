/**
 * Pacote que cont�m todas as exce��es personalizadas do sistema Jackut.
 * As exce��es deste pacote representam situa��es excepcionais espec�ficas
 * da l�gica de neg�cio da aplica��o de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta criar uma amizade que j� existe.
 *
 * <p>Esta exce��o indica uma viola��o da regra de neg�cio que impede a cria��o
 * de amizades duplicadas entre os mesmos usu�rios.</p>
 *
 * <p>Situa��es t�picas que geram esta exce��o:</p>
 * <ul>
 *   <li>Tentativa de adicionar um usu�rio que j� � amigo</li>
 *   <li>Opera��es que verificam relacionamentos existentes</li>
 * </ul>
 *
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#adicionarAmigo
 * @see br.ufal.ic.p2.jackut.Users#ehAmigo
 */
public class AmigoJaExistenteException extends Exception
{

    /**
     * Constr�i uma nova exce��o com a mensagem padr�o.
     * A mensagem padr�o �: "Usu�rio j� est� adicionado como amigo."
     */
    public AmigoJaExistenteException() {
        super("Usu�rio j� est� adicionado como amigo.");
    }
}