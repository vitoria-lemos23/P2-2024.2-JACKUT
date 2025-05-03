/**
 * Pacote contendo todas as exce��es personalizadas do sistema Jackut.
 * Estas exce��es representam situa��es excepcionais espec�ficas da
 * l�gica de neg�cio da aplica��o de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta utilizar uma sess�o inv�lida ou expirada.
 *
 * <p>Esta exce��o ocorre nas seguintes situa��es:</p>
 * <ul>
 *   <li>Tentativa de usar um ID de sess�o inexistente</li>
 *   <li>Sess�o expirada por inatividade</li>
 *   <li>Token de sess�o malformado</li>
 *   <li>Acesso ap�s logout</li>
 * </ul>
 *
 * <p>Recomenda-se capturar esta exce��o para:</p>
 * <ul>
 *   <li>Redirecionar o usu�rio para a tela de login</li>
 *   <li>Registrar tentativas de acesso n�o autorizado</li>
 *   <li>Encerrar processos que dependiam da sess�o</li>
 * </ul>
 *
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#abrirSessao
 * @see br.ufal.ic.p2.jackut.Jackut#encerrarSistema
 */
public class SessaoInvalidaExecption extends Exception {

    /**
     * Constr�i a exce��o com a mensagem padr�o "Sess�o inv�lida.".
     * Usado quando a valida��o da sess�o falha por motivos gen�ricos.
     */
    public SessaoInvalidaExecption() {
        super("Sess�o inv�lida.");
    }


}