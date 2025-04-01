package br.ufal.ic.p2.jackut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Users implements Serializable {
    private static final long serialVersionUID = 2L;
    private final String login;
    private final String senha;
    private final String nome;
    private final List<String> amigos = new ArrayList<>();
    private final List<String> solicitacoesRecebidas = new ArrayList<>();
    private final List<Atributo> atributos = new ArrayList<>();

    private Queue<String> mensagens = new LinkedList<>();

    public void receberRecado(String recado) {
        mensagens.add(recado);
    }

    public String lerRecado() {
        return mensagens.poll(); // Retorna null se vazio
    }


    public static class Atributo implements Serializable {
        private static final long serialVersionUID = 1L;
        final String chave;
        final String valor;

        public Atributo(String chave, String valor) {
            this.chave = chave.toLowerCase();
            this.valor = valor;
        }
    }

    public Users(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
    }

    public void receberSolicitacao(String deUsuario) {
        if (!amigos.contains(deUsuario) && !solicitacoesRecebidas.contains(deUsuario)) {
            solicitacoesRecebidas.add(deUsuario);
        }
    }

    public boolean aceitarSolicitacao(String deUsuario) {
        if (solicitacoesRecebidas.remove(deUsuario)) {
            if (!amigos.contains(deUsuario)) {
                amigos.add(deUsuario);
            }
            return true;
        }
        return false;
    }

    public boolean ehAmigo(String usuario) {
        return amigos.contains(usuario);
    }

    public String getAtributo(String chave) {
        for (Atributo a : atributos) {
            if (a.chave.equalsIgnoreCase(chave)) {
                return a.valor;
            }
        }
        return null;
    }

    public void setAtributo(String chave, String valor) {
        atributos.removeIf(a -> a.chave.equalsIgnoreCase(chave));
        atributos.add(new Atributo(chave, valor));
    }

    public List<String> getAmigos() {
        return new ArrayList<>(amigos);
    }

    public List<String> getSolicitacoesPendentes() {
        return new ArrayList<>(solicitacoesRecebidas);
    }

    // Método auxiliar para verificar se há solicitação pendente de um usuário
    public boolean temSolicitacaoPendente(String deUsuario) {
        return solicitacoesRecebidas.contains(deUsuario);
    }

    // Método auxiliar para registrar a amizade (confirmação) no usuário
    public void adicionarAmigo(String amigo) {
        if (!amigos.contains(amigo)) {
            amigos.add(amigo);
        }
    }

    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public String getNome() { return nome; }
}
