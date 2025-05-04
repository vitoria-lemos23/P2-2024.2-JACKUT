/**
 * Pacote contendo todas as exce��es personalizadas do sistema Jackut.
 * Estas exce��es representam situa��es excepcionais espec�ficas da
 * l�gica de neg�cio da aplica��o de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma tentativa de cadastro utiliza um login j� existente.
 *
 * <p>Esta exce��o garante a unicidade dos logins no sistema, sendo lan�ada quando:</p>
 * <ul>
 *   <li>Um novo usu�rio tenta se cadastrar com login j� existente</li>
 *   <li>Uma opera��o de atualiza��o tenta duplicar um login</li>
 * </ul>
 *
 * <p>A mensagem padr�o "Conta com esse nome j� existe." � destinada a ser exibida
 * diretamente ao usu�rio final em interfaces gr�ficas.</p>
 *
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#criarUsuario
 * @see br.ufal.ic.p2.jackut.Users
 */
public class LoginJaExistenteException extends Exception
{

    /**
     * Constr�i a exce��o com a mensagem padr�o.
     * A mensagem padr�o �: "Conta com esse nome j� existe."
     */
    public LoginJaExistenteException() {
        super("Conta com esse nome j� existe.");
    }
}