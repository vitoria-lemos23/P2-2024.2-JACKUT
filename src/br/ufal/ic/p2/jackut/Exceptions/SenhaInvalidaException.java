/**
 * Pacote contendo todas as exce��es personalizadas do sistema Jackut.
 * Estas exce��es representam viola��es das regras de neg�cio da aplica��o.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma senha n�o atende aos requisitos do sistema.
 *
 * <p>Esta exce��o � utilizada para sinalizar problemas de valida��o de senhas durante:</p>
 * <ul>
 *   <li>Cria��o de novos usu�rios</li>
 *   <li>Altera��o de senhas</li>
 *   <li>Processos de autentica��o</li>
 * </ul>
 *
 * <p>Condi��es comuns que geram esta exce��o:</p>
 * <ul>
 *   <li>Senha nula ou vazia</li>
 *   <li>Senha muito curta (abaixo do m�nimo exigido)</li>
 *   <li>Senha sem os caracteres necess�rios (n�meros, s�mbolos, etc.)</li>
 *   <li>Senha que n�o atende �s pol�ticas de seguran�a</li>
 * </ul>
 *
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#criarUsuario
 * @see br.ufal.ic.p2.jackut.Users
 */
public class SenhaInvalidaException extends Exception
{

    /**
     * Constr�i a exce��o com a mensagem padr�o "Senha inv�lida.".
     * Usado quando a senha fornecida n�o atende aos requisitos b�sicos do sistema.
     */
    public SenhaInvalidaException() {
        super("Senha inv�lida.");
    }
}