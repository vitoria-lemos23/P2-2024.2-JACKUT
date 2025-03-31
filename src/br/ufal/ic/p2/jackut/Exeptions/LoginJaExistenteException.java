package br.ufal.ic.p2.jackut.Exeptions;

public class LoginJaExistenteException extends Exception{
    public LoginJaExistenteException() {
        super("Conta com esse nome já existe.");
    }

}