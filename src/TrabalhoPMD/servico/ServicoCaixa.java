package TrabalhoPMD.servico;

import TrabalhoPMD.dados.BancoDados;
import TrabalhoPMD.dados.EntidadeDAO;
import TrabalhoPMD.modelo.Caixa;
import TrabalhoPMD.modelo.Colecao;
import TrabalhoPMD.modelo.Entidade;
import TrabalhoPMD.modelo.Skin;
import TrabalhoPMD.modelo.Usuario;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ServicoCaixa {

    private final BancoDados banco;
    private final Random aleatorio;

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

    // Retorna as skins que podem aparecer em determinada caixa.
    public List<Skin> listarSkins(Caixa caixa) {

        if (caixa == null) {
            throw new IllegalArgumentException("Caixa não informada.");
        }

        List<Skin> skinsDisponiveis = new ArrayList<>();

        EntidadeDAO daoSkin = banco.getDAO(Skin.class);

        for (Entidade entidade : daoSkin.carregar()) {

            Skin skin = (Skin) entidade;

            if (skin.getJogo() == null
                    || !skin.getJogo().equalsIgnoreCase(caixa.getJogo())) {
                continue;
            }

            if (skin.getRaridade() != null
                    && caixa.getProbabilidades().containsKey(skin.getRaridade())) {

                skinsDisponiveis.add(skin);
            }
        }

        return skinsDisponiveis;
    }

    // Sorteia primeiro a raridade e depois uma skin daquela raridade.
    // Este método não altera a coleção do usuário.
    public Skin sortearSkin(Caixa caixa) {

        if (caixa == null) {
            throw new IllegalArgumentException("Caixa não informada.");
        }

        Map<String, List<Skin>> skinsPorRaridade = new LinkedHashMap<>();

        for (String raridade : caixa.getProbabilidades().keySet()) {
            skinsPorRaridade.put(raridade, new ArrayList<Skin>());
        }

        for (Skin skin : listarSkins(caixa)) {

            List<Skin> grupo = skinsPorRaridade.get(skin.getRaridade());

            if (grupo != null) {
                grupo.add(skin);
            }
        }

        double pesoTotal = 0;

        for (Map.Entry<String, List<Skin>> entrada
                : skinsPorRaridade.entrySet()) {

            if (!entrada.getValue().isEmpty()) {
                pesoTotal += caixa.getProbabilidades().get(entrada.getKey());
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

            acumulado += caixa.getProbabilidades().get(entrada.getKey());

            if (sorteio < acumulado) {
                return grupo.get(aleatorio.nextInt(grupo.size()));
            }
        }

        // Segurança para eventuais diferenças de arredondamento.
        for (List<Skin> grupo : skinsPorRaridade.values()) {

            if (!grupo.isEmpty()) {
                return grupo.get(aleatorio.nextInt(grupo.size()));
            }
        }

        throw new IllegalStateException("Não foi possível sortear uma skin.");
    }

    // Adiciona a recompensa à coleção e persiste os dois objetos.
    public void registrarRecompensa(Usuario usuario, Skin skin) {

        if (usuario == null || skin == null) {
            throw new IllegalArgumentException(
                    "Usuário e skin devem ser informados.");
        }

        EntidadeDAO daoColecao = banco.getDAO(Colecao.class);
        EntidadeDAO daoUsuario = banco.getDAO(Usuario.class);

        Colecao colecao = obterOuCriarColecao(
                usuario,
                daoColecao
        );

        colecao.adicionarItem(skin, 1);

        Entidade existente = daoColecao.buscar(colecao.getId());

        if (existente == null) {

            if (!daoColecao.salvar(colecao)) {
                throw new IllegalStateException(
                        "Não foi possível salvar a coleção.");
            }

        } else {

            if (!daoColecao.atualizar(colecao)) {
                throw new IllegalStateException(
                        "Não foi possível atualizar a coleção.");
            }
        }

        usuario.setColecao(colecao);

        if (daoUsuario.buscar(usuario.getId()) != null) {
            if (!daoUsuario.atualizar(usuario)) {
                throw new IllegalStateException(
                        "Não foi possível atualizar o usuário.");
            }
        }

        if (!daoColecao.persistir()) {
            throw new IllegalStateException(
                    "Não foi possível persistir a coleção.");
        }

        if (!daoUsuario.persistir()) {
            throw new IllegalStateException(
                    "Não foi possível persistir o usuário.");
        }
    }

    // Procura uma coleção existente pelo dono antes de criar outra.
    private Colecao obterOuCriarColecao(
            Usuario usuario,
            EntidadeDAO daoColecao) {

        for (Entidade entidade : daoColecao.carregar()) {

            Colecao colecao = (Colecao) entidade;

            if (colecao.getUsuario() != null
                    && colecao.getUsuario().getId() == usuario.getId()) {

                colecao.setUsuario(usuario);
                usuario.setColecao(colecao);

                return colecao;
            }
        }

        Colecao colecao = usuario.getColecao();

        if (colecao != null) {

            Entidade existente = daoColecao.buscar(colecao.getId());

            if (existente instanceof Colecao) {

                Colecao armazenada = (Colecao) existente;

                if (armazenada.getUsuario() == null
                        || armazenada.getUsuario().getId() == usuario.getId()) {

                    colecao = armazenada;
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

    private int proximoIdColecao(EntidadeDAO daoColecao) {

        int maiorId = 0;

        for (Entidade entidade : daoColecao.carregar()) {

            if (entidade.getId() > maiorId) {
                maiorId = entidade.getId();
            }
        }

        return maiorId + 1;
    }
}
