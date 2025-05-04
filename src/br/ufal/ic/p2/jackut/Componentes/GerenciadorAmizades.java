package br.ufal.ic.p2.jackut.Componentes;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades;
import br.ufal.ic.p2.jackut.Users;
import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.Serializable;
import java.util.*;

/**
 * Gerencia todas as opera��es relacionadas a amizades e relacionamentos sociais no sistema Jackut.
 * <p>
 * Principais responsabilidades:
 * <ul>
 *   <li>Processar solicita��es de amizade entre usu�rios</li>
 *   <li>Gerenciar listas de amigos e solicita��es pendentes</li>
 *   <li>Validar rela��es sociais de acordo com as regras do sistema</li>
 *   <li>Garantir consist�ncia nas opera��es de amizade</li>
 * </ul>
 *
 * <p>Implementa {@link Serializable} para permitir persist�ncia do estado das rela��es.</p>
 */
public class GerenciadorAmizades implements IGerenciadorAmizades, Serializable {

    /**
     * Identificador de vers�o para controle de serializa��o.
     * <p>
     * Garante compatibilidade entre vers�es diferentes da classe durante desserializa��o.
     */
    private static final long serialVersionUID = 5L;

    /**
     * Mapa de usu�rios registrados (login ? objeto Users) para acesso �s rela��es sociais
     */
    private final Map<String, Users> usuarios;

    /**
     * Mapa de sess�es ativas (ID sess�o ? login) para valida��o de autentica��o
     */
    private final Map<String, String> sessoes;

    /**
     * Mapa reverso de sess�es (login ? ID sess�o) para controle de sess�es �nicas
     */
    private final Map<String, String> loginParaSessao;

    /**
     * Constr�i o gerenciador com depend�ncias necess�rias para opera��o completa.
     *
     * @param usuarios Mapa principal de usu�rios do sistema
     * @param sessoes Registro de sess�es ativas
     * @param loginParaSessao Mapeamento reverso para controle de sess�es
     */
    public GerenciadorAmizades(Map<String, Users> usuarios, Map<String, String> sessoes,
                               Map<String, String> loginParaSessao) {
        this.usuarios = usuarios;
        this.sessoes = sessoes;
        this.loginParaSessao = loginParaSessao;
    }

    /**
     * Processa solicita��o de amizade entre usu�rios ap�s m�ltiplas valida��es.
     * <p>
     * Fluxo completo de opera��o:
     * <ol>
     *   <li>Valida exist�ncia do usu�rio alvo</li>
     *   <li>Verifica autentica��o do solicitante via sess�o</li>
     *   <li>Impede auto-amizade</li>
     *   <li>Verifica se j� s�o amigos m�tuos</li>
     *   <li>Checa solicita��es pendentes existentes</li>
     *   <li>Aceita automaticamente solicita��es m�tuas pendentes</li>
     *   <li>Registra nova solicita��o se v�lido</li>
     * </ol>
     *
     * @param idSessao ID da sess�o v�lida do solicitante
     * @param amigoLogin Login do usu�rio alvo da amizade
     * @throws UsuarioNaoEncontradoException Se o amigo n�o existir ou sess�o inv�lida
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida/expirada
     * @throws AmigoDeSiException Se tentar adicionar a si mesmo como amigo
     * @throws AmigoJaExistenteException Se j� existir amizade m�tua
     * @throws AmigoPendenteException Se j� houver solicita��o pendente para este amigo
     *
     * <p><b>Exemplo de aceita��o autom�tica:</b><br>
     * Se Jo�o tem solicita��o pendente de Maria, quando Maria enviar nova solicita��o,
     * o sistema aceitar� automaticamente criando amizade m�tua</p>
     */
    @Override
    public void adicionarAmigo(String idSessao, String amigoLogin)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException {

        // 1. Verifica se o amigo existe
        if (amigoLogin == null || amigoLogin.trim().isEmpty() || !usuarios.containsKey(amigoLogin)) {
            throw new UsuarioNaoEncontradoException();
        }

        // 2. Valida a sess�o primeiro
        if (idSessao == null || idSessao.trim().isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }



        String usuarioLogin = sessoes.get(idSessao);
       if (usuarioLogin == null) {
            throw new UsuarioNaoEncontradoException();
        }

        // 3. Demais valida��es (auto-amizade, solicita��es, etc.)
        if (usuarioLogin.equals(amigoLogin)) {
            throw new AmigoDeSiException();
        }

        Users usuario = usuarios.get(usuarioLogin);
        Users amigo = usuarios.get(amigoLogin);

        if (usuario.ehAmigo(amigoLogin) && amigo.ehAmigo(usuarioLogin)) {
            throw new AmigoJaExistenteException();
        }

        if (amigo.temSolicitacaoPendente(usuarioLogin)) {
            throw new AmigoPendenteException();
        }

        if (usuario.temSolicitacaoPendente(amigoLogin)) {
            aceitarSolicitacao(usuarioLogin, amigoLogin);
            return;
        }

        amigo.receberSolicitacao(usuarioLogin);
    }

