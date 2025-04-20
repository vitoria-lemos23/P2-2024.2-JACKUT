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

    @Override
    public void adicionarMembro(String comunidade, String membro) throws ComunidadeNaoExisteException, MembroJaExisteException {
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
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Comunidade> entry : comunidades.entrySet()) {
            if (entry.getValue().getMembros().contains(usuario)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}