package br.ufal.ic.p2.jackut.Exceptions;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

/**
 * Exceção lançada quando uma operação tenta adicionar um usuário já existente em uma comunidade.
 * <p>
 * Ocorre em operações de inclusão de membros onde o usuário especificado já faz parte da comunidade alvo.
 *
 * <p><b>Causas comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar o mesmo usuário múltiplas vezes</li>
 *   <li>Falha na verificação prévia de membros existentes</li>
 *   <li>Sincronização incorreta entre diferentes componentes do sistema</li>
 * </ul>
 *
 * <p><b>Soluções recomendadas:</b>
 * <ul>
 *   <li>Verificar a lista de membros com {@code getMembros()} antes da adição</li>
 *   <li>Implementar verificação de duplicidade na lógica de negócio</li>
 *   <li>Garantir atomicidade nas operações de atualização de comunidades</li>
 * </ul>
 *
 * @see IGerenciadorComunidades#adicionarmembro(String, String)
 */
public class MembroJaExisteException extends Exception
{

    /**
     * Constrói a exceção com mensagem padrão indicando a duplicidade de membro.
     * <p>
     * Mensagem fixa: "Usuario já faz parte dessa comunidade."
     */
    public MembroJaExisteException() {
        super("Usuario já faz parte dessa comunidade.");
    }
}