    /**
     * Verifica rela��o de amizade unilateral entre usu�rios.
     *
     * @param login Usu�rio base para verifica��o
     * @param amigo Usu�rio alvo da verifica��o
     * @return true se o usu�rio base tiver o alvo em sua lista de amigos
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
     *
     * <p><b>Nota:</b> N�o verifica reciprocidade (consulte {@link #ehAmigoMutuo})</p>
     */
    @Override
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return usuario.ehAmigo(amigo);
    }

    /**
     * Verifica amizade m�tua entre dois usu�rios.
     *
     * @param login Primeiro usu�rio para verifica��o
     * @param amigo Segundo usu�rio para verifica��o
     * @return true se ambos estiverem na lista de amigos um do outro
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
     */
    @Override
    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        Users outroUsuario = usuarios.get(amigo);
        if (usuario == null || outroUsuario == null) throw new UsuarioNaoEncontradoException();
        return usuario.ehAmigo(amigo) && outroUsuario.ehAmigo(login);
    }


    /**
     * Retorna lista formatada de amigos de um usu�rio.
     *
     * @param login Usu�rio alvo da consulta
     * @return String no formato "{amigo1,amigo2,...}" ou "{}" se vazio
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o existir
     *
     */
    @Override
    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", usuario.getAmigos()) + "}";
    }

    /**
     * Retorna solicita��es de amizade pendentes formatadas.
     *
     * @param login Usu�rio alvo da consulta
     * @return String no formato "{solicitante1,solicitante2,...}" ou "{}"
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o existir
     */
    @Override
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", usuario.getSolicitacoesPendentes()) + "}";
    }

    /**
     * Aceita uma solicita��o de amizade pendente, estabelecendo rela��o m�tua.
     * <p>
     * Efeitos colaterais:
     * <ul>
     *   <li>Remove solicita��o da lista de pend�ncias</li>
     *   <li>Adiciona ambos usu�rios como amigos m�tuos</li>
     *   <li>Notifica ambos usu�rios via sistema</li>
     * </ul>
     *
     * @param usuario Usu�rio que est� aceitando a solicita��o
     * @param amigo Usu�rio que enviou a solicita��o
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
     */
    @Override
    public void aceitarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        Users user = usuarios.get(usuario);
        Users friend = usuarios.get(amigo);
        if (user == null || friend == null) throw new UsuarioNaoEncontradoException();

        if (user.aceitarSolicitacao(amigo)) {
            friend.adicionarAmigo(usuario);
        }
    }

    /**
     * Recusa permanentemente uma solicita��o de amizade.
     * <p>
     * Comportamento:
     * <ul>
     *   <li>Remove solicita��o da lista de pend�ncias</li>
     *   <li>N�o notifica o solicitante</li>
     *   <li>N�o registra hist�rico da opera��o</li>
     * </ul>
     *
     * @param usuario Usu�rio que est� recusando
     * @param amigo Usu�rio solicitante
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o existir
     */
    @Override
    public void recusarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        Users user = usuarios.get(usuario);
        if (user == null) throw new UsuarioNaoEncontradoException();
        user.getSolicitacoesPendentes().remove(amigo);
    }


    /**
     * Verifica exist�ncia de solicita��o pendente entre usu�rios.
     *
     * @param deUsuario Poss�vel solicitante
     * @param paraUsuario Poss�vel receptor
     * @return true se existir solicita��o n�o respondida do solicitante para o receptor
     * @throws UsuarioNaoEncontradoException Se o receptor n�o existir
     */
    @Override
    public boolean temSolicitacaoPendente(String deUsuario, String paraUsuario) throws UsuarioNaoEncontradoException {
        Users user = usuarios.get(paraUsuario);
        if (user == null) throw new UsuarioNaoEncontradoException();
        return user.temSolicitacaoPendente(deUsuario);
    }
}