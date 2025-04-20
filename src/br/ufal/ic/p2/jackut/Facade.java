/**
 * Classe Facade que fornece uma interface simplificada para o sistema Jackut.
 * Atua como um ponto �nico de acesso para todas as opera��es do sistema,
 * encapsulando a complexidade das opera��es internas.
 *
 * <p>Esta classe implementa o padr�o Facade, fornecendo m�todos simplificados
 * que delegam as opera��es para a classe Jackut.</p>
 *
 * <p>Respons�vel por gerenciar usu�rios, sess�es, amizades, perfis e recados.</p>
 *
 * @author Vit�ria Lemos
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.*;
import java.util.List;

public class Facade implements Serializable {
    private static final long serialVersionUID = 1L;
    private Jackut jackut;

    /**
     * Constr�i uma nova Facade e inicializa o sistema Jackut.
     * Carrega os dados persistentes se existirem.
     */
    public Facade() {
        this.jackut = Jackut.iniciarSistema();
    }

    /**
     * Remove todos os dados do sistema, reiniciando-o para o estado inicial.
     * Limpa todos os usu�rios, sess�es e relacionamentos.
     */
    public void zerarSistema() {
        jackut.zerarSistema();
    }

    /**
     * Obt�m o valor de um atributo espec�fico do perfil de um usu�rio.
     *
     * @param login Login do usu�rio (n�o pode ser nulo ou vazio)
     * @param atributo Nome do atributo a ser obtido (n�o pode ser nulo ou vazio)
     * @return Valor do atributo solicitado
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o for encontrado no sistema
     * @throws AtributoNaoPreenchidoException Se o atributo n�o existir ou estiver vazio
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoEncontradoException, AtributoNaoPreenchidoException {
        return jackut.getAtributoUsuario(login, atributo);
    }

    /**
     * Adiciona um usu�rio como amigo, criando uma solicita��o de amizade ou
     * estabelecendo amizade m�tua se j� houver uma solicita��o pendente.
     *
     * @param idSessao ID da sess�o do usu�rio que est� adicionando (n�o pode ser nulo ou vazio)
     * @param amigo Login do usu�rio a ser adicionado como amigo (n�o pode ser nulo ou vazio)
     * @throws UsuarioNaoEncontradoException Se o amigo n�o for encontrado no sistema
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida ou expirada
     * @throws AmigoDeSiException Se tentar adicionar a si mesmo como amigo
     * @throws AmigoJaExistenteException Se j� forem amigos
     * @throws AmigoPendenteException Se j� houver uma solicita��o pendente para este amigo
     */
    public void adicionarAmigo(String idSessao, String amigo)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException {
        jackut.adicionarAmigo(idSessao, amigo);
    }

    /**
     * Cria um novo usu�rio no sistema com os dados fornecidos.
     *
     * @param Login do novo usu�rio (n�o pode ser nulo, vazio ou j� existente)
     * @param senha Senha do novo usu�rio (n�o pode ser nula ou vazia)
     * @param nome Nome completo do novo usu�rio (n�o pode ser nulo ou vazio)
     * @throws LoginInvalidoException Se o login for inv�lido
     * @throws SenhaInvalidaException Se a senha for inv�lida
     * @throws LoginJaExistenteException Se o login j� estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoException, SenhaInvalidaException, LoginJaExistenteException {
        jackut.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica um usu�rio e abre uma nova sess�o.
     *
     * @param login Login do usu�rio (n�o pode ser nulo ou vazio)
     * @param senha Senha do usu�rio (n�o pode ser nula ou vazia)
     * @return ID da sess�o criada
     * @throws CredenciaisInvalidasException Se as credenciais forem inv�lidas
     */
    public String abrirSessao(String login, String senha) throws CredenciaisInvalidasException {
        return jackut.abrirSessao(login, senha);
    }

    /**
     * Encerra o sistema, salvando todos os dados atuais no arquivo de persist�ncia.
     */
    public void encerrarSistema() {
        jackut.encerrarSistema();
    }

    /**
     * Inicializa o sistema Jackut e retorna uma nova inst�ncia da Facade.
     * Carrega os dados persistentes se existirem.
     *
     * @return Nova inst�ncia da Facade
     */
    public static Facade iniciarSistema() {
        return new Facade();
    }

    /**
     * Edita um atributo do perfil do usu�rio da sess�o atual.
     *
     * @param idSessao ID da sess�o do usu�rio (n�o pode ser nulo ou vazio)
     * @param atributo Nome do atributo a ser editado (n�o pode ser nulo ou vazio)
     * @param valor Novo valor para o atributo (pode ser vazio)
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o for encontrado
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida
     * @throws AtributoNaoPreenchidoException Se o nome do atributo for inv�lido
     */
    public void editarPerfil(String idSessao, String atributo, String valor)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, AtributoNaoPreenchidoException {
        jackut.editarPerfil(idSessao, atributo, valor);
    }

    /**
     * Verifica se um usu�rio � amigo de outro (rela��o unilateral).
     *
     * @param login Login do primeiro usu�rio (n�o pode ser nulo ou vazio)
     * @param amigo Login do poss�vel amigo (n�o pode ser nulo ou vazio)
     * @return true se o primeiro usu�rio tem o segundo como amigo, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o for encontrado
     */
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return jackut.ehAmigo(login, amigo);
    }

    /**
     * Verifica se dois usu�rios s�o amigos m�tuos (ambos adicionaram um ao outro).
     *
     * @param login Login do primeiro usu�rio (n�o pode ser nulo ou vazio)
     * @param amigo Login do segundo usu�rio (n�o pode ser nulo ou vazio)
     * @return true se forem amigos m�tuos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o for encontrado
     */
    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return jackut.ehAmigoMutuo(login, amigo);
    }

    /**
     * Obt�m a lista de amigos de um usu�rio no formato {amigo1,amigo2,...}.
     *
     * @param login Login do usu�rio (n�o pode ser nulo ou vazio)
     * @return String formatada com a lista de amigos entre chaves
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o for encontrado
     */
    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        return jackut.getAmigos(login);
    }

    /**
     * Obt�m as solicita��es de amizade pendentes de um usu�rio no formato {solicitante1,solicitante2,...}.
     *
     * @param login Login do usu�rio (n�o pode ser nulo ou vazio)
     * @return String formatada com as solicita��es pendentes entre chaves
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o for encontrado
     */
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        return jackut.getSolicitacoesPendentes(login);
    }

    /**
     * Envia um recado para outro usu�rio.
     *
     * @param idSessao ID da sess�o do remetente (n�o pode ser nulo ou vazio)
     * @param destinatario Login do usu�rio destinat�rio (n�o pode ser nulo ou vazio)
     * @param recado Texto do recado (n�o pode ser nulo ou vazio)
     * @throws UsuarioNaoEncontradoException Se o destinat�rio n�o for encontrado
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida
     * @throws SemRecadoException Se ocorrer um erro ao enviar o recado
     */
    public void enviarRecado(String idSessao, String destinatario, String recado)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, SemRecadoException {
        jackut.enviarRecado(idSessao, destinatario, recado);
    }

    /**
     * L� o pr�ximo recado n�o lido do usu�rio da sess�o (sistema FIFO).
     *
     * @param idSessao ID da sess�o do usu�rio (n�o pode ser nulo ou vazio)
     * @return Texto do recado mais antigo n�o lido
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida
     * @throws SemRecadoException Se n�o houver recados para ler
     */
    public String lerRecado(String idSessao) throws SessaoInvalidaExecption, SemRecadoException {
        return jackut.lerRecado(idSessao);
    }

    public void criarComunidade(String idSessao, String nome, String descricao)
            throws SessaoInvalidaExecption, ComunidadeJaExisteException {
        String login = jackut.getLoginPorSessao(idSessao); // Implemente getLoginPorSessao no Jackut
        jackut.registrarComunidade(nome, descricao, login);
    }



    public String getDonoComunidade(String nome) throws ComudadeNaoExisteException {
        return jackut.getDonoComunidade(nome);
    }

    public String getMembrosComunidade(String nome) throws ComudadeNaoExisteException {
        List<String> membros = jackut.getMembrosComunidade(nome);
        return "{" + String.join(",", membros) + "}";
    }

    public String getDescricaoComunidade(String nome) throws ComudadeNaoExisteException {
        return jackut.getDescricaoComunidade(nome);
    }


}