package br.ufal.ic.p2.jackut.Interfaces;

import br.ufal.ic.p2.jackut.Exceptions.*;
import java.util.List;

/**
 * Interface para gerenciamento de comunidades no sistema Jackut.
 * <p>
 * Define operações para:
 * <ul>
 *   <li>Criação e remoção de comunidades</li>
 *   <li>Gestão de membros</li>
 *   <li>Consulta de informações comunitárias</li>
 *   <li>Controle de propriedade</li>
 * </ul>
 *
 * <p>Lança exceções específicas para casos de comunidades duplicadas, inexistentes e membros já cadastrados.</p>
 */

public interface IGerenciadorComunidades {
    /**
     * Cria uma nova comunidade com nome único e dados básicos.
     *
     * @param nome Nome único da comunidade (case-sensitive)
     * @param descricao Descrição detalhada da comunidade
     * @param dono Login do usuário fundador
     * @throws ComunidadeJaExisteException Se o nome já estiver em uso
     */
    void criarComunidade(String nome, String descricao, String dono) throws ComunidadeJaExisteException;

    /**
     * Recupera a descrição de uma comunidade existente.
     *
     * @param nome Nome da comunidade (case-sensitive)
     * @return Descrição original cadastrada
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     */
    String getDescricao(String nome) throws ComunidadeNaoExisteException;

    /**
     * Lista todos os membros de uma comunidade.
     *
     * @param nome Nome da comunidade (case-sensitive)
     * @return Lista imutável de logins dos membros (ordem alfabética)
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     */
    List<String> getMembros(String nome) throws ComunidadeNaoExisteException;

    /**
     * Identifica o criador/dono de uma comunidade.
     *
     * @param nome Nome da comunidade (case-sensitive)
     * @return Login do usuário fundador
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     */
    String getDono(String nome) throws ComunidadeNaoExisteException;

    /**
     * Verifica existência de uma comunidade pelo nome.
     *
     * @param nome Nome exato da comunidade (case-sensitive)
     * @return true se a comunidade existir, false caso contrário
     */
    boolean existeComunidade(String nome);

    /**
     * Adiciona um usuário como membro de uma comunidade.
     *
     * @param comunidade Nome da comunidade alvo (case-sensitive)
     * @param membro Login do usuário a ser adicionado (case-sensitive)
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     * @throws MembroJaExisteException Se o usuário já for membro
     */
    void adicionarMembro(String comunidade, String membro) throws ComunidadeNaoExisteException, MembroJaExisteException;

    /**
     * Lista todas as comunidades registradas no sistema.
     *
     * @return Lista imutável de nomes de comunidades (ordem alfabética)
     */
    List<String> getComunidades();

    /**
     * Lista comunidades das quais um usuário é membro.
     *
     * @param usuario Login do usuário (case-sensitive)
     * @return Lista imutável de nomes de comunidades (ordem alfabética)
     */
    List<String> getComunidadesDoUsuario(String usuario);


    /**
     * Remove um usuário de todas as comunidades.
     * <p>
     * Comportamento:
     * <ul>
     *   <li>Retira o usuário de todas as listas de membros</li>
     *   <li>Transfere propriedade se o usuário for dono de comunidades</li>
     * </ul>
     *
     * @param login Login do usuário a ser removido (case-sensitive)
     */
    void removerUsuario(String login);
}