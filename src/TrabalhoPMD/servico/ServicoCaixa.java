package TrabalhoPMD.servico;

import TrabalhoPMD.dados.BancoDados;
import TrabalhoPMD.dados.EntidadeDAO;
import TrabalhoPMD.dados.PersistenceException;

import TrabalhoPMD.modelo.Caixa;
import TrabalhoPMD.modelo.Colecao;
import TrabalhoPMD.modelo.Entidade;
import TrabalhoPMD.modelo.Skin;
import TrabalhoPMD.modelo.Usuario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.HashMap;

public class ServicoCaixa {

    private final BancoDados banco;
    private final Random aleatorio;
    private static final Path ARQUIVO_VALORANT
            = Path.of("dados", "skins_valorant.json");

    private static final Path ARQUIVO_CS2
            = Path.of("dados", "skins_cs2.json");

    private static final Path ARQUIVO_LOL
            = Path.of("dados", "skins_lol.json");

    private static final Path ARQUIVO_TXT
            = Path.of("dados_skins.txt");

    private static final Path ARQUIVO_TXT_DADOS
            = Path.of("dados", "dados_skins.txt");

    private static final Path ARQUIVO_FORTNITE
            = Path.of("dados", "skins_fortnite.json");

    // Guarda as categorias do Valorant para evitar
    // consultar a API repetidamente.
    private Map<String, String> categoriasValorant;
    private List<Skin> catalogoCache;

    public ServicoCaixa() {

        banco = BancoDados.getInstancia();
        aleatorio = new Random();
    }

    // Retorna as caixas disponíveis e suas probabilidades.
    public List<Caixa> listarCaixas() {

        List<Caixa> caixas = new ArrayList<>();

        caixas.add(new Caixa(
                "Caixa Premium",
                "Valorant",
                criarProbabilidades(
                        new String[]{"Premium", "Exclusive", "Ultra"},
                        new double[]{70, 25, 5}
                )
        ));

        caixas.add(new Caixa(
                "Caixa de Armas",
                "CS2",
                criarProbabilidades(
                        new String[]{"Mil-Spec", "Restricted", "Classified", "Covert"},
                        new double[]{65, 22, 10, 3}
                )
        ));

        caixas.add(new Caixa(
                "Caixa de Skins",
                "Fortnite",
                criarProbabilidades(
                        new String[]{
                            "Rare", "Epic", "Legendary",
                            "Icon Series", "Gaming Legends", "Marvel", "DC"
                        },
                        new double[]{55, 28, 12, 1.25, 1.25, 1.25, 1.25}
                )
        ));

        caixas.add(new Caixa(
                "Caixa de Runeterra",
                "League of Legends",
                criarProbabilidades(
                        new String[]{"Epic", "Legendary", "Ultimate"},
                        new double[]{70, 27, 3}
                )
        ));

        caixas.add(new Caixa(
                "Caixa de Heróis",
                "Overwatch 2",
                criarProbabilidades(
                        new String[]{"Epic", "Legendary", "Mythic"},
                        new double[]{75, 23, 2}
                )
        ));

        return caixas;
    }

    // Monta o mapa de raridades e respectivas chances.
    private Map<String, Double> criarProbabilidades(
            String[] raridades,
            double[] chances) {

        if (raridades.length != chances.length) {

            throw new IllegalArgumentException(
                    "A quantidade de raridades e chances deve ser igual.");
        }

        Map<String, Double> probabilidades = new LinkedHashMap<>();

        for (int i = 0; i < raridades.length; i++) {
            probabilidades.put(raridades[i], chances[i]);
        }

        return probabilidades;
    }

    // Retorna as skins disponíveis para determinada caixa.
    public List<Skin> listarSkins(Caixa caixa) {

        if (caixa == null) {
            throw new IllegalArgumentException("Caixa não informada.");
        }

        List<Skin> catalogo = carregarCatalogoSkins();

        List<Skin> skinsDisponiveis = new ArrayList<>();

        for (Skin skin : catalogo) {

            if (skin.getJogo() == null
                    || !skin.getJogo().equalsIgnoreCase(caixa.getJogo())) {
                continue;
            }

            if (skin.getRaridade() != null
                    && caixa.getProbabilidades().containsKey(
                            skin.getRaridade())) {

                skinsDisponiveis.add(skin);
            }
        }

        return skinsDisponiveis;
    }

