/**
 * Pacote principal do sistema Jackut, contendo as classes principais de funcionamento
 * do sistema de rede social e suas exceções personalizadas.
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.*;
import java.util.*;

/**
 * Classe principal do sistema Jackut que implementa a lógica de negócio da rede social.
 * Gerencia usuários, sessões, amizades, perfis e recados entre usuários.
 *
 * <p>Esta classe é responsável por todas as operações principais do sistema,
 * incluindo persistência dos dados em arquivo.</p>
 *
 * @author Vitória Lemos
 * @version 2.0
 * @since 1.0
 */

public class Jackut implements Serializable {
    private static final long serialVersionUID = 2L;
    private static final String ARQUIVO_DADOS = "arquivo.dat";

    private Map<String, Users> usuarios;
    private Map<String, String> sessoes;
    private Map<String, String> loginParaSessao;

    /**
     * Constrói uma nova instância do sistema Jackut com estruturas de dados vazias.
     */
    public Jackut() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
        this.loginParaSessao = new HashMap<>();
    }

    /**
     * Remove todos os dados do sistema, reiniciando-o para o estado inicial.
     * Limpa todos os usuários, sessões e relacionamentos.
     */
    public void zerarSistema() {
        usuarios.clear();
        sessoes.clear();
        loginParaSessao.clear();
    }

    /**
     * Inicializa o sistema Jackut, carregando dados persistentes se existirem.
     *
     * @return Instância do sistema Jackut com dados carregados ou nova instância vazia
     */
    public static Jackut iniciarSistema() {
        File arquivo = new File(ARQUIVO_DADOS);
        if (arquivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
                return (Jackut) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao carregar dados: " + e.getMessage());
            }
        }
        return new Jackut();
    }

    /**
     * Cria um novo usuário no sistema com os dados fornecidos.
     *
     * @param login Login do novo usuário (deve ser único, não nulo e não vazio)
     * @param senha Senha do novo usuário (não pode ser nula ou vazia)
     * @param nome Nome completo do novo usuário
     * @throws LoginInvalidoException Se o login for inválido (nulo ou vazio)
     * @throws SenhaInvalidaException Se a senha for inválida (nula ou vazia)
     * @throws LoginJaExistenteException Se o login já estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoException, SenhaInvalidaException, LoginJaExistenteException {
        if (login == null || login.trim().isEmpty()) throw new LoginInvalidoException();
        if (senha == null || senha.trim().isEmpty()) throw new SenhaInvalidaException();
        if (usuarios.containsKey(login)) throw new LoginJaExistenteException();

        usuarios.put(login, new Users(login, senha, nome));
    }


    /**
     * Autentica um usuário e abre uma nova sessão.
     *
     * @param login Login do usuário (deve existir no sistema)
     * @param senha Senha do usuário (deve corresponder ao cadastrado)
     * @return ID único da sessão criada
     * @throws CredenciaisInvalidasException Se as credenciais forem inválidas ou incorretas
     */
    public String abrirSessao(String login, String senha) throws CredenciaisInvalidasException {
        Users usuario = usuarios.get(login);


        if (usuario == null || !usuario.getSenha().equals(senha)) {
            throw new CredenciaisInvalidasException();
        }

        if (loginParaSessao.containsKey(login)) {
            sessoes.remove(loginParaSessao.get(login));
        }

        String idSessao = UUID.randomUUID().toString();
        sessoes.put(idSessao, login);
        loginParaSessao.put(login, idSessao);
        return idSessao;
    }

    /**
     * Adiciona um usuário como amigo, com tratamento para todos os casos especiais.
     *
     * @param idSessao ID da sessão do usuário que está adicionando
     * @param amigoLogin Login do usuário a ser adicionado como amigo
     * @throws UsuarioNaoEncontradoException Se o amigo não existir no sistema
     * @throws SessaoInvalidaExecption Se a sessão for inválida ou expirada
     * @throws AmigoDeSiException Se tentar adicionar a si mesmo como amigo
     * @throws AmigoJaExistenteException Se já forem amigos
     * @throws AmigoPendenteException Se já houver solicitação pendente para este amigo
     */
    public void adicionarAmigo(String idSessao, String amigoLogin)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException {

        // 1. Verifica se o amigo existe PRIMEIRO (como solicitado)
        if (amigoLogin == null || amigoLogin.trim().isEmpty() || !usuarios.containsKey(amigoLogin)) {
            throw new UsuarioNaoEncontradoException();
        }

        // 2. Verifica a sessão
        if (idSessao == null || idSessao.trim().isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }

        String usuarioLogin = sessoes.get(idSessao);
         if (usuarioLogin == null) {
            throw new SessaoInvalidaExecption();
        }

        // 3. Verifica auto-amizade
        if (usuarioLogin.equals(amigoLogin)) {
            throw new AmigoDeSiException();
        }

        Users usuario = usuarios.get(usuarioLogin);
        Users amigo = usuarios.get(amigoLogin);

        // 4. Verifica se já são amigos
        if (usuario.ehAmigo(amigoLogin) && amigo.ehAmigo(usuarioLogin)) {
            throw new AmigoJaExistenteException();
        }

        // 5. Verifica se já existe solicitação pendente DO USUÁRIO para O AMIGO
        if (amigo.temSolicitacaoPendente(usuarioLogin)) {
            throw new AmigoPendenteException();
        }

        // 6. Verifica se existe solicitação pendente DO AMIGO para O USUÁRIO (aceita automaticamente)
        if (usuario.temSolicitacaoPendente(amigoLogin)) {
            usuario.aceitarSolicitacao(amigoLogin);
            amigo.aceitarSolicitacao(usuarioLogin);
            usuario.adicionarAmigo(amigoLogin);
            amigo.adicionarAmigo(usuarioLogin);
            return;
        }

        // 7. Se nenhum dos casos acima, envia nova solicitação
        amigo.receberSolicitacao(usuarioLogin);
    }

    /**
     * Edita um atributo do perfil do usuário da sessão atual.
     *
     * @param idSessao ID da sessão do usuário
     * @param atributo Nome do atributo a ser editado
     * @param valor Novo valor para o atributo
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     * @throws SessaoInvalidaExecption Se a sessão for inválida
     * @throws AtributoNaoPreenchidoException Se o nome do atributo for inválido
     */
    public void editarPerfil(String idSessao, String atributo, String valor)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, AtributoNaoPreenchidoException {
        if (atributo == null || atributo.trim().isEmpty()) throw new AtributoNaoPreenchidoException();

        String login = sessoes.get(idSessao);
        if (login == null || !usuarios.containsKey(login)) {
            throw new UsuarioNaoEncontradoException();
        }

        Users usuario = usuarios.get(login);
        usuario.setAtributo(atributo, valor);
    }


    /**
     * Verifica se um usuário tem outro como amigo (relação unilateral).
     *
     * @param login Login do primeiro usuário
     * @param amigo Login do possível amigo
     * @return true se o primeiro usuário tem o segundo como amigo, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum usuário não for encontrado
     */
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        if (!usuarios.containsKey(login) || !usuarios.containsKey(amigo)) {
            throw new UsuarioNaoEncontradoException();
        }
        return usuarios.get(login).ehAmigo(amigo);
    }

    /**
     * Verifica se dois usuários são amigos mútuos (ambos adicionaram um ao outro).
     *
     * @param login Login do primeiro usuário
     * @param amigo Login do segundo usuário
     * @return true se forem amigos mútuos, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum usuário não for encontrado
     */
    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        Users outroUsuario = usuarios.get(amigo);
        if (usuario == null || outroUsuario == null) throw new UsuarioNaoEncontradoException();
        return usuario.ehAmigo(amigo) && outroUsuario.ehAmigo(login);
    }

    /**
     * Obtém a lista de amigos de um usuário formatada.
     *
     * @param login Login do usuário
     * @return String formatada com a lista de amigos no formato {amigo1,amigo2,...}
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", usuario.getAmigos()) + "}";
    }

    /**
     * Obtém as solicitações de amizade pendentes de um usuário formatadas.
     *
     * @param login Login do usuário
     * @return String formatada com as solicitações pendentes no formato {solicitante1,solicitante2,...}
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", usuario.getSolicitacoesPendentes()) + "}";
    }

    /**
     * Encerra o sistema, salvando todos os dados atuais no arquivo de persistência.
     */
    public void encerrarSistema() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    /**
     * Obtém o valor de um atributo específico do perfil de um usuário.
     *
     * @param login Login do usuário
     * @param atributo Nome do atributo a ser obtido
     * @return Valor do atributo solicitado
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     * @throws AtributoNaoPreenchidoException Se o atributo não existir ou estiver vazio
     */
    public String getAtributoUsuario(String login, String atributo) throws UsuarioNaoEncontradoException, AtributoNaoPreenchidoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();

        if ("nome".equalsIgnoreCase(atributo)) {
            return usuario.getNome();
        }

        String valor = usuario.getAtributo(atributo); // Assumindo que o método 'getAtributo' já exista na classe 'Users'
        if (valor == null || valor.isEmpty()) {
            throw new AtributoNaoPreenchidoException();
        }
        return valor;
    }

    /**
     * Envia um recado para outro usuário.
     *
     * @param idSessao ID da sessão do remetente
     * @param destinatarioLogin Login do usuário destinatário
     * @param recado Texto do recado
     * @throws UsuarioNaoEncontradoException Se o destinatário não for encontrado
     * @throws SessaoInvalidaExecption Se a sessão for inválida
     * @throws IllegalArgumentException Se tentar enviar recado para si mesmo
     */
    public void enviarRecado(String idSessao, String destinatarioLogin, String recado)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption {


        if (destinatarioLogin == null || !usuarios.containsKey(destinatarioLogin)) {
            throw new UsuarioNaoEncontradoException();
        }


        String remetenteLogin = sessoes.get(idSessao);
        if (remetenteLogin == null) {
            throw new SessaoInvalidaExecption();
        }


        if (remetenteLogin.equals(destinatarioLogin)) {
            throw new IllegalArgumentException("Usuário não pode enviar recado para si mesmo.");
        }


        Users destinatario = usuarios.get(destinatarioLogin);
        destinatario.receberRecado(recado);
    }

    /**
     * Lê o próximo recado não lido do usuário da sessão (sistema FIFO).
     *
     * @param idSessao ID da sessão do usuário
     * @return Texto do recado mais antigo não lido
     * @throws SessaoInvalidaExecption Se a sessão for inválida
     * @throws SemRecadoException Se não houver recados para ler
     */
    public String lerRecado(String idSessao) throws SessaoInvalidaExecption, SemRecadoException {
        // Valida sessão
        String usuarioLogin = sessoes.get(idSessao);
        if (usuarioLogin == null) {
            throw new SessaoInvalidaExecption();
        }

        // Lê recado
        Users usuario = usuarios.get(usuarioLogin);
        String recado = usuario.lerRecado();
        if (recado == null) {
            throw new SemRecadoException();
        }
        return recado;
    }

}
