/**
 * Pacote que contém todas as exceções personalizadas do sistema Jackut.
 * As exceções deste pacote são usadas para tratar situações específicas
 * da lógica de negócio da aplicação.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando um usuário tenta adicionar a si mesmo como amigo.
 *
 * <p>Esta exceção representa uma violação das regras de negócio do sistema,
 * onde um usuário não pode estabelecer relação de amizade consigo mesmo.</p>
 *
 * <p>Situações que geram esta exceção:</p>
 * <ul>
 *   <li>Tentativa de auto-amizade no método {@code adicionarAmigo}</li>
 *   <li>Operações que verificam relacionamentos entre usuários</li>
 * </ul>
 *
 * @author Vitória Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#adicionarAmigo
 */
public class AmigoDeSiException extends Exception {

    /**
     * Constrói uma nova exceção com a mensagem padrão.
     * A mensagem padrão é: "Usuário não pode adicionar a si mesmo como amigo."
     */
    public AmigoDeSiException() {
        super("Usuário não pode adicionar a si mesmo como amigo.");
    }


}