    // =====================================================
    // CARREGAR CATÁLOGO DE SKINS
    // =====================================================
    private List<Skin> carregarCatalogoSkins() {

        List<Skin> catalogo = new ArrayList<>();

        if (catalogoCache != null) {
            return catalogoCache;
        }

        Set<String> skinsAdicionadas = new HashSet<>();
        Set<Integer> idsUtilizados = new HashSet<>();

        // 1. Banco de dados
        EntidadeDAO<Skin> daoSkin = banco.getDAO(Skin.class);

        for (Skin skin : carregarOuVazio(daoSkin)) {

            adicionarSkinExistente(
                    catalogo,
                    skinsAdicionadas,
                    idsUtilizados,
                    skin
            );
        }

        // 2. Arquivos TXT
        carregarArquivoTxt(
                catalogo,
                skinsAdicionadas,
                idsUtilizados
        );

        // 3. Valorant
        carregarArquivoJson(
                ARQUIVO_VALORANT,
                "Valorant",
                catalogo,
                skinsAdicionadas,
                idsUtilizados
        );

        // 4. CS2
        carregarArquivoJson(
                ARQUIVO_CS2,
                "CS2",
                catalogo,
                skinsAdicionadas,
                idsUtilizados
        );

        // 5. League of Legends
        carregarArquivoJson(
                ARQUIVO_LOL,
                "League of Legends",
                catalogo,
                skinsAdicionadas,
                idsUtilizados
        );

        // 6. Fortnite
        carregarArquivoJson(
                ARQUIVO_FORTNITE,
                "Fortnite",
                catalogo,
                skinsAdicionadas,
                idsUtilizados
        );

        catalogoCache = catalogo;

        return catalogoCache;
    }

    // =====================================================
    // ADICIONAR SKINS EXISTENTES
    // =====================================================
    private void adicionarSkinExistente(
            List<Skin> catalogo,
            Set<String> skinsAdicionadas,
            Set<Integer> idsUtilizados,
            Skin original) {

        if (original == null
                || original.getNome() == null
                || original.getJogo() == null) {
            return;
        }

        String chave = criarChaveSkin(
                original.getJogo(),
                original.getNome()
        );

        if (!skinsAdicionadas.add(chave)) {
            return;
        }

        int id = original.getId();

        if (id <= 0 || !idsUtilizados.add(id)) {
            id = gerarIdDisponivel(idsUtilizados);
        }

        Skin skin = new Skin(
                id,
                original.getNome(),
                original.getJogo(),
                normalizarRaridade(
                        original.getJogo(),
                        original.getRaridade()
                ),
                original.getCadastradaPor(),
                original.getImagemUrl()
        );

        catalogo.add(skin);
    }

    // =====================================================
    // LER TXT SERIALIZADO
    // =====================================================
    private void carregarArquivoTxt(
            List<Skin> catalogo,
            Set<String> skinsAdicionadas,
            Set<Integer> idsUtilizados) {

        Path[] arquivos = {
            ARQUIVO_TXT,
            ARQUIVO_TXT_DADOS
        };

        for (Path arquivo : arquivos) {

            if (!Files.exists(arquivo)) {
                continue;
            }

            try (ObjectInputStream entrada
                    = new ObjectInputStream(
                            Files.newInputStream(arquivo))) {

                Object objeto = entrada.readObject();

                if (objeto instanceof Collection<?>) {

                    Collection<?> colecao = (Collection<?>) objeto;

                    for (Object item : colecao) {

                        if (item instanceof Skin) {

                            adicionarSkinExistente(
                                    catalogo,
                                    skinsAdicionadas,
                                    idsUtilizados,
                                    (Skin) item
                            );
                        }
                    }

                } else if (objeto instanceof Skin) {

                    adicionarSkinExistente(
                            catalogo,
                            skinsAdicionadas,
                            idsUtilizados,
                            (Skin) objeto
                    );
                }

                System.out.println(
                        "TXT carregado: " + arquivo
                );

            } catch (IOException | ClassNotFoundException e) {

                System.err.println(
                        "Erro ao carregar TXT: " + e.getMessage()
                );
            }
        }
    }

