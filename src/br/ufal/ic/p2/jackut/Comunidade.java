/**
 * Classe que representa uma Comunidade no sistema Jackut.
 * Gerencia membros, mensagens e atributos b�sicos da comunidade.
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa uma Comunidade no sistema Jackut, respons�vel por:
 * <ul>
 *   <li>Gerenciar membros e permiss�es</li>
 *   <li>Armazenar e distribuir mensagens comunit�rias</li>
 *   <li>Manter metadados b�sicos (nome, descri��o, dono)</li>
 *   <li>Controlar o ciclo de vida das intera��es comunit�rias</li>
 * </ul>
 *
 * <p>Implementa {@link Serializable} para permitir persist�ncia do estado.</p>
 */

public class Comunidade implements Serializable {
    private static final long serialVersionUID = 3L;
    private final String nome;
    private final String descricao;
    private final String dono;
    private final List<String> membros;
    private final List<String> mensagens = new ArrayList<>();

    /**
     * Constr�i uma nova comunidade com configura��es iniciais.
     *
     * @param nome Nome �nico da comunidade (case-sensitive, n�o nulo)
     * @param descricao Descri��o detalhada (n�o nula)
     * @param dono Login do usu�rio fundador (adicionado automaticamente como membro)
     *
     * @implNote Comportamento inicial:
     * <ul>
     *   <li>Cria lista de membros com o dono como primeiro integrante</li>
     *   <li>Inicializa lista vazia de mensagens</li>
     *   <li>Valida��es devem ser feitas pela classe caller</li>
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
     * Adiciona um novo membro � comunidade ap�s valida��es.
     *
     * @param membro Login do usu�rio a ser adicionado (case-sensitive)
     * @throws MembroJaExisteException Se o usu�rio j� for membro
     *
     * @implNote N�o realiza valida��o de exist�ncia do usu�rio no sistema
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
     * @param mensagem Texto completo da mensagem (n�o nulo)
     *
     * @implNote Mensagens s�o mantidas em ordem cronol�gica
     */
    public void adicionarMensagem(String mensagem) {
        mensagens.add(mensagem);
    }

    /**
     * Recupera e remove a mensagem mais antiga da comunidade.
     *
     * @return Mensagem no formato original de envio
     * @throws SemMensagemException Se n�o houver mensagens dispon�veis
     *
     * @implNote Sistema FIFO (First-In-First-Out)
     */
    public String lerMensagem() throws SemMensagemException {
        if (mensagens.isEmpty()) {
            throw new SemMensagemException();
        }
        return mensagens.remove(0);
    }

    /**
     * Verifica participa��o de usu�rio na comunidade.
     *
     * @param membro Login do usu�rio (case-sensitive)
     * @return true se for membro ativo, false caso contr�rio
     */
    public boolean isMembro(String membro) {
        return membros.contains(membro);
    }

    /**
     * Remove usu�rio da lista de membros sem valida��es adicionais.
     *
     * @param membro Login do usu�rio a ser removido
     *
     * @implNote N�o verifica permiss�es ou exist�ncia pr�via
     */


    /**
     * Remove um membro da comunidade sem realizar valida��es adicionais.
     * <p>
     * Comportamento da opera��o:
     * <ul>
     *   <li>Remove o login especificado da lista de membros</li>
     *   <li>N�o verifica se o membro existe previamente na lista</li>
     *   <li>N�o realiza nenhuma a��o se o membro n�o for encontrado</li>
     * </ul>
     *
     * @param membro Login do usu�rio a ser removido (case-sensitive)
     *
     * @implNote Considera��es importantes:
     * <ul>
     *   <li>N�o verifica permiss�es do solicitante</li>
     *   <li>N�o valida a exist�ncia do usu�rio no sistema</li>
     *   <li>N�o atualiza rela��es sociais ou mensagens do membro removido</li>
     * </ul>
     *
     * @see #adicionarMembro(String) Para opera��o inversa
     */
    public void removerMembro(String membro) {
        membros.remove(membro);
    }

    // Getters
    /**
     * @return Nome imut�vel da comunidade (case-sensitive)
     */
    public String getNome() { return nome; }

    /**
     * @return Descri��o original da comunidade
     */
    public String getDescricao() { return descricao; }

    /**
     * @return Login do criador/fundador (nunca alterado)
     */
    public String getDono() { return dono; }


    /**
     * @return C�pia defensiva da lista de membros (n�o modific�vel externamente)
     */
    public List<String> getMembros() { return new ArrayList<>(membros); }


}