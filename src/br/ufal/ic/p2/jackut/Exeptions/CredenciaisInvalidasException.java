package br.ufal.ic.p2.jackut.Exeptions;

public class CredenciaisInvalidasException extends Exception{
    public CredenciaisInvalidasException() {
        super("Login ou senha inválidos.");
    }
}