    // =====================================================
    // LER JSON
    // =====================================================
    private void carregarArquivoJson(
            Path arquivo,
            String jogo,
            List<Skin> catalogo,
            Set<String> skinsAdicionadas,
            Set<Integer> idsUtilizados) {

        if (!Files.exists(arquivo)) {

            System.out.println(
                    "Arquivo não encontrado: " + arquivo
            );

            return;
        }

        try {

            String conteudo = Files.readString(
                    arquivo,
                    StandardCharsets.UTF_8
            );

            if (conteudo.isBlank()) {
                return;
            }

            JsonArray skinsJson = JsonParser
                    .parseString(conteudo)
                    .getAsJsonArray();

            int quantidade = 0;

            for (JsonElement elemento : skinsJson) {

                if (!elemento.isJsonObject()) {
                    continue;
                }

                JsonObject objeto = elemento.getAsJsonObject();

                String nome = "";
                String imagem = "";
                String raridade = "";

                // VALORANT
                if (jogo.equals("Valorant")) {

                    nome = obterTexto(objeto, "displayName");

                    imagem = obterTexto(objeto, "displayIcon");

                    raridade = obterRaridadeValorant(objeto);
                } // CS2
                else if (jogo.equals("CS2")) {

                    nome = obterTexto(objeto, "name");

                    imagem = obterTexto(objeto, "image");

                    raridade = obterRaridadeCS2(objeto);
                } // LEAGUE OF LEGENDS
                else if (jogo.equals("League of Legends")) {

                    nome = obterTexto(objeto, "displayName");

                    if (nome.isEmpty()) {
                        nome = obterTexto(objeto, "displayNameEn");
                    }

                    imagem = obterTexto(objeto, "displayIcon");

                    raridade = obterTexto(objeto, "raridade");
                } // FORTNITE
                else if (jogo.equals("Fortnite")) {

                    nome = obterTexto(objeto, "displayName");

                    imagem = obterTexto(objeto, "displayIcon");

                    raridade = obterTexto(objeto, "raridade");
                }

                if (nome.isBlank()) {
                    continue;
                }

                String chave = criarChaveSkin(jogo, nome);

                if (!skinsAdicionadas.add(chave)) {
                    continue;
                }

                int idOriginal = obterIdJson(objeto);

                int id;

                if (idOriginal > 0
                        && idsUtilizados.add(idOriginal)) {

                    id = idOriginal;

                } else {

                    id = gerarIdDisponivel(idsUtilizados);
                }

                Skin skin = new Skin(
                        id,
                        nome,
                        jogo,
                        normalizarRaridade(jogo, raridade),
                        null,
                        imagem.isBlank() ? null : imagem
                );

                catalogo.add(skin);

                quantidade++;
            }

            System.out.println(
                    jogo + ": " + quantidade + " skins carregadas."
            );

        } catch (IOException | RuntimeException e) {

            System.err.println(
                    "Erro ao ler " + arquivo + ": " + e.getMessage()
            );
        }
    }

    // =====================================================
    // RARIDADE DO VALORANT
    // =====================================================
    private String obterRaridadeValorant(JsonObject objeto) {

        String uuid = obterTexto(objeto, "contentTierUuid");

        if (uuid.isEmpty()) {
            return "Sem categoria";
        }

        // Consulta as categorias somente uma vez
        if (categoriasValorant == null) {

            categoriasValorant = new HashMap<>();

            try {

                ServicoValorant servico = new ServicoValorant();

                JsonArray categorias = servico.buscarCategorias();

                for (JsonElement elemento : categorias) {

                    JsonObject categoria = elemento.getAsJsonObject();

                    String id = obterTexto(categoria, "uuid");

                    String nome = obterTexto(categoria, "displayName");

                    if (!id.isEmpty() && !nome.isEmpty()) {
                        categoriasValorant.put(id, nome);
                    }
                }

            } catch (IOException e) {

                System.err.println(
                        "Erro ao carregar categorias do Valorant: "
                        + e.getMessage()
                );

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
            }
        }

        return categoriasValorant.getOrDefault(
                uuid,
                "Sem categoria"
        );
    }

    // =====================================================
    // RARIDADE DO CS2
    // =====================================================
    private String obterRaridadeCS2(JsonObject objeto) {

        if (!objeto.has("rarity")
                || objeto.get("rarity").isJsonNull()) {

            return obterTexto(objeto, "raridade");
        }

        JsonElement elemento = objeto.get("rarity");

        String raridade;

        if (elemento.isJsonObject()) {

            raridade = obterTexto(
                    elemento.getAsJsonObject(),
                    "name"
            );

        } else {

            raridade = elemento.getAsString();
        }

        return raridade;
    }

