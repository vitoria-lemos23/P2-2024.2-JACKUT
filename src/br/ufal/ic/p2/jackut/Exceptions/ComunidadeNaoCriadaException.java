package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

/**
 * Exceção lançada quando uma operação tenta acessar uma comunidade que ainda não foi criada.
 * <p>
 * Ocorre em operações que requerem uma comunidade previamente criada para funcionar corretamente.
 *
 * <p><b>Cenários comuns:</b>
 * <ul>
 *   <li>Tentativa de acessar propriedades de uma comunidade não inicializada</li>
 *   <li>Operações em comunidades que falharam na criação</li>
 *   <li>Referência a comunidades deletadas ou não persistidas</li>
 * </ul>
 *
 * <p><b>Ações recomendadas:</b>
 * <ul>
 *   <li>Verificar o fluxo de criação da comunidade</li>
 *   <li>Garantir que a comunidade foi criada com sucesso antes de usá-la</li>
 *   <li>Utilizar {@code existeComunidade()} para validação prévia</li>
 * </ul>
 *
 * @see IGerenciadorComunidades#criarComunidade(String, String, String)
 */
public class ComunidadeNaoCriadaException extends Exception {

    /**
     * Constrói a exceção com mensagem padrão indicando o estado incompleto da comunidade.
     * <p>
     * Mensagem fixa: "Comunidade ainda não criada"
     */
    public ComunidadeNaoCriadaException() {
        super("Comunidade ainda não criada");
    }
}