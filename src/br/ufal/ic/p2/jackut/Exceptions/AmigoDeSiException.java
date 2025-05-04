/**
 * Pacote que cont�m todas as exce��es personalizadas do sistema Jackut.
 * As exce��es deste pacote s�o usadas para tratar situa��es espec�ficas
 * da l�gica de neg�cio da aplica��o.
 */
package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando um usu�rio tenta adicionar a si mesmo como amigo.
 *
 * <p>Esta exce��o representa uma viola��o das regras de neg�cio do sistema,
 * onde um usu�rio n�o pode estabelecer rela��o de amizade consigo mesmo.</p>
 *
 * <p>Situa��es que geram esta exce��o:</p>
 * <ul>
 *   <li>Tentativa de auto-amizade no m�todo {@code adicionarAmigo}</li>
 *   <li>Opera��es que verificam relacionamentos entre usu�rios</li>
 * </ul>
 *
 * @author Vit�ria Lemos
 * @see br.ufal.ic.p2.jackut.Jackut#adicionarAmigo
 */
public class AmigoDeSiException extends Exception
{

    /**
     * Constr�i uma nova exce��o com a mensagem padr�o.
     * A mensagem padr�o �: "Usu�rio n�o pode adicionar a si mesmo como amigo."
     */
    public AmigoDeSiException() {
        super("Usu�rio n�o pode adicionar a si mesmo como amigo.");
    }


}