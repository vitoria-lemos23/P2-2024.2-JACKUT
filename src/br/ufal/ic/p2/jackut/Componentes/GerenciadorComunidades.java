package br.ufal.ic.p2.jackut.Componentes;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;
import br.ufal.ic.p2.jackut.Comunidade;
import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.Serializable;
import java.util.*;

public class GerenciadorComunidades implements IGerenciadorComunidades, Serializable {
    private static final long serialVersionUID = 4L;
    private final Map<String, Comunidade> comunidades;

    public GerenciadorComunidades() {
        this.comunidades = new HashMap<>();
    }

    @Override
    public void criarComunidade(String nome, String descricao, String dono)
            throws ComunidadeJaExisteException {
        // Verificação case-sensitive
        if (comunidades.containsKey(nome)) {
            throw new ComunidadeJaExisteException();
        }
        comunidades.put(nome, new Comunidade(nome, descricao, dono));
    }

    @Override
    public String getDescricao(String nome) throws ComunidadeNaoExisteException {
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        return comunidades.get(nome).getDescricao();
    }

    // Em GerenciadorComunidades.java
    @Override
    public List<String> getMembros(String nome) throws ComunidadeNaoExisteException {
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        return comunidades.get(nome).getMembros();
    }

    @Override
    public String getDono(String nome) throws ComunidadeNaoExisteException {
        if (!comunidades.containsKey(nome)) {
            throw new ComunidadeNaoExisteException();
        }
        return comunidades.get(nome).getDono();
    }

    @Override
    public boolean existeComunidade(String nome) {
        return comunidades.containsKey(nome);
    }

    // In GerenciadorComunidades.java, ensure adicionarMembro adds the member
    @Override
    public void adicionarMembro(String comunidade, String membro)
            throws ComunidadeNaoExisteException, MembroJaExisteException {
        if (!comunidades.containsKey(comunidade)) {
            throw new ComunidadeNaoExisteException();
        }
        comunidades.get(comunidade).adicionarMembro(membro);
    }

    @Override
    public List<String> getComunidades() {
        return new ArrayList<>(comunidades.keySet());
    }

    @Override
    public List<String> getComunidadesDoUsuario(String usuario) {
        List<String> owned = new ArrayList<>();
        List<String> member = new ArrayList<>();

        for (Map.Entry<String, Comunidade> entry : comunidades.entrySet()) {
            String nomeComunidade = entry.getKey();
            Comunidade comunidade = entry.getValue();

            if (comunidade.getMembros().contains(usuario)) {
                if (comunidade.getDono().equals(usuario)) {
                    owned.add(nomeComunidade); // Communities owned by the user
                } else {
                    member.add(nomeComunidade); // Communities the user joined
                }
            }
        }

        // Sort both lists alphabetically
        Collections.sort(owned);
        Collections.sort(member);

        // Combine lists: owned first, then member
        owned.addAll(member);
        return owned;
    }

    public void removerUsuario(String login) {
        List<String> comunidadesParaRemover = new ArrayList<>();
        for (Comunidade comunidade : comunidades.values()) {
            if (comunidade.getDono().equals(login)) {
                comunidadesParaRemover.add(comunidade.getNome());
            }
        }
        for (String nome : comunidadesParaRemover) {
            comunidades.remove(nome);
        }

        for (Comunidade comunidade : comunidades.values()) {
            if (comunidade.isMembro(login)) {
                comunidade.removerMembro(login);
            }
        }
    }
}