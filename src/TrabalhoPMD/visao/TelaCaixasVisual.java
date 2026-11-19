package TrabalhoPMD.visao;

import TrabalhoPMD.modelo.Caixa;
import TrabalhoPMD.modelo.Skin;
import TrabalhoPMD.modelo.Usuario;
import TrabalhoPMD.servico.ServicoCaixa;
import TrabalhoPMD.modelo.Perfil;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Janela de abertura de caixas.
 *
 * Este formulário possui um arquivo .form para edição visual no NetBeans.
 */
public class TelaCaixasVisual extends javax.swing.JFrame {

    private Usuario usuario;
    private ServicoCaixa servico;
    private Random aleatorio;

    private List<Skin> skinsDisponiveis;
    private Skin skinSorteada;

    private Timer temporizador;
    private int passos;

    private final Color FUNDO = new Color(15, 18, 34);
    private final Color PAINEL = new Color(25, 30, 52);
    private final Color CARTAO = new Color(35, 42, 69);

    private final Color TEXTO = new Color(245, 246, 250);
    private final Color TEXTO_SECUNDARIO = new Color(180, 188, 210);

    private final Color ROXO = new Color(125, 108, 230);
    private final Color LAVANDA = new Color(201, 214, 255);
    private final Color DOURADO = new Color(245, 190, 75);

    private final Color BORDA = new Color(67, 76, 111);

    /**
     * Construtor sem usuário, mantido para permitir a pré-visualização do
     * formulário no NetBeans.
     */
    public TelaCaixasVisual() {
        this(null);
    }

