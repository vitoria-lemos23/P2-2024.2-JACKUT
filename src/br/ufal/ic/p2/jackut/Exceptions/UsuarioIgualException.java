/**
 * Pacote contendo todas as exce��es personalizadas do sistema Jackut.
 * Estas exce��es representam viola��es das regras de neg�cio da aplica��o.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta criar ou modificar um usu�rio com
 * credenciais que j� existem no sistema.
 *
 * <p>Esta exce��o � espec�fica para casos de duplica��o de identidade de usu�rio,
 * sendo lan�ada quando:</p>
 * <ul>
 *   <li>Tentativa de criar usu�rio com login j� existente</li>
 *   <li>Atualiza��o de credenciais para valores j� utilizados</li>
 *   <li>Conflito de unicidade em opera��es de merge</li>
 * </ul>
 *
 * <p>Diferen�a para {@link LoginJaExistenteException}:<br>
 * Esta exce��o � mais gen�rica e pode ser usada para outros tipos de conflitos
 * de identidade al�m do login (como email, CPF, etc.).</p>
 *
 * @author Vi�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#criarUsuario
 * @see br.ufal.ic.p2.jackut.Users
 */
public class UsuarioIgualException extends Exception
{

    /**
     * Constr�i a exce��o com a mensagem padr�o "Conta com esse nome j� existe.".
     * Usado quando � detectada duplica��o de informa��es de usu�rio.
     */
    public UsuarioIgualException() {
        super("Conta com esse nome j� existe.");
    }
}