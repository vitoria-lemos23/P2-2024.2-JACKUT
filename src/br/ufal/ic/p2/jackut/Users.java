package br.ufal.ic.p2.jackut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Users implements Serializable {
    private static final long serialVersionUID = 2L;
    private final String login;
    private final String senha;
    private final String nome;
    private final List<Atributo> atributos = new ArrayList<>();
    private final List<String> amigos = new ArrayList<>();
    private final List<String> amigosPendentes = new ArrayList<>();
    private final List<String> amigosConfirmados = new ArrayList<>();
    private final List<String> solicitacoesRecebidas = new ArrayList<>();

    // Classe interna para representar atributos
    private static class Atributo implements Serializable {
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

    // Métodos para gerenciar atributos
    public void setAtributo(String chave, String valor) {
        // Remove se já existir
        atributos.removeIf(a -> a.chave.equalsIgnoreCase(chave));
        atributos.add(new Atributo(chave, valor));
    }

    public void adicionarSolicitacaoAmizade(String amigo) {
        if (!solicitacoesRecebidas.contains(amigo)) {
            solicitacoesRecebidas.add(amigo);
        }
    }
    public void aceitarSolicitacaoAmizade(String amigo) {
        if (solicitacoesRecebidas.contains(amigo)) {
            amigosConfirmados.add(amigo);
            solicitacoesRecebidas.remove(amigo);
        }
    }
    public String getAtributo(String chave) {
        for (Atributo a : atributos) {
            if (a.chave.equalsIgnoreCase(chave)) {
                return a.valor;
            }
        }
        return null;
    }

    // Métodos para gerenciar amizades
    public void adicionarAmigo(String amigo, Users usuarioAmigo) {
        this.amigosPendentes.add(amigo);
        usuarioAmigo.amigosPendentes.add(this.getLogin());
    }

    public boolean ehAmigo(String amigo) {
        return amigos.contains(amigo);
    }

    // Getters
    public List<String> getAmigos() {
        return new ArrayList<>(amigosConfirmados);
    }

    public List<String> getSolicitacoesPendentes() {
        return new ArrayList<>(solicitacoesRecebidas);
    }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public String getNome() { return nome; }
}