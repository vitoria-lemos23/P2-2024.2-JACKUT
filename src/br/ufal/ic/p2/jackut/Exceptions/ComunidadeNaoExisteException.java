package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

/**
 * Exceção lançada quando uma operação tenta acessar uma comunidade inexistente no sistema.
 * <p>
 * Ocorre em operações que requerem uma comunidade previamente registrada para funcionar corretamente.
 *
 * <p><b>Cenários comuns:</b>
 * <ul>
 *   <li>Tentativa de acesso com nome de comunidade inválido/não cadastrado</li>
 *   <li>Referência a comunidades removidas ou não persistidas</li>
 *   <li>Erros de digitação no nome da comunidade</li>
 * </ul>
 *
 * <p><b>Ações recomendadas:</b>
 * <ul>
 *   <li>Verificar a existência da comunidade com {@code existeComunidade()} antes da operação</li>
 *   <li>Validar a grafia do nome da comunidade</li>
 *   <li>Listar comunidades disponíveis com {@code getComunidades()}</li>
 * </ul>
 *
 * @see IGerenciadorComunidades#getMembros(String)
 * @see IGerenciadorComunidades#getDono(String)
 */
public class ComunidadeNaoExisteException extends Exception {

    /**
     * Constrói a exceção com mensagem padrão indicando a ausência da comunidade.
     * <p>
     * Mensagem fixa: "Comunidade não existe."
     */
    public ComunidadeNaoExisteException() {
        super("Comunidade não existe.");
    }
}