    // =====================================================
    // NORMALIZAR RARIDADES
    // =====================================================
    private String normalizarRaridade(
            String jogo,
            String raridade) {

        if (raridade == null || raridade.isBlank()) {
            return "Sem categoria";
        }

        String valor = raridade.trim().toLowerCase(Locale.ROOT);

        if (jogo.equalsIgnoreCase("Valorant")) {

            if (valor.contains("premium")) {
                return "Premium";
            }

            if (valor.contains("exclusive")
                    || valor.contains("exclusiv")) {
                return "Exclusive";
            }

            if (valor.contains("ultra")) {
                return "Ultra";
            }
        }

        if (jogo.equalsIgnoreCase("CS2")) {

            if (valor.contains("mil-spec")) {
                return "Mil-Spec";
            }

            if (valor.contains("restricted")) {
                return "Restricted";
            }

            if (valor.contains("classified")) {
                return "Classified";
            }

            if (valor.contains("covert")) {
                return "Covert";
            }
        }

        // FORTNITE
        if (jogo.equalsIgnoreCase("Fortnite")) {

            switch (valor) {

                case "comum":
                    return "Common";

                case "incomum":
                    return "Uncommon";

                case "raro":
                    return "Rare";

                case "épico":
                    return "Epic";

                case "lendário":
                    return "Legendary";

                case "marvel":
                case "série marvel":
                    return "Marvel";

                case "série dc":
                case "dc":
                    return "DC";

                case "série ícones":
                case "icon series":
                    return "Icon Series";

                case "série lendas dos jogos":
                case "gaming legends":
                    return "Gaming Legends";

                default:
                    return raridade.trim();
            }
        }

        return raridade.trim();
    }

    // =====================================================
    // ID DO JSON
    // =====================================================
    private int obterIdJson(JsonObject objeto) {

        try {

            if (objeto.has("id")
                    && !objeto.get("id").isJsonNull()) {

                return objeto.get("id").getAsInt();
            }

        } catch (RuntimeException e) {
            return 0;
        }

        return 0;
    }

    // =====================================================
    // GERAR ID DISPONÍVEL
    // =====================================================
    private int gerarIdDisponivel(Set<Integer> idsUtilizados) {

        int id = 1;

        while (idsUtilizados.contains(id)) {
            id++;
        }

        idsUtilizados.add(id);

        return id;
    }

    // =====================================================
    // CHAVE DE IDENTIFICAÇÃO DA SKIN
    // =====================================================
    private String criarChaveSkin(String jogo, String nome) {

        return jogo.trim().toLowerCase(Locale.ROOT)
                + "|"
                + nome.trim().toLowerCase(Locale.ROOT);
    }

    // =====================================================
    // LER TEXTO DO JSON
    // =====================================================
    private String obterTexto(JsonObject objeto, String campo) {

        if (objeto != null
                && objeto.has(campo)
                && !objeto.get(campo).isJsonNull()) {

            return objeto.get(campo).getAsString();
        }

        return "";
    }

    // Sorteia primeiro a raridade e depois uma skin daquela raridade.
    // Este método não altera a coleção do usuário.
    public Skin sortearSkin(Caixa caixa) {

        if (caixa == null) {
            throw new IllegalArgumentException("Caixa não informada.");
        }

        Map<String, List<Skin>> skinsPorRaridade
                = new LinkedHashMap<>();

        for (String raridade : caixa.getProbabilidades().keySet()) {

            skinsPorRaridade.put(
                    raridade,
                    new ArrayList<Skin>()
            );
        }

        for (Skin skin : listarSkins(caixa)) {

            List<Skin> grupo
                    = skinsPorRaridade.get(skin.getRaridade());

            if (grupo != null) {
                grupo.add(skin);
            }
        }

        double pesoTotal = 0;

        for (Map.Entry<String, List<Skin>> entrada
                : skinsPorRaridade.entrySet()) {

            if (!entrada.getValue().isEmpty()) {

                pesoTotal += caixa.getProbabilidades()
                        .get(entrada.getKey());
            }
        }

        if (pesoTotal <= 0) {

            throw new IllegalStateException(
                    "Não existem skins cadastradas para esta caixa.");
        }

        double sorteio = aleatorio.nextDouble() * pesoTotal;
        double acumulado = 0;

        for (Map.Entry<String, List<Skin>> entrada
                : skinsPorRaridade.entrySet()) {

            List<Skin> grupo = entrada.getValue();

            // Raridades sem skins cadastradas não participam do sorteio.
            if (grupo.isEmpty()) {
                continue;
            }

            acumulado += caixa.getProbabilidades()
                    .get(entrada.getKey());

            if (sorteio < acumulado) {

                return grupo.get(
                        aleatorio.nextInt(grupo.size())
                );
            }
        }

        // Segurança para eventuais diferenças de arredondamento.
        for (List<Skin> grupo : skinsPorRaridade.values()) {

            if (!grupo.isEmpty()) {

                return grupo.get(
                        aleatorio.nextInt(grupo.size())
                );
            }
        }

        throw new IllegalStateException(
                "Não foi possível sortear uma skin.");
    }

