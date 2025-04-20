package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.SemMensagemException;
import br.ufal.ic.p2.jackut.Exceptions.UsuarioJaEhIdoloException;
import br.ufal.ic.p2.jackut.Exceptions.UsuarioJaEhInimigoException;
import br.ufal.ic.p2.jackut.Exceptions.UsuarioJaEhPaqueraException;
import br.ufal.ic.p2.jackut.Mensagem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Classe que representa um usuário no sistema Jackut, contendo informações de perfil,
 * relacionamentos, mensagens e funcionalidades de gerenciamento.
 * <p>
 * Implementa {@link Serializable} para permitir serialização dos dados do usuário.
 */
public class Users implements Serializable {
    private static final long serialVersionUID = 2L;
    private final String login;
    private final String senha;
    private final String nome;
    private final List<String> amigos = new ArrayList<>();
    private final List<String> solicitacoesRecebidas = new ArrayList<>();
    private final List<Atributo> atributos = new ArrayList<>();
    private Queue<Mensagem> mensagens = new LinkedList<>();
    private Queue<String> mensagensComunidade = new LinkedList<>();
    private List<String> fas = new ArrayList<>();
    private List<String> idolos = new ArrayList<>();
    private List<String> paqueras = new ArrayList<>();
    private List<String> inimigos = new ArrayList<>();

    /**
     * Classe interna que representa um atributo personalizado do usuário.
     * Armazena pares chave-valor (case-insensitive).
     */
    public static class Atributo implements Serializable {
        private static final long serialVersionUID = 1L;
        final String chave;
        final String valor;

        /**
         * Constrói um atributo com chave e valor.
         * @param chave Nome do atributo (convertido para minúsculas)
         * @param valor Valor associado à chave
         */
        public Atributo(String chave, String valor) {
            this.chave = chave.toLowerCase();
            this.valor = valor;
        }
    }

    // ========== CONSTRUTOR ==========

    /**
     * Constrói um novo usuário com login, senha e nome.
     * @param login Identificador único do usuário
     * @param senha Senha de acesso
     * @param nome Nome de exibição
     */
    public Users(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
    }

    // ========== MÉTODOS DE ACESSO ==========

    /** @return Login do usuário */
    public String getLogin() { return login; }

    /** @return Senha do usuário */
    public String getSenha() { return senha; }

    /** @return Nome do usuário */
    public String getNome() { return nome; }

    // ========== GERENCIAMENTO DE AMIZADES ==========

    /**
     * Registra uma solicitação de amizade recebida.
     * @param deUsuario Login do usuário que enviou a solicitação
     */
    public void receberSolicitacao(String deUsuario) {
        if (!amigos.contains(deUsuario) && !solicitacoesRecebidas.contains(deUsuario)) {
            solicitacoesRecebidas.add(deUsuario);
        }
    }

    /**
     * Aceita uma solicitação de amizade pendente.
     * @param deUsuario Login do solicitante
     * @return true se a solicitação foi aceita, false caso contrário
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
     * @param usuario Login do usuário a ser verificado
     * @return true se for amigo, false caso contrário
     */
    public boolean ehAmigo(String usuario) { return amigos.contains(usuario); }

    /** @return Lista de amigos do usuário */
    public List<String> getAmigos() { return new ArrayList<>(amigos); }

    /** @return Lista de solicitações de amizade pendentes */
    public List<String> getSolicitacoesPendentes() { return new ArrayList<>(solicitacoesRecebidas); }

    /**
     * Verifica se há uma solicitação pendente de um usuário específico.
     * @param deUsuario Login do solicitante
     * @return true se houver solicitação pendente, false caso contrário
     */
    public boolean temSolicitacaoPendente(String deUsuario) { return solicitacoesRecebidas.contains(deUsuario); }

    /**
     * Adiciona um usuário à lista de amigos.
     * @param amigo Login do amigo a ser adicionado
     */
    public void adicionarAmigo(String amigo) {
        if (!amigos.contains(amigo)) {
            amigos.add(amigo);
        }
    }

    // ========== GERENCIAMENTO DE ATRIBUTOS ==========

    /**
     * Obtém o valor de um atributo pelo nome.
     * @param chave Nome do atributo (case-insensitive)
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
     * Define ou atualiza um atributo do usuário.
     * @param chave Nome do atributo (case-insensitive)
     * @param valor Novo valor do atributo
     */
    public void setAtributo(String chave, String valor) {
        atributos.removeIf(a -> a.chave.equalsIgnoreCase(chave));
        atributos.add(new Atributo(chave, valor));
    }

    // ========== GERENCIAMENTO DE MENSAGENS ==========

