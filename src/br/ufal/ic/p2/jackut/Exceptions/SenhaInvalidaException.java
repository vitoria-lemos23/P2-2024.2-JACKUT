/**
 * Pacote contendo todas as exceções personalizadas do sistema Jackut.
 * Estas exceções representam violações das regras de negócio da aplicação.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma senha não atende aos requisitos do sistema.
 *
 * <p>Esta exceção é utilizada para sinalizar problemas de validação de senhas durante:</p>
 * <ul>
 *   <li>Criação de novos usuários</li>
 *   <li>Alteração de senhas</li>
 *   <li>Processos de autenticação</li>
 * </ul>
 *
 * <p>Condições comuns que geram esta exceção:</p>
 * <ul>
 *   <li>Senha nula ou vazia</li>
 *   <li>Senha muito curta (abaixo do mínimo exigido)</li>
 *   <li>Senha sem os caracteres necessários (números, símbolos, etc.)</li>
 *   <li>Senha que não atende às políticas de segurança</li>
 * </ul>
 *
 * @author Vitória Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#criarUsuario
 * @see br.ufal.ic.p2.jackut.Users
 */
public class SenhaInvalidaException extends Exception
{

    /**
     * Constrói a exceção com a mensagem padrão "Senha inválida.".
     * Usado quando a senha fornecida não atende aos requisitos básicos do sistema.
     */
    public SenhaInvalidaException() {
        super("Senha inválida.");
    }
}