package br.ufal.ic.p2.jackut.Exceptions;

/**
 * Exceção lançada quando uma operação tenta adicionar um usuário já existente na lista de paqueras.
 * <p>
 * Ocorre em operações de gestão de relações sociais onde o usuário alvo já foi previamente registrado como paquera.
 *
 * <p><b>Cenários comuns:</b>
 * <ul>
 *   <li>Tentativa de adicionar a mesma paquera múltiplas vezes</li>
 *   <li>Falha na verificação de existência prévia na lista de paqueras</li>
 *   <li>Sincronização inadequada entre componentes do sistema</li>
 * </ul>
 *
 * <p><b>Ações recomendadas:</b>
 * <ul>
 *   <li>Verificar a lista de paqueras antes da operação usando {@code getPaqueras()}</li>
 *   <li>Implementar checagem de duplicidade na lógica de adição de paqueras</li>
 *   <li>Notificar o usuário sobre a relação já existente</li>
 * </ul>
 *
 * @see br.ufal.ic.p2.jackut.Interfaces.IGerenciadorAmizades#adicionarPaquera(String, String)
 * @see UsuarioJaEhIdoloException Exceção similar para ídolos duplicados
 * @see UsuarioJaEhInimigoException Exceção similar para inimigos duplicados
 */
public class UsuarioJaEhPaqueraException extends Exception {

    /**
     * Constrói a exceção com mensagem padrão indicando a duplicidade na lista de paqueras.
     * <p>
     * Mensagem fixa: "Usuário já está adicionado como paquera."
     */
    public UsuarioJaEhPaqueraException() {
        super("Usuário já está adicionado como paquera.");
    }
}