/**
 * Pacote que contém todas as exceções personalizadas do sistema Jackut.
 * Estas exceções representam situações excepcionais específicas da
 * lógica de negócio da aplicação de rede social.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma operação tenta acessar um usuário inexistente no sistema.
 *
 * <p>Esta exceção é fundamental para garantir a integridade das operações que dependem
 * da existência de usuários registrados no sistema.</p>
 *
 *<p><b>Situações comuns que lançam esta exceção:</b>
 * <ul>
 *   <li>Tentativa de visualizar perfil de usuário não cadastrado</li>
 *   <li>Envio de mensagem para destinatário inexistente</li>
 *   <li>Solicitação de amizade para usuário não registrado</li>
 *   <li>Acesso via ID de usuário inválido</li>
 * </ul>
 * @author Vitória Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#getAtributoUsuario
 * @see br.ufal.ic.p2.jackut.Jackut#adicionarAmigo
 * @see br.ufal.ic.p2.jackut.Jackut#enviarRecado
 */
public class UsuarioNaoEncontradoException extends Exception {

    /**
     * Constrói a exceção com a mensagem padrão "Usuário não cadastrado.".
     *
     * <p>Esta mensagem é adequada para exibição direta ao usuário final,
     * sendo clara e objetiva sobre o problema ocorrido.</p>
     */
    public UsuarioNaoEncontradoException() {
        super("Usuário não cadastrado.");
    }
}