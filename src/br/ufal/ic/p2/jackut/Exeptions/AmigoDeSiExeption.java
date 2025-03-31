package br.ufal.ic.p2.jackut.Exeptions;

public class AmigoDeSiExeption extends Exception {
    public AmigoDeSiExeption() {
        super("Usuário não pode adicionar a si mesmo como amigo.");
    }
}
