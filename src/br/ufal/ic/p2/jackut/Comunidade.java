/**
 * Classe que representa uma Comunidade no sistema Jackut.
 * Gerencia membros, mensagens e atributos básicos da comunidade.
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma Comunidade no sistema Jackut, responsável por:
 * <ul>
 *   <li>Gerenciar membros e permissões</li>
 *   <li>Armazenar e distribuir mensagens comunitárias</li>
 *   <li>Manter metadados básicos (nome, descrição, dono)</li>
 *   <li>Controlar o ciclo de vida das interações comunitárias</li>
 * </ul>
 *
 * <p>Implementa {@link Serializable} para permitir persistência do estado.</p>
 */

public class Comunidade implements Serializable {
    private static final long serialVersionUID = 3L;
    private final String nome;
    private final String descricao;
    private final String dono;
    private final List<String> membros;
    private final List<String> mensagens = new ArrayList<>();

    /**
     * Constrói uma nova comunidade com configurações iniciais.
     *
     * @param nome Nome único da comunidade (case-sensitive, não nulo)
     * @param descricao Descrição detalhada (não nula)
     * @param dono Login do usuário fundador (adicionado automaticamente como membro)
     *
     * <p><b>Comportamento inicial:</b></p>
     * <ul>
     *   <li>Cria lista de membros com o dono como primeiro integrante</li>
     *   <li>Inicializa lista vazia de mensagens</li>
     *   <li>Validações devem ser feitas pela classe caller</li>
     * </ul>
     */
    public Comunidade(String nome, String descricao, String dono) {
        this.nome = nome;
        this.descricao = descricao;
        this.dono = dono;
        this.membros = new ArrayList<>();
        this.membros.add(dono);
    }

    /**
     * Adiciona um novo membro à comunidade após validações.
     *
     * @param membro Login do usuário a ser adicionado (case-sensitive)
     * @throws MembroJaExisteException Se o usuário já for membro
     *
     * <p><b>Observação de implementação:</b></p>
     * <ul>
     *   <li>Não realiza validação de existência do usuário no sistema</li>
     * </ul>
     */
    public void adicionarMembro(String membro) throws MembroJaExisteException {
        if (membros.contains(membro)) {
            throw new MembroJaExisteException();
        }
        membros.add(membro);
    }

    /**
     * Armazena nova mensagem na fila da comunidade.
     *
     * @param mensagem Texto completo da mensagem (não nulo)
     *
     * <p><b>Comportamento:</b> Mensagens são mantidas em ordem cronológica</p>
     */
    public void adicionarMensagem(String mensagem) {
        mensagens.add(mensagem);
    }

    /**
     * Recupera e remove a mensagem mais antiga da comunidade.
     *
     * @return Mensagem no formato original de envio
     * @throws SemMensagemException Se não houver mensagens disponíveis
     *
     * <p><b>Comportamento:</b></p>
     * <ul>
     *   <li>Sistema FIFO (First-In-First-Out)</li>
     *   <li>Mensagem é permanentemente removida após leitura</li>
     *   <li>Ordem estrita de chegada</li>
     * </ul>
     */
    public String lerMensagem() throws SemMensagemException {
        if (mensagens.isEmpty()) {
            throw new SemMensagemException();
        }
        return mensagens.remove(0);
    }

    /**
     * Verifica participação de usuário na comunidade.
     *
     * @param membro Login do usuário (case-sensitive)
     * @return true se for membro ativo, false caso contrário
     */
    public boolean isMembro(String membro) {
        return membros.contains(membro);
    }

    /**
     * Remove um membro da comunidade sem realizar validações adicionais.
     *
     * <p><b>Comportamento da operação:</b></p>
     * <ul>
     *   <li>Remove o login especificado da lista de membros</li>
     *   <li>Não verifica se o membro existe previamente na lista</li>
     *   <li>Não realiza nenhuma ação se o membro não for encontrado</li>
     * </ul>
     *
     * <p><b>Considerações importantes:</b></p>
     * <ul>
     *   <li>Não verifica permissões do solicitante</li>
     *   <li>Não valida a existência do usuário no sistema</li>
     *   <li>Não atualiza relações sociais ou mensagens do membro removido</li>
     * </ul>
     *
     * @param membro Login do usuário a ser removido (case-sensitive)
     *
     * @see #adicionarMembro(String) Para operação inversa
     */
    public void removerMembro(String membro) {
        membros.remove(membro);
    }

    // Getters
    /**
     * @return Nome imutável da comunidade (case-sensitive)
     */
    public String getNome() { return nome; }

    /**
     * @return Descrição original da comunidade
     */
    public String getDescricao() { return descricao; }

    /**
     * @return Login do criador/fundador (nunca alterado)
     */
    public String getDono() { return dono; }


    /**
     * @return Cópia defensiva da lista de membros (não modificável externamente)
     */
    public List<String> getMembros() { return new ArrayList<>(membros); }


}