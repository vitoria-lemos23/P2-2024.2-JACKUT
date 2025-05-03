package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta adicionar um usu�rio j� existente na lista de inimigos.
 * <p>
 * Ocorre em opera��es de gest�o de rela��es sociais onde o usu�rio alvo j� foi previamente registrado como inimigo.
 *
 * <p><b>Cen�rios comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar o mesmo inimigo m�ltiplas vezes</li>
 *   <li>Falha na verifica��o de exist�ncia pr�via na lista de inimigos</li>
 *   <li>Sincroniza��o inadequada entre componentes do sistema</li>
 * </ul>
 *
 * <p><b>A��es recomendadas:</b>
 * <ul>
 *   <li>Verificar a lista de inimigos antes da opera��o usando {@code getInimigos()}</li>
 *   <li>Implementar checagem de duplicidade na l�gica de adi��o de inimigos</li>
 *   <li>Notificar o usu�rio sobre a rela��o hostil j� existente</li>
 * </ul>
 *
 * @see br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades#adicionarInimigo(String, String)
 */
public class UsuarioJaEhInimigoException extends Exception {

    /**
     * Constr�i a exce��o com mensagem padr�o indicando a duplicidade na lista de inimigos.
     * <p>
     * Mensagem fixa: "Usu�rio j� est� adicionado como inimigo."
     */
    public UsuarioJaEhInimigoException() {
        super("Usu�rio j� est� adicionado como inimigo.");
    }
}