/**
 * Pacote principal do sistema Jackut, contendo as classes de modelo do sistema.
 */
package br.ufal.ic.p2.jackut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Classe que representa um usuário do sistema Jackut.
 * Armazena todas as informações pessoais, relacionamentos e mensagens de um usuário.
 *
 * <p>Esta classe é responsável por gerenciar:</p>
 * <ul>
 *   <li>Dados pessoais do usuário (login, senha, nome)</li>
 *   <li>Lista de amigos</li>
 *   <li>Solicitações de amizade pendentes</li>
 *   <li>Atributos adicionais do perfil</li>
 *   <li>Mensagens recebidas</li>
 * </ul>
 *
 * @author [Seu Nome ou Equipe]
 * @version 2.0
 * @since 1.0
 */
public class Users implements Serializable {
    private static final long serialVersionUID = 2L;
    private final String login;
    private final String senha;
    private final String nome;
    private final List<String> amigos = new ArrayList<>();
    private final List<String> solicitacoesRecebidas = new ArrayList<>();
    private final List<Atributo> atributos = new ArrayList<>();
    private Queue<String> mensagens = new LinkedList<>();

    /**
     * Classe interna que representa um atributo adicional do perfil do usuário.
     * Cada atributo possui uma chave e um valor associado.
     */
    public static class Atributo implements Serializable {
        private static final long serialVersionUID = 1L;
        final String chave;
        final String valor;

        /**
         * Cria um novo atributo para o perfil do usuário.
         *
         * @param chave Nome do atributo (será convertido para lowercase)
         * @param valor Valor do atributo
         */
        public Atributo(String chave, String valor) {
            this.chave = chave.toLowerCase();
            this.valor = valor;
        }
    }

    /**
     * Constrói um novo usuário com os dados básicos.
     *
     * @param login Identificador único do usuário (não pode ser nulo ou vazio)
     * @param senha Senha do usuário (não pode ser nula ou vazia)
     * @param nome Nome completo do usuário (não pode ser nulo)
     */
    public Users(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
    }

    /**
     * Obtém o login do usuário.
     *
     * @return Login do usuário
     */
    public String getLogin() { return login; }

    /**
     * Obtém a senha do usuário.
     *
     * @return Senha do usuário
     */
    public String getSenha() { return senha; }

    /**
     * Obtém o nome do usuário.
     *
     * @return Nome completo do usuário
     */
    public String getNome() { return nome; }

    /**
     * Recebe uma nova solicitação de amizade.
     *
     * @param deUsuario Login do usuário que enviou a solicitação
     */
    public void receberSolicitacao(String deUsuario) {
        if (!amigos.contains(deUsuario) && !solicitacoesRecebidas.contains(deUsuario)) {
            solicitacoesRecebidas.add(deUsuario);
        }
    }

    /**
     * Aceita uma solicitação de amizade pendente.
     *
     * @param deUsuario Login do usuário cuja solicitação será aceita
     * @return true se a solicitação existia e foi aceita, false caso contrário
     */
    public boolean aceitarSolicitacao(String deUsuario) {
        if (solicitacoesRecebidas.remove(deUsuario)) {
            if (!amigos.contains(deUsuario)) {
                amigos.add(deUsuario);
            }
            return true;
        }
        return false;
    }

    /**
     * Verifica se um usuário é amigo.
     *
     * @param usuario Login do usuário a ser verificado
     * @return true se for amigo, false caso contrário
     */
    public boolean ehAmigo(String usuario) {
        return amigos.contains(usuario);
    }

    /**
     * Obtém o valor de um atributo do perfil.
     *
     * @param chave Nome do atributo a ser obtido (case insensitive)
     * @return Valor do atributo ou null se não existir
     */
    public String getAtributo(String chave) {
        for (Atributo a : atributos) {
            if (a.chave.equalsIgnoreCase(chave)) {
                return a.valor;
            }
        }
        return null;
    }

    /**
     * Define ou atualiza um atributo do perfil.
     *
     * @param chave Nome do atributo (case insensitive)
     * @param valor Novo valor do atributo
     */
    public void setAtributo(String chave, String valor) {
        atributos.removeIf(a -> a.chave.equalsIgnoreCase(chave));
        atributos.add(new Atributo(chave, valor));
    }

    /**
     * Obtém uma cópia da lista de amigos.
     *
     * @return Lista contendo os logins dos amigos
     */
    public List<String> getAmigos() {
        return new ArrayList<>(amigos);
    }

    /**
     * Obtém uma cópia das solicitações de amizade pendentes.
     *
     * @return Lista contendo os logins dos solicitantes
     */
    public List<String> getSolicitacoesPendentes() {
        return new ArrayList<>(solicitacoesRecebidas);
    }

    /**
     * Verifica se existe solicitação pendente de um usuário específico.
     *
     * @param deUsuario Login do usuário a verificar
     * @return true se existir solicitação pendente, false caso contrário
     */
    public boolean temSolicitacaoPendente(String deUsuario) {
        return solicitacoesRecebidas.contains(deUsuario);
    }

    /**
     * Adiciona um usuário à lista de amigos sem verificação.
     *
     * @param amigo Login do usuário a ser adicionado como amigo
     */
    public void adicionarAmigo(String amigo) {
        if (!amigos.contains(amigo)) {
            amigos.add(amigo);
        }
    }

    /**
     * Recebe um novo recado na fila de mensagens.
     *
     * @param recado Texto do recado recebido
     */
    public void receberRecado(String recado) {
        mensagens.add(recado);
    }

    /**
     * Lê e remove o recado mais antigo da fila (FIFO).
     *
     * @return O recado mais antigo ou null se não houver recados
     */
    public String lerRecado() {
        return mensagens.poll();
    }
}