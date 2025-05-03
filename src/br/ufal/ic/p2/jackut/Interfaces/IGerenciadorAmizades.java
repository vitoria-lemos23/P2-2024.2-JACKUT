package br.ufal.ic.p2.jackut.Interfaces;

import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.Serializable;

/**
 * Interface que define as opera��es para gerenciamento de amizades e relacionamentos sociais no sistema Jackut.
 * <p>
 * Fornece m�todos para:
 * <ul>
 *   <li>Envio e gest�o de solicita��es de amizade</li>
 *   <li>Verifica��o de status de amizade</li>
 *   <li>Consulta de listas de amigos e solicita��es pendentes</li>
 *   <li>Aceite/recusa de solicita��es</li>
 * </ul>
 *
 * <p>Implementa {@link Serializable} para permitir serializa��o das opera��es.</p>
 */
public interface IGerenciadorAmizades extends Serializable {

        /**
         * Envia solicita��o de amizade ou estabelece amizade m�tua.
         * <p>
         * Fluxo principal:
         * <ul>
         *   <li>Valida sess�o do solicitante</li>
         *   <li>Verifica exist�ncia do usu�rio alvo</li>
         *   <li>Impede auto-amizade</li>
         *   <li>Aceita automaticamente solicita��es m�tuas pendentes</li>
         * </ul>
         *
         * @param idSessao ID da sess�o v�lida do solicitante
         * @param amigoLogin Login do usu�rio alvo
         * @throws UsuarioNaoEncontradoException Se usu�rio alvo n�o existir ou sess�o inv�lida
         * @throws SessaoInvalidaExecption Se sess�o for inv�lida/expirada
         * @throws AmigoDeSiException Se tentar adicionar a si mesmo
         * @throws AmigoJaExistenteException Se j� forem amigos
         * @throws AmigoPendenteException Se j� houver solicita��o pendente
         */
        void adicionarAmigo(String idSessao, String amigoLogin)
                throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
                AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException;

        /**
         * Verifica rela��o de amizade unilateral.
         *
         * @param login Usu�rio base para verifica��o
         * @param amigo Usu�rio alvo da verifica��o
         * @return true se o usu�rio base tiver o alvo como amigo
         * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
         */
        boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException;

        /**
         * Verifica amizade m�tua entre dois usu�rios.
         *
         * @param login Primeiro usu�rio
         * @param amigo Segundo usu�rio
         * @return true se ambos forem amigos um do outro
         * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
         */
        boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException;

        /**
         * Retorna lista de amigos formatada.
         *
         * @param login Usu�rio alvo
         * @return String no formato "{amigo1,amigo2,...}" (vazio se n�o houver amigos)
         * @throws UsuarioNaoEncontradoException Se usu�rio n�o existir
         */
        String getAmigos(String login) throws UsuarioNaoEncontradoException;

        /**
         * Retorna solicita��es pendentes formatadas.
         *
         * @param login Usu�rio alvo
         * @return String no formato "{solicitante1,solicitante2,...}" (vazio se n�o houver pend�ncias)
         * @throws UsuarioNaoEncontradoException Se usu�rio n�o existir
         */
        String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException;

        /**
         * Aceita uma solicita��o de amizade pendente.
         *
         * @param usuario Usu�rio que aceita a solicita��o
         * @param amigo Usu�rio que enviou a solicita��o
         * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
         */
        void aceitarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException;

        /**
         * Recusa uma solicita��o de amizade pendente.
         *
         * @param usuario Usu�rio que recusa a solicita��o
         * @param amigo Usu�rio que enviou a solicita��o
         * @throws UsuarioNaoEncontradoException Se usu�rio n�o existir
         */
        void recusarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException;

        /**
         * Verifica exist�ncia de solicita��o pendente entre usu�rios.
         *
         * @param deUsuario Poss�vel solicitante
         * @param paraUsuario Poss�vel receptor
         * @return true se houver solicita��o n�o respondida do solicitante para o receptor
         * @throws UsuarioNaoEncontradoException Se o receptor n�o existir
         */
        boolean temSolicitacaoPendente(String deUsuario, String paraUsuario) throws UsuarioNaoEncontradoException;
}