package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

/**
 * Exceção lançada quando uma operação tenta criar uma comunidade com nome duplicado.
 * <p>
 * Ocorre especificamente em operações de criação de comunidades onde o nome fornecido já está registrado no sistema.
 *
 * <p><b>Causas comuns:</b>
 * <ul>
 *   <li>Tentativa de recriar uma comunidade existente</li>
 *   <li>Falha na verificação de unicidade antes da criação</li>
 *   <li>Colisão acidental de nomes entre usuários</li>
 * </ul>
 *
 * <p><b>Solução recomendada:</b>
 * <ul>
 *   <li>Verificar a existência da comunidade com {@code existeComunidade()} antes da criação</li>
 *   <li>Utilizar nomes únicos contendo identificadores extras (ex: turma, região)</li>
 *   <li>Informar ao usuário para escolher um nome diferente</li>
 * </ul>
 *
 * @see IGerenciadorComunidades#criarComunidade(String, String, String)
 */
public class ComunidadeJaExisteException extends Exception {

    /**
     * Constrói a exceção com mensagem padrão contendo informações sobre o erro de duplicidade.
     * <p>
     * Mensagem fixa: "Comunidade com esse nome já existe."
     */
    public ComunidadeJaExisteException() {
        super("Comunidade com esse nome já existe.");
    }
}