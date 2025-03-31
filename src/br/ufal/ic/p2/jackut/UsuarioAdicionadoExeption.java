package br.ufal.ic.p2.jackut;

public class UsuarioAdicionadoExeption extends Exception{
    public UsuarioAdicionadoExeption() {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }
}
