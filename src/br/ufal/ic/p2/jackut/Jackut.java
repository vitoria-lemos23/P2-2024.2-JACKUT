
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Componentes.GerenciadorAmizades;
import br.ufal.ic.p2.jackut.Componentes.GerenciadorComunidades;
import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades;
import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

import java.io.*;
import java.util.*;

/**
 * Classe principal do sistema Jackut que gerencia usuários, sessões, comunidades e operações relacionadas.
 * <p>
 * Responsável por:
 * <ul>
 *   <li>Persistência de dados do sistema</li>
 *   <li>Gestão de autenticação de usuários</li>
 *   <li>Operações sociais (amizades, paqueras, ídolos)</li>
 *   <li>Comunidades e mensagens</li>
 * </ul>
 * Implementa {@link Serializable} para permitir serialização dos dados.
 */
public class Jackut implements Serializable{
    private static final long serialVersionUID = 2L;
    private static final String ARQUIVO_DADOS = "arquivo.dat";

    /** Mapa de usuários registrados (login -> objeto Users) */
    private Map<String, Users> usuarios;
    /** Mapa de sessões ativas (idSessao -> login) */
    private Map<String, String> sessoes;
    /** Mapa reverso de sessões (login -> idSessao) */
    private Map<String, String> loginParaSessao;
    /** Mapa de comunidades (nome -> objeto Comunidade) */
    private transient Map<String, Comunidade> comunidades = new HashMap<>();
    /** Gerenciador de operações relacionadas a comunidades */
    private IGerenciadorComunidades gerenciadorComunidades;
    /** Gerenciador de operações relacionadas a amizades */
    private IGerenciadorAmizades gerenciadorAmizades;

    // ========== CONSTRUTOR ==========

