/**
 * Pacote contendo todas as exce��es personalizadas do sistema Jackut.
 * Estas exce��es representam viola��es das regras de neg�cio da aplica��o.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando um login n�o atende aos requisitos do sistema.
 *
 * <p>Esta exce��o indica problemas na valida��o de logins durante:</p>
 * <ul>
 *   <li>Cria��o de novos usu�rios</li>
 *   <li>Modifica��o de credenciais</li>
 *   <li>Opera��es de autentica��o</li>
 * </ul>
 *
 * <p>Motivos comuns para esta exce��o:</p>
 * <ul>
 *   <li>Login nulo ou vazio</li>
 *   <li>Login com formato inv�lido (caracteres especiais, espa�os, etc.)</li>
 *   <li>Login muito curto ou muito longo</li>
 * </ul>
 *
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#criarUsuario
 */
public class LoginInvalidoException extends Exception
{

    /**
     * Constr�i a exce��o com a mensagem padr�o "Login inv�lido.".
     * Usado quando o login fornecido n�o atende aos requisitos b�sicos.
     */
    public LoginInvalidoException() {
        super("Login inv�lido.");
    }

    public static class ComunidadeJaExiste {
    }
}