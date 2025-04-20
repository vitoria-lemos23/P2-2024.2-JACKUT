package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma operação tenta adicionar um usuário já existente na lista de inimigos.
 * <p>
 * Ocorre em operações de gestão de relações sociais onde o usuário alvo já foi previamente registrado como inimigo.
 *
 * <p><b>Cenários comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar o mesmo inimigo múltiplas vezes</li>
 *   <li>Falha na verificação de existência prévia na lista de inimigos</li>
 *   <li>Sincronização inadequada entre componentes do sistema</li>
 * </ul>
 *
 * <p><b>Ações recomendadas:</b>
 * <ul>
 *   <li>Verificar a lista de inimigos antes da operação usando {@code getInimigos()}</li>
 *   <li>Implementar checagem de duplicidade na lógica de adição de inimigos</li>
 *   <li>Notificar o usuário sobre a relação hostil já existente</li>
 * </ul>
 *
 * @see br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades#adicionarInimigo(String, String)
 */
public class UsuarioJaEhInimigoException extends Exception {

    /**
     * Constrói a exceção com mensagem padrão indicando a duplicidade na lista de inimigos.
     * <p>
     * Mensagem fixa: "Usuário já está adicionado como inimigo."
     */
    public UsuarioJaEhInimigoException() {
        super("Usuário já está adicionado como inimigo.");
    }
}