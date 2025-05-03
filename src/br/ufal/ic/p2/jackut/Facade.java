/**
 * Classe Facade que fornece uma interface simplificada para o sistema Jackut.
 * Atua como um ponto �nico de acesso para todas as opera��es do sistema,
 * encapsulando a complexidade das opera��es internas.
 *
 * <p>Esta classe implementa o padr�o Facade, fornecendo m�todos simplificados
 * que delegam as opera��es para a classe Jackut.</p>
 *
 * <p>Respons�vel por gerenciar usu�rios, sess�es, amizades, perfis e recados.</p>
 *
 * @author Vit�ria Lemos
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
     * Constr�i uma nova inst�ncia do sistema Jackut e carrega dados persistentes.
     * <p>
     * Comportamento de inicializa��o:
     * <ul>
     *   <li>Verifica a exist�ncia do arquivo de dados serializados</li>
     *   <li>Recupera estado anterior se arquivo existir</li>
     *   <li>Inicializa nova inst�ncia com estruturas vazias caso contr�rio</li>
     *   <li>Configura mecanismos de serializa��o/deserializa��o</li>
     * </ul>
     */
    public Facade() {
        this.jackut = Jackut.iniciarSistema();
    }

    /**
     * Remove completamente todos os dados do sistema e reinicia estruturas.
     * <p>
     * A��es executadas:
     * <ul>
     *   <li>Limpa todos os registros de usu�rios e sess�es</li>
     *   <li>Remove comunidades e relacionamentos</li>
     *   <li>Destr�i e recria gerenciadores internos</li>
     *   <li>Exclui arquivo de persist�ncia permanentemente</li>
     * </ul>
     */
    public void zerarSistema() {
        jackut.zerarSistema();
    }

    /**
     * Recupera um atributo espec�fico do perfil de usu�rio com valida��es.
     * <p>
     * Fluxo de opera��o:
     * <ul>
     *   <li>Verifica exist�ncia do usu�rio no sistema</li>
     *   <li>Valida preenchimento do atributo solicitado</li>
     *   <li>Retorna valor formatado conforme tipo</li>
     *   <li>Trata atributos reservados (ex: 'nome')</li>
     * </ul>
     *
     * @param login Identificador �nico do usu�rio
     * @param atributo Nome do campo desejado (case-insensitive)
     * @return Valor do atributo em formato String
     * @throws UsuarioNaoEncontradoException Se usu�rio n�o cadastrado
     * @throws AtributoNaoPreenchidoException Se atributo inexistente/vazio
     */
    public String getAtributoUsuario(String login, String atributo)
            throws UsuarioNaoEncontradoException, AtributoNaoPreenchidoException {
        return jackut.getAtributoUsuario(login, atributo);
    }

    /**
     * Estabelece rela��o de amizade entre dois usu�rios.
     * <p>
     * Comportamento complexo:
     * <ul>
     *   <li>Valida sess�o ativa do solicitante</li>
     *   <li>Verifica exist�ncia do usu�rio alvo</li>
     *   <li>Cria solicita��o pendente ou aceita existente</li>
     *   <li>Notifica ambos usu�rios em caso de match</li>
     *   <li>Atualiza listas de amigos bilateralmente</li>
     * </ul>
     *
     * @param idSessao Identificador �nico da sess�o ativa
     * @param amigo Login do usu�rio a ser adicionado
     * @throws SessaoInvalidaExecption Se sess�o inexistente/expirada
     * @throws UsuarioNaoEncontradoException Se amigo n�o cadastrado
     * @throws AmigoDeSiException Se tentativa de auto-amizade
     */
    public void adicionarAmigo(String idSessao, String amigo)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption,
            AmigoDeSiException, AmigoJaExistenteException, AmigoPendenteException, InimigoException {
        jackut.adicionarAmigo(idSessao, amigo);
    }

    /**
     * Cria novo usu�rio no sistema com valida��o de credenciais.
     * <p>
     * Requisitos de cria��o:
     * <ul>
     *   <li>Login �nico e n�o vazio</li>
     *   <li>Senha com pelo menos 1 caractere</li>
     *   <li>Nome n�o nulo para exibi��o p�blica</li>
     *   <li>Verifica��o de duplicidade de login</li>
     * </ul>
     *
     * @param login Identificador �nico para acesso
     * @param senha Credencial de autentica��o
     * @param nome Nome completo para exibi��o
     * @throws LoginInvalidoException Se login inv�lido
     * @throws SenhaInvalidaException Se senha n�o atender crit�rios
     * @throws LoginJaExistenteException Se login j� cadastrado
     */
    public void criarUsuario(String login, String senha, String nome)
            throws LoginInvalidoException, SenhaInvalidaException, LoginJaExistenteException {
        jackut.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica usu�rio e inicia nova sess�o exclusiva.
     * <p>
     * Seguran�a:
     * <ul>
     *   <li>Valida combina��o login/senha</li>
     *   <li>Encerra sess�es anteriores do usu�rio</li>
     *   <li>Gera novo UUID como identificador de sess�o</li>
     *   <li>Armazena dados de sess�o criptografados</li>
     * </ul>
     *
     * @param login Credencial de identifica��o
     * @param senha Credencial de autentica��o
     * @return UUID �nico para sess�o criada
     * @throws CredenciaisInvalidasException Se falha na autentica��o
     */
    public String abrirSessao(String login, String senha) throws CredenciaisInvalidasException {
        return jackut.abrirSessao(login, senha);
    }


    /**
     * Persiste estado atual do sistema em arquivo.
     * <p>
     * Processo de serializa��o:
     * <ul>
     *   <li>Converte estado interno em fluxo de bytes</li>
     *   <li>Armazena em arquivo com formato espec�fico</li>
     *   <li>Trata poss�veis erros de I/O</li>
     *   <li>Mant�m compatibilidade entre vers�es</li>
     * </ul>
     */
    public void encerrarSistema() {
        jackut.encerrarSistema();
    }


    public static Facade iniciarSistema() {
        return new Facade();
    }

    /**
     * Modifica atributo do perfil do usu�rio autenticado.
     * <p>
     * Valida��es:
     * <ul>
     *   <li>Exist�ncia da sess�o informada</li>
     *   <li>Permiss�o para edi��o do atributo</li>
     *   <li>Formata��o adequada do valor</li>
     *   <li>Restri��es para atributos reservados</li>
     * </ul>
     *
     * @param idSessao Identificador de sess�o v�lida
     * @param atributo Campo a ser modificado
     * @param valor Novo conte�do do campo
     * @throws SessaoInvalidaExecption Se sess�o inv�lida
     * @throws AtributoNaoPreenchidoException Se nome do atributo inv�lido
     */
    public void editarPerfil(String idSessao, String atributo, String valor)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, AtributoNaoPreenchidoException {
        jackut.editarPerfil(idSessao, atributo, valor);
    }

    /**
     * Verifica rela��o de amizade unilateral entre usu�rios.
     * <p>
     * L�gica de verifica��o:
     * <ul>
     *   <li>Consulta lista de amigos do usu�rio origem</li>
     *   <li>Compara com login do usu�rio destino</li>
     *   <li>N�o considera reciprocidade</li>
     *   <li>Case-sensitive para logins</li>
     * </ul>
     *
     * @param login Usu�rio base para verifica��o
     * @param amigo Usu�rio alvo da verifica��o
     * @return true se rela��o existir, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum usu�rio inexistente
     */
    public boolean ehAmigo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return jackut.ehAmigo(login, amigo);
    }

    /**
     * Verifica se dois usu�rios s�o amigos m�tuos (ambos adicionaram um ao outro).
     *
     * @param login Login do primeiro usu�rio (n�o pode ser nulo ou vazio)
     * @param amigo Login do segundo usu�rio (n�o pode ser nulo ou vazio)
     * @return true se forem amigos m�tuos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o for encontrado
     */
    public boolean ehAmigoMutuo(String login, String amigo) throws UsuarioNaoEncontradoException {
        return jackut.ehAmigoMutuo(login, amigo);
    }

    /**
     * Recupera lista formatada de amigos de um usu�rio.
     * <p>
     * Formato de retorno:
     * <ul>
     *   <li>Delimitadores: chaves {} envolvendo a lista</li>
     *   <li>Separadores: v�rgulas entre logins</li>
     *   <li>Ordena��o: alfab�tica de A-Z</li>
     *   <li>Case-sensitive: mant�m capitaliza��o original</li>
     * </ul>
     *
     * @param login Usu�rio alvo da consulta
     * @return String formatada com lista de amigos
     * @throws UsuarioNaoEncontradoException Se usu�rio inexistente
     */
    public String getAmigos(String login) throws UsuarioNaoEncontradoException {
        return jackut.getAmigos(login);
    }

    /**
     * Obt�m as solicita��es de amizade pendentes de um usu�rio no formato {solicitante1,solicitante2,...}.
     *
     * @param login Login do usu�rio (n�o pode ser nulo ou vazio)
     * @return String formatada com as solicita��es pendentes entre chaves
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o for encontrado
     */
    public String getSolicitacoesPendentes(String login) throws UsuarioNaoEncontradoException {
        return jackut.getSolicitacoesPendentes(login);
    }

    /**
     * Envia mensagem privada entre usu�rios com restri��es.
     * <p>
     * Controles aplicados:
     * <ul>
     *   <li>Valida��o de sess�o do remetente</li>
     *   <li>Verifica��o de bloqueios/inimizades</li>
     *   <li>Armazenamento FIFO no destinat�rio</li>
     *   <li>Limite m�ximo de mensagens armazenadas</li>
     * </ul>
     *
     * @param idSessao Identificador de sess�o v�lida
     * @param destinatario Login do usu�rio alvo
     * @param recado Conte�do textual da mensagem
     * @throws InimigoException Se existir rela��o de inimizade
     * @throws SemRecadoException Se conte�do da mensagem for inv�lido
     */
    public void enviarRecado(String idSessao, String destinatario, String recado)
            throws UsuarioNaoEncontradoException, SessaoInvalidaExecption, SemRecadoException, InimigoException {
        jackut.enviarRecado(idSessao, destinatario, recado);
    }


    /**
     * Remove e retorna a mensagem mais antiga n�o lida.
     * <p>
     * Comportamento FIFO:
     * <ul>
     *   <li>Acessa fila de mensagens do usu�rio</li>
     *   <li>Remove mensagem mais antiga</li>
     *   <li>Mant�m hist�rico de mensagens lidas</li>
     *   <li>Limpa mensagens ap�s per�odo de expira��o</li>
     * </ul>
     *
     * @param idSessao Identificador de sess�o v�lida
     * @return Conte�do da mensagem mais antiga
     * @throws SemRecadoException Se nenhuma mensagem dispon�vel
     */
    public String lerRecado(String idSessao) throws SessaoInvalidaExecption, SemRecadoException, UsuarioNaoEncontradoException {
        return jackut.lerRecado(idSessao);
    }


    /**
     * Cria nova comunidade com usu�rio autenticado como dono.
     * <p>
     * Requisitos de cria��o:
     * <ul>
     *   <li>Nome �nico n�o registrado anteriormente</li>
     *   <li>Descri��o n�o vazia</li>
     *   <li>Sess�o v�lida do usu�rio criador</li>
     *   <li>Adi��o autom�tica do criador como membro</li>
     * </ul>
     *
     * @param idSessao Identificador de sess�o v�lida
     * @param nome Nome �nico da comunidade
     * @param descricao Texto descritivo
     * @throws ComunidadeJaExisteException Se nome j� registrado
     */
    public void criarComunidade(String idSessao, String nome, String descricao)
            throws SessaoInvalidaExecption, ComunidadeJaExisteException, UsuarioNaoEncontradoException {
        // Valida a sess�o primeiro
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
     * @param idSessao ID da sess�o do remetente (n�o pode ser nulo ou vazio)
     * @param comunidade Nome da comunidade (n�o pode ser nulo ou vazio)
     * @param mensagem Texto da mensagem (n�o pode ser nulo ou vazio)
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida
     * @throws ComunidadeNaoExisteException Se a comunidade n�o existir
     * @throws UsuarioNaoEncontradoException Se o usu�rio da sess�o n�o for encontrado
     */
    public void enviarMensagem(String idSessao, String comunidade, String mensagem)
            throws SessaoInvalidaExecption, ComunidadeNaoExisteException, UsuarioNaoEncontradoException {
        // Valida a sess�o e obt�m o remetente
        String remetente = jackut.getLoginPorSessao(idSessao);

        // Obt�m os membros da comunidade
        List<String> membros = jackut.getMembrosComunidade(comunidade);

        // Envia a mensagem para todos os membros
        for (String membro : membros) {
            Users usuario = jackut.getUsuarios().get(membro);
            usuario.receberMensagemComunidade(mensagem);
        }
    }

    /**
     * Recupera e remove a pr�xima mensagem da comunidade destinada ao usu�rio autenticado (sistema FIFO).
     * <p>
     * Fluxo de opera��o:
     * <ul>
     *   <li>Valida a sess�o do usu�rio atrav�s do ID fornecido</li>
     *   <li>Verifica exist�ncia do usu�rio no sistema</li>
     *   <li>Acessa a fila de mensagens da comunidade</li>
     *   <li>Remove e retorna a mensagem mais antiga</li>
     * </ul>
     *
     * @param idSessao ID da sess�o v�lida do usu�rio
     * @return Conte�do completo da mensagem no formato "Comunidade: texto"
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>ID de sess�o inv�lido/expirado</li>
     *   <li>Usu�rio associado n�o existir</li>
     * </ul>
     * @throws SemMensagemException Se n�o houver mensagens dispon�veis
     *
     * @implNote Comportamento cr�tico:
     * <ul>
     *   <li>Mensagem � permanentemente removida ap�s leitura</li>
     *   <li>Ordem estrita de chegada (primeira mensagem enviada � a primeira lida)</li>
     *   <li>Mensagens s�o armazenadas mesmo ap�s logout</li>
     * </ul>
     *
     * @exampleFormato Exemplo de retorno:
     * {@code "Geral: Reuni�o marcada para sexta-feira"}
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
     * Estabelece uma rela��o de admira��o (f�-�dolo) entre o usu�rio autenticado e outro usu�rio.
     * <p>
     * Valida��es e comportamentos:
     * <ul>
     *   <li>Verifica validade da sess�o do usu�rio admirador</li>
     *   <li>Confirma exist�ncia do usu�rio �dolo</li>
     *   <li>Impede auto-idolatria (usu�rio n�o pode ser f� de si mesmo)</li>
     *   <li>Verifica se a rela��o j� existe</li>
     *   <li>Bloqueia opera��o se existir inimizade m�tua</li>
     * </ul>
     *
     * @param idSessao ID da sess�o v�lida do usu�rio que est� adicionando o �dolo
     * @param idolo Login do usu�rio sendo admirado/�dolo
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>Sess�o inv�lida ou expirada</li>
     *   <li>ID de sess�o malformado</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se o �dolo n�o existir no sistema
     * @throws UsuarioJaEhIdoloException Se j� existir rela��o de admira��o
     * @throws NaoPodeSerFaDeSiException Se tentar admirar a si mesmo
     * @throws InimigoException Se houver rela��o de inimizade bilateral
     *
     * @implNote Comportamentos especiais:
     * <ul>
     *   <li>Cria rela��o m�tua f�/�dolo automaticamente se ambos se adicionarem</li>
     *   <li>Envia mensagem autom�tica do sistema para ambos em caso de match</li>
     *   <li>Atualiza listas de f�s e �dolos em tempo real</li>
     *   <li>Case-sensitive para logins</li>
     * </ul>
     *
     * @see #getFas(String) Para consultar f�s de um usu�rio
     * @see #ehFa(String, String) Para verificar rela��o de admira��o
     * @see #adicionarInimigo(String, String) Opera��o conflitante
     *
     * @exampleSample Exemplo de uso v�lido:
     * {@code adicionarIdolo("sessao123", "artista_famoso")}
     */
    public void adicionarIdolo(String idSessao, String idolo)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException,
            UsuarioJaEhIdoloException, AmigoDeSiException, NaoPodeSerFaDeSiException, InimigoException {

        jackut.adicionarIdolo(idSessao, idolo); // Passa o idSessao, n�o o login
    }


    /**
     * Estabelece uma rela��o de inimizade unilateral entre o usu�rio autenticado e outro usu�rio.
     * <p>
     * Comportamento e valida��es:
     * <ul>
     *   <li>Verifica validade da sess�o do usu�rio solicitante</li>
     *   <li>Confirma exist�ncia do usu�rio alvo da inimizade</li>
     *   <li>Impede auto-inimizade (usu�rio n�o pode ser inimigo de si mesmo)</li>
     *   <li>Verifica se a rela��o de inimizade j� existe</li>
     *   <li>Bloqueia opera��o se houver rela��es conflitantes (ex: amigos/�dolos)</li>
     * </ul>
     *
     * @param idSessao ID da sess�o v�lida do usu�rio que est� declarando inimizade
     * @param inimigo Login do usu�rio alvo da inimizade
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>Sess�o n�o existir</li>
     *   <li>Sess�o expirada</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se o usu�rio alvo n�o existir
     * @throws UsuarioJaEhInimigoException Se a rela��o de inimizade j� existir
     * @throws InimigoDeSiException Se tentar declarar inimizade contra si mesmo
     *
     * @implNote Efeitos colaterais:
     * <ul>
     *   <li>Atualiza lista de inimigos de ambos usu�rios</li>
     *   <li>Bloqueia envio de mensagens entre os usu�rios</li>
     *   <li>Remove rela��es de amizade/paquera existentes</li>
     *   <li>Impede participa��o em mesmas comunidades</li>
     * </ul>
     *
     * @see #removerInimigo(String, String) Para reverter esta opera��o
     * @see #getInimigos(String) Para consultar inimigos
     * @see InimigoException Para detalhes sobre bloqueios relacionados
     */
    public void adicionarInimigo(String idSessao, String inimigo)
            throws SessaoInvalidaExecption, UsuarioNaoEncontradoException,
            UsuarioJaEhInimigoException, AmigoDeSiException, InimigoDeSiException {
        jackut.adicionarInimigo(idSessao, inimigo); // Passa o idSessao
    }


    /**
     * Verifica se um usu�rio � f� de outro usu�rio espec�fico (rela��o de idolatria).
     * <p>
     * Comportamento da verifica��o:
     * <ul>
     *   <li>Confirma a exist�ncia de ambos os usu�rios no sistema</li>
     *   <li>Verifica se o usu�rio alvo (f�) est� na lista de f�s do �dolo</li>
     *   <li>N�o verifica a rela��o inversa (se o �dolo tamb�m � f� do usu�rio)</li>
     * </ul>
     *
     * @param usuario Login do usu�rio potencial f� (case-sensitive)
     * @param idolo Login do usu�rio �dolo (case-sensitive)
     * @return true se o usu�rio for f� do �dolo, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se:
     * <ul>
     *   <li>O usu�rio f� n�o existir</li>
     *   <li>O usu�rio �dolo n�o estiver cadastrado</li>
     * </ul>
     *
     * @implNote Caracter�sticas t�cnicas:
     * <ul>
     *   <li>Verifica��o case-sensitive para ambos os logins</li>
     *   <li>Consulta direta na lista de f�s do �dolo</li>
     *   <li>N�o modifica nenhum dado do sistema</li>
     * </ul>
     *
     * @exampleSample Exemplo de uso:
     * Se "maria" est� na lista de f�s de "joao":
     * {@code ehFa("maria", "joao")} retorna true
     *
     * @see #getFas(String) Para obter a lista completa de f�s
     * @see #adicionarIdolo(String, String) Para criar novas rela��es de idolatria
     */
    public boolean ehFa(String usuario, String idolo) throws UsuarioNaoEncontradoException {
        Users user = jackut.getUsuarios().get(usuario);
        Users idol = jackut.getUsuarios().get(idolo);
        if (user == null || idol == null) throw new UsuarioNaoEncontradoException();
        return idol.getFas().contains(usuario);
    }

    /**
     * Recupera a lista de f�s de um usu�rio em formato estruturado.
     * <p>
     * Formato de retorno:
     * <ul>
     *   <li>Delimitado por chaves "{}"</li>
     *   <li>Logins separados por v�rgulas</li>
     *   <li>Ordena��o alfab�tica A-Z</li>
     *   <li>Mant�m capitaliza��o original dos logins</li>
     * </ul>
     *
     * @param usuario Login do usu�rio alvo (case-sensitive)
     * @return String formatada com a lista de f�s
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o estiver cadastrado no sistema
     *
     * @implNote Comportamento especial:
     * <ul>
     *   <li>Retorna "{}" se o usu�rio n�o tiver f�s</li>
     *   <li>Lista atualizada em tempo real conforme novos f�s s�o adicionados</li>
     *   <li>N�o inclui rela��es de inimizade ou paqueras</li>
     * </ul>
     *
     * @see #adicionarIdolo(String, String) Para entender como os f�s s�o registrados
     * @see #ehFa(String, String) Para verificar se um usu�rio espec�fico � f�
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
     * Verifica se um usu�rio espec�fico est� na lista de paqueras do usu�rio autenticado.
     * <p>
     * Comportamento da verifica��o:
     * <ul>
     *   <li>Valida a sess�o do usu�rio que realiza a consulta</li>
     *   <li>Confirma a exist�ncia do usu�rio alvo (paquera)</li>
     *   <li>Verifica presen�a unilateral na lista de paqueras</li>
     * </ul>
     *
     * @param idSessao ID da sess�o v�lida do usu�rio consultante
     * @param paquera Login do usu�rio alvo da verifica��o
     * @return true se o usu�rio alvo estiver na lista de paqueras, false caso contr�rio
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>ID de sess�o for inv�lido</li>
     *   <li>Sess�o estiver expirada</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se:
     * <ul>
     *   <li>Usu�rio da sess�o n�o existir</li>
     *   <li>Usu�rio alvo (paquera) n�o estiver cadastrado</li>
     * </ul>
     *
     * @implNote Detalhes t�cnicos:
     * <ul>
     *   <li>Verifica��o case-sensitive para logins</li>
     *   <li>N�o verifica reciprocidade (apenas rela��o unilateral)</li>
     *   <li>Consulta direta na lista de paqueras do usu�rio</li>
     * </ul>
     *
     * @exampleSample Exemplo:
     * Se usu�rioA tem usu�rioB em sua lista de paqueras:
     * {@code ehPaquera("sessaoA", "usuarioB")} retorna true
     *
     * @see #adicionarPaquera(String, String) Para criar novas rela��es de paquera
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
     * Recupera a lista de paqueras do usu�rio autenticado em formato estruturado.
     * <p>
     * Formato de retorno:
     * <ul>
     *   <li>Delimitado por chaves "{}"</li>
     *   <li>Logins separados por v�rgulas</li>
     *   <li>Ordena��o alfab�tica A-Z</li>
     *   <li>Case-sensitive (mant�m capitaliza��o original)</li>
     * </ul>
     *
     * @param idSessao ID da sess�o v�lida do usu�rio
     * @return String formatada com a lista de paqueras
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>ID de sess�o for nulo/vazio</li>
     *   <li>Sess�o n�o estiver ativa</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se o usu�rio associado � sess�o n�o existir
     *
     * @implNote Caracter�sticas:
     * <ul>
     *   <li>Retorna lista vazia "{}" se n�o houver paqueras</li>
     *   <li>Exibe apenas paqueras ativas (n�o mostra rejeitadas)</li>
     *   <li>Atualiza��o em tempo real conforme novas paqueras s�o adicionadas</li>
     * </ul>
     *
     * @see #adicionarPaquera(String, String) Para adicionar novas paqueras
     * @see #ehPaquera(String, String) Para verificar paquera espec�fica
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
     * Estabelece uma rela��o de paquera entre o usu�rio autenticado e outro usu�rio.
     * <p>
     * Comportamento principal:
     * <ul>
     *   <li>Valida a sess�o do usu�rio solicitante</li>
     *   <li>Verifica exist�ncia do usu�rio alvo</li>
     *   <li>Cria rela��o unilateral de paquera</li>
     *   <li>Notifica ambos usu�rios em caso de paquera m�tua</li>
     * </ul>
     *
     * @param idSessao ID da sess�o v�lida do usu�rio que est� paquerando
     * @param paquera Login do usu�rio sendo paquerado
     * @throws SessaoInvalidaExecption Se a sess�o for inv�lida ou expirada
     * @throws UsuarioNaoEncontradoException Se o usu�rio alvo n�o existir
     * @throws UsuarioJaEhPaqueraException Se j� existir rela��o de paquera
     * @throws PaqueraDeSiException Se tentar paquerar a si mesmo
     * @throws InimigoException Se existir rela��o de inimizade entre os usu�rios
     *
     * @implNote Comportamento especial:
     * <ul>
     *   <li>Gera mensagem autom�tica do sistema para ambos usu�rios em caso de match m�tuo</li>
     *   <li>Verifica rela��es de inimizade antes de criar a paquera</li>
     *   <li>Armazena hist�rico de paqueras para sugest�es futuras</li>
     * </ul>
     *
     * @see #getPaqueras(String) Para consultar paqueras existentes
     * @see #ehPaquera(String, String) Para verificar rela��o de paquera
     * @see InimigoException Para detalhes sobre rela��es de inimizade
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
     * Recupera o login do usu�rio associado a uma sess�o ativa ap�s valida��es rigorosas.
     * <p>
     * Valida��es realizadas:
     * <ul>
     *   <li>Verifica formato b�sico do ID de sess�o (n�o nulo/n�o vazio)</li>
     *   <li>Consulta o registro de sess�es ativas</li>
     *   <li>Confirma exist�ncia do usu�rio na base de dados</li>
     * </ul>
     *
     * @param idSessao ID �nico da sess�o (formato UUID)
     * @return Login do usu�rio associado � sess�o (case-sensitive)
     * @throws SessaoInvalidaExecption Se:
     * <ul>
     *   <li>ID da sess�o for nulo</li>
     *   <li>ID da sess�o estiver vazio</li>
     *   <li>Sess�o n�o estiver registrada</li>
     * </ul>
     * @throws UsuarioNaoEncontradoException Se o usu�rio associado � sess�o n�o existir no sistema
     *
     * @implNote Fluxo de valida��o:
     * <ol>
     *   <li>Valida��o sint�tica do ID da sess�o</li>
     *   <li>Consulta ao mapa de sess�es ativas</li>
     *   <li>Verifica��o de exist�ncia do usu�rio</li>
     * </ol>
     *
     * @see #abrirSessao(String, String) Para entender como as sess�es s�o criadas
     * @see Jackut#getSessoes() Para acessar o mapa completo de sess�es
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
     * Remove permanentemente um usu�rio do sistema com base na sess�o ativa.
     * <p>
     * Opera��es realizadas durante a remo��o:
     * <ul>
     *   <li>Exclui todos os dados do perfil do usu�rio</li>
     *   <li>Encerra todas as sess�es ativas do usu�rio</li>
     *   <li>Remove o usu�rio de todas as comunidades</li>
     *   <li>Apaga relacionamentos sociais (amizades, �dolos, paqueras)</li>
     *   <li>Limpa mensagens e recados associados</li>
     * </ul>
     *
     * @param idSessao ID da sess�o v�lida do usu�rio a ser removido
     * @throws SessaoInvalidaExecption Se o ID de sess�o for inv�lido ou expirado
     * @throws UsuarioNaoEncontradoException Se o usu�rio associado � sess�o n�o existir
     *
     * @implNote Comportamento adicional:
     * <ul>
     *   <li>Invalida imediatamente a sess�o utilizada na opera��o</li>
     *   <li>Remove refer�ncias ao usu�rio em todos os componentes do sistema</li>
     *   <li>Atualiza automaticamente as estruturas de dados relacionadas</li>
     * </ul>
     *
     * @see #criarUsuario(String, String, String) M�todo complementar para cria��o de usu�rios
     * @see #getAtributoUsuario(String, String) Para recuperar dados antes da remo��o
     */
    public void removerUsuario(String idSessao) throws SessaoInvalidaExecption, UsuarioNaoEncontradoException {
        String login = jackut.getLoginPorSessao(idSessao);
        jackut.removerUsuario(login);
    }
}