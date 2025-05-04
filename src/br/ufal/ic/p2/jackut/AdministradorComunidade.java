/**
 * Classe que representa um Administrador de Comunidade no sistema Jackut.
 * Herda de Users e gerencia operações específicas de administração de comunidades.
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um Administrador de Comunidade no sistema Jackut,
 * especializado na gestão de comunidades. Herda funcionalidades básicas de {@link Users}
 * e adiciona operações específicas para criação e gestão de comunidades.
 *
 * <p>Principais responsabilidades:</p>
 * <ul>
 *   <li>Criar novas comunidades com validação de unicidade</li>
 *   <li>Manter registro das comunidades administradas</li>
 *   <li>Verificar permissões de gestão em comunidades</li>
 * </ul>
 *
 * <p>Relacionamentos chave:</p>
 * <ul>
 *   <li>Herda de {@link Users} para dados básicos de usuário</li>
 *   <li>Integra com {@link Jackut} para operações do sistema</li>
 *   <li>Utiliza exceções específicas para tratamento de erros</li>
 * </ul>
 */
public class AdministradorComunidade extends Users {
    /**
     * Lista de nomes das comunidades sob gestão deste administrador
     */
    private List<String> comunidadesAdministradas;

    /**
     * Constrói um Administrador de Comunidade com credenciais completas e inicializa suas comunidades.
     *
     * @param login     Identificador único do administrador (case-sensitive, não nulo/vazio)
     * @param senha     Credencial de autenticação (mínimo 6 caracteres, não nulo/vazio)
     * @param nome      Nome completo para exibição pública (não nulo/vazio)
     *
     * @throws IllegalArgumentException Se qualquer parâmetro for inválido:
     *                                  <ul>
     *                                    <li>Login vazio ou somente espaços</li>
     *                                    <li>Senha não atender requisitos mínimos</li>
     *                                    <li>Nome com menos de 2 caracteres</li>
     *                                  </ul>
     *
     * <p><b>Comportamentos específicos:</b></p>
     * <ul>
     *   <li>Herda validações da classe base {@link Users}</li>
     *   <li>Inicializa lista de comunidades administradas vazia</li>
     * </ul>
     */
    public AdministradorComunidade(String login, String senha, String nome) {
        super(login, senha, nome);
        this.comunidadesAdministradas = new ArrayList<>();
    }

    /**
     * Cria uma nova comunidade no sistema com validação de unicidade.
     *
     * @param nome Nome único da comunidade (case-sensitive)
     * @param descricao Texto descritivo (mínimo 5 caracteres)
     * @param sistema Referência ativa do sistema Jackut
     *
     * @throws ComunidadeJaExisteException Se o nome já estiver registrado
     * @throws IllegalArgumentException Se descrição for inválida
     *
     * <p><b>Efeitos colaterais:</b></p>
     * <ul>
     *   <li>Adiciona comunidade à lista de administradas</li>
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
     * Verifica direitos de gestão sobre uma comunidade específica.
     *
     * @param nomeComunidade Nome exato da comunidade (case-sensitive)
     * @param sistema Referência ativa do sistema Jackut
     * @return true se o administrador for o criador da comunidade
     *
     * @throws ComunidadeNaoExisteException Se a comunidade não estiver registrada
     *
     * <p><b>Lógica de implementação:</b></p>
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