/**
 * Pacote contendo todas as exceções personalizadas do sistema Jackut.
 * Estas exceções representam violações das regras de negócio da aplicação.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando um login não atende aos requisitos do sistema.
 *
 * <p>Esta exceção indica problemas na validação de logins durante:</p>
 * <ul>
 *   <li>Criação de novos usuários</li>
 *   <li>Modificação de credenciais</li>
 *   <li>Operações de autenticação</li>
 * </ul>
 *
 * <p>Motivos comuns para esta exceção:</p>
 * <ul>
 *   <li>Login nulo ou vazio</li>
 *   <li>Login com formato inválido (caracteres especiais, espaços, etc.)</li>
 *   <li>Login muito curto ou muito longo</li>
 * </ul>
 *
 * @author Vitória Lemos Pereira
 * @version 1.0
 * @since 1.0
 * @see br.ufal.ic.p2.jackut.Jackut#criarUsuario
 */
public class LoginInvalidoException extends Exception {

    /**
     * Constrói a exceção com a mensagem padrão "Login inválido.".
     * Usado quando o login fornecido não atende aos requisitos básicos.
     */
    public LoginInvalidoException() {
        super("Login inválido.");
    }
}