/**
 * Pacote contendo todas as exceções personalizadas do sistema Jackut.
 * Estas exceções representam situações excepcionais específicas da
 * lógica de negócio da aplicação de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma tentativa de cadastro utiliza um login já existente.
 *
 * <p>Esta exceção garante a unicidade dos logins no sistema, sendo lançada quando:</p>
 * <ul>
 *   <li>Um novo usuário tenta se cadastrar com login já existente</li>
 *   <li>Uma operação de atualização tenta duplicar um login</li>
 * </ul>
 *
 * <p>A mensagem padrão "Conta com esse nome já existe." é destinada a ser exibida
 * diretamente ao usuário final em interfaces gráficas.</p>
 *
 * @author Vitória Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#criarUsuario
 * @see br.ufal.ic.p2.jackut.Users
 */
public class LoginJaExistenteException extends Exception {

    /**
     * Constrói a exceção com a mensagem padrão.
     * A mensagem padrão é: "Conta com esse nome já existe."
     */
    public LoginJaExistenteException() {
        super("Conta com esse nome já existe.");
    }
}