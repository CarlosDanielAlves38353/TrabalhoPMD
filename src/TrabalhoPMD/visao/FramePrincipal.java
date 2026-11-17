package TrabalhoPMD.visao;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class FramePrincipal extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FramePrincipal.class.getName());

    public FramePrincipal() {

        initComponents();

        setTitle("Skin Collection");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        setMinimumSize(new Dimension(900, 600));
        setSize(1100, 700);

        setContentPane(construirInterface());

        setLocationRelativeTo(null);
    }

    private JPanel construirInterface() {

        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(new Color(246, 247, 252));

        // =====================================
        // PAINEL ESQUERDO - IDENTIDADE
        // =====================================
        JPanel painelMarca = new PainelGradiente(new Color(32, 25, 95), new Color(78, 67, 145));

        painelMarca.setPreferredSize(new Dimension(390, 0));
        painelMarca.setLayout(new BoxLayout(painelMarca, BoxLayout.Y_AXIS));

        painelMarca.setBorder(new EmptyBorder(65, 50, 45, 50));

        JLabel marca = new JLabel("SKIN COLLECTION");

        marca.setFont(new Font("SansSerif", Font.BOLD, 18));
        marca.setForeground(new Color(201, 214, 255));
        marca.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        //Adição de html para design de separação como uma mini pag
        JLabel tituloMarca = new JLabel("<html>Sua coleção,<br>seu estilo.</html>"); 

        tituloMarca.setFont(new Font("SansSerif", Font.BOLD, 39));

        tituloMarca.setForeground(Color.WHITE);
        tituloMarca.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel detalhe = new JPanel();
        detalhe.setBackground(new Color(201, 214, 255));
        detalhe.setMaximumSize(new Dimension(65, 5));
        detalhe.setPreferredSize(new Dimension(65, 5));
        detalhe.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descricaoMarca = new JLabel(
                "<html><div style='width:260px;'>"
                + "Organize suas skins, acompanhe sua coleção "
                + "e descubra tudo o que faz parte do seu estilo."
                + "</div></html>"
        );

        descricaoMarca.setFont(new Font("SansSerif", Font.PLAIN, 15));

        descricaoMarca.setForeground(new Color(225, 225, 245));

        descricaoMarca.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel rodapeMarca = new JLabel("ORGANIZE  •  EXPLORE  •  COLECIONE");

        rodapeMarca.setFont(new Font("SansSerif", Font.BOLD, 10));

        rodapeMarca.setForeground(new Color(201, 214, 255));

        rodapeMarca.setAlignmentX(Component.LEFT_ALIGNMENT);

        painelMarca.add(marca);
        painelMarca.add(Box.createVerticalStrut(95));
        painelMarca.add(tituloMarca);
        painelMarca.add(Box.createVerticalStrut(25));
        painelMarca.add(detalhe);
        painelMarca.add(Box.createVerticalStrut(25));
        painelMarca.add(descricaoMarca);
        painelMarca.add(Box.createVerticalGlue());
        painelMarca.add(rodapeMarca);

        // =====================================
        // PAINEL DIREITO - BOAS-VINDAS
        // =====================================
        JPanel painelDireito = new JPanel(new GridBagLayout());
        painelDireito.setBackground(new Color(246, 247, 252));

        PainelArredondado cartao = new PainelArredondado(Color.WHITE, new Color(230, 232, 242));

        cartao.setLayout(new BoxLayout(cartao,BoxLayout.Y_AXIS));

        cartao.setBorder(new EmptyBorder(45, 42, 45, 42));

        cartao.setPreferredSize(new Dimension(460, 445));

        // Logo
        JLabel logo = new JLabel("Nx", SwingConstants.CENTER); //Nexora ou Vanta, colocar na logo uma imagem

        logo.setFont(new Font("SansSerif", Font.BOLD, 21));
        logo.setForeground(new Color(32, 25, 95));
        logo.setOpaque(true);
        logo.setBackground(new Color(201, 214, 255));
        logo.setPreferredSize(new Dimension(58, 58));
        logo.setMaximumSize(new Dimension(58, 58));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Título
        JLabel titulo = new JLabel("Bem-vindo ao Skin Collection");

        titulo.setFont(new Font("SansSerif", Font.BOLD, 23));
        titulo.setForeground(new Color(32, 25, 95));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtítulo
        JLabel subtitulo = new JLabel(
                "<html><div style='text-align:center;width:300px;'>"
                + "Entre na sua conta ou crie um cadastro "
                + "para começar a organizar sua coleção."
                + "</div></html>"
        );

        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));

        subtitulo.setForeground(new Color(120, 120, 140));

        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Botões
        BotaoArredondado botaoEntrar = new BotaoArredondado(
                "Entrar na conta",
                new Color(32, 25, 95),
                new Color(55, 47, 125),
                Color.WHITE,
                null
        );

        BotaoArredondado botaoCriar = new BotaoArredondado(
                "Criar minha conta",
                Color.WHITE,
                new Color(245, 246, 255),
                new Color(32, 25, 95),
                new Color(201, 214, 255)
        );

        botaoEntrar.addActionListener(e -> abrirLogin());
        botaoCriar.addActionListener(e -> abrirCadastro());

        // Rodapé do cartão
        JLabel rodape = new JLabel(
                "SUA COLEÇÃO COMEÇA AQUI"
        );

        rodape.setFont(
                new Font("SansSerif", Font.BOLD, 10)
        );

        rodape.setForeground(
                new Color(150, 150, 170)
        );

        rodape.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Organização do cartão
        cartao.add(logo);
        cartao.add(Box.createVerticalStrut(25));
        cartao.add(titulo);
        cartao.add(Box.createVerticalStrut(12));
        cartao.add(subtitulo);
        cartao.add(Box.createVerticalStrut(38));

        cartao.add(botaoEntrar);
        cartao.add(Box.createVerticalStrut(15));
        cartao.add(botaoCriar);

        cartao.add(Box.createVerticalGlue());
        cartao.add(rodape);

        painelDireito.add(cartao);

        principal.add(painelMarca, BorderLayout.WEST);
        principal.add(painelDireito, BorderLayout.CENTER);

        return principal;
    }

    private void abrirLogin() {

        FrameEntrar tela = new FrameEntrar();

        tela.setLocationRelativeTo(null);
        tela.setVisible(true);

        this.dispose();
    }

    private void abrirCadastro() {

        FrameCadastro tela = new FrameCadastro();

        tela.setLocationRelativeTo(null);
        tela.setVisible(true);

        this.dispose();
    }

    private class PainelGradiente extends JPanel {

        private Color corInicial;
        private Color corFinal;

        public PainelGradiente(Color inicial, Color finalCor) {

            this.corInicial = inicial;
            this.corFinal = finalCor;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            GradientPaint degradê = new GradientPaint(0, 0, corInicial, getWidth(), getHeight(), corFinal);

            g2.setPaint(degradê);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.dispose();

            super.paintComponent(g);
        }
    }

    private class PainelArredondado extends JPanel {

        private Color fundo;
        private Color borda;

        public PainelArredondado(Color fundo, Color borda) {

            this.fundo = fundo;
            this.borda = borda;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(fundo);

            g2.fillRoundRect(
                    0, 0, getWidth() - 1, getHeight() - 1,
                    24, 24
            );

            g2.setColor(borda);

            g2.drawRoundRect(
                    0, 0, getWidth() - 1, getHeight() - 1,
                    24, 24
            );

            g2.dispose();
        }
    }

    private class BotaoArredondado extends JButton {

        private Color cor;
        private Color corHover;
        private Color corBorda;

        public BotaoArredondado(
                String texto,
                Color cor,
                Color corHover,
                Color textoCor,
                Color corBorda) {

            super(texto);

            this.cor = cor;
            this.corHover = corHover;
            this.corBorda = corBorda;

            setFont(new Font("SansSerif", Font.BOLD, 13));
            setForeground(textoCor);

            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);

            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setBorder(new EmptyBorder(15, 20, 15, 20));

            setAlignmentX(Component.CENTER_ALIGNMENT);

            setMaximumSize(new Dimension(340, 52));
            setPreferredSize(new Dimension(340, 52));
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            Color corAtual = cor;

            if (getModel().isPressed()) {
                corAtual = corHover.darker();
            } else if (getModel().isRollover()) {
                corAtual = corHover;
            }

            g2.setColor(corAtual);

            g2.fillRoundRect(
                    0, 0, getWidth() - 1, getHeight() - 1,
                    14, 14
            );

            if (corBorda != null) {

                g2.setColor(corBorda);

                g2.drawRoundRect(
                        0, 0, getWidth() - 1, getHeight() - 1,
                        14, 14
                );
            }

            g2.dispose();

            super.paintComponent(g);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 878, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 565, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> new FramePrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
