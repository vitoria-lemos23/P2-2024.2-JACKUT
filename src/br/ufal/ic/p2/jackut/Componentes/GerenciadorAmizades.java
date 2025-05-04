package br.ufal.ic.p2.jackut.Componentes;

import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades;
import br.ufal.ic.p2.jackut.Users;
import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.Serializable;
import java.util.*;

/**
 * Gerencia todas as operações relacionadas a amizades e relacionamentos sociais no sistema Jackut.
 * <p>
 * Principais responsabilidades:
 * <ul>
 *   <li>Processar solicitações de amizade entre usuários</li>
 *   <li>Gerenciar listas de amigos e solicitações pendentes</li>
 *   <li>Validar relações sociais de acordo com as regras do sistema</li>
 *   <li>Garantir consistência nas operações de amizade</li>
 * </ul>
 *
 * <p>Implementa {@link Serializable} para permitir persistência do estado das relações.</p>
 */
public class GerenciadorAmizades implements IGerenciadorAmizades, Serializable {

    /**
     * Identificador de versão para controle de serialização.
     * <p>
     * Garante compatibilidade entre versões diferentes da classe durante desserialização.
     */
    private static final long serialVersionUID = 5L;

    /**
     * Mapa de usuários registrados (login ? objeto Users) para acesso às relações sociais
     */
    private final Map<String, Users> usuarios;

    /**
     * Mapa de sessões ativas (ID sessão ? login) para validação de autenticação
     */
    private final Map<String, String> sessoes;

    /**
     * Mapa reverso de sessões (login ? ID sessão) para controle de sessões únicas
     */
    private final Map<String, String> loginParaSessao;

    /**
     * Constrói o gerenciador com dependências necessárias para operação completa.
     *
     * @param usuarios Mapa principal de usuários do sistema
     * @param sessoes Registro de sessões ativas
     * @param loginParaSessao Mapeamento reverso para controle de sessões
     */
    public GerenciadorAmizades(Map<String, Users> usuarios, Map<String, String> sessoes,
                               Map<String, String> loginParaSessao) {
        this.usuarios = usuarios;
        this.sessoes = sessoes;
        this.loginParaSessao = loginParaSessao;
    }

    /**
     * Processa solicitação de amizade entre usuários após múltiplas validações.
     * <p>
     * Fluxo completo de operação:
     * <ol>
     *   <li>Valida existência do usuário alvo</li>
     *   <li>Verifica autenticação do solicitante via sessão</li>
     *   <li>Impede auto-amizade</li>
     *   <li>Verifica se já são amigos mútuos</li>
     *   <li>Checa solicitações pendentes existentes</li>
     *   <li>Aceita automaticamente solicitações mútuas pendentes</li>
     *   <li>Registra nova solicitação se válido</li>
     * </ol>
     *
     * @param idSessao ID da sessão válida do solicitante
     * @param amigoLogin Login do usuário alvo da amizade
     * @throws UsuarioNaoEncontradoException Se o amigo não existir ou sessão inválida
     * @throws SessaoInvalidaExecption Se a sessão for inválida/expirada
     * @throws AmigoDeSiException Se tentar adicionar a si mesmo como amigo
     * @throws AmigoJaExistenteException Se já existir amizade mútua
     * @throws AmigoPendenteException Se já houver solicitação pendente para este amigo
     *
     * <p><b>Exemplo de aceitação automática:</b><br>
     * Se João tem solicitação pendente de Maria, quando Maria enviar nova solicitação,
     * o sistema aceitará automaticamente criando amizade mútua</p>
     */
    @Override
    public void adicionarAmigo(String idSessao, String amigoLogin)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException {

        // 1. Verifica se o amigo existe
        if (amigoLogin == null || amigoLogin.trim().isEmpty() || !usuarios.containsKey(amigoLogin)) {
            throw new UsuarioNaoEncontradoException();
        }

        // 2. Valida a sessão primeiro
        if (idSessao == null || idSessao.trim().isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }



        String usuarioLogin = sessoes.get(idSessao);
       if (usuarioLogin == null) {
            throw new UsuarioNaoEncontradoException();
        }

        // 3. Demais validações (auto-amizade, solicitações, etc.)
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
     * Verifica relação de amizade unilateral entre usuários.
     *
     * @param login Usuário base para verificação
     * @param amigo Usuário alvo da verificação
     * @return true se o usuário base tiver o alvo em sua lista de amigos
     * @throws UsuarioNaoEncontradoException Se algum usuário não existir
     *
     * <p><b>Nota:</b> Não verifica reciprocidade (consulte {@link #ehAmigoMutuo})</p>
     */
    @Override
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return usuario.ehAmigo(amigo);
    }

    /**
     * Verifica amizade mútua entre dois usuários.
     *
     * @param login Primeiro usuário para verificação
     * @param amigo Segundo usuário para verificação
     * @return true se ambos estiverem na lista de amigos um do outro
     * @throws UsuarioNaoEncontradoException Se algum usuário não existir
     */
    @Override
    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        Users outroUsuario = usuarios.get(amigo);
        if (usuario == null || outroUsuario == null) throw new UsuarioNaoEncontradoException();
        return usuario.ehAmigo(amigo) && outroUsuario.ehAmigo(login);
    }


    /**
     * Retorna lista formatada de amigos de um usuário.
     *
     * @param login Usuário alvo da consulta
     * @return String no formato "{amigo1,amigo2,...}" ou "{}" se vazio
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     *
     */
    @Override
    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", usuario.getAmigos()) + "}";
    }

    /**
     * Retorna solicitações de amizade pendentes formatadas.
     *
     * @param login Usuário alvo da consulta
     * @return String no formato "{solicitante1,solicitante2,...}" ou "{}"
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     */
    @Override
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", usuario.getSolicitacoesPendentes()) + "}";
    }

    /**
     * Aceita uma solicitação de amizade pendente, estabelecendo relação mútua.
     * <p>
     * Efeitos colaterais:
     * <ul>
     *   <li>Remove solicitação da lista de pendências</li>
     *   <li>Adiciona ambos usuários como amigos mútuos</li>
     *   <li>Notifica ambos usuários via sistema</li>
     * </ul>
     *
     * @param usuario Usuário que está aceitando a solicitação
     * @param amigo Usuário que enviou a solicitação
     * @throws UsuarioNaoEncontradoException Se algum usuário não existir
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
     * Recusa permanentemente uma solicitação de amizade.
     * <p>
     * Comportamento:
     * <ul>
     *   <li>Remove solicitação da lista de pendências</li>
     *   <li>Não notifica o solicitante</li>
     *   <li>Não registra histórico da operação</li>
     * </ul>
     *
     * @param usuario Usuário que está recusando
     * @param amigo Usuário solicitante
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     */
    @Override
    public void recusarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        Users user = usuarios.get(usuario);
        if (user == null) throw new UsuarioNaoEncontradoException();
        user.getSolicitacoesPendentes().remove(amigo);
    }


    /**
     * Verifica existência de solicitação pendente entre usuários.
     *
     * @param deUsuario Possível solicitante
     * @param paraUsuario Possível receptor
     * @return true se existir solicitação não respondida do solicitante para o receptor
     * @throws UsuarioNaoEncontradoException Se o receptor não existir
     */
    @Override
    public boolean temSolicitacaoPendente(String deUsuario, String paraUsuario) throws UsuarioNaoEncontradoException {
        Users user = usuarios.get(paraUsuario);
        if (user == null) throw new UsuarioNaoEncontradoException();
        return user.temSolicitacaoPendente(deUsuario);
    }
}