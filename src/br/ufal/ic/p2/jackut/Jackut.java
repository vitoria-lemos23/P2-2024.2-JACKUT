/**
 * Pacote principal do sistema Jackut, contendo as classes principais de funcionamento
 * do sistema de rede social e suas exce��es personalizadas.
 */
package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Componentes.GerenciadorAmizades;
import br.ufal.ic.p2.jackut.Componentes.GerenciadorComunidades;
import br.ufal.ic.p2.jackut.Exceptions.*;
import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades;
import br.ufal.ic.p2.jackut.Interfaces.IGerenciadorComunidades;

import java.io.*;
import java.util.*;

/**
 * Classe principal do sistema Jackut que implementa a l�gica de neg�cio da rede social.
 * Gerencia usu�rios, sess�es, amizades, perfis e recados entre usu�rios.
 *
 * <p>Esta classe � respons�vel por todas as opera��es principais do sistema,
 * incluindo persist�ncia dos dados em arquivo.</p>
 *
 * @author Vit�ria Lemos
 */

public class Jackut implements Serializable{
    private static final long serialVersionUID = 2L;
    private static final String ARQUIVO_DADOS = "arquivo.dat";

    private Map<String, Users> usuarios;
    private Map<String, String> sessoes;
    private Map<String, String> loginParaSessao;
    private transient Map<String, Comunidade> comunidades = new HashMap<>();

    private IGerenciadorComunidades gerenciadorComunidades;

    private IGerenciadorAmizades gerenciadorAmizades;
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


    /**
     * Remove todos os dados do sistema, reiniciando-o para o estado inicial.
     * Limpa todos os usu�rios, sess�es e relacionamentos.
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

    /**
     * Inicializa o sistema Jackut, carregando dados persistentes se existirem.
     *
     * @return Inst�ncia do sistema Jackut com dados carregados ou nova inst�ncia vazia
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
     * Cria um novo usu�rio no sistema com os dados fornecidos.
     *
     * @param login Login do novo usu�rio (deve ser �nico, n�o nulo e n�o vazio)
     * @param senha Senha do novo usu�rio (n�o pode ser nula ou vazia)
     * @param nome Nome completo do novo usu�rio
     * @throws LoginInvalidoException Se o login for inv�lido (nulo ou vazio)
     * @throws SenhaInvalidaException Se a senha for inv�lida (nula ou vazia)
     * @throws LoginJaExistenteException Se o login j� estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoException, SenhaInvalidaException, LoginJaExistenteException {
        if (login == null || login.trim().isEmpty()) throw new LoginInvalidoException();
        if (senha == null || senha.trim().isEmpty()) throw new SenhaInvalidaException();
        if (usuarios.containsKey(login)) throw new LoginJaExistenteException();

        usuarios.put(login, new Users(login, senha, nome));
    }


    /**
     * Autentica um usu�rio e abre uma nova sess�o.
     *
     * @param login Login do usu�rio (deve existir no sistema)
     * @param senha Senha do usu�rio (deve corresponder ao cadastrado)
     * @return ID �nico da sess�o criada
     * @throws CredenciaisInvalidasException Se as credenciais forem inv�lidas ou incorretas
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
     * Adiciona um usu�rio como amigo, com tratamento para todos os casos especiais.
     *
     * @param idSessao ID da sess�o do usu�rio que est� adicionando
     * @param amigoLogin Login do usu�rio a ser adicionado como amigo
     * @throws UsuarioNaoEncontradoException Se o amigo n�o existir no sistema
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida ou expirada
     * @throws AmigoDeSiException Se tentar adicionar a si mesmo como amigo
     * @throws AmigoJaExistenteException Se j� forem amigos
     * @throws AmigoPendenteException Se j� houver solicita��o pendente para este amigo
     */
//    public void adicionarAmigo(String idSessao, String amigoLogin)
//            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
//            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException {
//
//        // 1. Verifica se o amigo existe PRIMEIRO (como solicitado)
//        if (amigoLogin == null || amigoLogin.trim().isEmpty() || !usuarios.containsKey(amigoLogin)) {
//            throw new UsuarioNaoEncontradoException();
//        }
//
//        // 2. Verifica a sess�o
//        if (idSessao == null || idSessao.trim().isEmpty()) {
//            throw new UsuarioNaoEncontradoException();
//        }
//
//        String usuarioLogin = sessoes.get(idSessao);
//         if (usuarioLogin == null) {
//            throw new SessaoInvalidaExecption();
//        }
//
//        // 3. Verifica auto-amizade
//        if (usuarioLogin.equals(amigoLogin)) {
//            throw new AmigoDeSiException();
//        }
//
//        Users usuario = usuarios.get(usuarioLogin);
//        Users amigo = usuarios.get(amigoLogin);
//
//        // 4. Verifica se j� s�o amigos
//        if (usuario.ehAmigo(amigoLogin) && amigo.ehAmigo(usuarioLogin)) {
//            throw new AmigoJaExistenteException();
//        }
//
//        // 5. Verifica se j� existe solicita��o pendente DO USU�RIO para O AMIGO
//        if (amigo.temSolicitacaoPendente(usuarioLogin)) {
//            throw new AmigoPendenteException();
//        }
//
//        // 6. Verifica se existe solicita��o pendente DO AMIGO para O USU�RIO (aceita automaticamente)
//        if (usuario.temSolicitacaoPendente(amigoLogin)) {
//            usuario.aceitarSolicitacao(amigoLogin);
//            amigo.aceitarSolicitacao(usuarioLogin);
//            usuario.adicionarAmigo(amigoLogin);
//            amigo.adicionarAmigo(usuarioLogin);
//            return;
//        }
//
//        // 7. Se nenhum dos casos acima, envia nova solicita��o
//        amigo.receberSolicitacao(usuarioLogin);
//    }

