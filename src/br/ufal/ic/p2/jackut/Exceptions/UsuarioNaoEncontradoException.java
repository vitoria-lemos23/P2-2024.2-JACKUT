/**
 * Pacote que cont�m todas as exce��es personalizadas do sistema Jackut.
 * Estas exce��es representam situa��es excepcionais espec�ficas da
 * l�gica de neg�cio da aplica��o de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar um usu�rio inexistente no sistema.
 *
 * <p>Esta exce��o � fundamental para garantir a integridade das opera��es que dependem
 * da exist�ncia de usu�rios registrados no sistema.</p>
 *
 *<p><b>Situa��es comuns que lan�am esta exce��o:</b>
 * <ul>
 *   <li>Tentativa de visualizar perfil de usu�rio n�o cadastrado</li>
 *   <li>Envio de mensagem para destinat�rio inexistente</li>
 *   <li>Solicita��o de amizade para usu�rio n�o registrado</li>
 *   <li>Acesso via ID de usu�rio inv�lido</li>
 * </ul>
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#getAtributoUsuario
 * @see br.ufal.ic.p2.jackut.Jackut#adicionarAmigo
 * @see br.ufal.ic.p2.jackut.Jackut#enviarRecado
 */
public class UsuarioNaoEncontradoException extends Exception {

    /**
     * Constr�i a exce��o com a mensagem padr�o "Usu�rio n�o cadastrado.".
     *
     * <p>Esta mensagem � adequada para exibi��o direta ao usu�rio final,
     * sendo clara e objetiva sobre o problema ocorrido.</p>
     */
    public UsuarioNaoEncontradoException() {
        super("Usu�rio n�o cadastrado.");
    }
}