package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar uma comunidade inexistente no sistema.
 * <p>
 * Ocorre em opera��es que requerem uma comunidade previamente registrada para funcionar corretamente.
 *
 * <p><b>Cen�rios comuns:</b>
 * <ul>
 *   <li>Tentativa de acesso com nome de comunidade inv�lido/n�o cadastrado</li>
 *   <li>Refer�ncia a comunidades removidas ou n�o persistidas</li>
 *   <li>Erros de digita��o no nome da comunidade</li>
 * </ul>
 *
 * <p><b>A��es recomendadas:</b>
 * <ul>
 *   <li>Verificar a exist�ncia da comunidade com {@code existeComunidade()} antes da opera��o</li>
 *   <li>Validar a grafia do nome da comunidade</li>
 *   <li>Listar comunidades dispon�veis com {@code getComunidades()}</li>
 * </ul>
 *
 * @see IGerenciadorComunidades#getMembros(String)
 * @see IGerenciadorComunidades#getDono(String)
 */
public class ComunidadeNaoExisteException extends Exception {

    /**
     * Constr�i a exce��o com mensagem padr�o indicando a aus�ncia da comunidade.
     * <p>
     * Mensagem fixa: "Comunidade n�o existe."
     */
    public ComunidadeNaoExisteException() {
        super("Comunidade n�o existe.");
    }
}