package br.ufal.ic.p2.jackut.Exceptions;

public class RecadoParaSiException  extends Exception
{
    public RecadoParaSiException(){super("Usuário não pode enviar recado para si mesmo.");}
}
