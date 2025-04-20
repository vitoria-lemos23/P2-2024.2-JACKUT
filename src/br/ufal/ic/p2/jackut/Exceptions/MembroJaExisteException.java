package br.ufal.ic.p2.jackut.Exceptions;

public class MembroJaExisteException extends Exception {
    public MembroJaExisteException() {
        super("Membro já existe na comunidade");
    }
}
