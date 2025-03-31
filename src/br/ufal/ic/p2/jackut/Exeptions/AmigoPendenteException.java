package br.ufal.ic.p2.jackut.Exeptions;

public class AmigoPendenteException extends Exception {
    public AmigoPendenteException() {
        super("Usuário já está adicionado como amigo, esperando aceitação do convite.");
    }
}