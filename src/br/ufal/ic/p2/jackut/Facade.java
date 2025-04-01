package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.Exeptions.*;
import java.io.*;

public class Facade implements Serializable {
    private static final long serialVersionUID = 1L;
    private Jackut jackut;

    public Facade() {
        this.jackut = Jackut.iniciarSistema();
    }

    public void zerarSistema() {
        jackut.zerarSistema();
    }

    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoEncontradoException, AtributoNaoPreenchidoException {
        return jackut.getAtributoUsuario(login, atributo);
    }

    public void adicionarAmigo(String idSessao, String amigo)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiExeption, AmigoJaExistenteException, AmigoPendenteException {
        jackut.adicionarAmigo(idSessao, amigo);
    }

    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoExeption, SenhaInvalidaExeption, LoginJaExistenteException {
        jackut.criarUsuario(login, senha, nome);
    }

    public String abrirSessao(String login, String senha) throws CredenciaisInvalidasException {
        return jackut.abrirSessao(login, senha);
    }

    public void encerrarSistema() {
        jackut.encerrarSistema();
    }

    public static Facade iniciarSistema() {
        return new Facade(); // Já inicializa o Jackut no construtor
    }

    public void editarPerfil(String idSessao, String atributo, String valor)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, AtributoNaoPreenchidoException {
        jackut.editarPerfil(idSessao, atributo, valor);
    }

    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return jackut.ehAmigo(login, amigo);
    }

    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return jackut.ehAmigoMutuo(login, amigo);
    }

    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        return jackut.getAmigos(login);
    }

    // Método adicional para obter solicitações pendentes (se necessário)
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        return jackut.getSolicitacoesPendentes(login);
    }

    public void enviarRecado(String idSessao, String destinatario, String recado)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, SemRecadoExeption {
        jackut.enviarRecado(idSessao, destinatario, recado);
    }

    public String lerRecado(String idSessao) throws SessaoInvalidaExecption, SemRecadoExeption {
        return jackut.lerRecado(idSessao);
    }
}
