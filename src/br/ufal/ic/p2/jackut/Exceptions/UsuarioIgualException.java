/**
 * Pacote contendo todas as exceções personalizadas do sistema Jackut.
 * Estas exceções representam violações das regras de negócio da aplicação.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma operação tenta criar ou modificar um usuário com
 * credenciais que já existem no sistema.
 *
 * <p>Esta exceção é específica para casos de duplicação de identidade de usuário,
 * sendo lançada quando:</p>
 * <ul>
 *   <li>Tentativa de criar usuário com login já existente</li>
 *   <li>Atualização de credenciais para valores já utilizados</li>
 *   <li>Conflito de unicidade em operações de merge</li>
 * </ul>
 *
 * <p>Diferença para {@link LoginJaExistenteException}:<br>
 * Esta exceção é mais genérica e pode ser usada para outros tipos de conflitos
 * de identidade além do login (como email, CPF, etc.).</p>
 *
 * @author Viória Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#criarUsuario
 * @see br.ufal.ic.p2.jackut.Users
 */
public class UsuarioIgualException extends Exception {

    /**
     * Constrói a exceção com a mensagem padrão "Conta com esse nome já existe.".
     * Usado quando é detectada duplicação de informações de usuário.
     */
    public UsuarioIgualException() {
        super("Conta com esse nome já existe.");
    }
}