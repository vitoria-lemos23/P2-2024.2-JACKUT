/**
 * Classe Facade que fornece uma interface simplificada para o sistema Jackut.
 * Atua como um ponto único de acesso para todas as operações do sistema,
 * encapsulando a complexidade das operações internas.
 *
 * <p>Esta classe implementa o padrão Facade, fornecendo métodos simplificados
 * que delegam as operações para a classe Jackut.</p>
 *
 * <p>Responsável por gerenciar usuários, sessões, amizades, perfis e recados.</p>
 *
 * @author Vitória Lemos
 */
package br.ufal.ic.p2.jackut;


import br.ufal.ic.p2.jackut.Exceptions.*;
import java.io.*;
import java.util.List;
import br.ufal.ic.p2.jackut.Exceptions.*;


public class Facade implements Serializable {
    private static final long serialVersionUID = 1L;
    private Jackut jackut;

    /**
     * Constrói uma nova instância do sistema Jackut e carrega dados persistentes.
     * <p>
     * Comportamento de inicialização:
     * <ul>
     *   <li>Verifica a existência do arquivo de dados serializados</li>
     *   <li>Recupera estado anterior se arquivo existir</li>
     *   <li>Inicializa nova instância com estruturas vazias caso contrário</li>
     *   <li>Configura mecanismos de serialização/deserialização</li>
     * </ul>
     */
    public Facade() {
        this.jackut = Jackut.iniciarSistema();
    }

    /**
     * Remove completamente todos os dados do sistema e reinicia estruturas.
     * <p>
     * Ações executadas:
     * <ul>
     *   <li>Limpa todos os registros de usuários e sessões</li>
     *   <li>Remove comunidades e relacionamentos</li>
     *   <li>Destrói e recria gerenciadores internos</li>
     *   <li>Exclui arquivo de persistência permanentemente</li>
     * </ul>
     */
    public void zerarSistema() {
        jackut.zerarSistema();
    }

    /**
     * Recupera um atributo específico do perfil de usuário com validações.
     * <p>
     * Fluxo de operação:
     * <ul>
     *   <li>Verifica existência do usuário no sistema</li>
     *   <li>Valida preenchimento do atributo solicitado</li>
     *   <li>Retorna valor formatado conforme tipo</li>
     *   <li>Trata atributos reservados (ex: 'nome')</li>
     * </ul>
     *
     * @param login Identificador único do usuário
     * @param atributo Nome do campo desejado (case-insensitive)
     * @return Valor do atributo em formato String
     * @throws UsuarioNaoEncontradoException Se usuário não cadastrado
     * @throws AtributoNaoPreenchidoException Se atributo inexistente/vazio
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoEncontradoException, AtributoNaoPreenchidoException {
        return jackut.getAtributoUsuario(login, atributo);
    }

    /**
     * Estabelece relação de amizade entre dois usuários.
     * <p>
     * Comportamento complexo:
     * <ul>
     *   <li>Valida sessão ativa do solicitante</li>
     *   <li>Verifica existência do usuário alvo</li>
     *   <li>Cria solicitação pendente ou aceita existente</li>
     *   <li>Notifica ambos usuários em caso de match</li>
     *   <li>Atualiza listas de amigos bilateralmente</li>
     * </ul>
     *
     * @param idSessao Identificador único da sessão ativa
     * @param amigo Login do usuário a ser adicionado
     * @throws SessaoInvalidaExecption Se sessão inexistente/expirada
     * @throws UsuarioNaoEncontradoException Se amigo não cadastrado
     * @throws AmigoDeSiException Se tentativa de auto-amizade
     */
    public void adicionarAmigo(String idSessao, String amigo)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException, InimigoException {
        jackut.adicionarAmigo(idSessao, amigo);
    }

