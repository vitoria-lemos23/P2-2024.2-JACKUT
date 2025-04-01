/**
 * Classe Facade que fornece uma interface simplificada para o sistema Jackut.
 * Atua como um ponto único de acesso para todas as operações do sistema,
 * encapsulando a complexidade das operações internas.
 *
 * <p>Esta classe implementa o padrão Facade, fornecendo métodos simplificados
 * que delegam as operações para a classe Jackut.</p>
 *
 * <p>Responsável por gerenciar usuários, sessões, amizades, perfis e recados.</p>
 *
 * @author Vitória Lemos Pereira
 * @version 1.0
 * @see Jackut
 * @since 1.0
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.*;

public class Facade implements Serializable {
    private static final long serialVersionUID = 1L;
    private Jackut jackut;

    /**
     * Constrói uma nova Facade e inicializa o sistema Jackut.
     * Carrega os dados persistentes se existirem.
     */
    public Facade() {
        this.jackut = Jackut.iniciarSistema();
    }

    /**
     * Remove todos os dados do sistema, reiniciando-o para o estado inicial.
     * Limpa todos os usuários, sessões e relacionamentos.
     */
    public void zerarSistema() {
        jackut.zerarSistema();
    }

    /**
     * Obtém o valor de um atributo específico do perfil de um usuário.
     *
     * @param login Login do usuário (não pode ser nulo ou vazio)
     * @param atributo Nome do atributo a ser obtido (não pode ser nulo ou vazio)
     * @return Valor do atributo solicitado
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado no sistema
     * @throws AtributoNaoPreenchidoException Se o atributo não existir ou estiver vazio
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoEncontradoException, AtributoNaoPreenchidoException {
        return jackut.getAtributoUsuario(login, atributo);
    }

    /**
     * Adiciona um usuário como amigo, criando uma solicitação de amizade ou
     * estabelecendo amizade mútua se já houver uma solicitação pendente.
     *
     * @param idSessao ID da sessão do usuário que está adicionando (não pode ser nulo ou vazio)
     * @param amigo Login do usuário a ser adicionado como amigo (não pode ser nulo ou vazio)
     * @throws UsuarioNaoEncontradoException Se o amigo não for encontrado no sistema
     * @throws SessaoInvalidaExecption Se a sessão for inválida ou expirada
     * @throws AmigoDeSiException Se tentar adicionar a si mesmo como amigo
     * @throws AmigoJaExistenteException Se já forem amigos
     * @throws AmigoPendenteException Se já houver uma solicitação pendente para este amigo
     */
    public void adicionarAmigo(String idSessao, String amigo)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException {
        jackut.adicionarAmigo(idSessao, amigo);
    }

    /**
     * Cria um novo usuário no sistema com os dados fornecidos.
     *
     * @param Login do novo usuário (não pode ser nulo, vazio ou já existente)
     * @param senha Senha do novo usuário (não pode ser nula ou vazia)
     * @param nome Nome completo do novo usuário (não pode ser nulo ou vazio)
     * @throws LoginInvalidoException Se o login for inválido
     * @throws SenhaInvalidaException Se a senha for inválida
     * @throws LoginJaExistenteException Se o login já estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoException, SenhaInvalidaException, LoginJaExistenteException {
        jackut.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica um usuário e abre uma nova sessão.
     *
     * @param login Login do usuário (não pode ser nulo ou vazio)
     * @param senha Senha do usuário (não pode ser nula ou vazia)
     * @return ID da sessão criada
     * @throws CredenciaisInvalidasException Se as credenciais forem inválidas
     */
    public String abrirSessao(String login, String senha) throws CredenciaisInvalidasException {
        return jackut.abrirSessao(login, senha);
    }

    /**
     * Encerra o sistema, salvando todos os dados atuais no arquivo de persistência.
     */
    public void encerrarSistema() {
        jackut.encerrarSistema();
    }

    /**
     * Inicializa o sistema Jackut e retorna uma nova instância da Facade.
     * Carrega os dados persistentes se existirem.
     *
     * @return Nova instância da Facade
     */
    public static Facade iniciarSistema() {
        return new Facade();
    }

    /**
     * Edita um atributo do perfil do usuário da sessão atual.
     *
     * @param idSessao ID da sessão do usuário (não pode ser nulo ou vazio)
     * @param atributo Nome do atributo a ser editado (não pode ser nulo ou vazio)
     * @param valor Novo valor para o atributo (pode ser vazio)
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     * @throws SessaoInvalidaExecption Se a sessão for inválida
     * @throws AtributoNaoPreenchidoException Se o nome do atributo for inválido
     */
    public void editarPerfil(String idSessao, String atributo, String valor)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, AtributoNaoPreenchidoException {
        jackut.editarPerfil(idSessao, atributo, valor);
    }

    /**
     * Verifica se um usuário é amigo de outro (relação unilateral).
     *
     * @param login Login do primeiro usuário (não pode ser nulo ou vazio)
     * @param amigo Login do possível amigo (não pode ser nulo ou vazio)
     * @return true se o primeiro usuário tem o segundo como amigo, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum usuário não for encontrado
     */
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return jackut.ehAmigo(login, amigo);
    }

    /**
     * Verifica se dois usuários são amigos mútuos (ambos adicionaram um ao outro).
     *
     * @param login Login do primeiro usuário (não pode ser nulo ou vazio)
     * @param amigo Login do segundo usuário (não pode ser nulo ou vazio)
     * @return true se forem amigos mútuos, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum usuário não for encontrado
     */
    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return jackut.ehAmigoMutuo(login, amigo);
    }

    /**
     * Obtém a lista de amigos de um usuário no formato {amigo1,amigo2,...}.
     *
     * @param login Login do usuário (não pode ser nulo ou vazio)
     * @return String formatada com a lista de amigos entre chaves
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        return jackut.getAmigos(login);
    }

    /**
     * Obtém as solicitações de amizade pendentes de um usuário no formato {solicitante1,solicitante2,...}.
     *
     * @param login Login do usuário (não pode ser nulo ou vazio)
     * @return String formatada com as solicitações pendentes entre chaves
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        return jackut.getSolicitacoesPendentes(login);
    }

    /**
     * Envia um recado para outro usuário.
     *
     * @param idSessao ID da sessão do remetente (não pode ser nulo ou vazio)
     * @param destinatario Login do usuário destinatário (não pode ser nulo ou vazio)
     * @param recado Texto do recado (não pode ser nulo ou vazio)
     * @throws UsuarioNaoEncontradoException Se o destinatário não for encontrado
     * @throws SessaoInvalidaExecption Se a sessão for inválida
     * @throws SemRecadoException Se ocorrer um erro ao enviar o recado
     */
    public void enviarRecado(String idSessao, String destinatario, String recado)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, SemRecadoException {
        jackut.enviarRecado(idSessao, destinatario, recado);
    }

    /**
     * Lê o próximo recado não lido do usuário da sessão (sistema FIFO).
     *
     * @param idSessao ID da sessão do usuário (não pode ser nulo ou vazio)
     * @return Texto do recado mais antigo não lido
     * @throws SessaoInvalidaExecption Se a sessão for inválida
     * @throws SemRecadoException Se não houver recados para ler
     */
    public String lerRecado(String idSessao) throws SessaoInvalidaExecption, SemRecadoException {
        return jackut.lerRecado(idSessao);
    }
}