package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades;

/**
 * Exceção lançada quando uma operação é bloqueada devido a uma relação de inimizade entre usuários.
 * <p>
 * Serve como base para restrições sociais no sistema, sendo utilizada em operações como:
 * <ul>
 *   <li>Envio de mensagens entre inimigos</li>
 *   <li>Criação de relações sociais conflitantes</li>
 *   <li>Acesso a funcionalidades restritas entre usuários hostis</li>
 * </ul>
 *
 *
 *
 * @see IGerenciadorAmizades#adicionarAmigo(String, String)
 */
public class InimigoException extends Exception {

    /**
     * Constrói a exceção com mensagem personalizada para o contexto específico.
     *
     * @param message Detalhes sobre a restrição de inimizade que causou o erro
     *
     * @exampleSample Exemplo de mensagem:
     * "Operação bloqueada: João é seu inimigo e não pode receber mensagens"
     */
    public InimigoException(String message) {
        super(message);
    }
}