    /**
     * Cria novo usuário no sistema com validação de credenciais.
     * <p>
     * Requisitos de criação:
     * <ul>
     *   <li>Login único e não vazio</li>
     *   <li>Senha com pelo menos 1 caractere</li>
     *   <li>Nome não nulo para exibição pública</li>
     *   <li>Verificação de duplicidade de login</li>
     * </ul>
     *
     * @param login Identificador único para acesso
     * @param senha Credencial de autenticação
     * @param nome Nome completo para exibição
     * @throws LoginInvalidoException Se login inválido
     * @throws SenhaInvalidaException Se senha não atender critérios
     * @throws LoginJaExistenteException Se login já cadastrado
     */
    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoException, SenhaInvalidaException, LoginJaExistenteException {
        jackut.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica usuário e inicia nova sessão exclusiva.
     * <p>
     * Segurança:
     * <ul>
     *   <li>Valida combinação login/senha</li>
     *   <li>Encerra sessões anteriores do usuário</li>
     *   <li>Gera novo UUID como identificador de sessão</li>
     *   <li>Armazena dados de sessão criptografados</li>
     * </ul>
     *
     * @param login Credencial de identificação
     * @param senha Credencial de autenticação
     * @return UUID único para sessão criada
     * @throws CredenciaisInvalidasException Se falha na autenticação
     */
    public String abrirSessao(String login, String senha) throws CredenciaisInvalidasException {
        return jackut.abrirSessao(login, senha);
    }


    /**
     * Persiste estado atual do sistema em arquivo.
     * <p>
     * Processo de serialização:
     * <ul>
     *   <li>Converte estado interno em fluxo de bytes</li>
     *   <li>Armazena em arquivo com formato específico</li>
     *   <li>Trata possíveis erros de I/O</li>
     *   <li>Mantém compatibilidade entre versões</li>
     * </ul>
     */
    public void encerrarSistema() {
        jackut.encerrarSistema();
    }


    public static Facade iniciarSistema() {
        return new Facade();
    }

    /**
     * Modifica atributo do perfil do usuário autenticado.
     * <p>
     * Validações:
     * <ul>
     *   <li>Existência da sessão informada</li>
     *   <li>Permissão para edição do atributo</li>
     *   <li>Formatação adequada do valor</li>
     *   <li>Restrições para atributos reservados</li>
     * </ul>
     *
     * @param idSessao Identificador de sessão válida
     * @param atributo Campo a ser modificado
     * @param valor Novo conteúdo do campo
     * @throws SessaoInvalidaExecption Se sessão inválida
     * @throws AtributoNaoPreenchidoException Se nome do atributo inválido
     */
    public void editarPerfil(String idSessao, String atributo, String valor)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, AtributoNaoPreenchidoException {
        jackut.editarPerfil(idSessao, atributo, valor);
    }

    /**
     * Verifica relação de amizade unilateral entre usuários.
     * <p>
     * Lógica de verificação:
     * <ul>
     *   <li>Consulta lista de amigos do usuário origem</li>
     *   <li>Compara com login do usuário destino</li>
     *   <li>Não considera reciprocidade</li>
     *   <li>Case-sensitive para logins</li>
     * </ul>
     *
     * @param login Usuário base para verificação
     * @param amigo Usuário alvo da verificação
     * @return true se relação existir, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum usuário inexistente
     */
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return jackut.ehAmigo(login, amigo);
    }

