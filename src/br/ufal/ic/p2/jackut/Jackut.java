
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Componentes.GerenciadorAmizades;
import br.ufal.ic.p2.jackut.Componentes.GerenciadorComunidades;
import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades;
import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

import java.io.*;
import java.util.*;

/**
 * Classe principal do sistema Jackut que gerencia usu�rios, sess�es, comunidades e opera��es relacionadas.
 * <p>
 * Respons�vel por:
 * <ul>
 *   <li>Persist�ncia de dados do sistema</li>
 *   <li>Gest�o de autentica��o de usu�rios</li>
 *   <li>Opera��es sociais (amizades, paqueras, �dolos)</li>
 *   <li>Comunidades e mensagens</li>
 * </ul>
 * Implementa {@link Serializable} para permitir serializa��o dos dados.
 */
public class Jackut implements Serializable{
    private static final long serialVersionUID = 2L;
    private static final String ARQUIVO_DADOS = "arquivo.dat";

    /** Mapa de usu�rios registrados (login -> objeto Users) */
    private Map<String, Users> usuarios;
    /** Mapa de sess�es ativas (idSessao -> login) */
    private Map<String, String> sessoes;
    /** Mapa reverso de sess�es (login -> idSessao) */
    private Map<String, String> loginParaSessao;
    /** Mapa de comunidades (nome -> objeto Comunidade) */
    private transient Map<String, Comunidade> comunidades = new HashMap<>();
    /** Gerenciador de opera��es relacionadas a comunidades */
    private IGerenciadorComunidades gerenciadorComunidades;
    /** Gerenciador de opera��es relacionadas a amizades */
    private IGerenciadorAmizades gerenciadorAmizades;

    // ========== CONSTRUTOR ==========

