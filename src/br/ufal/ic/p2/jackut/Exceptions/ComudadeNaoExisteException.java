package br.ufal.ic.p2.jackut.Exceptions;

public class ComudadeNaoExisteException extends Exception{
    public ComudadeNaoExisteException() {
        super("Comunidade não existe.");
    }
}
