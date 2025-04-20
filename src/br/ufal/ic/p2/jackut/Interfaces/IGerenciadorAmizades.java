package br.ufal.ic.p2.jackut.Interfaces;

import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.Serializable;

/**
 * Interface que define as operações para gerenciamento de amizades e relacionamentos sociais no sistema Jackut.
 * <p>
 * Fornece métodos para:
 * <ul>
 *   <li>Envio e gestão de solicitações de amizade</li>
 *   <li>Verificação de status de amizade</li>
 *   <li>Consulta de listas de amigos e solicitações pendentes</li>
 *   <li>Aceite/recusa de solicitações</li>
 * </ul>
 *
 * <p>Implementa {@link Serializable} para permitir serialização das operações.</p>
 */
public interface IGerenciadorAmizades extends Serializable {

        /**
         * Envia solicitação de amizade ou estabelece amizade mútua.
         * <p>
         * Fluxo principal:
         * <ul>
         *   <li>Valida sessão do solicitante</li>
         *   <li>Verifica existência do usuário alvo</li>
         *   <li>Impede auto-amizade</li>
         *   <li>Aceita automaticamente solicitações mútuas pendentes</li>
         * </ul>
         *
         * @param idSessao ID da sessão válida do solicitante
         * @param amigoLogin Login do usuário alvo
         * @throws UsuarioNaoEncontradoException Se usuário alvo não existir ou sessão inválida
         * @throws SessaoInvalidaExecption Se sessão for inválida/expirada
         * @throws AmigoDeSiException Se tentar adicionar a si mesmo
         * @throws AmigoJaExistenteException Se já forem amigos
         * @throws AmigoPendenteException Se já houver solicitação pendente
         */
        void adicionarAmigo(String idSessao, String amigoLogin)
                throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
                AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException;

        /**
         * Verifica relação de amizade unilateral.
         *
         * @param login Usuário base para verificação
         * @param amigo Usuário alvo da verificação
         * @return true se o usuário base tiver o alvo como amigo
         * @throws UsuarioNaoEncontradoException Se algum usuário não existir
         */
        boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException;

        /**
         * Verifica amizade mútua entre dois usuários.
         *
         * @param login Primeiro usuário
         * @param amigo Segundo usuário
         * @return true se ambos forem amigos um do outro
         * @throws UsuarioNaoEncontradoException Se algum usuário não existir
         */
        boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException;

        /**
         * Retorna lista de amigos formatada.
         *
         * @param login Usuário alvo
         * @return String no formato "{amigo1,amigo2,...}" (vazio se não houver amigos)
         * @throws UsuarioNaoEncontradoException Se usuário não existir
         */
        String getAmigos(String login) throws UsuarioNaoEncontradoException;

        /**
         * Retorna solicitações pendentes formatadas.
         *
         * @param login Usuário alvo
         * @return String no formato "{solicitante1,solicitante2,...}" (vazio se não houver pendências)
         * @throws UsuarioNaoEncontradoException Se usuário não existir
         */
        String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException;

        /**
         * Aceita uma solicitação de amizade pendente.
         *
         * @param usuario Usuário que aceita a solicitação
         * @param amigo Usuário que enviou a solicitação
         * @throws UsuarioNaoEncontradoException Se algum usuário não existir
         */
        void aceitarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException;

        /**
         * Recusa uma solicitação de amizade pendente.
         *
         * @param usuario Usuário que recusa a solicitação
         * @param amigo Usuário que enviou a solicitação
         * @throws UsuarioNaoEncontradoException Se usuário não existir
         */
        void recusarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException;

        /**
         * Verifica existência de solicitação pendente entre usuários.
         *
         * @param deUsuario Possível solicitante
         * @param paraUsuario Possível receptor
         * @return true se houver solicitação não respondida do solicitante para o receptor
         * @throws UsuarioNaoEncontradoException Se o receptor não existir
         */
        boolean temSolicitacaoPendente(String deUsuario, String paraUsuario) throws UsuarioNaoEncontradoException;
}