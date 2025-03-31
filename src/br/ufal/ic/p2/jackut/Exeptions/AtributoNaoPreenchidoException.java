package br.ufal.ic.p2.jackut.Exeptions;

public class AtributoNaoPreenchidoException extends Exception {
    public AtributoNaoPreenchidoException() {
        super("Atributo não preenchido.");
    }
}