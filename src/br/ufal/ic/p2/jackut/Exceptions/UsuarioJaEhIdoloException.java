package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta adicionar um �dolo j� existente na lista de admira��o do usu�rio.
 * <p>
 * Ocorre em opera��es de gest�o de relacionamentos sociais onde um usu�rio j� registrou outro como seu �dolo anteriormente.
 *
 * <p><b>Cen�rios comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar o mesmo �dolo m�ltiplas vezes</li>
 *   <li>Falha na verifica��o de exist�ncia pr�via na lista de �dolos</li>
 *   <li>Sincroniza��o incorreta entre diferentes componentes do sistema</li>
 * </ul>
 *
 * <p><b>A��es recomendadas:</b>
 * <ul>
 *   <li>Verificar a lista de �dolos com {@code getFas()} antes da adi��o</li>
 *   <li>Implementar verifica��o de duplicidade na l�gica de adi��o de �dolos</li>
 *   <li>Notificar o usu�rio sobre a rela��o j� existente</li>
 * </ul>
 *
 * @see br.ufal.ic.p2.jackut.Componentes.GerenciadorAmizades#adicionarIdolo(String, String)
 */
public class UsuarioJaEhIdoloException extends Exception {

    /**
     * Constr�i a exce��o com mensagem padr�o indicando a duplicidade na lista de �dolos.
     * <p>
     * Mensagem fixa: "Usu�rio j� est� adicionado como �dolo."
     */
    public UsuarioJaEhIdoloException() {
        super("Usu�rio j� est� adicionado como �dolo.");
    }
}