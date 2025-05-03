package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando um usu�rio tenta estabelecer rela��o de paquera consigo mesmo.
 * <p>
 * Representa uma viola��o das regras de relacionamento social que impedem auto-paquera.
 *
 * <p><b>Cen�rios comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar pr�prio login na lista de paqueras</li>
 *   <li>Erro de sele��o de usu�rio alvo em interfaces</li>
 *   <li>Falha na valida��o de entradas de usu�rio</li>
 * </ul>
 *
 * <p><b>A��es recomendadas:</b>
 * <ul>
 *   <li>Validar identidade do usu�rio alvo antes da opera��o</li>
 *   <li>Implementar checagem de auto-refer�ncia na l�gica de paqueras</li>
 *   <li>Exibir mensagem preventiva em formul�rios de relacionamento</li>
 * </ul>
 *
 * @see IGerenciadorAmizades#adicionarPaquera(String, String)
 */
public class NaoPodePaquerarASiMesmoException extends Exception {

    /**
     * Constr�i a exce��o com mensagem padr�o que descreve a restri��o de auto-paquera.
     * <p>
     * Mensagem fixa: "Usu�rio n�o pode ser paquera de si mesmo."
     */
    public NaoPodePaquerarASiMesmoException() {
        super("Usu�rio n�o pode ser paquera de si mesmo.");
    }
}