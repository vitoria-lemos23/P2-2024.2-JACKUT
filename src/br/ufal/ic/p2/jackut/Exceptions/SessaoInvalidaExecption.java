/**
 * Pacote contendo todas as exceções personalizadas do sistema Jackut.
 * Estas exceções representam situações excepcionais específicas da
 * lógica de negócio da aplicação de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma operação tenta utilizar uma sessão inválida ou expirada.
 *
 * <p>Esta exceção ocorre nas seguintes situações:</p>
 * <ul>
 *   <li>Tentativa de usar um ID de sessão inexistente</li>
 *   <li>Sessão expirada por inatividade</li>
 *   <li>Token de sessão malformado</li>
 *   <li>Acesso após logout</li>
 * </ul>
 *
 * <p>Recomenda-se capturar esta exceção para:</p>
 * <ul>
 *   <li>Redirecionar o usuário para a tela de login</li>
 *   <li>Registrar tentativas de acesso não autorizado</li>
 *   <li>Encerrar processos que dependiam da sessão</li>
 * </ul>
 *
 * @author Vitória Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#abrirSessao
 * @see br.ufal.ic.p2.jackut.Jackut#encerrarSistema
 */
public class SessaoInvalidaExecption extends Exception {

    /**
     * Constrói a exceção com a mensagem padrão "Sessão inválida.".
     * Usado quando a validação da sessão falha por motivos genéricos.
     */
    public SessaoInvalidaExecption() {
        super("Sessão inválida.");
    }


}