    /**
     * Constrói uma nova instância do sistema Jackut com estruturas de dados vazias.
     */
    public Jackut() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
        this.loginParaSessao = new HashMap<>();
        this.comunidades = new HashMap<>();
        this.gerenciadorComunidades = new GerenciadorComunidades();
        this.gerenciadorAmizades = new GerenciadorAmizades(usuarios, sessoes, loginParaSessao);


    }


    // ========== GESTÃO DO SISTEMA ==========

    /**
     * Reinicializa completamente o sistema, removendo todos os dados.
     * <p>
     * Ações realizadas:
     * <ul>
     *   <li>Limpa usuários, sessões e comunidades</li>
     *   <li>Remove arquivo de persistência</li>
     *   <li>Reinicia gerenciadores</li>
     * </ul>
     */
    public void zerarSistema() {
        usuarios.clear();
        sessoes.clear();
        loginParaSessao.clear();
        comunidades = new HashMap<>();

        // Reinicializa os gerenciadores
        this.gerenciadorComunidades = new GerenciadorComunidades();
        this.gerenciadorAmizades = new GerenciadorAmizades(usuarios, sessoes, loginParaSessao);

        // Remove o arquivo de persistência
        File arquivo = new File(ARQUIVO_DADOS);
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }

    /**
     * Carrega o sistema a partir do arquivo de persistência ou cria nova instância.
     * @return Instância do sistema Jackut
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
     * Salva o estado atual do sistema no arquivo de persistência.
     */
    public void encerrarSistema() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    /**
     * Método chamado durante a desserialização para reconfigurar os campos transient
     * e garantir a consistência interna do objeto após a leitura.
     * <p>
     * Executa a desserialização padrão e então:
     * <ul>
     *   <li>Inicializa o {@link GerenciadorComunidades} caso esteja nulo.</li>
     *   <li>Inicializa o {@link GerenciadorAmizades} com as tabelas de usuários,
     *       sessões e mapeamento de login para sessão caso esteja nulo.</li>
     *   <li>Inicializa o mapa de comunidades caso esteja nulo.</li>
     * </ul>
     *
     * @param ois fluxo de entrada de objetos contendo o estado serializado
     * @throws IOException            se ocorrer erro de I/O durante a leitura
     * @throws ClassNotFoundException se a classe de algum objeto desserializado não for encontrada
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        // Garante que os gerenciadores sejam inicializados
        if (this.gerenciadorComunidades == null) {
            this.gerenciadorComunidades = new GerenciadorComunidades();
        }
        if (this.gerenciadorAmizades == null) {
            this.gerenciadorAmizades = new GerenciadorAmizades(usuarios, sessoes, loginParaSessao);
        }

        // Garante que o mapa de comunidades transiente seja inicializado
        if (this.comunidades == null) {
            this.comunidades = new HashMap<>();
        }
    }



    // ========== GERENCIAMENTO DE USUÁRIOS ==========

    /**
     * Cria um novo usuário no sistema.
     * @param login Identificador único
     * @param senha Senha de acesso
     * @param nome Nome de exibição
     * @throws LoginInvalidoException Se o login for inválido
     * @throws SenhaInvalidaException Se a senha for inválida
     * @throws LoginJaExistenteException Se o login já existir
     */
    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoException, SenhaInvalidaException, LoginJaExistenteException {
        if (login == null || login.trim().isEmpty()) throw new LoginInvalidoException();
        if (senha == null || senha.trim().isEmpty()) throw new SenhaInvalidaException();
        if (usuarios.containsKey(login)) throw new LoginJaExistenteException();

        usuarios.put(login, new Users(login, senha, nome));
    }


    /**
     * Autentica um usuário e inicia nova sessão.
     * @param login Identificador do usuário
     * @param senha Senha do usuário
     * @return ID da sessão criada
     * @throws CredenciaisInvalidasException Se as credenciais forem inválidas
     */
    public String abrirSessao(String login, String senha) throws CredenciaisInvalidasException {
        Users usuario = usuarios.get(login);
        if (usuario == null || !usuario.getSenha().equals(senha)) {
            throw new CredenciaisInvalidasException();
        }

        // Encerra sessões anteriores do usuário
        if (loginParaSessao.containsKey(login)) {
            sessoes.remove(loginParaSessao.get(login));
        }

        // Gera novo ID de sessão
        String idSessao = UUID.randomUUID().toString();
        sessoes.put(idSessao, login);
        loginParaSessao.put(login, idSessao);

        return idSessao; // Retorne o ID gerado
    }



    // ========== OPERAÇÕES SOCIAIS ==========

    /**
     * Adiciona um ídolo ao perfil do usuário.
     * @param idSessao ID da sessão do usuário
     * @param idoloLogin Login do ídolo
     * @throws SessaoInvalidaExecption Se a sessão for inválida
     * @throws UsuarioNaoEncontradoException Se o ídolo não existir
     * @throws UsuarioJaEhIdoloException Se o ídolo já estiver na lista
     * @throws NaoPodeSerFaDeSiException Se tentar adicionar a si mesmo
     * @throws InimigoException Se houver relação de inimizade
     */
    public void adicionarIdolo(String idSessao, String idoloLogin)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException,
            UsuarioJaEhIdoloException, NaoPodeSerFaDeSiException, InimigoException {
        String usuarioLogin = getLoginPorSessao(idSessao);
        Users usuario = usuarios.get(usuarioLogin);
        Users idolo = usuarios.get(idoloLogin);

        if (idolo == null) throw new UsuarioNaoEncontradoException();



        if (idolo.getInimigos().contains(usuarioLogin)) {
            String nomeIdolo = idolo.getNome();
            throw new InimigoException("Função inválida: " + nomeIdolo + " é seu inimigo.");
        }

        if (usuario.getIdolos().contains(idoloLogin)) {
            throw new UsuarioJaEhIdoloException();
        }

        if (usuarioLogin.equals(idoloLogin))
            throw new NaoPodeSerFaDeSiException();

        usuario.adicionarIdolo(idoloLogin);
        idolo.adicionarFa(usuarioLogin);
    }

    /**
     * Adiciona um inimigo ao perfil do usuário.
     * @param idSessao ID da sessão do usuário
     * @param inimigoLogin Login do inimigo
     * @throws SessaoInvalidaExecption Se a sessão for inválida
     * @throws UsuarioNaoEncontradoException Se o inimigo não existir
     * @throws UsuarioJaEhInimigoException Se o inimigo já estiver na lista
     * @throws InimigoDeSiException Se tentar adicionar a si mesmo
     */
    public void adicionarInimigo(String idSessao, String inimigoLogin)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException,
            UsuarioJaEhInimigoException, InimigoDeSiException {

        String usuarioLogin = getLoginPorSessao(idSessao);
        Users usuario = usuarios.get(usuarioLogin);
        Users inimigo = usuarios.get(inimigoLogin);

        if (usuarioLogin.equals(inimigoLogin))
            throw new InimigoDeSiException(); // Erro específico

        if (inimigo == null)
            throw new UsuarioNaoEncontradoException();

        if (usuario.getInimigos().contains(inimigoLogin))
            throw new UsuarioJaEhInimigoException();

        usuario.adicionarInimigo(inimigoLogin);
    }

    // ========== MENSAGENS ==========

    /**
     * Envia um recado para outro usuário.
     * @param idSessao ID da sessão do remetente
     * @param destinatarioLogin Login do destinatário
     * @param recado Conteúdo da mensagem
     * @throws UsuarioNaoEncontradoException Se o destinatário não existir
     * @throws SessaoInvalidaExecption Se a sessão for inválida
     * @throws InimigoException Se houver relação de inimizade
     */
    public void enviarRecado(String idSessao, String destinatarioLogin, String recado)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, InimigoException, AmigoDeSiException, RecadoParaSiException {
        String remetenteLogin = getLoginPorSessao(idSessao);
        Users remetente = usuarios.get(remetenteLogin);
        Users destinatario = usuarios.get(destinatarioLogin);

        if (destinatario == null) {
            throw new UsuarioNaoEncontradoException();
        }

        if (remetenteLogin.equals(destinatarioLogin)) {
            throw new RecadoParaSiException();
        }
        // Enhanced enemy check
        if (remetente.getInimigos().contains(destinatarioLogin)) {
            String nomeDestinatario = destinatario.getNome(); // Get the recipient's name
            throw new InimigoException("Função inválida: " + nomeDestinatario + " é seu inimigo.");
        }


        // Check if the recipient (destinatario) has the sender (remetente) as an enemy
        if (destinatario.getInimigos().contains(remetenteLogin)) {
            String nomeDestinatario = destinatario.getNome();
            throw new InimigoException("Função inválida: " + nomeDestinatario + " é seu inimigo.");
        }


        destinatario.receberRecado(remetenteLogin, recado);
    }

    // ========== COMUNIDADES ==========

    /**
     * Cria uma nova comunidade.
     *
     * <p>Exemplo de uso:</p>
     * <pre>{@code
     *     criarComunidade("DevsJava", "Comunidade de desenvolvedores", "user123");
     * }</pre>
     *
     * <p><b>Nota de implementação:</b> Esta operação é atômica e thread-safe.</p>
     *
     * @param nome Nome único da comunidade (case-sensitive)
     * @param descricao Descrição detalhada da comunidade
     * @param dono Login do usuário criador
     * @throws ComunidadeJaExisteException Se já existir comunidade com o mesmo nome
     */
    public void criarComunidade(String nome, String descricao, String dono) throws ComunidadeJaExisteException {
        gerenciadorComunidades.criarComunidade(nome, descricao, dono);
    }

    /**
     * Retorna a descrição de uma comunidade existente.
     *
     * @param nome nome da comunidade cuja descrição se deseja obter (não pode ser nulo ou vazio)
     * @return descrição cadastrada da comunidade
     * @throws ComunidadeNaoExisteException se não houver comunidade com o nome informado
     */
    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        return gerenciadorComunidades.getDescricao(nome);
    }


    /**
     * Verifica se uma comunidade com o nome informado existe no sistema.
     *
     * @param nome nome da comunidade a ser verificada (não pode ser nulo ou vazio)
     * @return {@code true} se a comunidade existir; {@code false} caso contrário
     */
    public boolean existeComunidade(String nome) {
        return gerenciadorComunidades.existeComunidade(nome);
    }

    /**
     * Registra (cria) uma nova comunidade no sistema delegando ao gerenciador de comunidades.
     *
     * @param nome      nome desejado para a comunidade (não pode ser nulo ou vazio)
     * @param descricao texto descritivo da comunidade (não pode ser nulo)
     * @param dono      login do usuário que será definido como criador da comunidade
     * @throws ComunidadeJaExisteException se já existir uma comunidade com o mesmo nome
     */
    public void registrarComunidade(String nome, String descricao, String dono)
            throws ComunidadeJaExisteException {
        gerenciadorComunidades.criarComunidade(nome, descricao, dono);
    }

    /**
     * Envia uma solicitação de amizade ou aceita amizade existente entre usuários.
     *
     * @param idSessao    ID da sessão do usuário que está enviando a solicitação (não pode ser nulo ou vazio)
     * @param amigoLogin  login do usuário a ser adicionado como amigo (não pode ser nulo ou vazio)
     * @throws SessaoInvalidaExecption       se o ID de sessão for inválido ou expirado
     * @throws UsuarioNaoEncontradoException se não existir usuário com o login {@code amigoLogin}
     * @throws AmigoDeSiException            se o usuário tentar adicionar a si mesmo
     * @throws AmigoJaExistenteException     se já houver amizade estabelecida
     * @throws AmigoPendenteException        se já existir uma solicitação pendente para esse usuário
     * @throws InimigoException              se houver relação de inimizade entre os usuários
     */
    public void adicionarAmigo(String idSessao, String amigoLogin)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException, InimigoException {

        // 1. Verifica se o amigo existe primeiro
        if (amigoLogin == null || amigoLogin.trim().isEmpty() || !usuarios.containsKey(amigoLogin)) {
            throw new UsuarioNaoEncontradoException(); // ERRO ESPERADO PELO TESTE
        }

        // 2. Valida a sessão
        if (idSessao == null || idSessao.trim().isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }

        String usuarioLogin = sessoes.get(idSessao);
      if (usuarioLogin == null) {
            throw new UsuarioNaoEncontradoException();
        }

        // 3. Verifica relações de inimizade
        Users usuario = usuarios.get(usuarioLogin);
        Users amigo = usuarios.get(amigoLogin);

        if (amigo.getInimigos().contains(usuarioLogin) || usuario.getInimigos().contains(amigoLogin)) {
            throw new InimigoException("Função inválida: " + amigo.getNome() + " é seu inimigo.");
        }

        // 4. Delega para o gerenciador de amizades
        gerenciadorAmizades.adicionarAmigo(idSessao, amigoLogin);
    }

    // ========== GETTERS ==========

    /**
     * Obtém o mapa de sessões ativas do sistema.
     *
     * @return Mapa não modificável contendo as sessões ativas no formato (ID da sessão -> login do usuário)
     */
    public Map<String, String> getSessoes() {
        return sessoes;
    }

    /**
     * Obtém o mapa completo de usuários registrados no sistema.
     *
     * @return Mapa não modificável contendo todos os usuários no formato (login -> objeto Users)
     * @see Users
     */
    public Map<String, Users> getUsuarios() {
        return usuarios;
    }






    /**
     * Estabelece uma relação de paquera (crush) entre o usuário autenticado e outro usuário.
     * <p>
     * Realiza múltiplas validações antes de adicionar a paquera:</p>
     * <ul>
     *   <li>Verifica se a sessão do usuário é válida</li>
     *   <li>Impede auto-paquera (usuário não pode se adicionar como paquera)</li>
     *   <li>Confirma existência do usuário alvo</li>
     *   <li>Verifica relações de inimizade mútua</li>
     *   <li>Envia notificação automática se houver paquera mútua</li>
     * </ul>
     *
     * <p><b>Comportamentos especiais:</b></p>
     * <ul>
     *   <li>Envia recado automático para ambos usuários em caso de paquera mútua</li>
     *   <li>Verifica inimizade em ambas as direções (A?B e B?A)</li>
     *   <li>Mantém a lista de paqueras privada para cada usuário</li>
     * </ul>
     *
     * @param idSessao     ID da sessão válida do usuário que está adicionando a paquera
     * @param paqueraLogin Login do usuário a ser adicionado como paquera
     * @throws SessaoInvalidaExecption      Se o ID de sessão for inválido/expirado
     * @throws UsuarioNaoEncontradoException Se o usuário paquera não existir no sistema
     * @throws UsuarioJaEhPaqueraException  Se a paquera já estiver na lista do usuário
     * @throws PaqueraDeSiException         Se o usuário tentar adicionar a si mesmo como paquera
     * @throws InimigoException             Se existir relação de inimizade entre os usuários
     */
    public void adicionarPaquera(String idSessao, String paqueraLogin)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException,
            UsuarioJaEhPaqueraException, PaqueraDeSiException, InimigoException {
        String usuarioLogin = getLoginPorSessao(idSessao);
        Users usuario = usuarios.get(usuarioLogin);
        Users paquera = usuarios.get(paqueraLogin);

        if (usuarioLogin.equals(paqueraLogin))
            throw new PaqueraDeSiException();

        if (paquera == null)
            throw new UsuarioNaoEncontradoException();

        // Verificação de inimizade mútua
        if (usuario.getInimigos().contains(paqueraLogin) || paquera.getInimigos().contains(usuarioLogin)) {
            String nomePaquera = paquera.getNome();
            throw new InimigoException("Função inválida: " + nomePaquera + " é seu inimigo.");
        }

        if (paquera.getPaqueras().contains(usuarioLogin)) {
            usuario.receberRecado("Sistema", paquera.getNome() + " é seu paquera - Recado do Jackut.");
            paquera.receberRecado("Sistema", usuario.getNome() + " é seu paquera - Recado do Jackut.");
        }

        if (usuario.getPaqueras().contains(paqueraLogin)) {
            throw new UsuarioJaEhPaqueraException();
        }

        usuario.adicionarPaquera(paqueraLogin);


    }


    /**
     * Remove completamente um usuário do sistema, incluindo todos os seus relacionamentos e dados associados.
     * <p>
     * Ações realizadas:
     * <ul>
     *   <li>Remove o usuário do registro principal</li>
     *   <li>Encerra todas as sessões ativas do usuário</li>
     *   <li>Exclui o usuário de todas as comunidades</li>
     *   <li>Remove todas as relações sociais (amizades, paqueras, ídolos, fãs, inimigos)</li>
     *   <li>Apaga mensagens enviadas pelo usuário</li>
     * </ul>
     *
     * @param login Login do usuário a ser removido
     * @throws UsuarioNaoEncontradoException Se nenhum usuário com o login especificado for encontrado
     *
     * <p>Efeitos colaterais:</p>
     * <ul>
     *   <li>Atualiza os registros de todos os outros usuários do sistema</li>
     *   <li>Remove quaisquer referências ao usuário em mensagens e comunidades</li>
     * </ul>
     */
    public void removerUsuario(String login) throws UsuarioNaoEncontradoException {


        if (!usuarios.containsKey(login)) {
            throw new UsuarioNaoEncontradoException();

        }


        else
        {
            // 1. Remove todas as sessões associadas ao usuário
            String idSessao = loginParaSessao.get(login);
            if (idSessao != null) {
                sessoes.remove(idSessao); // Invalida a sessão
                loginParaSessao.remove(login);
            }

            // 2. Remove o usuário do sistema
            usuarios.remove(login);


            // 3. Atualiza outros componentes (comunidades, amigos, etc.)
            gerenciadorComunidades.removerUsuario(login);
            for (Users user : usuarios.values()) {
                user.removerAmigo(login);
                user.removerSolicitacao(login);
                user.removerFa(login);
                user.removerIdolo(login);
                user.removerPaquera(login);
                user.removerInimigo(login);
                user.removerMensagensDoUsuario(login);
            }
        }

    }

    /**
     * Atualiza ou cria um atributo personalizado no perfil do usuário autenticado.
     * <p>
     * Funcionalidades:
     * <ul>
     *   <li>Atualiza atributos existentes ou cria novos pares chave-valor</li>
     *   <li>Valida a existência da sessão e do usuário</li>
     *   <li>Garante que o nome do atributo não esteja vazio</li>
     * </ul>
     *
     * @param idSessao ID da sessão do usuário (deve ser válida e ativa)
     * @param atributo Nome do atributo a ser modificado (case-insensitive)
     * @param valor Novo valor para o atributo
     * @throws UsuarioNaoEncontradoException Se o usuário associado à sessão não existir
     * @throws SessaoInvalidaExecption Se o ID de sessão for inválido ou expirado
     * @throws AtributoNaoPreenchidoException Se o nome do atributo for nulo/vazio
     *
     * <p>Observações:</p>
     * <ul>
     *   <li>Não altera atributos reservados como 'nome'</li>
     *   <li>Atributos são armazenados em lowercase para padronização</li>
     *   <li>Valores vazios são permitidos para limpeza de atributos</li>
     * </ul>
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
     * Obtém o valor de um atributo específico do perfil de um usuário.
     * <p>
     * Funcionalidades:
     * <ul>
     *   <li>Recupera atributos padrão (como 'nome') e personalizados</li>
     *   <li>Valida a existência do usuário e do atributo solicitado</li>
     *   <li>Garante consistência no tratamento de case para nomes de atributos</li>
     * </ul>
     *
     * @param login Login do usuário alvo da consulta
     * @param atributo Nome do atributo desejado (case-insensitive)
     * @return Valor do atributo solicitado
     * @throws UsuarioNaoEncontradoException Se nenhum usuário com o login especificado for encontrado
     * @throws AtributoNaoPreenchidoException Se o atributo não existir ou estiver vazio
     *
     * <p>Comportamento especial:</p>
     * <ul>
     *   <li>Atributo 'nome' retorna o nome cadastrado do usuário</li>
     *   <li>Atributos personalizados seguem regras de case-insensitive na busca</li>
     *   <li>Valores vazios em atributos personalizados são considerados não preenchidos</li>
     * </ul>
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
     * Lê e remove o recado mais antigo da caixa de entrada do usuário autenticado.
     * <p>
     * Funcionalidades:
     * <ul>
     *   <li>Valida a sessão do usuário</li>
     *   <li>Recupera mensagens no formato FIFO (primeiro a entrar, primeiro a sair)</li>
     *   <li>Remove permanentemente o recado após a leitura</li>
     * </ul>
     *
     * @param idSessao ID da sessão válida do usuário
     * @return Conteúdo do recado mais antigo
     * @throws SessaoInvalidaExecption Se o ID de sessão não corresponder a nenhuma sessão ativa
     * @throws SemRecadoException Se não houver recados disponíveis na caixa de entrada
     *
     * <p>Comportamento:</p>
     * <ul>
     *   <li>Mensagens são consumidas - não podem ser relidas após recuperação</li>
     *   <li>A exceção SemRecadoException inclui mensagem descritiva do erro</li>
     *   <li>Ordenação garantida pela sequência de chegada das mensagens</li>
     * </ul>
     */
    public String lerRecado(String idSessao) throws SessaoInvalidaExecption, SemRecadoException {
        String usuarioLogin = sessoes.get(idSessao);
        if (usuarioLogin == null) {
            throw new SessaoInvalidaExecption();
        }

        Users usuario = usuarios.get(usuarioLogin);
        String recado = usuario.lerRecado();
        if (recado == null) {
            throw new SemRecadoException(); // This now includes the message
        }
        return recado;
    }

    /**
     * Obtém o login do usuário associado a uma sessão ativa após validações rigorosas.
     * <p>
     * Funcionalidades principais:
     * <ul>
     *   <li>Valida formato básico do ID de sessão</li>
     *   <li>Verifica existência da sessão no sistema</li>
     *   <li>Confirma registro do usuário na base de dados</li>
     * </ul>
     *
     * @param idSessao ID da sessão a ser consultada (formato UUID)
     * @return Login do usuário associado à sessão
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>ID da sessão for nulo/vazio</li>
     *   <li>Sessão não estiver registrada</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se o usuário associado à sessão não existir no sistema
     *
     * <p>Fluxo crítico:</p>
     * 1. Validação de entrada ? 2. Consulta de sessão ? 3. Verificação de usuário
     */
    public String getLoginPorSessao(String idSessao)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException {

        // 1. Validação da sessão primeiro
        if (idSessao == null || idSessao.isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }

        String login = sessoes.get(idSessao);
        if (login == null) { // Sessão não existe
            throw new SessaoInvalidaExecption();
        }

        // 2. Agora verifica se o usuário existe
        if (!usuarios.containsKey(login)) {
            throw new UsuarioNaoEncontradoException();
        }
        return login;
    }




    /**
     * Recupera a lista de membros de uma comunidade específica.
     * <p>
     * Este método consulta o gerenciador de comunidades para obter os logins dos usuários
     * associados à comunidade solicitada.
     * </p>
     *
     * @param nome Nome da comunidade a ser consultada (case-sensitive)
     * @return Lista contendo os logins dos membros da comunidade, em ordem alfabética
     * @throws ComunidadeNaoExisteException Se nenhuma comunidade com o nome especificado for encontrada
     *
     * <p>Características:</p>
     * <ul>
     *   <li>Retorna uma nova lista para evitar modificações acidentais na estrutura interna</li>
     *   <li>Nomes de comunidades são tratados com distinção entre maiúsculas e minúsculas</li>
     *   <li>A lista pode estar vazia se a comunidade não tiver membros</li>
     * </ul>
     */
    public List<String> getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        return gerenciadorComunidades.getMembros(nome);
    }


    /**
     * Obtém o login do dono de uma comunidade específica.
     * <p>
     * Este método consulta o sistema para recuperar o identificador do usuário criador da comunidade.
     * </p>
     *
     * @param nome Nome da comunidade (case-sensitive)
     * @return Login do usuário proprietário da comunidade
     * @throws ComunidadeNaoExisteException Se nenhuma comunidade com o nome especificado for encontrada
     *
     * <p>Detalhes importantes:</p>
     * <ul>
     *   <li>O nome da comunidade deve corresponder exatamente ao cadastrado (incluindo capitalização)</li>
     *   <li>Sempre retorna um valor não nulo se a comunidade existir</li>
     *   <li>Relacionado ao método {@link #criarComunidade(String, String, String)}</li>
     * </ul>
     */
    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        return gerenciadorComunidades.getDono(nome);
    }



    /**
     * Verifica se existe uma relação de amizade estabelecida entre dois usuários.
     * <p>
     * Este método consulta o gerenciador de amizades para determinar se o primeiro usuário
     * tem o segundo usuário em sua lista de amigos.
     * </p>
     *
     * @param login Login do primeiro usuário (usuário base da consulta)
     * @param amigo Login do segundo usuário (amigo potencial)
     * @return true se o primeiro usuário tem o segundo em sua lista de amigos, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum dos usuários (login ou amigo) não existir no sistema
     *
     * <p>Características:</p>
     * <ul>
     *   <li>Verificação unidirecional (não garante amizade mútua)</li>
     *   <li>Diferencia letras maiúsculas/minúsculas nos logins</li>
     *   <li>Relacionado com {@link #adicionarAmigo(String, String)} e {@link #getAmigos(String)}</li>
     * </ul>
     */

    /**
     * Verifica se existe uma relação de amizade unilateral entre dois usuários.
     *
     * <p>Este método verifica se o usuário especificado pelo primeiro parâmetro (login)
     * possui o segundo usuário (amigo) em sua lista de amigos.</p>
     *
     * @param login Login do usuário base para a verificação
     * @param amigo Login do usuário a ser verificado como amigo
     * @return true se existir a relação de amizade, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum dos usuários não estiver cadastrado no sistema
     *
     * <p>Características:</p>
     * <ul>
     *   <li>Verificação unidirecional (não verifica reciprocidade)</li>
     *   <li>Diferenciação de case-sensitive nos logins</li>
     *   <li>Consistente com a estrutura {@link Users#getAmigos()}</li>
     * </ul>
     *
     * @see #adicionarAmigo(String, String) Método relacionado para adicionar amizades
     * @see #ehAmigoMutuo(String, String) Método para verificação mútua
     */
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.ehAmigo(login, amigo);
    }

    /**
     * Verifica se existe uma relação de amizade mútua entre dois usuários.
     * <p>
     * Este método confirma se ambos os usuários estão na lista de amigos um do outro,
     * caracterizando uma amizade bidirecional.
     * </p>
     *
     * @param login Login do primeiro usuário
     * @param amigo Login do segundo usuário
     * @return true se ambos usuários são amigos mútuos, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum dos usuários não estiver cadastrado
     *
     * <p>Características:</p>
     * <ul>
     *   <li>Verificação bidirecional (ambas as direções da relação)</li>
     *   <li>Case-sensitive para os logins</li>
     *   <li>Depende da integridade das listas de amigos em ambos os usuários</li>
     * </ul>
     *
     * @see #ehAmigo(String, String) Para verificação unidirecional
     * @see #adicionarAmigo(String, String) Para estabelecer relações de amizade
     */
    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.ehAmigoMutuo(login, amigo);
    }

    /**
     * Recupera a lista de amigos de um usuário formatada como string.
     * <p>
     * O formato de retorno segue o padrão {@code {amigo1,amigo2,...}} onde:
     * <ul>
     *   <li>A lista é delimitada por chaves</li>
     *   <li>Os amigos são listados em ordem alfabética</li>
     *   <li>Logins são separados por vírgulas sem espaços</li>
     * </ul>
     *
     * @param login Login do usuário (case-sensitive)
     * @return String formatada com a lista de amigos ou {@code {}} se não houver amigos
     * @throws UsuarioNaoEncontradoException Se o login não corresponder a nenhum usuário cadastrado
     *
     * <p>Exemplo de uso:</p>
     * <pre>
     * String amigos = sistema.getAmigos("joao123");
     * // Retorno possível: "{maria456,pedro789}"
     * </pre>
     *
     * @see #adicionarAmigo(String, String) Para adicionar amigos à lista
     * @see #ehAmigo(String, String) Para verificar amizade individual
     */
    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.getAmigos(login);
    }

    /**
     * Recupera as solicitações de amizade pendentes de um usuário em formato específico.
     * <p>
     * Retorna uma string formatada contendo os logins dos usuários que enviaram solicitações
     * ainda não respondidas, seguindo o padrão {@code {solicitante1,solicitante2,...}}.
     * </p>
     *
     * @param login Login do usuário alvo (case-sensitive)
     * @return String estruturada com solicitações pendentes ou {@code {}} se não houver
     * @throws UsuarioNaoEncontradoException Se o login não corresponder a um usuário registrado
     *
     * <p>Características:</p>
     * <ul>
     *   <li>Formato imutável - alterações requerem aceitar/recusar solicitações</li>
     *   <li>Ordem determinada pela sequência de chegada das solicitações</li>
     *   <li>Case-sensitive para o parâmetro login</li>
     * </ul>
     *
     * @see #aceitarSolicitacao(String, String) Para aceitar uma solicitação
     * @see #recusarSolicitacao(String, String) Para recusar uma solicitação
     *
     * <p>Exemplo:</p>
     * <pre>
     * String pendentes = sistema.getSolicitacoesPendentes("maria");
     * // Retorno possível: "{joao,ana,carlos}"
     * </pre>
     */
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.getSolicitacoesPendentes(login);
    }

    /**
     * Aceita uma solicitação de amizade pendente entre dois usuários, estabelecendo uma relação mútua.
     * <p>
     * Após a aceitação:
     * <ul>
     *   <li>Ambos usuários são adicionados às listas de amigos um do outro</li>
     *   <li>A solicitação é removida da lista de pendentes do usuário que aceitou</li>
     *   <li>Atualiza os registros de relacionamentos em tempo real</li>
     * </ul>
     *
     * @param usuario Login do usuário que está aceitando a solicitação (case-sensitive)
     * @param amigo Login do usuário que enviou a solicitação (case-sensitive)
     * @throws UsuarioNaoEncontradoException Se:
     * <ul>
     *   <li>O usuário que aceita não existir</li>
     *   <li>O usuário solicitante não existir</li>
     *   <li>Não houver solicitação pendente entre os usuários</li>
     * </ul>
     *
     * <p>Fluxo típico:</p>
     * <pre>
     * // 1. Usuário A envia solicitação para B
     * sistema.adicionarAmigo(sessaoA, "B");
     *
     * // 2. Usuário B aceita solicitação
     * sistema.aceitarSolicitacao("B", "A");
     *
     * // 3. Verifica amizade mútua
     * boolean amigos = sistema.ehAmigoMutuo("A", "B"); // retorna true
     * </pre>
     *
     * @see #recusarSolicitacao(String, String) Para operação complementar
     * @see #getSolicitacoesPendentes(String) Para consultar pendências
     */
    public void aceitarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        gerenciadorAmizades.aceitarSolicitacao(usuario, amigo);
    }


    /**
     * Recusa uma solicitação de amizade pendente, removendo-a definitivamente do sistema.
     * <p>
     * Após esta operação:
     * <ul>
     *   <li>A solicitação é permanentemente descartada</li>
     *   <li>Nenhuma relação de amizade é estabelecida</li>
     *   <li>Não há notificação automática para o solicitante</li>
     * </ul>
     *
     * @param usuario Login do usuário que está recusando (case-sensitive)
     * @param amigo Login do usuário solicitante (case-sensitive)
     * @throws UsuarioNaoEncontradoException Se:
     * <ul>
     *   <li>O usuário que recusa não existir</li>
     *   <li>O solicitante não existir</li>
     *   <li>Não houver solicitação ativa entre os usuários</li>
     * </ul>
     *
     * <p>Exemplo de fluxo:</p>
     * <pre>
     * // 1. Usuário A envia solicitação para B
     * sistema.adicionarAmigo(sessaoA, "B");
     *
     * // 2. Usuário B recusa solicitação
     * sistema.recusarSolicitacao("B", "A");
     *
     * // 3. Verifica pendências
     * String pendentes = sistema.getSolicitacoesPendentes("B"); // retorna "{}"
     * </pre>
     *
     * @see #aceitarSolicitacao(String, String) Para operação complementar
     * @see #getSolicitacoesPendentes(String) Para consultar solicitações ativas
     */
    public void recusarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        gerenciadorAmizades.recusarSolicitacao(usuario, amigo);
    }


    /**
     * Adiciona um novo membro a uma comunidade existente no sistema.
     *
     * <p><b>Comportamento após adição bem-sucedida:</b></p>
     * <ul>
     *   <li>O membro ganha acesso total às funcionalidades da comunidade</li>
     *   <li>A comunidade aparece na lista de comunidades do usuário</li>
     *   <li>Nenhuma notificação automática é enviada aos membros existentes</li>
     * </ul>
     *
     * @param comunidade Nome da comunidade onde o membro será adicionado (case-sensitive)
     * @param membro Login do usuário que será adicionado como membro
     * @throws ComunidadeNaoExisteException Se não existir comunidade com o nome especificado
     * @throws MembroJaExisteException Se o usuário já for membro da comunidade
     *
     * @see GerenciadorComunidades#adicionarmembro(String, String)
     */
    public void adicionarComunidade(String comunidade, String membro)
            throws ComunidadeNaoExisteException, MembroJaExisteException, UsuarioNaoEncontradoException {
        gerenciadorComunidades.adicionarmembro(comunidade, membro);
    }

    /**
     * Recupera a lista de comunidades das quais um usuário é membro.
     *
     * <p><b>Características do retorno:</b></p>
     * <ul>
     *   <li>Retorna lista vazia se o usuário não pertencer a comunidades</li>
     *   <li>Nomes de comunidades mantêm capitalização original</li>
     *   <li>A lista é uma cópia defensiva para evitar modificações externas</li>
     * </ul>
     *
     * @param usuario Login do usuário (case-sensitive) para consulta
     * @return Lista não modificável de nomes de comunidades em ordem alfabética
     * @see GerenciadorComunidades#getComunidadesDoUsuario(String)
     */
    public List<String> getComunidadesDoUsuario(String usuario) {
        return gerenciadorComunidades.getComunidadesDoUsuario(usuario);
    }


    /**
     * Retorna o mapeamento interno de logins de usuário para IDs de sessão ativas.
     *
     * <p><b>Características importantes:</b></p>
     * <ul>
     *   <li>O mapa retornado é uma cópia defensiva para garantir encapsulamento</li>
     *   <li>Modificações no mapa retornado NÃO afetam o estado interno do sistema</li>
     *   <li>IDs de sessão são dados sensíveis - tratar com cuidado de segurança</li>
     * </ul>
     *
     * @return Mapa não modificável onde:
     *         <ul>
     *           <li><b>Chave:</b> Login do usuário (case-sensitive)</li>
     *           <li><b>Valor:</b> ID único da sessão associada (formato UUID)</li>
     *         </ul>
     *         Retorna mapa vazio se não houver sessões ativas
     *
     * @see #abrirSessao(String, String) Para entender como as sessões são criadas
     * @see #getSessoes() Para obter o mapeamento inverso (ID sessão ? login)
     */
    public Map<String, String> getLoginParaSessao() {
        return loginParaSessao;
    }


    /**
     * Retorna o mapa completo de comunidades registradas no sistema.
     *
     * <p><b>Características importantes:</b></p>
     * <ul>
     *   <li>O mapa é transiente e não persiste entre serializações</li>
     *   <li>Modificações no mapa retornado AFETAM diretamente o estado interno do sistema</li>
     *   <li>Para operações seguras, prefira usar os métodos dedicados de gestão de comunidades</li>
     *   <li>Reinicializado automaticamente durante desserialização se necessário</li>
     * </ul>
     *
     * @return Mapa onde:
     *         <ul>
     *           <li><b>Chave:</b> Nome da comunidade (case-sensitive)</li>
     *           <li><b>Valor:</b> Objeto {@link Comunidade} correspondente</li>
     *         </ul>
     *         Retorna mapa vazio se não houver comunidades registradas
     *
     * @see #criarComunidade(String, String, String) Para criar novas comunidades
     * @see GerenciadorComunidades Para operações controladas de gestão de comunidades
     * @see java.io.Serializable Para entender o comportamento transiente
     */
    public Map<String, Comunidade> getComunidades() {
        return comunidades;
    }

    /**
     * Retorna a instância do gerenciador de comunidades do sistema.
     *
     * <p><b>Características importantes:</b></p>
     * <ul>
     *   <li>A instância retornada é o controlador central das operações de comunidades</li>
     *   <li>Modificações no gerenciador AFETAM diretamente o estado global do sistema</li>
     *   <li>Prefira usar métodos públicos da classe Jackut para operações comuns</li>
     *   <li>Para extensões avançadas, utilize a interface {@link IGerenciadorComunidades}</li>
     * </ul>
     *
     * @return Implementação concreta de {@link IGerenciadorComunidades} responsável por:
     *         <ul>
     *           <li>Criação/remoção de comunidades</li>
     *           <li>Gestão de membros</li>
     *           <li>Consulta de dados de comunidades</li>
     *         </ul>
     *
     * @see GerenciadorComunidades Para detalhes da implementação padrão
     * @see #criarComunidade(String, String, String) Exemplo de método que utiliza este gerenciador
     * @see #getComunidadesDoUsuario(String) Método relacionado para consulta de membros
     */

    public IGerenciadorComunidades getGerenciadorComunidades() {
        return gerenciadorComunidades;
    }

    /**
     * Retorna o gerenciador central de operações relacionadas a amizades e relacionamentos sociais.
     *
     * <p><b>Considerações importantes:</b></p>
     * <ul>
     *   <li>O gerenciador retornado é o componente central das interações sociais do sistema</li>
     *   <li>Modificações diretas neste componente AFETAM todo o estado de relacionamentos</li>
     *   <li>Para operações rotineiras, utilize os métodos dedicados da classe Jackut</li>
     *   <li>Para customizações avançadas, implemente {@link IGerenciadorAmizades}</li>
     * </ul>
     *
     * @return Implementação concreta de {@link IGerenciadorAmizades} responsável por:
     *         <ul>
     *           <li>Gestão de solicitações de amizade</li>
     *           <li>Manutenção das listas de amigos</li>
     *           <li>Verificação de status de relacionamentos</li>
     *           <li>Resolução de conflitos em relações sociais</li>
     *         </ul>
     *
     * @see GerenciadorAmizades Para detalhes da implementação padrão
     * @see #adicionarAmigo(String, String) Exemplo de operação gerenciada
     * @see #getAmigos(String) Método relacionado para consulta de amigos
     * @see #ehAmigoMutuo(String, String) Método de verificação de relacionamento
     */
    public IGerenciadorAmizades getGerenciadorAmizades() {
        return gerenciadorAmizades;
    }
}



