package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta adicionar um usu�rio j� existente na lista de paqueras.
 * <p>
 * Ocorre em opera��es de gest�o de rela��es sociais onde o usu�rio alvo j� foi previamente registrado como paquera.
 *
 * <p><b>Cen�rios comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar a mesma paquera m�ltiplas vezes</li>
 *   <li>Falha na verifica��o de exist�ncia pr�via na lista de paqueras</li>
 *   <li>Sincroniza��o inadequada entre componentes do sistema</li>
 * </ul>
 *
 * <p><b>A��es recomendadas:</b>
 * <ul>
 *   <li>Verificar a lista de paqueras antes da opera��o usando {@code getPaqueras()}</li>
 *   <li>Implementar checagem de duplicidade na l�gica de adi��o de paqueras</li>
 *   <li>Notificar o usu�rio sobre a rela��o j� existente</li>
 * </ul>
 *
 * @see br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades#adicionarPaquera(String, String)
 * @see UsuarioJaEhIdoloException Exce��o similar para �dolos duplicados
 * @see UsuarioJaEhInimigoException Exce��o similar para inimigos duplicados
 */
public class UsuarioJaEhPaqueraException extends Exception {

    /**
     * Constr�i a exce��o com mensagem padr�o indicando a duplicidade na lista de paqueras.
     * <p>
     * Mensagem fixa: "Usu�rio j� est� adicionado como paquera."
     */
    public UsuarioJaEhPaqueraException() {
        super("Usu�rio j� est� adicionado como paquera.");
    }
}