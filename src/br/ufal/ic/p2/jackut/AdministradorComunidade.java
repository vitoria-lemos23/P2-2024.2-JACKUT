/**
 * Classe que representa um Administrador de Comunidade no sistema Jackut.
 * Herda de Users e gerencia opera��es espec�ficas de administra��o de comunidades.
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um Administrador de Comunidade no sistema Jackut,
 * especializado na gest�o de comunidades. Herda funcionalidades b�sicas de {@link Users}
 * e adiciona opera��es espec�ficas para cria��o e gest�o de comunidades.
 *
 * <p>Principais responsabilidades:</p>
 * <ul>
 *   <li>Criar novas comunidades com valida��o de unicidade</li>
 *   <li>Manter registro das comunidades administradas</li>
 *   <li>Verificar permiss�es de gest�o em comunidades</li>
 * </ul>
 *
 * <p>Relacionamentos chave:</p>
 * <ul>
 *   <li>Herda de {@link Users} para dados b�sicos de usu�rio</li>
 *   <li>Integra com {@link Jackut} para opera��es do sistema</li>
 *   <li>Utiliza exce��es espec�ficas para tratamento de erros</li>
 * </ul>
 */
public class AdministradorComunidade extends Users {
    /**
     * Lista de nomes das comunidades sob gest�o deste administrador
     */
    private List<String> comunidadesAdministradas;

    /**
     * Constr�i um Administrador de Comunidade com credenciais completas e inicializa suas comunidades.
     *
     * @param login     Identificador �nico do administrador (case-sensitive, n�o nulo/vazio)
     * @param senha     Credencial de autentica��o (m�nimo 6 caracteres, n�o nulo/vazio)
     * @param nome      Nome completo para exibi��o p�blica (n�o nulo/vazio)
     *
     * @throws IllegalArgumentException Se qualquer par�metro for inv�lido:
     *                                  <ul>
     *                                    <li>Login vazio ou somente espa�os</li>
     *                                    <li>Senha n�o atender requisitos m�nimos</li>
     *                                    <li>Nome com menos de 2 caracteres</li>
     *                                  </ul>
     *
     * <p><b>Comportamentos espec�ficos:</b></p>
     * <ul>
     *   <li>Herda valida��es da classe base {@link Users}</li>
     *   <li>Inicializa lista de comunidades administradas vazia</li>
     * </ul>
     */
    public AdministradorComunidade(String login, String senha, String nome) {
        super(login, senha, nome);
        this.comunidadesAdministradas = new ArrayList<>();
    }

    /**
     * Cria uma nova comunidade no sistema com valida��o de unicidade.
     *
     * @param nome Nome �nico da comunidade (case-sensitive)
     * @param descricao Texto descritivo (m�nimo 5 caracteres)
     * @param sistema Refer�ncia ativa do sistema Jackut
     *
     * @throws ComunidadeJaExisteException Se o nome j� estiver registrado
     * @throws IllegalArgumentException Se descri��o for inv�lida
     *
     * <p><b>Efeitos colaterais:</b></p>
     * <ul>
     *   <li>Adiciona comunidade � lista de administradas</li>
     *   <li>Registra comunidade no sistema</li>
     * </ul>
     *
     * @see Jackut#registrarComunidade(String, String, String)
     */
    public void criarComunidade(String nome, String descricao, Jackut sistema)
            throws ComunidadeJaExisteException {
        if (sistema.existeComunidade(nome)) {
            throw new ComunidadeJaExisteException();
        }
        sistema.registrarComunidade(nome, descricao, this.getLogin());
        this.comunidadesAdministradas.add(nome);
    }

    /**
     * Verifica direitos de gest�o sobre uma comunidade espec�fica.
     *
     * @param nomeComunidade Nome exato da comunidade (case-sensitive)
     * @param sistema Refer�ncia ativa do sistema Jackut
     * @return true se o administrador for o criador da comunidade
     *
     * @throws ComunidadeNaoExisteException Se a comunidade n�o estiver registrada
     *
     * <p><b>L�gica de implementa��o:</b></p>
     * <ul>
     *   <li>Compara login do administrador com o dono da comunidade</li>
     *   <li>Depende da integridade dos registros do sistema</li>
     * </ul>
     *
     * @see Jackut#getDonoComunidade(String)
     */
    public boolean podeGerenciarComunidade(String nomeComunidade, Jackut sistema) throws ComunidadeNaoExisteException {
        return sistema.getDonoComunidade(nomeComunidade).equals(this.getLogin());
    }
}