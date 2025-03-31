package br.ufal.ic.p2.jackut.Exeptions;

public class UsuarioIgualExpection extends Exception{
    public UsuarioIgualExpection() {
        super("Conta com esse nome já existe.");
    }
}
