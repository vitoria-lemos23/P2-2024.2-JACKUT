/**
 * Pacote que contém as exceções específicas do sistema Jackut.
 * Estas exceções representam violações das regras de negócio da aplicação.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma operação tenta acessar um atributo de perfil
 * que não existe ou não foi preenchido.
 *
 * <p>Esta exceção ocorre em situações onde:</p>
 * <ul>
 *   <li>Um atributo obrigatório não foi definido</li>
 *   <li>Tentativa de acesso a um atributo inexistente</li>
 *   <li>Operações com atributos vazios ou nulos</li>
 * </ul>
 *
 * @author Vitória Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#getAtributoUsuario
 * @see br.ufal.ic.p2.jackut.Jackut#editarPerfil
 * @see br.ufal.ic.p2.jackut.Users#getAtributo
 */
public class AtributoNaoPreenchidoException extends Exception
{

    /**
     * Constrói a exceção com mensagem padrão "Atributo não preenchido.".
     * Usado quando um atributo requerido não está disponível.
     */
    public AtributoNaoPreenchidoException() {
        super("Atributo não preenchido.");
    }
}