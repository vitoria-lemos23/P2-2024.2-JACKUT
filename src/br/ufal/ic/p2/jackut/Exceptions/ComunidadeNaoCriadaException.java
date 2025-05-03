package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar uma comunidade que ainda n�o foi criada.
 * <p>
 * Ocorre em opera��es que requerem uma comunidade previamente criada para funcionar corretamente.
 *
 * <p><b>Cen�rios comuns:</b>
 * <ul>
 *   <li>Tentativa de acessar propriedades de uma comunidade n�o inicializada</li>
 *   <li>Opera��es em comunidades que falharam na cria��o</li>
 *   <li>Refer�ncia a comunidades deletadas ou n�o persistidas</li>
 * </ul>
 *
 * <p><b>A��es recomendadas:</b>
 * <ul>
 *   <li>Verificar o fluxo de cria��o da comunidade</li>
 *   <li>Garantir que a comunidade foi criada com sucesso antes de us�-la</li>
 *   <li>Utilizar {@code existeComunidade()} para valida��o pr�via</li>
 * </ul>
 *
 * @see IGerenciadorComunidades#criarComunidade(String, String, String)
 */
public class ComunidadeNaoCriadaException extends Exception {

    /**
     * Constr�i a exce��o com mensagem padr�o indicando o estado incompleto da comunidade.
     * <p>
     * Mensagem fixa: "Comunidade ainda n�o criada"
     */
    public ComunidadeNaoCriadaException() {
        super("Comunidade ainda n�o criada");
    }
}