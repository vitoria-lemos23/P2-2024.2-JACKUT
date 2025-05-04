/**
 * Pacote contendo todas as exce��es personalizadas do sistema Jackut.
 * Estas exce��es representam situa��es excepcionais espec�ficas da
 * l�gica de neg�cio da aplica��o de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma tentativa de leitura de recados � feita,
 * mas n�o h� mensagens dispon�veis para o usu�rio.
 *
 * <p>Esta exce��o ocorre em situa��es onde:</p>
 * <ul>
 *   <li>O usu�rio tenta ler um recado quando sua caixa de entrada est� vazia</li>
 *   <li>Todas as mensagens j� foram lidas e removidas da fila</li>
 *   <li>Nenhuma mensagem foi enviada para o usu�rio</li>
 * </ul>
 *
 * <p>Diferente de outras exce��es, esta representa uma condi��o normal
 * do sistema, n�o um erro de programa��o.</p>
 *
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#lerRecado
 * @see br.ufal.ic.p2.jackut.Users#lerRecado
 */
public class SemRecadoException extends Exception
{

    /**
     * Constr�i a exce��o com a mensagem padr�o "N�o h� recados.".
     * Usado quando a opera��o de leitura de recados n�o encontra mensagens dispon�veis.
     */
    public SemRecadoException() {
        super("N�o h� recados.");
    }
}