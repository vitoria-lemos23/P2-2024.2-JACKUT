/**
 * Pacote contendo todas as exce��es personalizadas do sistema Jackut.
 * Estas exce��es representam situa��es excepcionais espec�ficas da l�gica
 * de neg�cio da aplica��o de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando as credenciais de autentica��o s�o inv�lidas.
 *
 * <p>Esta exce��o indica falha no processo de autentica��o quando:</p>
 * <ul>
 *   <li>O login n�o existe no sistema</li>
 *   <li>A senha n�o corresponde ao usu�rio</li>
 *   <li>As credenciais est�o em formato inv�lido</li>
 * </ul>
 *
 * <p>Recomenda-se capturar esta exce��o para fornecer feedback adequado
 * ao usu�rio sobre falhas de autentica��o.</p>
 *
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#abrirSessao
 */
public class CredenciaisInvalidasException extends Exception
{

    /**
     * Constr�i a exce��o com a mensagem padr�o "Login ou senha inv�lidos.".
     * Usado quando a autentica��o falha por credenciais incorretas.
     */
    public CredenciaisInvalidasException() {
        super("Login ou senha inv�lidos.");
    }
}