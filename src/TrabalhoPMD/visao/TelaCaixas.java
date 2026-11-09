package TrabalhoPMD.visao;

import TrabalhoPMD.modelo.Caixa;
import TrabalhoPMD.modelo.Skin;
import TrabalhoPMD.modelo.Usuario;
import TrabalhoPMD.servico.ServicoCaixa;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;

public class TelaCaixas extends JFrame {

    private static final Color FUNDO = new Color(18, 20, 32);
    private static final Color PAINEL = new Color(29, 33, 49);
    private static final Color CARTAO = new Color(40, 46, 65);
    private static final Color TEXTO = new Color(245, 246, 250);
    private static final Color ROXO = new Color(123, 104, 238);
    private static final Color DOURADO = new Color(245, 190, 75);

    private final Usuario usuario;
    private final ServicoCaixa servico;
    private final Random aleatorio;

    private JComboBox<Caixa> seletorCaixa;
    private JLabel textoProbabilidades;
    private JLabel textoStatus;
    private JLabel textoPremio;
    private JLabel imagemPremio;
    private JButton botaoAbrir;

    private final JPanel[] cartoes = new JPanel[7];
    private final JLabel[] nomesCartoes = new JLabel[7];
    private final JLabel[] raridadesCartoes = new JLabel[7];

    private List<Skin> skinsDisponiveis;
    private Skin skinSorteada;
    private Timer temporizador;
    private int passos;

    public TelaCaixas(Usuario usuario) {

        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não informado.");
        }

        this.usuario = usuario;
        this.servico = new ServicoCaixa();
        this.aleatorio = new Random();

