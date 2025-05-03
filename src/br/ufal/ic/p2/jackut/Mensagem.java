package br.ufal.ic.p2.jackut;

import java.io.Serializable;

/**
 * Classe que representa uma mensagem trocada no sistema Jackut.
 * <p>
 * Armazena informa��es b�sicas sobre uma mensagem, incluindo:
 * <ul>
 *   <li>Identifica��o do remetente</li>
 *   <li>Conte�do textual da mensagem</li>
 *   <li>Metadados para serializa��o</li>
 * </ul>
 *
 * <p>Implementa {@link Serializable} para permitir persist�ncia e transmiss�o.</p>
 */
public class Mensagem implements Serializable {

    /**
     * Identificador de vers�o para controle de serializa��o.
     * <p>
     * Garante compatibilidade entre vers�es diferentes da classe durante desserializa��o.
     */
    private static final long serialVersionUID = 1L;
    /**
     * Login do usu�rio que enviou a mensagem (case-sensitive).
     */
    private final String remetente;
    /**
     * Texto completo da mensagem (formato livre).
     */
    private final String conteudo;

    /**
     * Constr�i uma nova mensagem com dados completos.
     *
     * @param remetente Login do usu�rio remetente (n�o nulo)
     * @param conteudo Texto da mensagem (n�o nulo)
     *
     * @implNote N�o realiza valida��es nos par�metros
     */
    public Mensagem(String remetente, String conteudo) {
        this.remetente = remetente;
        this.conteudo = conteudo;
    }

    // Getters

    /**
     * Recupera o identificador do autor da mensagem.
     *
     * @return Login do remetente (formato original de cria��o)
     */
    public String getRemetente() { return remetente; }

    /**
     * Recupera o conte�do completo da mensagem.
     *
     * @return Texto da mensagem exatamente como foi enviado
     */
    public String getConteudo() { return conteudo; }
}