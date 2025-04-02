/**
 * Pacote contendo todas as exceções personalizadas do sistema Jackut.
 * Estas exceções representam situações excepcionais específicas da
 * lógica de negócio da aplicação de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma tentativa de leitura de recados é feita,
 * mas não há mensagens disponíveis para o usuário.
 *
 * <p>Esta exceção ocorre em situações onde:</p>
 * <ul>
 *   <li>O usuário tenta ler um recado quando sua caixa de entrada está vazia</li>
 *   <li>Todas as mensagens já foram lidas e removidas da fila</li>
 *   <li>Nenhuma mensagem foi enviada para o usuário</li>
 * </ul>
 *
 * <p>Diferente de outras exceções, esta representa uma condição normal
 * do sistema, não um erro de programação.</p>
 *
 * @author Vitória Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#lerRecado
 * @see br.ufal.ic.p2.jackut.Users#lerRecado
 */
public class SemRecadoException extends Exception {

    /**
     * Constrói a exceção com a mensagem padrão "Não há recados.".
     * Usado quando a operação de leitura de recados não encontra mensagens disponíveis.
     */
    public SemRecadoException() {
        super("Não há recados.");
    }
}