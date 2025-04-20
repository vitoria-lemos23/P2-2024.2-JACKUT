package br.ufal.ic.p2.jackut.Interfaces;

import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.Serializable;

public interface IGerenciadorAmizades extends Serializable {
        void adicionarAmigo(String idSessao, String amigoLogin)
                throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
                AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException;

        boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException;
        boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException;
        String getAmigos(String login) throws UsuarioNaoEncontradoException;
        String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException;
        void aceitarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException;
        void recusarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException;
        boolean temSolicitacaoPendente(String deUsuario, String paraUsuario) throws UsuarioNaoEncontradoException;
}