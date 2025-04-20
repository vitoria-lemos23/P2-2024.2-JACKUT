package br.ufal.ic.p2.jackut.Exceptions;

public class ComunidadeJaExisteException extends Exception{
    public ComunidadeJaExisteException() {
        super("Comunidade com esse nome já existe.");
    }
}
