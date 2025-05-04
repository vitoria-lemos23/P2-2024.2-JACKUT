package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

/**
 * Exce��o lan�ada quando uma opera��o tenta adicionar um usu�rio j� existente em uma comunidade.
 * <p>
 * Ocorre em opera��es de inclus�o de membros onde o usu�rio especificado j� faz parte da comunidade alvo.
 *
 * <p><b>Causas comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar o mesmo usu�rio m�ltiplas vezes</li>
 *   <li>Falha na verifica��o pr�via de membros existentes</li>
 *   <li>Sincroniza��o incorreta entre diferentes componentes do sistema</li>
 * </ul>
 *
 * <p><b>Solu��es recomendadas:</b>
 * <ul>
 *   <li>Verificar a lista de membros com {@code getMembros()} antes da adi��o</li>
 *   <li>Implementar verifica��o de duplicidade na l�gica de neg�cio</li>
 *   <li>Garantir atomicidade nas opera��es de atualiza��o de comunidades</li>
 * </ul>
 *
 * @see IGerenciadorComunidades#adicionarmembro(String, String)
 */
public class MembroJaExisteException extends Exception
{

    /**
     * Constr�i a exce��o com mensagem padr�o indicando a duplicidade de membro.
     * <p>
     * Mensagem fixa: "Usuario j� faz parte dessa comunidade."
     */
    public MembroJaExisteException() {
        super("Usuario j� faz parte dessa comunidade.");
    }
}