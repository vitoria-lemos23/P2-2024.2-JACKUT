package br.ufal.ic.p2.jackut.Exceptions;

public class ComunidadeNaoCriadaException extends Exception{
    public ComunidadeNaoCriadaException() {
        super("Comunidade ainda não criada");
    }
}
