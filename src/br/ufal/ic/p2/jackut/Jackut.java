package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exeptions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Jackut implements Serializable {
    private static final long serialVersionUID = 2L;
    private static final String ARQUIVO_DADOS = "arquivo.dat";
    private List<Users> usersList = new ArrayList<>();
    private List<Sessao> sessoesList;

    // Classe interna para substituir o Map de sessões
    private static class Sessao implements Serializable {
        private final String idSessao;
        private final String login;

        public Sessao(String idSessao, String login) {
            this.idSessao = idSessao;
            this.login = login;
        }
    }


    public Jackut() {
        this.usersList = new ArrayList<>();
        this.sessoesList = new ArrayList<>();
    }

    public void zerarSistema() {
        this.usersList = new ArrayList<>();
        this.sessoesList = new ArrayList<>();
    }

    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoEncontradoException, AtributoNaoPreenchidoException {

        Users usuario = buscarUsuario(login);

        if ("nome".equalsIgnoreCase(atributo)) {
            return usuario.getNome();
        }

        String valor = usuario.getAtributo(atributo);

        if (valor == null || valor.isEmpty()) {
            throw new AtributoNaoPreenchidoException();
        }

        return valor;
    }

    public void adicionarAmigo(String idSessao, String amigo)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiExeption, AmigoPendenteException, AmigoJaExistenteException {

        // Validações iniciais
        if (idSessao == null || idSessao.trim().isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }
        validarSessao(idSessao);

        Users usuarioAtual = getUsuarioPorSessao(idSessao);
        Users usuarioAmigo = buscarUsuario(amigo);

        // Verifica auto-amizade
        if (usuarioAtual.getLogin().equals(amigo)) {
            throw new AmigoDeSiExeption();
        }

        // Verifica se já é amigo confirmado
        if (usuarioAtual.ehAmigo(amigo)) {
            throw new AmigoJaExistenteException();
        }

        // Verifica solicitação pendente
        if (usuarioAtual.getSolicitacoesPendentes().contains(amigo)) {
            throw new AmigoPendenteException();
        }

        // Verifica se o amigo já enviou solicitação
        if (usuarioAmigo.getSolicitacoesPendentes().contains(usuarioAtual.getLogin())) {
            // Aceita a amizade mutuamente
            usuarioAtual.aceitarSolicitacaoAmizade(amigo);
            usuarioAmigo.aceitarSolicitacaoAmizade(usuarioAtual.getLogin());
        } else {
            // Adiciona solicitação pendente
            usuarioAmigo.adicionarSolicitacaoAmizade(usuarioAtual.getLogin());
        }
    }


    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoExeption, SenhaInvalidaExeption, LoginJaExistenteException {

        validarCredenciais(login, senha);
        verificarLoginExistente(login);
        usersList.add(new Users(login, senha, nome));
    }
    private void validarCredenciais(String login, String senha)
            throws LoginInvalidoExeption, SenhaInvalidaExeption {

        if (login == null || login.trim().isEmpty()) {
            throw new LoginInvalidoExeption();
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new SenhaInvalidaExeption();
        }
    }

    private void verificarLoginExistente(String login) throws LoginJaExistenteException {
        for (Users user : usersList) {
            if (user.getLogin().equals(login)) {
                throw new LoginJaExistenteException();
            }
        }
    }



    public String abrirSessao(String login, String senha) throws CredenciaisInvalidasException {
        try {
            Users usuario = validarLoginSenha(login, senha);
            String idSessao = UUID.randomUUID().toString();
            sessoesList.add(new Sessao(idSessao, login));
            return idSessao;
        } catch (UsuarioNaoEncontradoException | SenhaInvalidaExeption e) {
            throw new CredenciaisInvalidasException();
        }
    }



    private Users validarLoginSenha(String login, String senha)
            throws UsuarioNaoEncontradoException, SenhaInvalidaExeption {

        Users usuario = null;

        // Primeiro verifica se o usuário existe
        try {
            usuario = buscarUsuario(login);
        } catch (UsuarioNaoEncontradoException e) {
            throw new UsuarioNaoEncontradoException();
        }

        // Depois verifica a senha
        if (!usuario.getSenha().equals(senha)) {
            throw new SenhaInvalidaExeption();
        }

        return usuario;
    }

    public void encerrarSistema() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(this);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public static Facade iniciarSistema() {
        File arquivo = new File(ARQUIVO_DADOS);
        if (arquivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_DADOS))) {
                return (Facade) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao carregar dados: " + e.getMessage());
            }
        }
        return new Facade();
    }


    public void editarPerfil(String idSessao, String atributo, String valor)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, AtributoNaoPreenchidoException {

        // Primeiro verifica se o ID da sessão está vazio
        if (idSessao == null || idSessao.trim().isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }

        // Depois verifica se o atributo é válido
        if (atributo == null || atributo.trim().isEmpty()) {
            throw new AtributoNaoPreenchidoException();
        }

        // Valida a sessão
        validarSessao(idSessao);

        // Busca o usuário
        Users usuario = getUsuarioPorSessao(idSessao);

        usuario.setAtributo(atributo, valor);
    }


    private Users getUsuarioPorSessao(String idSessao) throws UsuarioNaoEncontradoException {
        for (Sessao s : sessoesList) {
            if (s.idSessao.equals(idSessao)) {
                return buscarUsuario(s.login);
            }
        }
        throw new UsuarioNaoEncontradoException(); // Altera a mensagem aqui
    }
    private void validarSessao(String idSessao) throws SessaoInvalidaExecption {
        for (Sessao s : sessoesList) {
            if (s.idSessao.equals(idSessao)) {
                return;
            }
        }
        throw new SessaoInvalidaExecption();
    }



    private Users buscarUsuario(String login) throws UsuarioNaoEncontradoException {
        if (login == null || login.trim().isEmpty()) {
            throw new UsuarioNaoEncontradoException();
        }

        for (Users u : usersList) {
            if (u.getLogin().equals(login)) {
                return u;
            }
        }
        throw new UsuarioNaoEncontradoException();
    }


    public boolean ehAmigo(String login, String amigo)
            throws UsuarioNaoEncontradoException {

        Users usuario1 = buscarUsuario(login);
        Users usuario2 = buscarUsuario(amigo);

        return usuario1.ehAmigo(amigo) && usuario2.ehAmigo(login);
    }

    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        List<String> amigos = buscarUsuario(login).getAmigos();
        return formatarComoConjunto(amigos);
    }

    private String formatarComoConjunto(List<String> lista) {
        // Mantém a ordem original de inserção
        return "{" + String.join(",", lista) + "}";
    }
}