    // Adiciona a recompensa à coleção e persiste os objetos.
    public void registrarRecompensa(Usuario usuario, Skin skin) {

        if (usuario == null || skin == null) {

            throw new IllegalArgumentException(
                    "Usuário e skin devem ser informados.");
        }

        EntidadeDAO<Colecao> daoColecao
                = banco.getDAO(Colecao.class);

        EntidadeDAO<Usuario> daoUsuario
                = banco.getDAO(Usuario.class);

        try {

            Colecao colecao = obterOuCriarColecao(
                    usuario,
                    daoColecao
            );

            colecao.adicionarItem(skin, 1);

            // Verifica se a coleção já existe.
            Colecao existente = buscarOuNull(
                    daoColecao,
                    colecao.getId()
            );

            if (existente == null) {

                daoColecao.salvar(colecao);

            } else {

                daoColecao.atualizar(colecao);
            }

            usuario.setColecao(colecao);

            // Atualiza o usuário caso esteja cadastrado.
            Usuario usuarioExistente = buscarOuNull(
                    daoUsuario,
                    usuario.getId()
            );

            if (usuarioExistente != null) {
                daoUsuario.atualizar(usuario);
            }

            // Persiste as alterações nos arquivos.
            daoColecao.persistir();
            daoUsuario.persistir();

        } catch (PersistenceException e) {

            throw new IllegalStateException(
                    "Erro ao registrar a recompensa: "
                    + e.getMessage(),
                    e
            );
        }
    }

    // Procura uma coleção existente pelo dono antes de criar outra.
    private Colecao obterOuCriarColecao(
            Usuario usuario,
            EntidadeDAO<Colecao> daoColecao) {

        for (Colecao colecao : carregarOuVazio(daoColecao)) {

            if (colecao.getUsuario() != null
                    && colecao.getUsuario().getId() == usuario.getId()) {

                colecao.setUsuario(usuario);
                usuario.setColecao(colecao);

                return colecao;
            }
        }

        Colecao colecao = usuario.getColecao();

        if (colecao != null) {

            Colecao existente = buscarOuNull(
                    daoColecao,
                    colecao.getId()
            );

            if (existente != null) {

                if (existente.getUsuario() == null
                        || existente.getUsuario().getId() == usuario.getId()) {

                    colecao = existente;
                }
            }
        }

        if (colecao == null) {

            colecao = new Colecao(
                    proximoIdColecao(daoColecao),
                    usuario,
                    "Minha Coleção"
            );
        }

        colecao.setUsuario(usuario);
        usuario.setColecao(colecao);

        return colecao;
    }

    // Gera o próximo ID disponível para uma coleção.
    private int proximoIdColecao(
            EntidadeDAO<Colecao> daoColecao) {

        int maiorId = 0;

        for (Colecao colecao : carregarOuVazio(daoColecao)) {

            if (colecao.getId() > maiorId) {
                maiorId = colecao.getId();
            }
        }

        return maiorId + 1;
    }

    // Busca uma entidade e retorna null quando ela não existe.
    private <E extends Entidade> E buscarOuNull(
            EntidadeDAO<E> dao,
            int id) {

        try {

            return dao.buscar(id);

        } catch (PersistenceException e) {

            if ("BUSCAR".equals(e.getOperacao())) {
                return null;
            }

            throw new IllegalStateException(
                    "Erro ao buscar entidade: " + e.getMessage(),
                    e
            );
        }
    }

    // Carrega as entidades ou retorna uma lista vazia.
    private <E extends Entidade> List<E> carregarOuVazio(
            EntidadeDAO<E> dao) {

        try {

            return Arrays.asList(dao.carregar());

        } catch (PersistenceException e) {

            if ("CARREGAR TODOS".equals(e.getOperacao())) {
                return new ArrayList<>();
            }

            throw new IllegalStateException(
                    "Erro ao carregar entidades: " + e.getMessage(),
                    e
            );
        }
    }
}
