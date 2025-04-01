package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exeptions.*;
import java.io.*;
import java.util.*;

public class Jackut implements Serializable {
    private static final long serialVersionUID = 2L;
    private static final String ARQUIVO_DADOS = "arquivo.dat";

    private Map<String, Users> usuarios;
    private Map<String, String> sessoes;
    private Map<String, String> loginParaSessao;

    public Jackut() {
        this.usuarios = new HashMap<>();
        this.sessoes = new HashMap<>();
        this.loginParaSessao = new HashMap<>();
    }

    public void zerarSistema() {
        usuarios.clear();
        sessoes.clear();
        loginParaSessao.clear();
    }

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

    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoExeption, SenhaInvalidaExeption, LoginJaExistenteException {
        if (login == null || login.trim().isEmpty()) throw new LoginInvalidoExeption();
        if (senha == null || senha.trim().isEmpty()) throw new SenhaInvalidaExeption();
        if (usuarios.containsKey(login)) throw new LoginJaExistenteException();

        usuarios.put(login, new Users(login, senha, nome));
    }

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

    public void adicionarAmigo(String idSessao, String amigoLogin)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiExeption, AmigoJaExistenteException, AmigoPendenteException {

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
            throw new AmigoDeSiExeption();
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

    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        if (!usuarios.containsKey(login) || !usuarios.containsKey(amigo)) {
            throw new UsuarioNaoEncontradoException();
        }
        return usuarios.get(login).ehAmigo(amigo);
    }

    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        Users outroUsuario = usuarios.get(amigo);
        if (usuario == null || outroUsuario == null) throw new UsuarioNaoEncontradoException();
        return usuario.ehAmigo(amigo) && outroUsuario.ehAmigo(login);
    }

    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", usuario.getAmigos()) + "}";
    }

    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        Users usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", usuario.getSolicitacoesPendentes()) + "}";
    }

    public void encerrarSistema() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

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

    public void enviarRecado(String idSessao, String destinatarioLogin, String recado)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption {

        // Valida destinatário
        if (destinatarioLogin == null || !usuarios.containsKey(destinatarioLogin)) {
            throw new UsuarioNaoEncontradoException();
        }

        // Valida sessão
        String remetenteLogin = sessoes.get(idSessao);
        if (remetenteLogin == null) {
            throw new SessaoInvalidaExecption();
        }

        // Auto-envio
        if (remetenteLogin.equals(destinatarioLogin)) {
            throw new IllegalArgumentException("Usuário não pode enviar recado para si mesmo.");
        }

        // Envia recado
        Users destinatario = usuarios.get(destinatarioLogin);
        destinatario.receberRecado(recado);
    }

    public String lerRecado(String idSessao) throws SessaoInvalidaExecption, SemRecadoExeption {
        // Valida sessão
        String usuarioLogin = sessoes.get(idSessao);
        if (usuarioLogin == null) {
            throw new SessaoInvalidaExecption();
        }

        // Lê recado
        Users usuario = usuarios.get(usuarioLogin);
        String recado = usuario.lerRecado();
        if (recado == null) {
            throw new SemRecadoExeption();
        }
        return recado;
    }

}
