package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar mensagens inexistentes.
 * <p>
 * Ocorre em opera��es de leitura de mensagens quando n�o h� conte�do dispon�vel para recupera��o.
 *
 * <p><b>Cen�rios comuns:</b>
 * <ul>
 *   <li>Tentativa de ler mensagens de uma comunidade vazia</li>
 *   <li>Acesso a caixa de entrada sem recados</li>
 *   <li>Leitura de hist�rico de mensagens n�o persistido</li>
 * </ul>
 *
 * <p><b>A��es recomendadas:</b>
 * <ul>
 *   <li>Verificar a exist�ncia de mensagens antes da leitura</li>
 *   <li>Implementar tratamento para casos de listas vazias</li>
 *   <li>Informar ao usu�rio sobre a aus�ncia de conte�do</li>
 * </ul>
 *
 */
public class SemMensagemException extends Exception
{

    /**
     * Constr�i a exce��o com mensagem padr�o indicando a aus�ncia de mensagens.
     * <p>
     * Mensagem fixa: "N�o h� mensagens."
     */
    public SemMensagemException() {
        super("N�o h� mensagens.");
    }
}