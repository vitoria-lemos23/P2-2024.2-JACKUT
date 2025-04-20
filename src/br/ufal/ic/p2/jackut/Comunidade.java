package br.ufal.ic.p2.jackut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Comunidade implements Serializable {
    private static final long serialVersionUID = 3L;

    private final String nome;
    private final String descricao;
    private final String dono;
    private final List<String> membros;

    public Comunidade(String nome, String descricao, String dono) {
        this.nome = nome;
        this.descricao = descricao;
        this.dono = dono;
        this.membros = new ArrayList<>();
        this.membros.add(dono); // Dono é automaticamente membro
    }

    // Getters
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getDono() { return dono; }
    public List<String> getMembros() { return new ArrayList<>(membros); }


}