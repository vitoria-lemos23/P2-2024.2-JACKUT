package br.ufal.ic.p2.jackut.Exeptions;

public class AmigoJaExistenteException extends Exception{
    public AmigoJaExistenteException() {
        super("Usuário já está adicionado como amigo.");
    }
}
