package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades;

/**
 * Exce��o lan�ada quando um usu�rio tenta se adicionar como pr�prio inimigo.
 * <p>
 * Representa uma viola��o das regras de relacionamento social do sistema que impedem
 * auto-inimizade.
 *
 * <p><b>Cen�rios comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar o pr�prio login na lista de inimigos</li>
 *   <li>Erro l�gico na sele��o do usu�rio alvo</li>
 *   <li>Falha na valida��o de entrada de dados</li>
 * </ul>
 *
 * <p><b>A��es recomendadas:</b>
 * <ul>
 *   <li>Verificar a l�gica de sele��o de usu�rio alvo</li>
 *   <li>Implementar valida��o de identidade antes da opera��o</li>
 *   <li>Exibir mensagem clara de erro ao usu�rio final</li>
 * </ul>
 *
 */
public class InimigoDeSiException extends Exception
{

    /**
     * Constr�i a exce��o com mensagem padr�o que descreve a restri��o de auto-inimizade.
     * <p>
     * Mensagem fixa: "Usu�rio n�o pode ser inimigo de si mesmo."
     */
    public InimigoDeSiException() {
        super("Usu�rio n�o pode ser inimigo de si mesmo.");
    }
}