    /**
     * Adiciona um recado à caixa de entrada do usuário.
     * @param remetente Login do remetente
     * @param recado Conteúdo da mensagem
     */
    public void receberRecado(String remetente, String recado) {
        mensagens.add(new Mensagem(remetente, recado));
    }

    /**
     * Remove todas as mensagens de um remetente específico.
     * @param remetente Login do remetente
     */
    public void removerMensagensDoUsuario(String remetente) {
        mensagens.removeIf(m -> m.getRemetente().equals(remetente));
    }

    /**
     * Lê e remove o recado mais antigo da fila.
     * @return Conteúdo do recado ou null se a fila estiver vazia
     */
    public String lerRecado() {
        Mensagem msg = mensagens.poll();
        return msg != null ? msg.getConteudo() : null;
    }

    // ========== GERENCIAMENTO DE COMUNIDADES ==========

    /**
     * Recebe uma mensagem de comunidade.
     * @param mensagem Conteúdo da mensagem
     */
    public void receberMensagemComunidade(String mensagem) {
        mensagensComunidade.add(mensagem);
    }

    /** @return true se houver mensagens de comunidade pendentes */
    public boolean temMensagensComunidade() { return !mensagensComunidade.isEmpty(); }

    /**
     * Lê e remove a mensagem de comunidade mais antiga.
     * @return Conteúdo da mensagem
     * @throws SemMensagemException Se não houver mensagens disponíveis
     */
    public String lerMensagemComunidade() throws SemMensagemException {
        if (mensagensComunidade.isEmpty()) {
            throw new SemMensagemException();
        }
        return mensagensComunidade.poll();
    }

    // ========== RELACIONAMENTOS (ADICIONAR/REMOVER) ==========

    /**
     * Adiciona um ídolo à lista do usuário.
     * @param idolo Login do ídolo
     * @throws UsuarioJaEhIdoloException Se o ídolo já estiver na lista
     */
    public void adicionarIdolo(String idolo) throws UsuarioJaEhIdoloException {
        if (idolos.contains(idolo)) {
            throw new UsuarioJaEhIdoloException();
        }
        idolos.add(idolo);
    }

    /**
     * Adiciona um fã à lista do usuário.
     * @param fa Login do fã
     */
    public void adicionarFa(String fa) {
        if (!fas.contains(fa)) {
            fas.add(fa);
        }
    }

    /**
     * Adiciona um inimigo à lista do usuário.
     * @param inimigo Login do inimigo
     * @throws UsuarioJaEhInimigoException Se o inimigo já estiver na lista
     */
    public void adicionarInimigo(String inimigo) throws UsuarioJaEhInimigoException {
        if (inimigos.contains(inimigo)) {
            throw new UsuarioJaEhInimigoException();
        }
        inimigos.add(inimigo);
    }

    /**
     * Adiciona uma paquera à lista do usuário.
     * @param paquera Login da paquera
     * @throws UsuarioJaEhPaqueraException Se a paquera já estiver na lista
     */
    public void adicionarPaquera(String paquera) throws UsuarioJaEhPaqueraException {
        if (this.paqueras.contains(paquera)) {
            throw new UsuarioJaEhPaqueraException();
        }
        this.paqueras.add(paquera);
    }

    // ========== MÉTODOS DE REMOÇÃO ==========

    /** @param amigo Login do amigo a ser removido */
    public void removerAmigo(String amigo) { amigos.remove(amigo); }

    /** @param solicitante Login do solicitante a ser removido */
    public void removerSolicitacao(String solicitante) { solicitacoesRecebidas.remove(solicitante); }

    /** @param fa Login do fã a ser removido */
    public void removerFa(String fa) { fas.remove(fa); }

    /** @param idolo Login do ídolo a ser removido */
    public void removerIdolo(String idolo) { idolos.remove(idolo); }

    /** @param paquera Login da paquera a ser removida */
    public void removerPaquera(String paquera) { paqueras.remove(paquera); }

    /** @param inimigo Login do inimigo a ser removido */
    public void removerInimigo(String inimigo) { inimigos.remove(inimigo); }

    // ========== GETTERS DE RELACIONAMENTOS ==========

    /** @return Lista de fãs do usuário */
    public List<String> getFas() { return new ArrayList<>(fas); }

    /** @return Lista de ídolos do usuário */
    public List<String> getIdolos() { return new ArrayList<>(idolos); }

    /** @return Lista de paqueras do usuário */
    public List<String> getPaqueras() { return new ArrayList<>(paqueras); }

    /** @return Lista de inimigos do usuário */
    public List<String> getInimigos() { return new ArrayList<>(inimigos); }
}