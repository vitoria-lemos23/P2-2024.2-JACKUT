/**
 * Classe que representa um Administrador de Comunidade no sistema Jackut.
 * Herda de Users e gerencia operações específicas de administração de comunidades.
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class AdministradorComunidade extends Users {
    private List<String> comunidadesAdministradas;

    /**
     * Constrói um Administrador de Comunidade com login, senha e nome.
     *
     * @param login Login do administrador (não pode ser nulo ou vazio)
     * @param senha Senha do administrador (não pode ser nula ou vazia)
     * @param nome Nome completo do administrador (não pode ser nulo ou vazio)
     */
    public AdministradorComunidade(String login, String senha, String nome) {
        super(login, senha, nome);
        this.comunidadesAdministradas = new ArrayList<>();
    }

    /**
     * Cria uma nova comunidade no sistema.
     *
     * @param nome Nome da comunidade (não pode ser nulo ou duplicado)
     * @param descricao Descrição da comunidade (não pode ser nula)
     * @param sistema Referência ao sistema Jackut para validação
     * @throws ComunidadeJaExisteException Se a comunidade já existir
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
     * Verifica se o administrador pode gerenciar uma comunidade específica.
     *
     * @param nomeComunidade Nome da comunidade a ser verificada
     * @param sistema Referência ao sistema Jackut para validação
     * @return true se o administrador é o dono da comunidade
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     */
    public boolean podeGerenciarComunidade(String nomeComunidade, Jackut sistema) throws ComunidadeNaoExisteException {
        return sistema.getDonoComunidade(nomeComunidade).equals(this.getLogin());
    }
}