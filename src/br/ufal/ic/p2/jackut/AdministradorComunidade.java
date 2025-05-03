/**
 * Classe que representa um Administrador de Comunidade no sistema Jackut.
 * Herda de Users e gerencia opera��es espec�ficas de administra��o de comunidades.
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class AdministradorComunidade extends Users {
    private List<String> comunidadesAdministradas;

    /**
     * Constr�i um Administrador de Comunidade com login, senha e nome.
     *
     * @param login Login do administrador (n�o pode ser nulo ou vazio)
     * @param senha Senha do administrador (n�o pode ser nula ou vazia)
     * @param nome Nome completo do administrador (n�o pode ser nulo ou vazio)
     */
    public AdministradorComunidade(String login, String senha, String nome) {
        super(login, senha, nome);
        this.comunidadesAdministradas = new ArrayList<>();
    }

    /**
     * Cria uma nova comunidade no sistema.
     *
     * @param nome Nome da comunidade (n�o pode ser nulo ou duplicado)
     * @param descricao Descri��o da comunidade (n�o pode ser nula)
     * @param sistema Refer�ncia ao sistema Jackut para valida��o
     * @throws ComunidadeJaExisteException Se a comunidade j� existir
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
     * Verifica se o administrador pode gerenciar uma comunidade espec�fica.
     *
     * @param nomeComunidade Nome da comunidade a ser verificada
     * @param sistema Refer�ncia ao sistema Jackut para valida��o
     * @return true se o administrador � o dono da comunidade
     * @throws ComunidadeNaoExisteException Se a comunidade n�o existir
     */
    public boolean podeGerenciarComunidade(String nomeComunidade, Jackut sistema) throws ComunidadeNaoExisteException {
        return sistema.getDonoComunidade(nomeComunidade).equals(this.getLogin());
    }
}