package br.ufal.ic.p2.jackut.Interfaces;

import br.ufal.ic.p2.jackut.Exceptions.*;
import java.util.List;

/**
 * Interface para gerenciamento de comunidades no sistema Jackut.
 * <p>
 * Define opera��es para:
 * <ul>
 *   <li>Cria��o e remo��o de comunidades</li>
 *   <li>Gest�o de membros</li>
 *   <li>Consulta de informa��es comunit�rias</li>
 *   <li>Controle de propriedade</li>
 * </ul>
 *
 * <p>Lan�a exce��es espec�ficas para casos de comunidades duplicadas, inexistentes e membros j� cadastrados.</p>
 */

public interface IGerenciadorComunidades {
    /**
     * Cria uma nova comunidade com nome �nico e dados b�sicos.
     *
     * @param nome Nome �nico da comunidade (case-sensitive)
     * @param descricao Descri��o detalhada da comunidade
     * @param dono Login do usu�rio fundador
     * @throws ComunidadeJaExisteException Se o nome j� estiver em uso
     */
    void criarComunidade(String nome, String descricao, String dono) throws ComunidadeJaExisteException;

    /**
     * Recupera a descri��o de uma comunidade existente.
     *
     * @param nome Nome da comunidade (case-sensitive)
     * @return Descri��o original cadastrada
     * @throws ComunidadeNaoExisteException Se a comunidade n�o existir
     */
    String getDescricao(String nome) throws ComunidadeNaoExisteException;

    /**
     * Lista todos os membros de uma comunidade.
     *
     * @param nome Nome da comunidade (case-sensitive)
     * @return Lista imut�vel de logins dos membros (ordem alfab�tica)
     * @throws ComunidadeNaoExisteException Se a comunidade n�o existir
     */
    List<String> getMembros(String nome) throws ComunidadeNaoExisteException;

    /**
     * Identifica o criador/dono de uma comunidade.
     *
     * @param nome Nome da comunidade (case-sensitive)
     * @return Login do usu�rio fundador
     * @throws ComunidadeNaoExisteException Se a comunidade n�o existir
     */
    String getDono(String nome) throws ComunidadeNaoExisteException;

    /**
     * Verifica exist�ncia de uma comunidade pelo nome.
     *
     * @param nome Nome exato da comunidade (case-sensitive)
     * @return true se a comunidade existir, false caso contr�rio
     */
    boolean existeComunidade(String nome);

    /**
     * Adiciona um usu�rio como membro de uma comunidade.
     *
     * @param comunidade Nome da comunidade alvo (case-sensitive)
     * @param membro Login do usu�rio a ser adicionado (case-sensitive)
     * @throws ComunidadeNaoExisteException Se a comunidade n�o existir
     * @throws MembroJaExisteException Se o usu�rio j� for membro
     */
    void adicionarMembro(String comunidade, String membro) throws ComunidadeNaoExisteException, MembroJaExisteException;

    /**
     * Lista todas as comunidades registradas no sistema.
     *
     * @return Lista imut�vel de nomes de comunidades (ordem alfab�tica)
     */
    List<String> getComunidades();

    /**
     * Lista comunidades das quais um usu�rio � membro.
     *
     * @param usuario Login do usu�rio (case-sensitive)
     * @return Lista imut�vel de nomes de comunidades (ordem alfab�tica)
     */
    List<String> getComunidadesDoUsuario(String usuario);


    /**
     * Remove um usu�rio de todas as comunidades.
     * <p>
     * Comportamento:
     * <ul>
     *   <li>Retira o usu�rio de todas as listas de membros</li>
     *   <li>Transfere propriedade se o usu�rio for dono de comunidades</li>
     * </ul>
     *
     * @param login Login do usu�rio a ser removido (case-sensitive)
     */
    void removerUsuario(String login);
}