        setTitle("Skin Collection - Abertura de Caixas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 650));
        setSize(1150, 720);
        setLocationRelativeTo(null);

        criarInterface();
        atualizarInformacoesCaixa();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evento) {

                if (temporizador != null && temporizador.isRunning()) {
                    temporizador.stop();
                }
            }
        });
    }

    private void criarInterface() {

        JPanel principal = new JPanel(new BorderLayout(18, 18));
        principal.setBackground(FUNDO);
        principal.setBorder(
                BorderFactory.createEmptyBorder(24, 28, 24, 28));

        JPanel cabecalho = new JPanel(new BorderLayout(8, 8));
        cabecalho.setOpaque(false);

        JLabel titulo = new JLabel(
                "ABERTURA DE CAIXAS",
                SwingConstants.CENTER
        );
        titulo.setFont(new Font("Arial", Font.BOLD, 30));
        titulo.setForeground(TEXTO);

        JLabel subtitulo = new JLabel(
                "Escolha uma caixa e descubra qual skin vai para sua coleção.",
                SwingConstants.CENTER
        );
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitulo.setForeground(new Color(185, 190, 207));

        JPanel titulos = new JPanel(new GridLayout(2, 1, 4, 4));
        titulos.setOpaque(false);
        titulos.add(titulo);
        titulos.add(subtitulo);

        JPanel selecao = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        selecao.setOpaque(false);

        JLabel textoCaixa = new JLabel("CAIXA:");
        textoCaixa.setForeground(TEXTO);
        textoCaixa.setFont(new Font("Arial", Font.BOLD, 14));

        List<Caixa> caixas = servico.listarCaixas();

        seletorCaixa = new JComboBox<>(
                caixas.toArray(new Caixa[0]));
        seletorCaixa.setFont(new Font("Arial", Font.PLAIN, 14));
        seletorCaixa.setPreferredSize(new Dimension(260, 34));

        selecao.add(textoCaixa);
        selecao.add(seletorCaixa);

        cabecalho.add(titulos, BorderLayout.NORTH);
        cabecalho.add(selecao, BorderLayout.CENTER);

        textoProbabilidades = new JLabel("", SwingConstants.CENTER);
        textoProbabilidades.setForeground(new Color(205, 210, 225));
        textoProbabilidades.setFont(new Font("Arial", Font.PLAIN, 13));
        cabecalho.add(textoProbabilidades, BorderLayout.SOUTH);

        JPanel painelRoleta = new JPanel(new GridLayout(1, 7, 9, 0));
        painelRoleta.setBackground(PAINEL);
        painelRoleta.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(65, 72, 98), 1),
                        BorderFactory.createEmptyBorder(18, 12, 18, 12)
                )
        );

        for (int i = 0; i < cartoes.length; i++) {
            cartoes[i] = criarCartao(i);
            painelRoleta.add(cartoes[i]);
            atualizarCartao(
                    i,
                    null,
                    i == 3 ? "RESULTADO" : "Aguardando..."
            );
        }

        JPanel painelCentro = new JPanel(new BorderLayout(8, 8));
        painelCentro.setOpaque(false);

        JLabel instrucao = new JLabel(
                "A roleta vai parar no item sorteado.",
                SwingConstants.CENTER
        );
        instrucao.setForeground(new Color(185, 190, 207));
        instrucao.setFont(new Font("Arial", Font.PLAIN, 14));

        painelCentro.add(instrucao, BorderLayout.NORTH);
        painelCentro.add(painelRoleta, BorderLayout.CENTER);

        JPanel painelResultado = new JPanel(new BorderLayout(18, 8));
        painelResultado.setBackground(PAINEL);
        painelResultado.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(65, 72, 98), 1),
                        BorderFactory.createEmptyBorder(12, 16, 12, 16)
                )
        );

        imagemPremio = new JLabel(
                "Sua recompensa aparecerá aqui",
                SwingConstants.CENTER
        );
        imagemPremio.setForeground(new Color(185, 190, 207));
        imagemPremio.setPreferredSize(new Dimension(210, 125));
        imagemPremio.setBorder(
                BorderFactory.createLineBorder(
                        new Color(65, 72, 98), 1));

        JPanel informacoes = new JPanel(new GridLayout(2, 1, 5, 5));
        informacoes.setOpaque(false);

        textoPremio = new JLabel(
                "Nenhuma caixa aberta ainda.",
                SwingConstants.LEFT
        );
        textoPremio.setForeground(TEXTO);
        textoPremio.setFont(new Font("Arial", Font.BOLD, 17));

        textoStatus = new JLabel(
                "Escolha uma caixa para começar.",
                SwingConstants.LEFT
        );
        textoStatus.setForeground(new Color(185, 190, 207));
        textoStatus.setFont(new Font("Arial", Font.PLAIN, 13));

        informacoes.add(textoPremio);
        informacoes.add(textoStatus);

        botaoAbrir = new JButton("ABRIR CAIXA");
        botaoAbrir.setFont(new Font("Arial", Font.BOLD, 15));
        botaoAbrir.setForeground(Color.WHITE);
        botaoAbrir.setBackground(ROXO);
        botaoAbrir.setFocusPainted(false);
        botaoAbrir.setBorderPainted(false);
        botaoAbrir.setPreferredSize(new Dimension(180, 48));
        botaoAbrir.addActionListener(evento -> iniciarRoleta());

        painelResultado.add(imagemPremio, BorderLayout.WEST);
        painelResultado.add(informacoes, BorderLayout.CENTER);
        painelResultado.add(botaoAbrir, BorderLayout.EAST);

        JPanel inferior = new JPanel(new BorderLayout(10, 10));
        inferior.setOpaque(false);
        inferior.add(painelCentro, BorderLayout.CENTER);
        inferior.add(painelResultado, BorderLayout.SOUTH);

        principal.add(cabecalho, BorderLayout.NORTH);
        principal.add(inferior, BorderLayout.CENTER);

        setContentPane(principal);

        seletorCaixa.addActionListener(
                evento -> atualizarInformacoesCaixa());
    }

    private JPanel criarCartao(int indice) {

        JPanel cartao = new JPanel(new BorderLayout(4, 8));
        cartao.setBackground(CARTAO);

        Border borda;

        if (indice == 3) {
            borda = BorderFactory.createLineBorder(DOURADO, 3);
        } else {
            borda = BorderFactory.createLineBorder(
                    new Color(70, 78, 105), 1);
        }

        cartao.setBorder(
                BorderFactory.createCompoundBorder(
                        borda,
                        BorderFactory.createEmptyBorder(12, 6, 12, 6)
                )
        );

        JLabel nome = new JLabel(
                "Aguardando...",
                SwingConstants.CENTER
        );
        nome.setForeground(TEXTO);
        nome.setFont(new Font("Arial", Font.BOLD, 13));

        JLabel raridade = new JLabel(
                " ",
                SwingConstants.CENTER
        );
        raridade.setForeground(new Color(185, 190, 207));
        raridade.setFont(new Font("Arial", Font.PLAIN, 11));

        nomesCartoes[indice] = nome;
        raridadesCartoes[indice] = raridade;

        cartao.add(nome, BorderLayout.CENTER);
        cartao.add(raridade, BorderLayout.SOUTH);

        return cartao;
    }

    private void atualizarCartao(
            int indice,
            Skin skin,
            String textoAlternativo) {

        if (skin == null) {
            nomesCartoes[indice].setText(
                    "<html><center>" + escaparHtml(textoAlternativo)
                    + "</center></html>");
            raridadesCartoes[indice].setText(" ");
            return;
        }

        nomesCartoes[indice].setText(
                "<html><center>" + escaparHtml(skin.getNome())
                + "</center></html>");

        raridadesCartoes[indice].setText(
                skin.getRaridade() == null
                        ? " "
                        : escaparHtml(skin.getRaridade()));
    }

    private void atualizarInformacoesCaixa() {

        if (temporizador != null && temporizador.isRunning()) {
            return;
        }

        Caixa caixa = (Caixa) seletorCaixa.getSelectedItem();

        if (caixa == null) {
            return;
        }

        StringBuilder texto = new StringBuilder(
                "<html><center>");

        for (java.util.Map.Entry<String, Double> entrada
                : caixa.getProbabilidades().entrySet()) {

            texto.append(escaparHtml(entrada.getKey()))
                    .append(": ")
                    .append(formatarPercentual(entrada.getValue()))
                    .append(" &nbsp;&nbsp; ");
        }

        texto.append("</center></html>");

        textoProbabilidades.setText(texto.toString());

        int quantidade = servico.listarSkins(caixa).size();

        if (quantidade == 0) {
            botaoAbrir.setEnabled(false);
            textoStatus.setText(
                    "Não há skins cadastradas para esta caixa.");
        } else {
            botaoAbrir.setEnabled(true);
            textoStatus.setText(
                    quantidade + " skins disponíveis nesta caixa.");
        }
    }

    private String formatarPercentual(double valor) {

        if (valor == Math.floor(valor)) {
            return ((int) valor) + "%";
        }

        return String.format(java.util.Locale.ROOT, "%.2f%%", valor);
    }

    private void iniciarRoleta() {

        if (temporizador != null && temporizador.isRunning()) {
            return;
        }

        Caixa caixa = (Caixa) seletorCaixa.getSelectedItem();

        if (caixa == null) {
            return;
        }

        try {

            skinsDisponiveis = servico.listarSkins(caixa);

            if (skinsDisponiveis.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Não existem skins cadastradas para esta caixa.",
                        "Caixa indisponível",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // O resultado real é definido antes da animação.
            skinSorteada = servico.sortearSkin(caixa);

            passos = 0;
            seletorCaixa.setEnabled(false);
            botaoAbrir.setEnabled(false);
            botaoAbrir.setText("SORTEANDO...");
            textoPremio.setText("A roleta está girando...");
            textoStatus.setText("Boa sorte, " + usuario.getNome() + "!");

            temporizador = new Timer(60, evento -> avancarRoleta());
            temporizador.start();

        } catch (RuntimeException erro) {

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage(),
                    "Erro ao abrir caixa",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void avancarRoleta() {

        for (int i = 0; i < cartoes.length; i++) {

            Skin visual = skinsDisponiveis.get(
                    aleatorio.nextInt(skinsDisponiveis.size()));

            atualizarCartao(i, visual, null);
        }

        passos++;

        // Aumenta o intervalo entre as trocas para simular desaceleração.
        if (passos > 16) {
            temporizador.setDelay(
                    Math.min(230, temporizador.getDelay() + 12));
        }

        if (passos >= 32) {

            temporizador.stop();

            // O cartão central mostra exatamente a skin sorteada.
            atualizarCartao(3, skinSorteada, null);
            finalizarAbertura();
        }
    }

    private void finalizarAbertura() {

        try {

            servico.registrarRecompensa(usuario, skinSorteada);

            textoPremio.setText(
                    "Você conseguiu: " + skinSorteada.getNome());

            textoStatus.setText(
                    skinSorteada.getRaridade()
                    + " • adicionada à sua coleção!");

            carregarImagemPremio(skinSorteada);

        } catch (RuntimeException erro) {

            textoPremio.setText(
                    "Resultado: " + skinSorteada.getNome());

            textoStatus.setText(
                    "Não foi possível salvar a recompensa.");

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage(),
                    "Erro ao salvar recompensa",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        seletorCaixa.setEnabled(true);
        botaoAbrir.setEnabled(true);
        botaoAbrir.setText("ABRIR NOVAMENTE");
    }

    private void carregarImagemPremio(Skin skin) {

        imagemPremio.setIcon(null);

        if (skin.getImagemUrl() == null
                || skin.getImagemUrl().trim().isEmpty()) {

            imagemPremio.setText("Imagem não cadastrada");
            return;
        }

        imagemPremio.setText("Carregando imagem...");

        new SwingWorker<ImageIcon, Void>() {

            @Override
            protected ImageIcon doInBackground() throws Exception {

                URL url = new URL(skin.getImagemUrl());
                URLConnection conexao = url.openConnection();

                conexao.setConnectTimeout(8000);
                conexao.setReadTimeout(10000);

                BufferedImage imagem;

                try (InputStream entrada = conexao.getInputStream()) {
                    imagem = ImageIO.read(entrada);
                }

                if (imagem == null) {
                    return null;
                }

                Image imagemRedimensionada = imagem.getScaledInstance(
                        190,
                        115,
                        Image.SCALE_SMOOTH
                );

                return new ImageIcon(imagemRedimensionada);
            }

            @Override
            protected void done() {

                try {

                    ImageIcon icone = get();

                    if (icone == null) {
                        imagemPremio.setText("Imagem indisponível");
                    } else {
                        imagemPremio.setText("");
                        imagemPremio.setIcon(icone);
                    }

                } catch (Exception erro) {

                    imagemPremio.setIcon(null);
                    imagemPremio.setText("Imagem indisponível");
                }
            }

        }.execute();
    }

    private String escaparHtml(String texto) {

        if (texto == null) {
            return "";
        }

        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }
}
