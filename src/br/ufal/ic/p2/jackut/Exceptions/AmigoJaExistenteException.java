/**
 * Pacote que contém todas as exceções personalizadas do sistema Jackut.
 * As exceções deste pacote representam situações excepcionais específicas
 * da lógica de negócio da aplicação de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma operação tenta criar uma amizade que já existe.
 *
 * <p>Esta exceção indica uma violação da regra de negócio que impede a criação
 * de amizades duplicadas entre os mesmos usuários.</p>
 *
 * <p>Situações típicas que geram esta exceção:</p>
 * <ul>
 *   <li>Tentativa de adicionar um usuário que já é amigo</li>
 *   <li>Operações que verificam relacionamentos existentes</li>
 * </ul>
 *
 * @author Vitória Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#adicionarAmigo
 * @see br.ufal.ic.p2.jackut.Users#ehAmigo
 */
public class AmigoJaExistenteException extends Exception
{

    /**
     * Constrói uma nova exceção com a mensagem padrão.
     * A mensagem padrão é: "Usuário já está adicionado como amigo."
     */
    public AmigoJaExistenteException() {
        super("Usuário já está adicionado como amigo.");
    }
}