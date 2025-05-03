package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando um usu�rio tenta se adicionar como pr�prio inimigo.
 * <p>
 * Representa uma viola��o das regras de relacionamento social que impedem auto-inimizade.
 *
 * <p><b>Cen�rios comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar pr�prio login na lista de inimigos</li>
 *   <li>Erro de l�gica na sele��o do usu�rio alvo</li>
 *   <li>Falha na valida��o de entrada em interfaces</li>
 * </ul>
 *
 * <p><b>A��es recomendadas:</b>
 * <ul>
 *   <li>Validar identidade do usu�rio alvo antes da opera��o</li>
 *   <li>Implementar checagem de auto-refer�ncia no fluxo de inimizades</li>
 *   <li>Exibir mensagem clara em formul�rios de relacionamento</li>
 * </ul>
 *
 * @see IGerenciadorAmizades#adicionarInimigo(String, String)
 * @see NaoPodeSerFaDeSiException Exce��o similar para auto-idolatria
 * @see NaoPodePaquerarASiMesmoException Exce��o similar para auto-paquera
 */
public class NaoPodeSerInimigoDeSiException extends Exception {

    /**
     * Constr�i a exce��o com mensagem padr�o que descreve a restri��o de auto-inimizade.
     * <p>
     * Mensagem fixa: "Usu�rio n�o pode ser inimigo de si mesmo."
     */
    public NaoPodeSerInimigoDeSiException() {
        super("Usu�rio n�o pode ser inimigo de si mesmo.");
    }
}