    /**
     * Verifica se dois usuários são amigos mútuos (ambos adicionaram um ao outro).
     *
     * @param login Login do primeiro usuário (não pode ser nulo ou vazio)
     * @param amigo Login do segundo usuário (não pode ser nulo ou vazio)
     * @return true se forem amigos mútuos, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum usuário não for encontrado
     */
    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return jackut.ehAmigoMutuo(login, amigo);
    }

    /**
     * Recupera lista formatada de amigos de um usuário.
     * <p>
     * Formato de retorno:
     * <ul>
     *   <li>Delimitadores: chaves {} envolvendo a lista</li>
     *   <li>Separadores: vírgulas entre logins</li>
     *   <li>Ordenação: alfabética de A-Z</li>
     *   <li>Case-sensitive: mantém capitalização original</li>
     * </ul>
     *
     * @param login Usuário alvo da consulta
     * @return String formatada com lista de amigos
     * @throws UsuarioNaoEncontradoException Se usuário inexistente
     */
    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        return jackut.getAmigos(login);
    }

    /**
     * Obtém as solicitações de amizade pendentes de um usuário no formato {solicitante1,solicitante2,...}.
     *
     * @param login Login do usuário (não pode ser nulo ou vazio)
     * @return String formatada com as solicitações pendentes entre chaves
     * @throws UsuarioNaoEncontradoException Se o usuário não for encontrado
     */
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        return jackut.getSolicitacoesPendentes(login);
    }

    /**
     * Envia mensagem privada entre usuários com restrições.
     * <p>
     * Controles aplicados:
     * <ul>
     *   <li>Validação de sessão do remetente</li>
     *   <li>Verificação de bloqueios/inimizades</li>
     *   <li>Armazenamento FIFO no destinatário</li>
     *   <li>Limite máximo de mensagens armazenadas</li>
     * </ul>
     *
     * @param idSessao Identificador de sessão válida
     * @param destinatario Login do usuário alvo
     * @param recado Conteúdo textual da mensagem
     * @throws InimigoException Se existir relação de inimizade
     * @throws SemRecadoException Se conteúdo da mensagem for inválido
     */
    public void enviarRecado(String idSessao, String destinatario, String recado)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, SemRecadoException, InimigoException {
        jackut.enviarRecado(idSessao, destinatario, recado);
    }


    /**
     * Remove e retorna a mensagem mais antiga não lida.
     * <p>
     * Comportamento FIFO:
     * <ul>
     *   <li>Acessa fila de mensagens do usuário</li>
     *   <li>Remove mensagem mais antiga</li>
     *   <li>Mantém histórico de mensagens lidas</li>
     *   <li>Limpa mensagens após período de expiração</li>
     * </ul>
     *
     * @param idSessao Identificador de sessão válida
     * @return Conteúdo da mensagem mais antiga
     * @throws SemRecadoException Se nenhuma mensagem disponível
     */
    public String lerRecado(String idSessao) throws SessaoInvalidaExecption, SemRecadoException, UsuarioNaoEncontradoException {
        return jackut.lerRecado(idSessao);
    }


    /**
     * Cria nova comunidade com usuário autenticado como dono.
     * <p>
     * Requisitos de criação:
     * <ul>
     *   <li>Nome único não registrado anteriormente</li>
     *   <li>Descrição não vazia</li>
     *   <li>Sessão válida do usuário criador</li>
     *   <li>Adição automática do criador como membro</li>
     * </ul>
     *
     * @param idSessao Identificador de sessão válida
     * @param nome Nome único da comunidade
     * @param descricao Texto descritivo
     * @throws ComunidadeJaExisteException Se nome já registrado
     */
    public void criarComunidade(String idSessao, String nome, String descricao)
            throws SessaoInvalidaExecption, ComunidadeJaExisteException, UsuarioNaoEncontradoException {
        // Valida a sessão primeiro
        String login = jackut.getLoginPorSessao(idSessao);
        jackut.registrarComunidade(nome, descricao, login);
    }

    public String getDonoComunidade(String nome) throws ComunidadeNaoExisteException {
        return jackut.getDonoComunidade(nome);
    }

    public String getMembrosComunidade(String nome) throws ComunidadeNaoExisteException {
        List<String> membros = jackut.getMembrosComunidade(nome);
        return "{" + String.join(",", membros) + "}";
    }

    public String getDescricaoComunidade(String nome) throws ComunidadeNaoExisteException {
        return jackut.getDescricaoComunidade(nome);
    }


    // Modify the adicionarComunidade method to handle exceptions
    public void adicionarComunidade(String idSessao, String nomeComunidade)
            throws SessaoInvalidaExecption, ComunidadeNaoExisteException, MembroJaExisteException, UsuarioNaoEncontradoException {
        String login = jackut.getLoginPorSessao(idSessao);
        jackut.adicionarMembroComunidade(nomeComunidade, login);
    }

    public String getComunidades(String login) throws UsuarioNaoEncontradoException {
        if (!jackut.getUsuarios().containsKey(login)) {
            throw new UsuarioNaoEncontradoException();
        }
        List<String> comunidades = jackut.getComunidadesDoUsuario(login);
        return "{" + String.join(",", comunidades) + "}";
    }


    /**
     * Envia uma mensagem para todos os membros de uma comunidade.
     *
     * @param idSessao ID da sessão do remetente (não pode ser nulo ou vazio)
     * @param comunidade Nome da comunidade (não pode ser nulo ou vazio)
     * @param mensagem Texto da mensagem (não pode ser nulo ou vazio)
     * @throws SessaoInvalidaExecption Se a sessão for inválida
     * @throws ComunidadeNaoExisteException Se a comunidade não existir
     * @throws UsuarioNaoEncontradoException Se o usuário da sessão não for encontrado
     */
    public void enviarMensagem(String idSessao, String comunidade, String mensagem)
            throws SessaoInvalidaExecption, ComunidadeNaoExisteException, UsuarioNaoEncontradoException {
        // Valida a sessão e obtém o remetente
        String remetente = jackut.getLoginPorSessao(idSessao);

        // Obtém os membros da comunidade
        List<String> membros = jackut.getMembrosComunidade(comunidade);

        // Envia a mensagem para todos os membros
        for (String membro : membros) {
            Users usuario = jackut.getUsuarios().get(membro);
            usuario.receberMensagemComunidade(mensagem);
        }
    }

    /**
     * Recupera e remove a próxima mensagem da comunidade destinada ao usuário autenticado (sistema FIFO).
     * <p>
     * Fluxo de operação:
     * <ul>
     *   <li>Valida a sessão do usuário através do ID fornecido</li>
     *   <li>Verifica existência do usuário no sistema</li>
     *   <li>Acessa a fila de mensagens da comunidade</li>
     *   <li>Remove e retorna a mensagem mais antiga</li>
     * </ul>
     *
     * @param idSessao ID da sessão válida do usuário
     * @return Conteúdo completo da mensagem no formato "Comunidade: texto"
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>ID de sessão inválido/expirado</li>
     *   <li>Usuário associado não existir</li>
     * </ul>
     * @throws SemMensagemException Se não houver mensagens disponíveis
     *
     * @implNote Comportamento crítico:
     * <ul>
     *   <li>Mensagem é permanentemente removida após leitura</li>
     *   <li>Ordem estrita de chegada (primeira mensagem enviada é a primeira lida)</li>
     *   <li>Mensagens são armazenadas mesmo após logout</li>
     * </ul>
     *
     * @exampleFormato Exemplo de retorno:
     * {@code "Geral: Reunião marcada para sexta-feira"}
     *
     * @see #enviarMensagem(String, String, String) Para origem das mensagens
     * @see #lerRecado(String) Para ler mensagens privadas
     */
    public String lerMensagem(String idSessao) throws SessaoInvalidaExecption, SemMensagemException {
        try {
            String login = jackut.getLoginPorSessao(idSessao);
            Users usuario = jackut.getUsuarios().get(login);
            if (usuario == null) {
                throw new SessaoInvalidaExecption();
            }
            return usuario.lerMensagemComunidade();
        } catch (SessaoInvalidaExecption | UsuarioNaoEncontradoException e) {
            throw new SessaoInvalidaExecption();
        }
    }


    /**
     * Estabelece uma relação de admiração (fã-ídolo) entre o usuário autenticado e outro usuário.
     * <p>
     * Validações e comportamentos:
     * <ul>
     *   <li>Verifica validade da sessão do usuário admirador</li>
     *   <li>Confirma existência do usuário ídolo</li>
     *   <li>Impede auto-idolatria (usuário não pode ser fã de si mesmo)</li>
     *   <li>Verifica se a relação já existe</li>
     *   <li>Bloqueia operação se existir inimizade mútua</li>
     * </ul>
     *
     * @param idSessao ID da sessão válida do usuário que está adicionando o ídolo
     * @param idolo Login do usuário sendo admirado/ídolo
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>Sessão inválida ou expirada</li>
     *   <li>ID de sessão malformado</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se o ídolo não existir no sistema
     * @throws UsuarioJaEhIdoloException Se já existir relação de admiração
     * @throws NaoPodeSerFaDeSiException Se tentar admirar a si mesmo
     * @throws InimigoException Se houver relação de inimizade bilateral
     *
     * @implNote Comportamentos especiais:
     * <ul>
     *   <li>Cria relação mútua fã/ídolo automaticamente se ambos se adicionarem</li>
     *   <li>Envia mensagem automática do sistema para ambos em caso de match</li>
     *   <li>Atualiza listas de fãs e ídolos em tempo real</li>
     *   <li>Case-sensitive para logins</li>
     * </ul>
     *
     * @see #getFas(String) Para consultar fãs de um usuário
     * @see #ehFa(String, String) Para verificar relação de admiração
     * @see #adicionarInimigo(String, String) Operação conflitante
     *
     * @exampleSample Exemplo de uso válido:
     * {@code adicionarIdolo("sessao123", "artista_famoso")}
     */
    public void adicionarIdolo(String idSessao, String idolo)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException,
            UsuarioJaEhIdoloException, AmigoDeSiException, NaoPodeSerFaDeSiException, InimigoException {

        jackut.adicionarIdolo(idSessao, idolo); // Passa o idSessao, não o login
    }


    /**
     * Estabelece uma relação de inimizade unilateral entre o usuário autenticado e outro usuário.
     * <p>
     * Comportamento e validações:
     * <ul>
     *   <li>Verifica validade da sessão do usuário solicitante</li>
     *   <li>Confirma existência do usuário alvo da inimizade</li>
     *   <li>Impede auto-inimizade (usuário não pode ser inimigo de si mesmo)</li>
     *   <li>Verifica se a relação de inimizade já existe</li>
     *   <li>Bloqueia operação se houver relações conflitantes (ex: amigos/ídolos)</li>
     * </ul>
     *
     * @param idSessao ID da sessão válida do usuário que está declarando inimizade
     * @param inimigo Login do usuário alvo da inimizade
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>Sessão não existir</li>
     *   <li>Sessão expirada</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se o usuário alvo não existir
     * @throws UsuarioJaEhInimigoException Se a relação de inimizade já existir
     * @throws InimigoDeSiException Se tentar declarar inimizade contra si mesmo
     *
     * @implNote Efeitos colaterais:
     * <ul>
     *   <li>Atualiza lista de inimigos de ambos usuários</li>
     *   <li>Bloqueia envio de mensagens entre os usuários</li>
     *   <li>Remove relações de amizade/paquera existentes</li>
     *   <li>Impede participação em mesmas comunidades</li>
     * </ul>
     *
     * @see #removerInimigo(String, String) Para reverter esta operação
     * @see #getInimigos(String) Para consultar inimigos
     * @see InimigoException Para detalhes sobre bloqueios relacionados
     */
    public void adicionarInimigo(String idSessao, String inimigo)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException,
            UsuarioJaEhInimigoException, AmigoDeSiException, InimigoDeSiException {
        jackut.adicionarInimigo(idSessao, inimigo); // Passa o idSessao
    }


    /**
     * Verifica se um usuário é fã de outro usuário específico (relação de idolatria).
     * <p>
     * Comportamento da verificação:
     * <ul>
     *   <li>Confirma a existência de ambos os usuários no sistema</li>
     *   <li>Verifica se o usuário alvo (fã) está na lista de fãs do ídolo</li>
     *   <li>Não verifica a relação inversa (se o ídolo também é fã do usuário)</li>
     * </ul>
     *
     * @param usuario Login do usuário potencial fã (case-sensitive)
     * @param idolo Login do usuário ídolo (case-sensitive)
     * @return true se o usuário for fã do ídolo, false caso contrário
     * @throws UsuarioNaoEncontradoException Se:
     * <ul>
     *   <li>O usuário fã não existir</li>
     *   <li>O usuário ídolo não estiver cadastrado</li>
     * </ul>
     *
     * @implNote Características técnicas:
     * <ul>
     *   <li>Verificação case-sensitive para ambos os logins</li>
     *   <li>Consulta direta na lista de fãs do ídolo</li>
     *   <li>Não modifica nenhum dado do sistema</li>
     * </ul>
     *
     * @exampleSample Exemplo de uso:
     * Se "maria" está na lista de fãs de "joao":
     * {@code ehFa("maria", "joao")} retorna true
     *
     * @see #getFas(String) Para obter a lista completa de fãs
     * @see #adicionarIdolo(String, String) Para criar novas relações de idolatria
     */
    public boolean ehFa(String usuario, String idolo) throws UsuarioNaoEncontradoException {
        Users user = jackut.getUsuarios().get(usuario);
        Users idol = jackut.getUsuarios().get(idolo);
        if (user == null || idol == null) throw new UsuarioNaoEncontradoException();
        return idol.getFas().contains(usuario);
    }

    /**
     * Recupera a lista de fãs de um usuário em formato estruturado.
     * <p>
     * Formato de retorno:
     * <ul>
     *   <li>Delimitado por chaves "{}"</li>
     *   <li>Logins separados por vírgulas</li>
     *   <li>Ordenação alfabética A-Z</li>
     *   <li>Mantém capitalização original dos logins</li>
     * </ul>
     *
     * @param usuario Login do usuário alvo (case-sensitive)
     * @return String formatada com a lista de fãs
     * @throws UsuarioNaoEncontradoException Se o usuário não estiver cadastrado no sistema
     *
     * @implNote Comportamento especial:
     * <ul>
     *   <li>Retorna "{}" se o usuário não tiver fãs</li>
     *   <li>Lista atualizada em tempo real conforme novos fãs são adicionados</li>
     *   <li>Não inclui relações de inimizade ou paqueras</li>
     * </ul>
     *
     * @see #adicionarIdolo(String, String) Para entender como os fãs são registrados
     * @see #ehFa(String, String) Para verificar se um usuário específico é fã
     *
     * @exampleFormato Exemplo de retorno:
     * {@code "{maria_2023,joao_silva,ana_123}"}
     */
    public String getFas(String usuario) throws UsuarioNaoEncontradoException {
        Users user = jackut.getUsuarios().get(usuario);
        if (user == null) throw new UsuarioNaoEncontradoException();
        return "{" + String.join(",", user.getFas()) + "}";
    }

    /**
     * Verifica se um usuário específico está na lista de paqueras do usuário autenticado.
     * <p>
     * Comportamento da verificação:
     * <ul>
     *   <li>Valida a sessão do usuário que realiza a consulta</li>
     *   <li>Confirma a existência do usuário alvo (paquera)</li>
     *   <li>Verifica presença unilateral na lista de paqueras</li>
     * </ul>
     *
     * @param idSessao ID da sessão válida do usuário consultante
     * @param paquera Login do usuário alvo da verificação
     * @return true se o usuário alvo estiver na lista de paqueras, false caso contrário
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>ID de sessão for inválido</li>
     *   <li>Sessão estiver expirada</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se:
     * <ul>
     *   <li>Usuário da sessão não existir</li>
     *   <li>Usuário alvo (paquera) não estiver cadastrado</li>
     * </ul>
     *
     * @implNote Detalhes técnicos:
     * <ul>
     *   <li>Verificação case-sensitive para logins</li>
     *   <li>Não verifica reciprocidade (apenas relação unilateral)</li>
     *   <li>Consulta direta na lista de paqueras do usuário</li>
     * </ul>
     *
     * @exampleSample Exemplo:
     * Se usuárioA tem usuárioB em sua lista de paqueras:
     * {@code ehPaquera("sessaoA", "usuarioB")} retorna true
     *
     * @see #adicionarPaquera(String, String) Para criar novas relações de paquera
     * @see #getPaqueras(String) Para obter a lista completa de paqueras
     */
    public boolean ehPaquera(String idSessao, String paquera)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException {
        String usuario = jackut.getLoginPorSessao(idSessao);
        Users user = jackut.getUsuarios().get(usuario);
        Users pq = jackut.getUsuarios().get(paquera);
        if (pq == null) throw new UsuarioNaoEncontradoException();
        return user.getPaqueras().contains(paquera);
    }

    /**
     * Recupera a lista de paqueras do usuário autenticado em formato estruturado.
     * <p>
     * Formato de retorno:
     * <ul>
     *   <li>Delimitado por chaves "{}"</li>
     *   <li>Logins separados por vírgulas</li>
     *   <li>Ordenação alfabética A-Z</li>
     *   <li>Case-sensitive (mantém capitalização original)</li>
     * </ul>
     *
     * @param idSessao ID da sessão válida do usuário
     * @return String formatada com a lista de paqueras
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>ID de sessão for nulo/vazio</li>
     *   <li>Sessão não estiver ativa</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se o usuário associado à sessão não existir
     *
     * @implNote Características:
     * <ul>
     *   <li>Retorna lista vazia "{}" se não houver paqueras</li>
     *   <li>Exibe apenas paqueras ativas (não mostra rejeitadas)</li>
     *   <li>Atualização em tempo real conforme novas paqueras são adicionadas</li>
     * </ul>
     *
     * @see #adicionarPaquera(String, String) Para adicionar novas paqueras
     * @see #ehPaquera(String, String) Para verificar paquera específica
     *
     * @exampleFormato Exemplo de retorno:
     * {@code "{joao_123,maria_silva,ana_99}"}
     */
    public String getPaqueras(String idSessao)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException {
        String usuario = jackut.getLoginPorSessao(idSessao);
        Users user = jackut.getUsuarios().get(usuario);
        return "{" + String.join(",", user.getPaqueras()) + "}";
    }



    /**
     * Estabelece uma relação de paquera entre o usuário autenticado e outro usuário.
     * <p>
     * Comportamento principal:
     * <ul>
     *   <li>Valida a sessão do usuário solicitante</li>
     *   <li>Verifica existência do usuário alvo</li>
     *   <li>Cria relação unilateral de paquera</li>
     *   <li>Notifica ambos usuários em caso de paquera mútua</li>
     * </ul>
     *
     * @param idSessao ID da sessão válida do usuário que está paquerando
     * @param paquera Login do usuário sendo paquerado
     * @throws SessaoInvalidaExecption Se a sessão for inválida ou expirada
     * @throws UsuarioNaoEncontradoException Se o usuário alvo não existir
     * @throws UsuarioJaEhPaqueraException Se já existir relação de paquera
     * @throws PaqueraDeSiException Se tentar paquerar a si mesmo
     * @throws InimigoException Se existir relação de inimizade entre os usuários
     *
     * @implNote Comportamento especial:
     * <ul>
     *   <li>Gera mensagem automática do sistema para ambos usuários em caso de match mútuo</li>
     *   <li>Verifica relações de inimizade antes de criar a paquera</li>
     *   <li>Armazena histórico de paqueras para sugestões futuras</li>
     * </ul>
     *
     * @see #getPaqueras(String) Para consultar paqueras existentes
     * @see #ehPaquera(String, String) Para verificar relação de paquera
     * @see InimigoException Para detalhes sobre relações de inimizade
     *
     * @exampleFormato Exemplo de uso:
     * {@code facade.adicionarPaquera("sessao123", "maria_silva");}
     */
    public void adicionarPaquera(String idSessao, String paquera)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException,
            UsuarioJaEhPaqueraException, AmigoDeSiException, PaqueraDeSiException, InimigoException {
        jackut.adicionarPaquera(idSessao, paquera); // Passa o idSessao
    }


    /**
     * Recupera o login do usuário associado a uma sessão ativa após validações rigorosas.
     * <p>
     * Validações realizadas:
     * <ul>
     *   <li>Verifica formato básico do ID de sessão (não nulo/não vazio)</li>
     *   <li>Consulta o registro de sessões ativas</li>
     *   <li>Confirma existência do usuário na base de dados</li>
     * </ul>
     *
     * @param idSessao ID único da sessão (formato UUID)
     * @return Login do usuário associado à sessão (case-sensitive)
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>ID da sessão for nulo</li>
     *   <li>ID da sessão estiver vazio</li>
     *   <li>Sessão não estiver registrada</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se o usuário associado à sessão não existir no sistema
     *
     * @implNote Fluxo de validação:
     * <ol>
     *   <li>Validação sintática do ID da sessão</li>
     *   <li>Consulta ao mapa de sessões ativas</li>
     *   <li>Verificação de existência do usuário</li>
     * </ol>
     *
     * @see #abrirSessao(String, String) Para entender como as sessões são criadas
     * @see Jackut#getSessoes() Para acessar o mapa completo de sessões
     */
    public String getLoginPorSessao(String idSessao)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException {
        if (idSessao == null || idSessao.isEmpty())
            throw new SessaoInvalidaExecption();
        String login = jackut.getSessoes().get(idSessao);
        if (login == null || !jackut.getUsuarios().containsKey(login))
            throw new UsuarioNaoEncontradoException();
        return login;
    }

    /**
     * Remove permanentemente um usuário do sistema com base na sessão ativa.
     * <p>
     * Operações realizadas durante a remoção:
     * <ul>
     *   <li>Exclui todos os dados do perfil do usuário</li>
     *   <li>Encerra todas as sessões ativas do usuário</li>
     *   <li>Remove o usuário de todas as comunidades</li>
     *   <li>Apaga relacionamentos sociais (amizades, ídolos, paqueras)</li>
     *   <li>Limpa mensagens e recados associados</li>
     * </ul>
     *
     * @param idSessao ID da sessão válida do usuário a ser removido
     * @throws SessaoInvalidaExecption Se o ID de sessão for inválido ou expirado
     * @throws UsuarioNaoEncontradoException Se o usuário associado à sessão não existir
     *
     * @implNote Comportamento adicional:
     * <ul>
     *   <li>Invalida imediatamente a sessão utilizada na operação</li>
     *   <li>Remove referências ao usuário em todos os componentes do sistema</li>
     *   <li>Atualiza automaticamente as estruturas de dados relacionadas</li>
     * </ul>
     *
     * @see #criarUsuario(String, String, String) Método complementar para criação de usuários
     * @see #getAtributoUsuario(String, String) Para recuperar dados antes da remoção
     */
    public void removerUsuario(String idSessao) throws SessaoInvalidaExecption, UsuarioNaoEncontradoException {
        String login = jackut.getLoginPorSessao(idSessao);
        jackut.removerUsuario(login);
    }
}