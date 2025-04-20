package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma operação tenta adicionar um ídolo já existente na lista de admiração do usuário.
 * <p>
 * Ocorre em operações de gestão de relacionamentos sociais onde um usuário já registrou outro como seu ídolo anteriormente.
 *
 * <p><b>Cenários comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar o mesmo ídolo múltiplas vezes</li>
 *   <li>Falha na verificação de existência prévia na lista de ídolos</li>
 *   <li>Sincronização incorreta entre diferentes componentes do sistema</li>
 * </ul>
 *
 * <p><b>Ações recomendadas:</b>
 * <ul>
 *   <li>Verificar a lista de ídolos com {@code getFas()} antes da adição</li>
 *   <li>Implementar verificação de duplicidade na lógica de adição de ídolos</li>
 *   <li>Notificar o usuário sobre a relação já existente</li>
 * </ul>
 *
 * @see br.ufal.ic.p2.jackut.Componentes.GerenciadorAmizades#adicionarIdolo(String, String)
 */
public class UsuarioJaEhIdoloException extends Exception {

    /**
     * Constrói a exceção com mensagem padrão indicando a duplicidade na lista de ídolos.
     * <p>
     * Mensagem fixa: "Usuário já está adicionado como ídolo."
     */
    public UsuarioJaEhIdoloException() {
        super("Usuário já está adicionado como ídolo.");
    }
}