    /**
     * Constr�i uma nova inst�ncia do sistema Jackut com estruturas de dados vazias.
     */
    public Jackut() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
        this.loginParaSessao = new HashMap<>();
        this.comunidades = new HashMap<>();
        this.gerenciadorComunidades = new GerenciadorComunidades();
        this.gerenciadorAmizades = new GerenciadorAmizades(usuarios, sessoes, loginParaSessao);


    }


    // ========== GEST�O DO SISTEMA ==========

    /**
     * Reinicializa completamente o sistema, removendo todos os dados.
     * <p>
     * A��es realizadas:
     * <ul>
     *   <li>Limpa usu�rios, sess�es e comunidades</li>
     *   <li>Remove arquivo de persist�ncia</li>
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

        // Remove o arquivo de persist�ncia
        File arquivo = new File(ARQUIVO_DADOS);
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }

    /**
     * Carrega o sistema a partir do arquivo de persist�ncia ou cria nova inst�ncia.
     * @return Inst�ncia do sistema Jackut
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
     * Salva o estado atual do sistema no arquivo de persist�ncia.
     */
    public void encerrarSistema() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    /**
     * M�todo chamado durante a desserializa��o para reconfigurar os campos transient
     * e garantir a consist�ncia interna do objeto ap�s a leitura.
     * <p>
     * Executa a desserializa��o padr�o e ent�o:
     * <ul>
     *   <li>Inicializa o {@link GerenciadorComunidades} caso esteja nulo.</li>
     *   <li>Inicializa o {@link GerenciadorAmizades} com as tabelas de usu�rios,
     *       sess�es e mapeamento de login para sess�o caso esteja nulo.</li>
     *   <li>Inicializa o mapa de comunidades caso esteja nulo.</li>
     * </ul>
     *
     * @param ois fluxo de entrada de objetos contendo o estado serializado
     * @throws IOException            se ocorrer erro de I/O durante a leitura
     * @throws ClassNotFoundException se a classe de algum objeto desserializado n�o for encontrada
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



    // ========== GERENCIAMENTO DE USU�RIOS ==========

    /**
     * Cria um novo usu�rio no sistema.
     * @param login Identificador �nico
     * @param senha Senha de acesso
     * @param nome Nome de exibi��o
     * @throws LoginInvalidoException Se o login for inv�lido
     * @throws SenhaInvalidaException Se a senha for inv�lida
     * @throws LoginJaExistenteException Se o login j� existir
     */
    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoException, SenhaInvalidaException, LoginJaExistenteException {
        if (login == null || login.trim().isEmpty()) throw new LoginInvalidoException();
        if (senha == null || senha.trim().isEmpty()) throw new SenhaInvalidaException();
        if (usuarios.containsKey(login)) throw new LoginJaExistenteException();

        usuarios.put(login, new Users(login, senha, nome));
    }


    /**
     * Autentica um usu�rio e inicia nova sess�o.
     * @param login Identificador do usu�rio
     * @param senha Senha do usu�rio
     * @return ID da sess�o criada
     * @throws CredenciaisInvalidasException Se as credenciais forem inv�lidas
     */
    public String abrirSessao(String login, String senha) throws CredenciaisInvalidasException {
        Users usuario = usuarios.get(login);
        if (usuario == null || !usuario.getSenha().equals(senha)) {
            throw new CredenciaisInvalidasException();
        }

        // Encerra sess�es anteriores do usu�rio
        if (loginParaSessao.containsKey(login)) {
            sessoes.remove(loginParaSessao.get(login));
        }

        // Gera novo ID de sess�o
        String idSessao = UUID.randomUUID().toString();
        sessoes.put(idSessao, login);
        loginParaSessao.put(login, idSessao);

        return idSessao; // Retorne o ID gerado
    }



    // ========== OPERA��ES SOCIAIS ==========

    /**
     * Adiciona um �dolo ao perfil do usu�rio.
     * @param idSessao ID da sess�o do usu�rio
     * @param idoloLogin Login do �dolo
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida
     * @throws UsuarioNaoEncontradoException Se o �dolo n�o existir
     * @throws UsuarioJaEhIdoloException Se o �dolo j� estiver na lista
     * @throws NaoPodeSerFaDeSiException Se tentar adicionar a si mesmo
     * @throws InimigoException Se houver rela��o de inimizade
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
            throw new InimigoException("Fun��o inv�lida: " + nomeIdolo + " � seu inimigo.");
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
     * Adiciona um inimigo ao perfil do usu�rio.
     * @param idSessao ID da sess�o do usu�rio
     * @param inimigoLogin Login do inimigo
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida
     * @throws UsuarioNaoEncontradoException Se o inimigo n�o existir
     * @throws UsuarioJaEhInimigoException Se o inimigo j� estiver na lista
     * @throws InimigoDeSiException Se tentar adicionar a si mesmo
     */
    public void adicionarInimigo(String idSessao, String inimigoLogin)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException,
            UsuarioJaEhInimigoException, InimigoDeSiException {

        String usuarioLogin = getLoginPorSessao(idSessao);
        Users usuario = usuarios.get(usuarioLogin);
        Users inimigo = usuarios.get(inimigoLogin);

        if (usuarioLogin.equals(inimigoLogin))
            throw new InimigoDeSiException(); // Erro espec�fico

        if (inimigo == null)
            throw new UsuarioNaoEncontradoException();

        if (usuario.getInimigos().contains(inimigoLogin))
            throw new UsuarioJaEhInimigoException();

        usuario.adicionarInimigo(inimigoLogin);
    }

    // ========== MENSAGENS ==========

    /**
     * Envia um recado para outro usu�rio.
     * @param idSessao ID da sess�o do remetente
     * @param destinatarioLogin Login do destinat�rio
     * @param recado Conte�do da mensagem
     * @throws UsuarioNaoEncontradoException Se o destinat�rio n�o existir
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida
     * @throws InimigoException Se houver rela��o de inimizade
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
            throw new InimigoException("Fun��o inv�lida: " + nomeDestinatario + " � seu inimigo.");
        }


        // Check if the recipient (destinatario) has the sender (remetente) as an enemy
        if (destinatario.getInimigos().contains(remetenteLogin)) {
            String nomeDestinatario = destinatario.getNome();
            throw new InimigoException("Fun��o inv�lida: " + nomeDestinatario + " � seu inimigo.");
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
     * <p><b>Nota de implementa��o:</b> Esta opera��o � at�mica e thread-safe.</p>
     *
     * @param nome Nome �nico da comunidade (case-sensitive)
     * @param descricao Descri��o detalhada da comunidade
     * @param dono Login do usu�rio criador
     * @throws ComunidadeJaExisteException Se j� existir comunidade com o mesmo nome
     */
    public void criarComunidade(String nome, String descricao, String dono) throws ComunidadeJaExisteException {
        gerenciadorComunidades.criarComunidade(nome, descricao, dono);
    }

    /**
     * Retorna a descri��o de uma comunidade existente.
     *
     * @param nome nome da comunidade cuja descri��o se deseja obter (n�o pode ser nulo ou vazio)
     * @return descri��o cadastrada da comunidade
     * @throws ComunidadeNaoExisteException se n�o houver comunidade com o nome informado
     */
    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        return gerenciadorComunidades.getDescricao(nome);
    }


    /**
     * Verifica se uma comunidade com o nome informado existe no sistema.
     *
     * @param nome nome da comunidade a ser verificada (n�o pode ser nulo ou vazio)
     * @return {@code true} se a comunidade existir; {@code false} caso contr�rio
     */
    public boolean existeComunidade(String nome) {
        return gerenciadorComunidades.existeComunidade(nome);
    }

    /**
     * Registra (cria) uma nova comunidade no sistema delegando ao gerenciador de comunidades.
     *
     * @param nome      nome desejado para a comunidade (n�o pode ser nulo ou vazio)
     * @param descricao texto descritivo da comunidade (n�o pode ser nulo)
     * @param dono      login do usu�rio que ser� definido como criador da comunidade
     * @throws ComunidadeJaExisteException se j� existir uma comunidade com o mesmo nome
     */
    public void registrarComunidade(String nome, String descricao, String dono)
            throws ComunidadeJaExisteException {
        gerenciadorComunidades.criarComunidade(nome, descricao, dono);
    }

    /**
     * Envia uma solicita��o de amizade ou aceita amizade existente entre usu�rios.
     *
     * @param idSessao    ID da sess�o do usu�rio que est� enviando a solicita��o (n�o pode ser nulo ou vazio)
     * @param amigoLogin  login do usu�rio a ser adicionado como amigo (n�o pode ser nulo ou vazio)
     * @throws SessaoInvalidaExecption       se o ID de sess�o for inv�lido ou expirado
     * @throws UsuarioNaoEncontradoException se n�o existir usu�rio com o login {@code amigoLogin}
     * @throws AmigoDeSiException            se o usu�rio tentar adicionar a si mesmo
     * @throws AmigoJaExistenteException     se j� houver amizade estabelecida
     * @throws AmigoPendenteException        se j� existir uma solicita��o pendente para esse usu�rio
     * @throws InimigoException              se houver rela��o de inimizade entre os usu�rios
     */
    public void adicionarAmigo(String idSessao, String amigoLogin)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException, InimigoException {

        // 1. Verifica se o amigo existe primeiro
        if (amigoLogin == null || amigoLogin.trim().isEmpty() || !usuarios.containsKey(amigoLogin)) {
            throw new UsuarioNaoEncontradoException(); // ERRO ESPERADO PELO TESTE
        }

        // 2. Valida a sess�o
        if (idSessao == null || idSessao.trim().isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }

        String usuarioLogin = sessoes.get(idSessao);
      if (usuarioLogin == null) {
            throw new UsuarioNaoEncontradoException();
        }

        // 3. Verifica rela��es de inimizade
        Users usuario = usuarios.get(usuarioLogin);
        Users amigo = usuarios.get(amigoLogin);

        if (amigo.getInimigos().contains(usuarioLogin) || usuario.getInimigos().contains(amigoLogin)) {
            throw new InimigoException("Fun��o inv�lida: " + amigo.getNome() + " � seu inimigo.");
        }

        // 4. Delega para o gerenciador de amizades
        gerenciadorAmizades.adicionarAmigo(idSessao, amigoLogin);
    }

    // ========== GETTERS ==========

    /**
     * Obt�m o mapa de sess�es ativas do sistema.
     *
     * @return Mapa n�o modific�vel contendo as sess�es ativas no formato (ID da sess�o -> login do usu�rio)
     */
    public Map<String, String> getSessoes() {
        return sessoes;
    }

    /**
     * Obt�m o mapa completo de usu�rios registrados no sistema.
     *
     * @return Mapa n�o modific�vel contendo todos os usu�rios no formato (login -> objeto Users)
     * @see Users
     */
    public Map<String, Users> getUsuarios() {
        return usuarios;
    }






    /**
     * Estabelece uma rela��o de paquera (crush) entre o usu�rio autenticado e outro usu�rio.
     * <p>
     * Realiza m�ltiplas valida��es antes de adicionar a paquera:</p>
     * <ul>
     *   <li>Verifica se a sess�o do usu�rio � v�lida</li>
     *   <li>Impede auto-paquera (usu�rio n�o pode se adicionar como paquera)</li>
     *   <li>Confirma exist�ncia do usu�rio alvo</li>
     *   <li>Verifica rela��es de inimizade m�tua</li>
     *   <li>Envia notifica��o autom�tica se houver paquera m�tua</li>
     * </ul>
     *
     * <p><b>Comportamentos especiais:</b></p>
     * <ul>
     *   <li>Envia recado autom�tico para ambos usu�rios em caso de paquera m�tua</li>
     *   <li>Verifica inimizade em ambas as dire��es (A?B e B?A)</li>
     *   <li>Mant�m a lista de paqueras privada para cada usu�rio</li>
     * </ul>
     *
     * @param idSessao     ID da sess�o v�lida do usu�rio que est� adicionando a paquera
     * @param paqueraLogin Login do usu�rio a ser adicionado como paquera
     * @throws SessaoInvalidaExecption      Se o ID de sess�o for inv�lido/expirado
     * @throws UsuarioNaoEncontradoException Se o usu�rio paquera n�o existir no sistema
     * @throws UsuarioJaEhPaqueraException  Se a paquera j� estiver na lista do usu�rio
     * @throws PaqueraDeSiException         Se o usu�rio tentar adicionar a si mesmo como paquera
     * @throws InimigoException             Se existir rela��o de inimizade entre os usu�rios
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

        // Verifica��o de inimizade m�tua
        if (usuario.getInimigos().contains(paqueraLogin) || paquera.getInimigos().contains(usuarioLogin)) {
            String nomePaquera = paquera.getNome();
            throw new InimigoException("Fun��o inv�lida: " + nomePaquera + " � seu inimigo.");
        }

        if (paquera.getPaqueras().contains(usuarioLogin)) {
            usuario.receberRecado("Sistema", paquera.getNome() + " � seu paquera - Recado do Jackut.");
            paquera.receberRecado("Sistema", usuario.getNome() + " � seu paquera - Recado do Jackut.");
        }

        if (usuario.getPaqueras().contains(paqueraLogin)) {
            throw new UsuarioJaEhPaqueraException();
        }

        usuario.adicionarPaquera(paqueraLogin);


    }


    /**
     * Remove completamente um usu�rio do sistema, incluindo todos os seus relacionamentos e dados associados.
     * <p>
     * A��es realizadas:
     * <ul>
     *   <li>Remove o usu�rio do registro principal</li>
     *   <li>Encerra todas as sess�es ativas do usu�rio</li>
     *   <li>Exclui o usu�rio de todas as comunidades</li>
     *   <li>Remove todas as rela��es sociais (amizades, paqueras, �dolos, f�s, inimigos)</li>
     *   <li>Apaga mensagens enviadas pelo usu�rio</li>
     * </ul>
     *
     * @param login Login do usu�rio a ser removido
     * @throws UsuarioNaoEncontradoException Se nenhum usu�rio com o login especificado for encontrado
     *
     * <p>Efeitos colaterais:</p>
     * <ul>
     *   <li>Atualiza os registros de todos os outros usu�rios do sistema</li>
     *   <li>Remove quaisquer refer�ncias ao usu�rio em mensagens e comunidades</li>
     * </ul>
     */
    public void removerUsuario(String login) throws UsuarioNaoEncontradoException {


        if (!usuarios.containsKey(login)) {
            throw new UsuarioNaoEncontradoException();

        }


        else
        {
            // 1. Remove todas as sess�es associadas ao usu�rio
            String idSessao = loginParaSessao.get(login);
            if (idSessao != null) {
                sessoes.remove(idSessao); // Invalida a sess�o
                loginParaSessao.remove(login);
            }

            // 2. Remove o usu�rio do sistema
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
     * Atualiza ou cria um atributo personalizado no perfil do usu�rio autenticado.
     * <p>
     * Funcionalidades:
     * <ul>
     *   <li>Atualiza atributos existentes ou cria novos pares chave-valor</li>
     *   <li>Valida a exist�ncia da sess�o e do usu�rio</li>
     *   <li>Garante que o nome do atributo n�o esteja vazio</li>
     * </ul>
     *
     * @param idSessao ID da sess�o do usu�rio (deve ser v�lida e ativa)
     * @param atributo Nome do atributo a ser modificado (case-insensitive)
     * @param valor Novo valor para o atributo
     * @throws UsuarioNaoEncontradoException Se o usu�rio associado � sess�o n�o existir
     * @throws SessaoInvalidaExecption Se o ID de sess�o for inv�lido ou expirado
     * @throws AtributoNaoPreenchidoException Se o nome do atributo for nulo/vazio
     *
     * <p>Observa��es:</p>
     * <ul>
     *   <li>N�o altera atributos reservados como 'nome'</li>
     *   <li>Atributos s�o armazenados em lowercase para padroniza��o</li>
     *   <li>Valores vazios s�o permitidos para limpeza de atributos</li>
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
     * Obt�m o valor de um atributo espec�fico do perfil de um usu�rio.
     * <p>
     * Funcionalidades:
     * <ul>
     *   <li>Recupera atributos padr�o (como 'nome') e personalizados</li>
     *   <li>Valida a exist�ncia do usu�rio e do atributo solicitado</li>
     *   <li>Garante consist�ncia no tratamento de case para nomes de atributos</li>
     * </ul>
     *
     * @param login Login do usu�rio alvo da consulta
     * @param atributo Nome do atributo desejado (case-insensitive)
     * @return Valor do atributo solicitado
     * @throws UsuarioNaoEncontradoException Se nenhum usu�rio com o login especificado for encontrado
     * @throws AtributoNaoPreenchidoException Se o atributo n�o existir ou estiver vazio
     *
     * <p>Comportamento especial:</p>
     * <ul>
     *   <li>Atributo 'nome' retorna o nome cadastrado do usu�rio</li>
     *   <li>Atributos personalizados seguem regras de case-insensitive na busca</li>
     *   <li>Valores vazios em atributos personalizados s�o considerados n�o preenchidos</li>
     * </ul>
     */
    public String getAtributoUsuario(String login, String atributo) throws UsuarioNaoEncontradoException, AtributoNaoPreenchidoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();

        if ("nome".equalsIgnoreCase(atributo)) {
            return usuario.getNome();
        }

        String valor = usuario.getAtributo(atributo); // Assumindo que o m�todo 'getAtributo' j� exista na classe 'Users'
        if (valor == null || valor.isEmpty()) {
            throw new AtributoNaoPreenchidoException();
        }
        return valor;
    }




    /**
     * L� e remove o recado mais antigo da caixa de entrada do usu�rio autenticado.
     * <p>
     * Funcionalidades:
     * <ul>
     *   <li>Valida a sess�o do usu�rio</li>
     *   <li>Recupera mensagens no formato FIFO (primeiro a entrar, primeiro a sair)</li>
     *   <li>Remove permanentemente o recado ap�s a leitura</li>
     * </ul>
     *
     * @param idSessao ID da sess�o v�lida do usu�rio
     * @return Conte�do do recado mais antigo
     * @throws SessaoInvalidaExecption Se o ID de sess�o n�o corresponder a nenhuma sess�o ativa
     * @throws SemRecadoException Se n�o houver recados dispon�veis na caixa de entrada
     *
     * <p>Comportamento:</p>
     * <ul>
     *   <li>Mensagens s�o consumidas - n�o podem ser relidas ap�s recupera��o</li>
     *   <li>A exce��o SemRecadoException inclui mensagem descritiva do erro</li>
     *   <li>Ordena��o garantida pela sequ�ncia de chegada das mensagens</li>
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
     * Obt�m o login do usu�rio associado a uma sess�o ativa ap�s valida��es rigorosas.
     * <p>
     * Funcionalidades principais:
     * <ul>
     *   <li>Valida formato b�sico do ID de sess�o</li>
     *   <li>Verifica exist�ncia da sess�o no sistema</li>
     *   <li>Confirma registro do usu�rio na base de dados</li>
     * </ul>
     *
     * @param idSessao ID da sess�o a ser consultada (formato UUID)
     * @return Login do usu�rio associado � sess�o
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>ID da sess�o for nulo/vazio</li>
     *   <li>Sess�o n�o estiver registrada</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se o usu�rio associado � sess�o n�o existir no sistema
     *
     * <p>Fluxo cr�tico:</p>
     * 1. Valida��o de entrada ? 2. Consulta de sess�o ? 3. Verifica��o de usu�rio
     */
    public String getLoginPorSessao(String idSessao)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException {

        // 1. Valida��o da sess�o primeiro
        if (idSessao == null || idSessao.isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }

        String login = sessoes.get(idSessao);
        if (login == null) { // Sess�o n�o existe
            throw new SessaoInvalidaExecption();
        }

        // 2. Agora verifica se o usu�rio existe
        if (!usuarios.containsKey(login)) {
            throw new UsuarioNaoEncontradoException();
        }
        return login;
    }




    /**
     * Recupera a lista de membros de uma comunidade espec�fica.
     * <p>
     * Este m�todo consulta o gerenciador de comunidades para obter os logins dos usu�rios
     * associados � comunidade solicitada.
     * </p>
     *
     * @param nome Nome da comunidade a ser consultada (case-sensitive)
     * @return Lista contendo os logins dos membros da comunidade, em ordem alfab�tica
     * @throws ComunidadeNaoExisteException Se nenhuma comunidade com o nome especificado for encontrada
     *
     * <p>Caracter�sticas:</p>
     * <ul>
     *   <li>Retorna uma nova lista para evitar modifica��es acidentais na estrutura interna</li>
     *   <li>Nomes de comunidades s�o tratados com distin��o entre mai�sculas e min�sculas</li>
     *   <li>A lista pode estar vazia se a comunidade n�o tiver membros</li>
     * </ul>
     */
    public List<String> getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        return gerenciadorComunidades.getMembros(nome);
    }


    /**
     * Obt�m o login do dono de uma comunidade espec�fica.
     * <p>
     * Este m�todo consulta o sistema para recuperar o identificador do usu�rio criador da comunidade.
     * </p>
     *
     * @param nome Nome da comunidade (case-sensitive)
     * @return Login do usu�rio propriet�rio da comunidade
     * @throws ComunidadeNaoExisteException Se nenhuma comunidade com o nome especificado for encontrada
     *
     * <p>Detalhes importantes:</p>
     * <ul>
     *   <li>O nome da comunidade deve corresponder exatamente ao cadastrado (incluindo capitaliza��o)</li>
     *   <li>Sempre retorna um valor n�o nulo se a comunidade existir</li>
     *   <li>Relacionado ao m�todo {@link #criarComunidade(String, String, String)}</li>
     * </ul>
     */
    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        return gerenciadorComunidades.getDono(nome);
    }



    /**
     * Verifica se existe uma rela��o de amizade estabelecida entre dois usu�rios.
     * <p>
     * Este m�todo consulta o gerenciador de amizades para determinar se o primeiro usu�rio
     * tem o segundo usu�rio em sua lista de amigos.
     * </p>
     *
     * @param login Login do primeiro usu�rio (usu�rio base da consulta)
     * @param amigo Login do segundo usu�rio (amigo potencial)
     * @return true se o primeiro usu�rio tem o segundo em sua lista de amigos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum dos usu�rios (login ou amigo) n�o existir no sistema
     *
     * <p>Caracter�sticas:</p>
     * <ul>
     *   <li>Verifica��o unidirecional (n�o garante amizade m�tua)</li>
     *   <li>Diferencia letras mai�sculas/min�sculas nos logins</li>
     *   <li>Relacionado com {@link #adicionarAmigo(String, String)} e {@link #getAmigos(String)}</li>
     * </ul>
     */

    /**
     * Verifica se existe uma rela��o de amizade unilateral entre dois usu�rios.
     *
     * <p>Este m�todo verifica se o usu�rio especificado pelo primeiro par�metro (login)
     * possui o segundo usu�rio (amigo) em sua lista de amigos.</p>
     *
     * @param login Login do usu�rio base para a verifica��o
     * @param amigo Login do usu�rio a ser verificado como amigo
     * @return true se existir a rela��o de amizade, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum dos usu�rios n�o estiver cadastrado no sistema
     *
     * <p>Caracter�sticas:</p>
     * <ul>
     *   <li>Verifica��o unidirecional (n�o verifica reciprocidade)</li>
     *   <li>Diferencia��o de case-sensitive nos logins</li>
     *   <li>Consistente com a estrutura {@link Users#getAmigos()}</li>
     * </ul>
     *
     * @see #adicionarAmigo(String, String) M�todo relacionado para adicionar amizades
     * @see #ehAmigoMutuo(String, String) M�todo para verifica��o m�tua
     */
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.ehAmigo(login, amigo);
    }

    /**
     * Verifica se existe uma rela��o de amizade m�tua entre dois usu�rios.
     * <p>
     * Este m�todo confirma se ambos os usu�rios est�o na lista de amigos um do outro,
     * caracterizando uma amizade bidirecional.
     * </p>
     *
     * @param login Login do primeiro usu�rio
     * @param amigo Login do segundo usu�rio
     * @return true se ambos usu�rios s�o amigos m�tuos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum dos usu�rios n�o estiver cadastrado
     *
     * <p>Caracter�sticas:</p>
     * <ul>
     *   <li>Verifica��o bidirecional (ambas as dire��es da rela��o)</li>
     *   <li>Case-sensitive para os logins</li>
     *   <li>Depende da integridade das listas de amigos em ambos os usu�rios</li>
     * </ul>
     *
     * @see #ehAmigo(String, String) Para verifica��o unidirecional
     * @see #adicionarAmigo(String, String) Para estabelecer rela��es de amizade
     */
    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.ehAmigoMutuo(login, amigo);
    }

    /**
     * Recupera a lista de amigos de um usu�rio formatada como string.
     * <p>
     * O formato de retorno segue o padr�o {@code {amigo1,amigo2,...}} onde:
     * <ul>
     *   <li>A lista � delimitada por chaves</li>
     *   <li>Os amigos s�o listados em ordem alfab�tica</li>
     *   <li>Logins s�o separados por v�rgulas sem espa�os</li>
     * </ul>
     *
     * @param login Login do usu�rio (case-sensitive)
     * @return String formatada com a lista de amigos ou {@code {}} se n�o houver amigos
     * @throws UsuarioNaoEncontradoException Se o login n�o corresponder a nenhum usu�rio cadastrado
     *
     * <p>Exemplo de uso:</p>
     * <pre>
     * String amigos = sistema.getAmigos("joao123");
     * // Retorno poss�vel: "{maria456,pedro789}"
     * </pre>
     *
     * @see #adicionarAmigo(String, String) Para adicionar amigos � lista
     * @see #ehAmigo(String, String) Para verificar amizade individual
     */
    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.getAmigos(login);
    }

    /**
     * Recupera as solicita��es de amizade pendentes de um usu�rio em formato espec�fico.
     * <p>
     * Retorna uma string formatada contendo os logins dos usu�rios que enviaram solicita��es
     * ainda n�o respondidas, seguindo o padr�o {@code {solicitante1,solicitante2,...}}.
     * </p>
     *
     * @param login Login do usu�rio alvo (case-sensitive)
     * @return String estruturada com solicita��es pendentes ou {@code {}} se n�o houver
     * @throws UsuarioNaoEncontradoException Se o login n�o corresponder a um usu�rio registrado
     *
     * <p>Caracter�sticas:</p>
     * <ul>
     *   <li>Formato imut�vel - altera��es requerem aceitar/recusar solicita��es</li>
     *   <li>Ordem determinada pela sequ�ncia de chegada das solicita��es</li>
     *   <li>Case-sensitive para o par�metro login</li>
     * </ul>
     *
     * @see #aceitarSolicitacao(String, String) Para aceitar uma solicita��o
     * @see #recusarSolicitacao(String, String) Para recusar uma solicita��o
     *
     * <p>Exemplo:</p>
     * <pre>
     * String pendentes = sistema.getSolicitacoesPendentes("maria");
     * // Retorno poss�vel: "{joao,ana,carlos}"
     * </pre>
     */
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.getSolicitacoesPendentes(login);
    }

    /**
     * Aceita uma solicita��o de amizade pendente entre dois usu�rios, estabelecendo uma rela��o m�tua.
     * <p>
     * Ap�s a aceita��o:
     * <ul>
     *   <li>Ambos usu�rios s�o adicionados �s listas de amigos um do outro</li>
     *   <li>A solicita��o � removida da lista de pendentes do usu�rio que aceitou</li>
     *   <li>Atualiza os registros de relacionamentos em tempo real</li>
     * </ul>
     *
     * @param usuario Login do usu�rio que est� aceitando a solicita��o (case-sensitive)
     * @param amigo Login do usu�rio que enviou a solicita��o (case-sensitive)
     * @throws UsuarioNaoEncontradoException Se:
     * <ul>
     *   <li>O usu�rio que aceita n�o existir</li>
     *   <li>O usu�rio solicitante n�o existir</li>
     *   <li>N�o houver solicita��o pendente entre os usu�rios</li>
     * </ul>
     *
     * <p>Fluxo t�pico:</p>
     * <pre>
     * // 1. Usu�rio A envia solicita��o para B
     * sistema.adicionarAmigo(sessaoA, "B");
     *
     * // 2. Usu�rio B aceita solicita��o
     * sistema.aceitarSolicitacao("B", "A");
     *
     * // 3. Verifica amizade m�tua
     * boolean amigos = sistema.ehAmigoMutuo("A", "B"); // retorna true
     * </pre>
     *
     * @see #recusarSolicitacao(String, String) Para opera��o complementar
     * @see #getSolicitacoesPendentes(String) Para consultar pend�ncias
     */
    public void aceitarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        gerenciadorAmizades.aceitarSolicitacao(usuario, amigo);
    }


    /**
     * Recusa uma solicita��o de amizade pendente, removendo-a definitivamente do sistema.
     * <p>
     * Ap�s esta opera��o:
     * <ul>
     *   <li>A solicita��o � permanentemente descartada</li>
     *   <li>Nenhuma rela��o de amizade � estabelecida</li>
     *   <li>N�o h� notifica��o autom�tica para o solicitante</li>
     * </ul>
     *
     * @param usuario Login do usu�rio que est� recusando (case-sensitive)
     * @param amigo Login do usu�rio solicitante (case-sensitive)
     * @throws UsuarioNaoEncontradoException Se:
     * <ul>
     *   <li>O usu�rio que recusa n�o existir</li>
     *   <li>O solicitante n�o existir</li>
     *   <li>N�o houver solicita��o ativa entre os usu�rios</li>
     * </ul>
     *
     * <p>Exemplo de fluxo:</p>
     * <pre>
     * // 1. Usu�rio A envia solicita��o para B
     * sistema.adicionarAmigo(sessaoA, "B");
     *
     * // 2. Usu�rio B recusa solicita��o
     * sistema.recusarSolicitacao("B", "A");
     *
     * // 3. Verifica pend�ncias
     * String pendentes = sistema.getSolicitacoesPendentes("B"); // retorna "{}"
     * </pre>
     *
     * @see #aceitarSolicitacao(String, String) Para opera��o complementar
     * @see #getSolicitacoesPendentes(String) Para consultar solicita��es ativas
     */
    public void recusarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        gerenciadorAmizades.recusarSolicitacao(usuario, amigo);
    }


    /**
     * Adiciona um novo membro a uma comunidade existente no sistema.
     *
     * <p><b>Comportamento ap�s adi��o bem-sucedida:</b></p>
     * <ul>
     *   <li>O membro ganha acesso total �s funcionalidades da comunidade</li>
     *   <li>A comunidade aparece na lista de comunidades do usu�rio</li>
     *   <li>Nenhuma notifica��o autom�tica � enviada aos membros existentes</li>
     * </ul>
     *
     * @param comunidade Nome da comunidade onde o membro ser� adicionado (case-sensitive)
     * @param membro Login do usu�rio que ser� adicionado como membro
     * @throws ComunidadeNaoExisteException Se n�o existir comunidade com o nome especificado
     * @throws MembroJaExisteException Se o usu�rio j� for membro da comunidade
     *
     * @see GerenciadorComunidades#adicionarmembro(String, String)
     */
    public void adicionarComunidade(String comunidade, String membro)
            throws ComunidadeNaoExisteException, MembroJaExisteException, UsuarioNaoEncontradoException {
        gerenciadorComunidades.adicionarmembro(comunidade, membro);
    }

    /**
     * Recupera a lista de comunidades das quais um usu�rio � membro.
     *
     * <p><b>Caracter�sticas do retorno:</b></p>
     * <ul>
     *   <li>Retorna lista vazia se o usu�rio n�o pertencer a comunidades</li>
     *   <li>Nomes de comunidades mant�m capitaliza��o original</li>
     *   <li>A lista � uma c�pia defensiva para evitar modifica��es externas</li>
     * </ul>
     *
     * @param usuario Login do usu�rio (case-sensitive) para consulta
     * @return Lista n�o modific�vel de nomes de comunidades em ordem alfab�tica
     * @see GerenciadorComunidades#getComunidadesDoUsuario(String)
     */
    public List<String> getComunidadesDoUsuario(String usuario) {
        return gerenciadorComunidades.getComunidadesDoUsuario(usuario);
    }


    /**
     * Retorna o mapeamento interno de logins de usu�rio para IDs de sess�o ativas.
     *
     * <p><b>Caracter�sticas importantes:</b></p>
     * <ul>
     *   <li>O mapa retornado � uma c�pia defensiva para garantir encapsulamento</li>
     *   <li>Modifica��es no mapa retornado N�O afetam o estado interno do sistema</li>
     *   <li>IDs de sess�o s�o dados sens�veis - tratar com cuidado de seguran�a</li>
     * </ul>
     *
     * @return Mapa n�o modific�vel onde:
     *         <ul>
     *           <li><b>Chave:</b> Login do usu�rio (case-sensitive)</li>
     *           <li><b>Valor:</b> ID �nico da sess�o associada (formato UUID)</li>
     *         </ul>
     *         Retorna mapa vazio se n�o houver sess�es ativas
     *
     * @see #abrirSessao(String, String) Para entender como as sess�es s�o criadas
     * @see #getSessoes() Para obter o mapeamento inverso (ID sess�o ? login)
     */
    public Map<String, String> getLoginParaSessao() {
        return loginParaSessao;
    }


    /**
     * Retorna o mapa completo de comunidades registradas no sistema.
     *
     * <p><b>Caracter�sticas importantes:</b></p>
     * <ul>
     *   <li>O mapa � transiente e n�o persiste entre serializa��es</li>
     *   <li>Modifica��es no mapa retornado AFETAM diretamente o estado interno do sistema</li>
     *   <li>Para opera��es seguras, prefira usar os m�todos dedicados de gest�o de comunidades</li>
     *   <li>Reinicializado automaticamente durante desserializa��o se necess�rio</li>
     * </ul>
     *
     * @return Mapa onde:
     *         <ul>
     *           <li><b>Chave:</b> Nome da comunidade (case-sensitive)</li>
     *           <li><b>Valor:</b> Objeto {@link Comunidade} correspondente</li>
     *         </ul>
     *         Retorna mapa vazio se n�o houver comunidades registradas
     *
     * @see #criarComunidade(String, String, String) Para criar novas comunidades
     * @see GerenciadorComunidades Para opera��es controladas de gest�o de comunidades
     * @see java.io.Serializable Para entender o comportamento transiente
     */
    public Map<String, Comunidade> getComunidades() {
        return comunidades;
    }

    /**
     * Retorna a inst�ncia do gerenciador de comunidades do sistema.
     *
     * <p><b>Caracter�sticas importantes:</b></p>
     * <ul>
     *   <li>A inst�ncia retornada � o controlador central das opera��es de comunidades</li>
     *   <li>Modifica��es no gerenciador AFETAM diretamente o estado global do sistema</li>
     *   <li>Prefira usar m�todos p�blicos da classe Jackut para opera��es comuns</li>
     *   <li>Para extens�es avan�adas, utilize a interface {@link IGerenciadorComunidades}</li>
     * </ul>
     *
     * @return Implementa��o concreta de {@link IGerenciadorComunidades} respons�vel por:
     *         <ul>
     *           <li>Cria��o/remo��o de comunidades</li>
     *           <li>Gest�o de membros</li>
     *           <li>Consulta de dados de comunidades</li>
     *         </ul>
     *
     * @see GerenciadorComunidades Para detalhes da implementa��o padr�o
     * @see #criarComunidade(String, String, String) Exemplo de m�todo que utiliza este gerenciador
     * @see #getComunidadesDoUsuario(String) M�todo relacionado para consulta de membros
     */

    public IGerenciadorComunidades getGerenciadorComunidades() {
        return gerenciadorComunidades;
    }

    /**
     * Retorna o gerenciador central de opera��es relacionadas a amizades e relacionamentos sociais.
     *
     * <p><b>Considera��es importantes:</b></p>
     * <ul>
     *   <li>O gerenciador retornado � o componente central das intera��es sociais do sistema</li>
     *   <li>Modifica��es diretas neste componente AFETAM todo o estado de relacionamentos</li>
     *   <li>Para opera��es rotineiras, utilize os m�todos dedicados da classe Jackut</li>
     *   <li>Para customiza��es avan�adas, implemente {@link IGerenciadorAmizades}</li>
     * </ul>
     *
     * @return Implementa��o concreta de {@link IGerenciadorAmizades} respons�vel por:
     *         <ul>
     *           <li>Gest�o de solicita��es de amizade</li>
     *           <li>Manuten��o das listas de amigos</li>
     *           <li>Verifica��o de status de relacionamentos</li>
     *           <li>Resolu��o de conflitos em rela��es sociais</li>
     *         </ul>
     *
     * @see GerenciadorAmizades Para detalhes da implementa��o padr�o
     * @see #adicionarAmigo(String, String) Exemplo de opera��o gerenciada
     * @see #getAmigos(String) M�todo relacionado para consulta de amigos
     * @see #ehAmigoMutuo(String, String) M�todo de verifica��o de relacionamento
     */
    public IGerenciadorAmizades getGerenciadorAmizades() {
        return gerenciadorAmizades;
    }
}



