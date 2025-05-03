package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

/**
 * Exce��o lan�ada quando uma opera��o tenta criar uma comunidade com nome duplicado.
 * <p>
 * Ocorre especificamente em opera��es de cria��o de comunidades onde o nome fornecido j� est� registrado no sistema.
 *
 * <p><b>Causas comuns:</b>
 * <ul>
 *   <li>Tentativa de recriar uma comunidade existente</li>
 *   <li>Falha na verifica��o de unicidade antes da cria��o</li>
 *   <li>Colis�o acidental de nomes entre usu�rios</li>
 * </ul>
 *
 * <p><b>Solu��o recomendada:</b>
 * <ul>
 *   <li>Verificar a exist�ncia da comunidade com {@code existeComunidade()} antes da cria��o</li>
 *   <li>Utilizar nomes �nicos contendo identificadores extras (ex: turma, regi�o)</li>
 *   <li>Informar ao usu�rio para escolher um nome diferente</li>
 * </ul>
 *
 * @see IGerenciadorComunidades#criarComunidade(String, String, String)
 */
public class ComunidadeJaExisteException extends Exception {

    /**
     * Constr�i a exce��o com mensagem padr�o contendo informa��es sobre o erro de duplicidade.
     * <p>
     * Mensagem fixa: "Comunidade com esse nome j� existe."
     */
    public ComunidadeJaExisteException() {
        super("Comunidade com esse nome j� existe.");
    }
}