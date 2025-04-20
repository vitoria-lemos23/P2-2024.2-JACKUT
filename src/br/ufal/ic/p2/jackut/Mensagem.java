package br.ufal.ic.p2.jackut;

import java.io.Serializable;

/**
 * Classe que representa uma mensagem trocada no sistema Jackut.
 * <p>
 * Armazena informações básicas sobre uma mensagem, incluindo:
 * <ul>
 *   <li>Identificação do remetente</li>
 *   <li>Conteúdo textual da mensagem</li>
 *   <li>Metadados para serialização</li>
 * </ul>
 *
 * <p>Implementa {@link Serializable} para permitir persistência e transmissão.</p>
 */
public class Mensagem implements Serializable {

    /**
     * Identificador de versão para controle de serialização.
     * <p>
     * Garante compatibilidade entre versões diferentes da classe durante desserialização.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Login do usuário que enviou a mensagem (case-sensitive).
     */
    private final String remetente;
    /**
     * Texto completo da mensagem (formato livre).
     */
    private final String conteudo;

    /**
     * Constrói uma nova mensagem com dados completos.
     *
     * @param remetente Login do usuário remetente (não nulo)
     * @param conteudo Texto da mensagem (não nulo)
     *
     * @implNote Não realiza validações nos parâmetros
     */
    public Mensagem(String remetente, String conteudo) {
        this.remetente = remetente;
        this.conteudo = conteudo;
    }

    // Getters

    /**
     * Recupera o identificador do autor da mensagem.
     *
     * @return Login do remetente (formato original de criação)
     */
    public String getRemetente() { return remetente; }

    /**
     * Recupera o conteúdo completo da mensagem.
     *
     * @return Texto da mensagem exatamente como foi enviado
     */
    public String getConteudo() { return conteudo; }
}