package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades;

/**
 * Exce��o lan�ada quando uma opera��o � bloqueada devido a uma rela��o de inimizade entre usu�rios.
 * <p>
 * Serve como base para restri��es sociais no sistema, sendo utilizada em opera��es como:
 * <ul>
 *   <li>Envio de mensagens entre inimigos</li>
 *   <li>Cria��o de rela��es sociais conflitantes</li>
 *   <li>Acesso a funcionalidades restritas entre usu�rios hostis</li>
 * </ul>
 *
 *
 *
 * @see IGerenciadorAmizades#adicionarAmigo(String, String)
 */
public class InimigoException extends Exception {

    /**
     * Constr�i a exce��o com mensagem personalizada para o contexto espec�fico.
     *
     * @param message Detalhes sobre a restri��o de inimizade que causou o erro
     *
     * @exampleSample Exemplo de mensagem:
     * "Opera��o bloqueada: Jo�o � seu inimigo e n�o pode receber mensagens"
     */
    public InimigoException(String message) {
        super(message);
    }
}