    public TelaCaixasVisual(Usuario usuario) {

        this.usuario = usuario;
        this.servico = new ServicoCaixa();
        this.aleatorio = new Random();

        initComponents();

        configurarVisual();

        setTitle("Skin Collection - Abertura de Caixas");
        setDefaultCloseOperation(
                javax.swing.WindowConstants.DISPOSE_ON_CLOSE
        );

        setMinimumSize(new java.awt.Dimension(1050, 700));
        setSize(1200, 800);

        // Monta a nova interface utilizando os componentes existentes.
        setContentPane(construirInterface());

        carregarCaixas();

        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent evento) {

                if (temporizador != null && temporizador.isRunning()) {
                    temporizador.stop();
                }
            }
        });
    }

    private void configurarVisual() {

        // Título principal
        tituloLabel.setFont(
                new Font("SansSerif", Font.BOLD, 29)
        );

        tituloLabel.setForeground(TEXTO);
        tituloLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Subtítulo
        subtituloLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        subtituloLabel.setForeground(TEXTO_SECUNDARIO);
        subtituloLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Seleção da caixa
        caixaLabel.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        caixaLabel.setForeground(LAVANDA);
        caixaLabel.setText("ESCOLHA SUA CAIXA");

        seletorCaixaComboBox.setFont(
                new Font("SansSerif", Font.PLAIN, 14)
        );

        seletorCaixaComboBox.setBackground(PAINEL);
        seletorCaixaComboBox.setForeground(TEXTO);

        seletorCaixaComboBox.setPreferredSize(
                new java.awt.Dimension(310, 38)
        );

        seletorCaixaComboBox.setBorder(
                BorderFactory.createLineBorder(BORDA)
        );

        // Probabilidades
        probabilidadesLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        probabilidadesLabel.setForeground(TEXTO_SECUNDARIO);
        probabilidadesLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Status
        statusLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        statusLabel.setForeground(LAVANDA);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Resultado
        resultadoLabel.setFont(
                new Font("SansSerif", Font.BOLD, 19)
        );

        resultadoLabel.setForeground(DOURADO);

        detalheResultadoLabel.setFont(
                new Font("SansSerif", Font.PLAIN, 13)
        );

        detalheResultadoLabel.setForeground(TEXTO_SECUNDARIO);

        // Imagem da recompensa
        imagemPremioLabel.setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );

        imagemPremioLabel.setForeground(LAVANDA);
        imagemPremioLabel.setBackground(CARTAO);
        imagemPremioLabel.setOpaque(true);

        imagemPremioLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imagemPremioLabel.setPreferredSize(
                new java.awt.Dimension(210, 145)
        );

        imagemPremioLabel.setBorder(
                BorderFactory.createLineBorder(ROXO, 1)
        );

        // Botões
        estilizarBotao(abrirButton, ROXO, Color.WHITE);

        estilizarBotao(
                fecharButton,
                new Color(45, 52, 78),
                LAVANDA
        );

        abrirButton.setText("ABRIR CAIXA");
        fecharButton.setText("VOLTAR");

        abrirButton.setPreferredSize(
                new java.awt.Dimension(175, 48)
        );

        fecharButton.setPreferredSize(
                new java.awt.Dimension(110, 48)
        );

        jPanelPrincipal.setBackground(FUNDO);
    }

    private void estilizarBotao(JButton botao, Color fundo, Color texto) {

        botao.setFont(
                new Font("SansSerif", Font.BOLD, 12)
        );

        botao.setForeground(texto);
        botao.setBackground(fundo);

        botao.setFocusPainted(false);
        botao.setBorderPainted(false);

        botao.setOpaque(true);
        botao.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        botao.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                fundo, 1, true
                        ),
                        BorderFactory.createEmptyBorder(
                                10, 17, 10, 17
                        )
                )
        );
    }

    private JPanel construirInterface() {

        JPanel principal = new JPanel(new BorderLayout(0, 18));

        principal.setBackground(FUNDO);

        principal.setBorder(
                BorderFactory.createEmptyBorder(25, 28, 25, 28)
        );

        // =====================================
        // CABEÇALHO
        // =====================================
        JPanel cabecalho = new JPanel();

        cabecalho.setOpaque(false);

        cabecalho.setLayout(
                new javax.swing.BoxLayout(
                        cabecalho,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        tituloLabel.setText("ABERTURA DE CAIXAS");

        subtituloLabel.setText(
                "Descubra novas skins e adicione recompensas à sua coleção."
        );

        cabecalho.add(tituloLabel);
        cabecalho.add(javax.swing.Box.createVerticalStrut(5));
        cabecalho.add(subtituloLabel);

        // =====================================
        // SELEÇÃO DE CAIXA
        // =====================================
        PainelArredondado painelSelecao = new PainelArredondado(
                PAINEL,
                BORDA
        );

        painelSelecao.setLayout(new BorderLayout(10, 8));

        painelSelecao.setBorder(
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        );

        JPanel linhaSelecao = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 12, 0)
        );

        linhaSelecao.setOpaque(false);

        linhaSelecao.add(caixaLabel);
        linhaSelecao.add(seletorCaixaComboBox);

        painelSelecao.add(
                linhaSelecao,
                BorderLayout.NORTH
        );

        painelSelecao.add(
                probabilidadesLabel,
                BorderLayout.CENTER
        );

        // =====================================
        // CARTÕES DA ROLETA
        // =====================================
        JPanel fileiraRoleta = new JPanel(
                new GridLayout(1, 7, 10, 0)
        );

        fileiraRoleta.setOpaque(false);

        JLabel[] cartoes = obterCartoes();

        for (int i = 0; i < cartoes.length; i++) {

            fileiraRoleta.add(
                    criarCartaoRoleta(
                            cartoes[i],
                            i == 3
                    )
            );
        }

        PainelArredondado painelRoleta = new PainelArredondado(
                PAINEL,
                BORDA
        );

        painelRoleta.setLayout(new BorderLayout());

        painelRoleta.setBorder(
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        );

        painelRoleta.add(
                fileiraRoleta,
                BorderLayout.CENTER
        );

        // =====================================
        // ÁREA CENTRAL
        // =====================================
        JPanel centro = new JPanel(new BorderLayout(0, 12));

        centro.setOpaque(false);

        centro.add(
                painelSelecao,
                BorderLayout.NORTH
        );

        centro.add(
                painelRoleta,
                BorderLayout.CENTER
        );

        centro.add(
                statusLabel,
                BorderLayout.SOUTH
        );

        // =====================================
        // ÁREA DE RECOMPENSA
        // =====================================
        PainelArredondado painelRecompensa = new PainelArredondado(
                PAINEL,
                BORDA
        );

        painelRecompensa.setLayout(
                new BorderLayout(20, 0)
        );

        painelRecompensa.setBorder(
                BorderFactory.createEmptyBorder(16, 18, 16, 18)
        );

        // Imagem
        painelRecompensa.add(
                imagemPremioLabel,
                BorderLayout.WEST
        );

        // Informações
        JPanel informacoes = new JPanel();

        informacoes.setOpaque(false);

        informacoes.setLayout(
                new javax.swing.BoxLayout(
                        informacoes,
                        javax.swing.BoxLayout.Y_AXIS
                )
        );

        resultadoLabel.setText(
                "Sua próxima recompensa"
        );

        detalheResultadoLabel.setText(
                "Abra uma caixa para descobrir sua próxima skin."
        );

        informacoes.add(
                resultadoLabel
        );

        informacoes.add(
                javax.swing.Box.createVerticalStrut(10)
        );

        informacoes.add(
                detalheResultadoLabel
        );

        painelRecompensa.add(
                informacoes,
                BorderLayout.CENTER
        );

        // Botões
        JPanel botoes = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0)
        );

        botoes.setOpaque(false);

        botoes.add(abrirButton);
        botoes.add(fecharButton);

        painelRecompensa.add(
                botoes,
                BorderLayout.EAST
        );

        // =====================================
        // MONTAGEM FINAL
        // =====================================
        principal.add(
                cabecalho,
                BorderLayout.NORTH
        );

        principal.add(
                centro,
                BorderLayout.CENTER
        );

        principal.add(
                painelRecompensa,
                BorderLayout.SOUTH
        );

        return principal;
    }

    private JPanel criarCartaoRoleta(
            JLabel etiqueta,
            boolean destaque) {

        estilizarCartao(etiqueta, destaque);

        PainelArredondado cartao = new PainelArredondado(
                destaque
                        ? new Color(48, 43, 78)
                        : CARTAO,
                destaque
                        ? DOURADO
                        : BORDA
        );

        cartao.setLayout(new BorderLayout());

        cartao.setBorder(
                BorderFactory.createEmptyBorder(10, 7, 10, 7)
        );

        cartao.setPreferredSize(
                new java.awt.Dimension(145, 195)
        );

        cartao.add(
                etiqueta,
                BorderLayout.CENTER
        );

        return cartao;
    }

    private void estilizarCartao(
            JLabel cartao,
            boolean destaque) {

        cartao.setOpaque(false);

        cartao.setForeground(
                destaque ? DOURADO : TEXTO
        );

        cartao.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        destaque ? 14 : 12
                )
        );

        cartao.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        cartao.setVerticalAlignment(
                SwingConstants.CENTER
        );

        cartao.setBorder(
                BorderFactory.createEmptyBorder(10, 5, 10, 5)
        );
    }

    private void carregarCaixas() {

        DefaultComboBoxModel<Caixa> modelo
                = new DefaultComboBoxModel<>();

        for (Caixa caixa : servico.listarCaixas()) {
            modelo.addElement(caixa);
        }

        seletorCaixaComboBox.setModel(modelo);
        atualizarInformacoesCaixa();
    }

    private void atualizarInformacoesCaixa() {

        if (temporizador != null && temporizador.isRunning()) {
            return;
        }

        Caixa caixa = (Caixa) seletorCaixaComboBox.getSelectedItem();

        if (caixa == null) {
            return;
        }

        StringBuilder texto = new StringBuilder(
                "<html><div style='text-align:left;'>"
        );

        for (java.util.Map.Entry<String, Double> entrada
                : caixa.getProbabilidades().entrySet()) {

            texto.append(escaparHtml(entrada.getKey()))
                    .append(": ")
                    .append(formatarPercentual(entrada.getValue()))
                    .append(" &nbsp;&nbsp;&nbsp; ");
        }

        texto.append("</div></html>");
        probabilidadesLabel.setText(texto.toString());

        int quantidade = servico.listarSkins(caixa).size();

        if (quantidade == 0) {

            statusLabel.setText(
                    "Não existem skins cadastradas para esta caixa.");

            abrirButton.setEnabled(false);

        } else {

            statusLabel.setText(
                    quantidade + " skins disponíveis nesta caixa.");

            abrirButton.setEnabled(true);
        }
    }

    private String formatarPercentual(double valor) {

        if (valor == Math.floor(valor)) {
            return ((int) valor) + "%";
        }

        return String.format(java.util.Locale.ROOT, "%.2f%%", valor);
    }

    private void iniciarRoleta() {

        if (usuario == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Entre com uma conta para receber a recompensa.",
                    "Usuário não informado",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        if (temporizador != null && temporizador.isRunning()) {
            return;
        }

        Caixa caixa = (Caixa) seletorCaixaComboBox.getSelectedItem();

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

            // O prêmio real é sorteado antes do início da animação.
            skinSorteada = servico.sortearSkin(caixa);

            passos = 0;

            seletorCaixaComboBox.setEnabled(false);
            abrirButton.setEnabled(false);
            abrirButton.setText("SORTEANDO...");

            resultadoLabel.setText("A roleta está girando...");
            detalheResultadoLabel.setText(
                    "Boa sorte, " + escaparHtml(usuario.getNome()) + "!");
            imagemPremioLabel.setIcon(null);
            imagemPremioLabel.setText("AGUARDANDO RESULTADO");

            temporizador = new Timer(65, evento -> avancarRoleta());
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

        JLabel[] cartoes = obterCartoes();

        for (int i = 0; i < cartoes.length; i++) {

            Skin skinVisual = skinsDisponiveis.get(
                    aleatorio.nextInt(skinsDisponiveis.size()));

            exibirSkinNoCartao(cartoes[i], skinVisual);
        }

        passos++;

        // Aumenta o intervalo entre as trocas para simular desaceleração.
        if (passos > 17) {
            temporizador.setDelay(
                    Math.min(240, temporizador.getDelay() + 13));
        }

        if (passos >= 32) {

            temporizador.stop();

            // O cartão central mostra a recompensa verdadeira.
            exibirSkinNoCartao(roletaSkin4, skinSorteada);

            finalizarAbertura();
        }
    }

    private JLabel[] obterCartoes() {

        return new JLabel[]{
            roletaSkin1,
            roletaSkin2,
            roletaSkin3,
            roletaSkin4,
            roletaSkin5,
            roletaSkin6,
            roletaSkin7
        };
    }

    private void exibirSkinNoCartao(JLabel cartao, Skin skin) {

        String nome = escaparHtml(skin.getNome());
        String raridade = escaparHtml(skin.getRaridade());

        cartao.setText(
                "<html><center>"
                + nome
                + "<br><br><font size='-1'>"
                + raridade
                + "</font></center></html>"
        );
    }

    private void finalizarAbertura() {

        try {

            // Registra e persiste a recompensa na coleção do usuário.
            servico.registrarRecompensa(usuario, skinSorteada);

            resultadoLabel.setText(
                    "Você conseguiu: " + skinSorteada.getNome());

            detalheResultadoLabel.setText(
                    skinSorteada.getJogo()
                    + " • "
                    + skinSorteada.getRaridade()
                    + " • adicionada à sua coleção!");

            carregarImagemPremio(skinSorteada);

        } catch (RuntimeException erro) {

            resultadoLabel.setText(
                    "Resultado: " + skinSorteada.getNome());

            detalheResultadoLabel.setText(
                    "Não foi possível salvar a recompensa.");

            JOptionPane.showMessageDialog(
                    this,
                    erro.getMessage(),
                    "Erro ao salvar recompensa",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        seletorCaixaComboBox.setEnabled(true);
        abrirButton.setEnabled(true);
        abrirButton.setText("ABRIR NOVAMENTE");
    }

    private void carregarImagemPremio(Skin skin) {

        imagemPremioLabel.setIcon(null);

        if (skin.getImagemUrl() == null
                || skin.getImagemUrl().trim().isEmpty()) {

            imagemPremioLabel.setText("Imagem não cadastrada");
            return;
        }

        imagemPremioLabel.setText("Carregando imagem...");

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
                        210,
                        135,
                        Image.SCALE_SMOOTH
                );

                return new ImageIcon(imagemRedimensionada);
            }

            @Override
            protected void done() {

                try {

                    ImageIcon icone = get();

                    if (icone == null) {

                        imagemPremioLabel.setText(
                                "Imagem indisponível");

                    } else {

                        imagemPremioLabel.setText("");
                        imagemPremioLabel.setIcon(icone);
                    }

                } catch (Exception erro) {

                    imagemPremioLabel.setIcon(null);
                    imagemPremioLabel.setText(
                            "Imagem indisponível");
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

    private void seletorCaixaComboBoxActionPerformed(
            java.awt.event.ActionEvent evt) {

        atualizarInformacoesCaixa();
    }

    private void abrirButtonActionPerformed(
            java.awt.event.ActionEvent evt) {

        iniciarRoleta();
    }

    private void fecharButtonActionPerformed(
        java.awt.event.ActionEvent evt) {

    // Interrompe a animação, caso esteja acontecendo.
    if (temporizador != null && temporizador.isRunning()) {
        temporizador.stop();
    }

    // Retorna para o menu correspondente ao perfil.
    if (usuario != null) {

        if (usuario.getPerfil() == Perfil.ADMIN) {

            FrameMenuAdmin menu = new FrameMenuAdmin(usuario);
            menu.setLocationRelativeTo(null);
            menu.setVisible(true);

        } else {

            FrameUsuarioMenu menu = new FrameUsuarioMenu(usuario);
            menu.setLocationRelativeTo(null);
            menu.setVisible(true);
        }

    } else {

        // Caso a tela tenha sido aberta sem usuário para pré-visualização.
        FramePrincipal tela = new FramePrincipal();
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }

    // Fecha a tela atual.
    this.dispose();
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelPrincipal = new javax.swing.JPanel();
        tituloLabel = new javax.swing.JLabel();
        subtituloLabel = new javax.swing.JLabel();
        caixaLabel = new javax.swing.JLabel();
        seletorCaixaComboBox = new javax.swing.JComboBox();
        probabilidadesLabel = new javax.swing.JLabel();
        roletaSkin1 = new javax.swing.JLabel();
        roletaSkin2 = new javax.swing.JLabel();
        roletaSkin3 = new javax.swing.JLabel();
        roletaSkin4 = new javax.swing.JLabel();
        roletaSkin5 = new javax.swing.JLabel();
        roletaSkin6 = new javax.swing.JLabel();
        roletaSkin7 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        imagemPremioLabel = new javax.swing.JLabel();
        resultadoLabel = new javax.swing.JLabel();
        detalheResultadoLabel = new javax.swing.JLabel();
        abrirButton = new javax.swing.JButton();
        fecharButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Skin Collection - Abertura de Caixas");
        setMinimumSize(new java.awt.Dimension(1050, 650));
        setPreferredSize(new java.awt.Dimension(1150, 720));

        jPanelPrincipal.setBackground(new java.awt.Color(18, 20, 32));

        tituloLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tituloLabel.setText("ABERTURA DE CAIXAS");

        subtituloLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        subtituloLabel.setText("Escolha uma caixa e descubra qual skin vai para sua coleção.");

        caixaLabel.setText("CAIXA:");

        seletorCaixaComboBox.addActionListener(this::seletorCaixaComboBoxActionPerformed);

        probabilidadesLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        probabilidadesLabel.setText("Probabilidades da caixa");

        roletaSkin1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roletaSkin1.setText("Aguardando...");

        roletaSkin2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roletaSkin2.setText("Aguardando...");

        roletaSkin3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roletaSkin3.setText("Aguardando...");

        roletaSkin4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roletaSkin4.setText("RESULTADO");

        roletaSkin5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roletaSkin5.setText("Aguardando...");

        roletaSkin6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roletaSkin6.setText("Aguardando...");

        roletaSkin7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        roletaSkin7.setText("Aguardando...");

        statusLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statusLabel.setText("Escolha uma caixa para começar.");

        imagemPremioLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagemPremioLabel.setText("SUA RECOMPENSA");

        resultadoLabel.setText("Nenhuma caixa aberta ainda.");

        detalheResultadoLabel.setText("A recompensa será adicionada à sua coleção.");

        abrirButton.setText("ABRIR CAIXA");
        abrirButton.addActionListener(this::abrirButtonActionPerformed);

        fecharButton.setText("FECHAR");
        fecharButton.addActionListener(this::fecharButtonActionPerformed);

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tituloLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subtituloLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(caixaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(seletorCaixaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(probabilidadesLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(roletaSkin1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(roletaSkin2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(roletaSkin3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(roletaSkin4, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(roletaSkin5, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(roletaSkin6, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(roletaSkin7, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(imagemPremioLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resultadoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(detalheResultadoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(abrirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(fecharButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(24, 24, 24))
        );
        jPanelPrincipalLayout.setVerticalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(tituloLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(subtituloLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(caixaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seletorCaixaComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addComponent(probabilidadesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(roletaSkin1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roletaSkin2, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roletaSkin3, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roletaSkin4, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roletaSkin5, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roletaSkin6, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roletaSkin7, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(resultadoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(detalheResultadoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(imagemPremioLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(abrirButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(fecharButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton abrirButton;
    private javax.swing.JLabel caixaLabel;
    private javax.swing.JLabel detalheResultadoLabel;
    private javax.swing.JButton fecharButton;
    private javax.swing.JLabel imagemPremioLabel;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JLabel probabilidadesLabel;
    private javax.swing.JLabel resultadoLabel;
    private javax.swing.JLabel roletaSkin1;
    private javax.swing.JLabel roletaSkin2;
    private javax.swing.JLabel roletaSkin3;
    private javax.swing.JLabel roletaSkin4;
    private javax.swing.JLabel roletaSkin5;
    private javax.swing.JLabel roletaSkin6;
    private javax.swing.JLabel roletaSkin7;
    private javax.swing.JComboBox seletorCaixaComboBox;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel subtituloLabel;
    private javax.swing.JLabel tituloLabel;
    // End of variables declaration//GEN-END:variables

    private class PainelArredondado extends JPanel {

        private final Color fundo;
        private final Color borda;

        public PainelArredondado(Color fundo, Color borda) {

            this.fundo = fundo;
            this.borda = borda;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {

            super.paintComponent(g);

            java.awt.Graphics2D g2
                    = (java.awt.Graphics2D) g.create();

            g2.setRenderingHint(
                    java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(fundo);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    22,
                    22
            );

            g2.setColor(borda);

            g2.drawRoundRect(
                    0,
                    0,
                    getWidth() - 1,
                    getHeight() - 1,
                    22,
                    22
            );

            g2.dispose();
        }
    }
}