    /**
     * Edita um atributo do perfil do usu�rio da sess�o atual.
     *
     * @param idSessao ID da sess�o do usu�rio
     * @param atributo Nome do atributo a ser editado
     * @param valor Novo valor para o atributo
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o for encontrado
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida
     * @throws AtributoNaoPreenchidoException Se o nome do atributo for inv�lido
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
     * Verifica se um usu�rio tem outro como amigo (rela��o unilateral).
     *
     * @param login Login do primeiro usu�rio
     * @param amigo Login do poss�vel amigo
     * @return true se o primeiro usu�rio tem o segundo como amigo, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o for encontrado
     */
//    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
//        if (!usuarios.containsKey(login) || !usuarios.containsKey(amigo)) {
//            throw new UsuarioNaoEncontradoException();
//        }
//        return usuarios.get(login).ehAmigo(amigo);
//    }

    /**
     * Verifica se dois usu�rios s�o amigos m�tuos (ambos adicionaram um ao outro).
     *
     * @param login Login do primeiro usu�rio
     * @param amigo Login do segundo usu�rio
     * @return true se forem amigos m�tuos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o for encontrado
     */
//    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
//        Users usuario = usuarios.get(login);
//        Users outroUsuario = usuarios.get(amigo);
//        if (usuario == null || outroUsuario == null) throw new UsuarioNaoEncontradoException();
//        return usuario.ehAmigo(amigo) && outroUsuario.ehAmigo(login);
//    }

    /**
     * Obt�m a lista de amigos de um usu�rio formatada.
     *
     * @param login Login do usu�rio
     * @return String formatada com a lista de amigos no formato {amigo1,amigo2,...}
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o for encontrado
     */
//    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
//        Users usuario = usuarios.get(login);
//        if (usuario == null) throw new UsuarioNaoEncontradoException();
//        return "{" + String.join(",", usuario.getAmigos()) + "}";
//    }

    /**
     * Obt�m as solicita��es de amizade pendentes de um usu�rio formatadas.
     *
     * @param login Login do usu�rio
     * @return String formatada com as solicita��es pendentes no formato {solicitante1,solicitante2,...}
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o for encontrado
     */
//    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
//        Users usuario = usuarios.get(login);
//        if (usuario == null) throw new UsuarioNaoEncontradoException();
//        return "{" + String.join(",", usuario.getSolicitacoesPendentes()) + "}";
//    }

