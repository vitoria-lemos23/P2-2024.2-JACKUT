/**
 * Pacote contendo todas as exceções personalizadas do sistema Jackut.
 * Estas exceções representam situações excepcionais específicas da lógica
 * de negócio da aplicação de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando as credenciais de autenticação são inválidas.
 *
 * <p>Esta exceção indica falha no processo de autenticação quando:</p>
 * <ul>
 *   <li>O login não existe no sistema</li>
 *   <li>A senha não corresponde ao usuário</li>
 *   <li>As credenciais estão em formato inválido</li>
 * </ul>
 *
 * <p>Recomenda-se capturar esta exceção para fornecer feedback adequado
 * ao usuário sobre falhas de autenticação.</p>
 *
 * @author Vitória Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#abrirSessao
 */
public class CredenciaisInvalidasException extends Exception
{

    /**
     * Constrói a exceção com a mensagem padrão "Login ou senha inválidos.".
     * Usado quando a autenticação falha por credenciais incorretas.
     */
    public CredenciaisInvalidasException() {
        super("Login ou senha inválidos.");
    }
}