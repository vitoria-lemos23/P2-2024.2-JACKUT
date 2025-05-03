package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando um usu�rio tenta se adicionar como pr�prio f�/�dolo.
 * <p>
 * Representa uma viola��o das regras de relacionamento social que impedem auto-idolatria.
 *
 * <p><b>Cen�rios comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar pr�prio login na lista de f�s/�dolos</li>
 *   <li>Erro de l�gica na sele��o do usu�rio alvo</li>
 *   <li>Falha na valida��o de entrada em interfaces de usu�rio</li>
 * </ul>
 *
 * <p><b>A��es recomendadas:</b>
 * <ul>
 *   <li>Validar identidade do usu�rio alvo antes da opera��o</li>
 *   <li>Implementar checagem de auto-refer�ncia no fluxo de adi��o de �dolos</li>
 *   <li>Exibir alerta claro em formul�rios de relacionamento</li>
 * </ul>
 *
 * @see IGerenciadorAmizades#adicionarIdolo(String, String)
 * @see InimigoDeSiException Exce��o similar para auto-inimizade
 */
public class NaoPodeSerFaDeSiException extends Exception {

    /**
     * Constr�i a exce��o com mensagem padr�o que descreve a restri��o de auto-idolatria.
     * <p>
     * Mensagem fixa: "Usu�rio n�o pode ser f� de si mesmo."
     */
    public NaoPodeSerFaDeSiException() {
        super("Usu�rio n�o pode ser f� de si mesmo.");
    }
}