    /**
     * Encerra o sistema, salvando todos os dados atuais no arquivo de persist�ncia.
     */
    public void encerrarSistema() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    /**
     * Obt�m o valor de um atributo espec�fico do perfil de um usu�rio.
     *
     * @param login Login do usu�rio
     * @param atributo Nome do atributo a ser obtido
     * @return Valor do atributo solicitado
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o for encontrado
     * @throws AtributoNaoPreenchidoException Se o atributo n�o existir ou estiver vazio
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
     * Envia um recado para outro usu�rio.
     *
     * @param idSessao ID da sess�o do remetente
     * @param destinatarioLogin Login do usu�rio destinat�rio
     * @param recado Texto do recado
     * @throws UsuarioNaoEncontradoException Se o destinat�rio n�o for encontrado
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida
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
            throw new IllegalArgumentException("Usu�rio n�o pode enviar recado para si mesmo.");
        }


        Users destinatario = usuarios.get(destinatarioLogin);
        destinatario.receberRecado(recado);
    }

    /**
     * L� o pr�ximo recado n�o lido do usu�rio da sess�o (sistema FIFO).
     *
     * @param idSessao ID da sess�o do usu�rio
     * @return Texto do recado mais antigo n�o lido
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida
     * @throws SemRecadoException Se n�o houver recados para ler
     */
    public String lerRecado(String idSessao) throws SessaoInvalidaExecption, SemRecadoException {
        // Valida sess�o
        String usuarioLogin = sessoes.get(idSessao);
        if (usuarioLogin == null) {
            throw new SessaoInvalidaExecption();
        }

        // L� recado
        Users usuario = usuarios.get(usuarioLogin);
        String recado = usuario.lerRecado();
        if (recado == null) {
            throw new SemRecadoException();
        }
        return recado;
    }


    public String getLoginPorSessao(String idSessao) throws SessaoInvalidaExecption {
        String login = sessoes.get(idSessao);
        if (login == null) throw new SessaoInvalidaExecption();
        return login;
    }



    //Metodos da comunidade

//    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
//        if (!comunidades.containsKey(nome)) {
//            throw new ComunidadeNaoExisteException();
//        }
//        return comunidades.get(nome).getDescricao();
//    }

//    public boolean existeComunidade(String nome) {
//        return comunidades.containsKey(nome);
//    }
//
//    public void registrarComunidade(String nome, String descricao, String dono)
//            throws ComunidadeJaExisteException {
//        if (existeComunidade(nome)) {
//            throw new ComunidadeJaExisteException();
//        }
//        comunidades.put(nome, new Comunidade(nome, descricao, dono));
//    }
//
//    public List<String> getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
//        if (!comunidades.containsKey(nome)) {
//            throw new ComunidadeNaoExisteException();
//        }
//        return comunidades.get(nome).getMembros(); // Retorna List<String>
//    }
//
//
//    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
//        if (!comunidades.containsKey(nome)) {
//            throw new ComunidadeNaoExisteException();
//        }
//        return comunidades.get(nome).getDono();
//    }


    public void criarComunidade(String nome, String descricao, String dono) throws ComunidadeJaExisteException {
        gerenciadorComunidades.criarComunidade(nome, descricao, dono);
    }
    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        return gerenciadorComunidades.getDescricao(nome);
    }



    public boolean existeComunidade(String nome) {
        return gerenciadorComunidades.existeComunidade(nome);
    }

    public void registrarComunidade(String nome, String descricao, String dono)
            throws ComunidadeJaExisteException {
        gerenciadorComunidades.criarComunidade(nome, descricao, dono);
    }

    public List<String> getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        return gerenciadorComunidades.getMembros(nome);
    }

    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        return gerenciadorComunidades.getDono(nome);
    }


    public void adicionarAmigo(String idSessao, String amigoLogin)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException {
        gerenciadorAmizades.adicionarAmigo(idSessao, amigoLogin);
    }

    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.ehAmigo(login, amigo);
    }

    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.ehAmigoMutuo(login, amigo);
    }

    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.getAmigos(login);
    }

    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        return gerenciadorAmizades.getSolicitacoesPendentes(login);
    }

    public void aceitarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        gerenciadorAmizades.aceitarSolicitacao(usuario, amigo);
    }

    public void recusarSolicitacao(String usuario, String amigo) throws UsuarioNaoEncontradoException {
        gerenciadorAmizades.recusarSolicitacao(usuario, amigo);
    }


}
