package br.ufal.ic.p2.jackut.Componentes;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades;
import br.ufal.ic.p2.jackut.Users;
import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.Serializable;
import java.util.*;

public class GerenciadorAmizades implements IGerenciadorAmizades, Serializable {
    private static final long serialVersionUID = 5L;
    private final Map<String, Users> usuarios;
    private final Map<String, String> sessoes;
    private final Map<String, String> loginParaSessao;

    public GerenciadorAmizades(Map<String, Users> usuarios, Map<String, String> sessoes,
                               Map<String, String> loginParaSessao) {
        this.usuarios = usuarios;
        this.sessoes = sessoes;
        this.loginParaSessao = loginParaSessao;
    }

    @Override
    public void adicionarAmigo(String idSessao, String amigoLogin)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException {

        // 1. Verifica se o amigo existe
        if (amigoLogin == null || amigoLogin.trim().isEmpty() || !usuarios.containsKey(amigoLogin)) {
            throw new UsuarioNaoEncontradoException();
        }

        // 2. Verifica a sessão
        if (idSessao == null || idSessao.trim().isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }

        String usuarioLogin = sessoes.get(idSessao);
        if (usuarioLogin == null) {
            throw new SessaoInvalidaExecption();
        }

        // 3. Verifica auto-amizade
        if (usuarioLogin.equals(amigoLogin)) {
            throw new AmigoDeSiException();
        }

        Users usuario = usuarios.get(usuarioLogin);
        Users amigo = usuarios.get(amigoLogin);

        // 4. Verifica se já são amigos
        if (usuario.ehAmigo(amigoLogin) && amigo.ehAmigo(usuarioLogin)) {
            throw new AmigoJaExistenteException();
        }

        // 5. Verifica se já existe solicitação pendente DO USUÁRIO para O AMIGO
        if (amigo.temSolicitacaoPendente(usuarioLogin)) {
            throw new AmigoPendenteException();
        }

        // 6. Verifica se existe solicitação pendente DO AMIGO para O USUÁRIO (aceita automaticamente)
        if (usuario.temSolicitacaoPendente(amigoLogin)) {
            aceitarSolicitacao(usuarioLogin, amigoLogin);
            return;
        }

        // 7. Se nenhum dos casos acima, envia nova solicitação
        amigo.receberSolicitacao(usuarioLogin);
    }

    @Override
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return usuario.ehAmigo(amigo);
    }

    @Override
    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        Users outroUsuario = usuarios.get(amigo);
        if (usuario == null || outroUsuario == null) throw new UsuarioNaoEncontradoException();
        return usuario.ehAmigo(amigo) && outroUsuario.ehAmigo(login);
    }

    @Override
    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", usuario.getAmigos()) + "}";
    }

    @Override
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", usuario.getSolicitacoesPendentes()) + "}";
    }

    @Override
    public void aceitarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        Users user = usuarios.get(usuario);
        Users friend = usuarios.get(amigo);
        if (user == null || friend == null) throw new UsuarioNaoEncontradoException();

        if (user.aceitarSolicitacao(amigo)) {
            friend.adicionarAmigo(usuario);
        }
    }

    @Override
    public void recusarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        Users user = usuarios.get(usuario);
        if (user == null) throw new UsuarioNaoEncontradoException();
        user.getSolicitacoesPendentes().remove(amigo);
    }

    @Override
    public boolean temSolicitacaoPendente(String deUsuario, String paraUsuario) throws UsuarioNaoEncontradoException {
        Users user = usuarios.get(paraUsuario);
        if (user == null) throw new UsuarioNaoEncontradoException();
        return user.temSolicitacaoPendente(deUsuario);
    }
}