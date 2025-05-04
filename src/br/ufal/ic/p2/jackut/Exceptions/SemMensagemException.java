package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma operação tenta acessar mensagens inexistentes.
 * <p>
 * Ocorre em operações de leitura de mensagens quando não há conteúdo disponível para recuperação.
 *
 * <p><b>Cenários comuns:</b>
 * <ul>
 *   <li>Tentativa de ler mensagens de uma comunidade vazia</li>
 *   <li>Acesso a caixa de entrada sem recados</li>
 *   <li>Leitura de histórico de mensagens não persistido</li>
 * </ul>
 *
 * <p><b>Ações recomendadas:</b>
 * <ul>
 *   <li>Verificar a existência de mensagens antes da leitura</li>
 *   <li>Implementar tratamento para casos de listas vazias</li>
 *   <li>Informar ao usuário sobre a ausência de conteúdo</li>
 * </ul>
 *
 */
public class SemMensagemException extends Exception
{

    /**
     * Constrói a exceção com mensagem padrão indicando a ausência de mensagens.
     * <p>
     * Mensagem fixa: "Não há mensagens."
     */
    public SemMensagemException() {
        super("Não há mensagens.");
    }
}