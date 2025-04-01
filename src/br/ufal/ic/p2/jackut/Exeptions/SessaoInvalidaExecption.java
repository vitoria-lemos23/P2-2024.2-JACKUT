package br.ufal.ic.p2.jackut.Exeptions;

public class SessaoInvalidaExecption extends Exception{
    public SessaoInvalidaExecption() {
        super("Sessão inválida.");
    }

    public static class UsuarioAdicionadoExeption extends Exception{
        public UsuarioAdicionadoExeption() {
            super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
        }
    }
}
