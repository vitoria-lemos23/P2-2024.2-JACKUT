/**
 * Pacote que cont�m as exce��es espec�ficas do sistema Jackut.
 * Estas exce��es representam viola��es das regras de neg�cio da aplica��o.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar um atributo de perfil
 * que n�o existe ou n�o foi preenchido.
 *
 * <p>Esta exce��o ocorre em situa��es onde:</p>
 * <ul>
 *   <li>Um atributo obrigat�rio n�o foi definido</li>
 *   <li>Tentativa de acesso a um atributo inexistente</li>
 *   <li>Opera��es com atributos vazios ou nulos</li>
 * </ul>
 *
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#getAtributoUsuario
 * @see br.ufal.ic.p2.jackut.Jackut#editarPerfil
 * @see br.ufal.ic.p2.jackut.Users#getAtributo
 */
public class AtributoNaoPreenchidoException extends Exception
{

    /**
     * Constr�i a exce��o com mensagem padr�o "Atributo n�o preenchido.".
     * Usado quando um atributo requerido n�o est� dispon�vel.
     */
    public AtributoNaoPreenchidoException() {
        super("Atributo n�o preenchido.");
    }
}