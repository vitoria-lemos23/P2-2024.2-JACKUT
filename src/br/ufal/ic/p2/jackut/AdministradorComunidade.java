package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class AdministradorComunidade extends Users {
    private List<String> comunidadesAdministradas;

    public AdministradorComunidade(String login, String senha, String nome) {
        super(login, senha, nome);
        this.comunidadesAdministradas = new ArrayList<>();
    }

    public void criarComunidade(String nome, String descricao, Jackut sistema)
            throws ComunidadeJaExisteException {
        if (sistema.existeComunidade(nome)) {
            throw new ComunidadeJaExisteException();
        }
        sistema.registrarComunidade(nome, descricao, this.getLogin());
        this.comunidadesAdministradas.add(nome);
    }

    public boolean podeGerenciarComunidade(String nomeComunidade, Jackut sistema) throws ComudadeNaoExisteException {
        return sistema.getDonoComunidade(nomeComunidade).equals(this.getLogin());
    }
}