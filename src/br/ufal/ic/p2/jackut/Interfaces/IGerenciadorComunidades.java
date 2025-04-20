package br.ufal.ic.p2.jackut.Interfaces;

import br.ufal.ic.p2.jackut.Exceptions.*;
import java.util.List;

public interface IGerenciadorComunidades {
    void criarComunidade(String nome, String descricao, String dono) throws ComunidadeJaExisteException;
    String getDescricao(String nome) throws ComunidadeNaoExisteException;
    List<String> getMembros(String nome) throws ComunidadeNaoExisteException;
    String getDono(String nome) throws ComunidadeNaoExisteException;
    boolean existeComunidade(String nome);
    void adicionarMembro(String comunidade, String membro) throws ComunidadeNaoExisteException, MembroJaExisteException;
    List<String> getComunidades();
    List<String> getComunidadesDoUsuario(String usuario);
}