package TrabalhoPMD.visao;

import TrabalhoPMD.dados.BancoDados;
import TrabalhoPMD.dados.EntidadeDAO;
import TrabalhoPMD.dados.PersistenceException;
import TrabalhoPMD.modelo.Perfil;
import TrabalhoPMD.modelo.Usuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class FrameGerenciarUsuarios extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrameGerenciarUsuarios.class.getName());

    private Usuario usuarioLogado;

    private final EntidadeDAO<Usuario> daoUsuario
            = BancoDados.getInstancia().getDAO(Usuario.class);

    private JTable tabelaUsuarios;
    private DefaultTableModel modeloTabela;
    private TableRowSorter<DefaultTableModel> ordenadorTabela;

    private JTextField campoBusca;
    private JLabel labelTotal;

    private final Color AZUL = new Color(32, 25, 95);
    private final Color LAVANDA = new Color(201, 214, 255);
    private final Color FUNDO = new Color(246, 247, 252);
    private final Color BRANCO = Color.WHITE;

    public FrameGerenciarUsuarios() {
        this(null);
    }

    public FrameGerenciarUsuarios(Usuario usuarioLogado) {
        initComponents();

        this.usuarioLogado = usuarioLogado;

        setTitle("Skin Collection - Gerenciar usuários");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1000, 650);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);

        construirInterface();
        carregarUsuarios();
    }

    private void construirInterface() {

        JPanel principal = new JPanel(new BorderLayout(0, 20));
        principal.setBackground(FUNDO);
        principal.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Título
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setOpaque(false);

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));

        JLabel titulo = new JLabel("Gerenciar usuários");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 27));
        titulo.setForeground(AZUL);

        JLabel subtitulo = new JLabel("Consulte, cadastre e gerencie os usuários do sistema.");
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitulo.setForeground(new Color(110, 110, 125));

        textos.add(titulo);
        textos.add(javax.swing.Box.createVerticalStrut(6));
        textos.add(subtitulo);

        JButton botaoVoltar = criarBotao("Voltar", AZUL);
        botaoVoltar.addActionListener(e -> voltarAoMenu());

        cabecalho.add(textos, BorderLayout.CENTER);
        cabecalho.add(botaoVoltar, BorderLayout.EAST);

        // Barra de busca
        JPanel barraBusca = new JPanel(new BorderLayout(10, 0));
        barraBusca.setOpaque(false);

        JPanel buscaEsquerda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buscaEsquerda.setOpaque(false);

        JLabel labelBusca = new JLabel("ID do usuário:");
        labelBusca.setFont(new Font("SansSerif", Font.BOLD, 13));
        labelBusca.setForeground(AZUL);

        campoBusca = new JTextField(13);
        campoBusca.setFont(new Font("SansSerif", Font.PLAIN, 13));
        campoBusca.setPreferredSize(new Dimension(160, 36));
        campoBusca.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(220, 222, 235)),new EmptyBorder(5, 10, 5, 10)));

        JButton botaoBuscar = criarBotao("Buscar", AZUL);
        JButton botaoLimpar = criarBotao("Limpar", new Color(115, 120, 140));

        botaoBuscar.addActionListener(e -> buscarUsuario());
        botaoLimpar.addActionListener(e -> limparBusca());
        campoBusca.addActionListener(e -> buscarUsuario());

        buscaEsquerda.add(labelBusca);
        buscaEsquerda.add(campoBusca);
        buscaEsquerda.add(botaoBuscar);
        buscaEsquerda.add(botaoLimpar);

        JButton botaoNovo = criarBotao(" + Novo usuário", AZUL);
        botaoNovo.addActionListener(e -> novoUsuario());

        barraBusca.add(buscaEsquerda, BorderLayout.WEST);
        barraBusca.add(botaoNovo, BorderLayout.EAST);

        // Cabeçalho da tabela
        JPanel topoTabela = new JPanel(new BorderLayout());
        topoTabela.setOpaque(false);

        JLabel tituloTabela = new JLabel("Usuários cadastrados");
        tituloTabela.setFont(new Font("SansSerif", Font.BOLD, 16));
        tituloTabela.setForeground(AZUL);

        labelTotal = new JLabel("0 usuários");
        labelTotal.setFont(new Font("SansSerif", Font.PLAIN, 12));
        labelTotal.setForeground(new Color(115, 115, 130));

        topoTabela.add(tituloTabela, BorderLayout.WEST);
        topoTabela.add(labelTotal, BorderLayout.EAST);

        // Modelo da tabela
        modeloTabela = new DefaultTableModel(
                new Object[]{"ID", "Nome", "E-mail", "Perfil"}, 0
        ) {
            @Override
            public boolean isCellEditable(int linha, int coluna) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int coluna) {

                switch (coluna) {
                    case 0:
                        return Integer.class;

                    case 3:
                        return Perfil.class;

                    default:
                        return String.class;
                }
            }
        };

        tabelaUsuarios = new JTable(modeloTabela);
        tabelaUsuarios.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabelaUsuarios.setRowHeight(35);
        tabelaUsuarios.setSelectionBackground(LAVANDA);
        tabelaUsuarios.setSelectionForeground(AZUL);
        tabelaUsuarios.setGridColor(new Color(235, 235, 242));
        tabelaUsuarios.setShowVerticalLines(false);
        tabelaUsuarios.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION
        );

        tabelaUsuarios.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 13)
        );
        tabelaUsuarios.getTableHeader().setBackground(AZUL);
        tabelaUsuarios.getTableHeader().setForeground(BRANCO);
        tabelaUsuarios.getTableHeader().setPreferredSize(
                new Dimension(0, 38)
        );

        ordenadorTabela = new TableRowSorter<>(modeloTabela);
        tabelaUsuarios.setRowSorter(ordenadorTabela);
        tabelaUsuarios.setRowHeight(44);
        tabelaUsuarios.setFillsViewportHeight(true);
        tabelaUsuarios.setShowGrid(false);
        tabelaUsuarios.setIntercellSpacing(new Dimension(0, 1));
        tabelaUsuarios.setBackground(BRANCO);

        tabelaUsuarios.setFont(new Font("SansSerif", Font.PLAIN, 13));

        tabelaUsuarios.setSelectionBackground(new Color(230, 234, 255));

        tabelaUsuarios.setSelectionForeground(AZUL);

        tabelaUsuarios.setForeground(new Color(55, 57, 75));

        javax.swing.table.DefaultTableCellRenderer renderizador
                = new javax.swing.table.DefaultTableCellRenderer() {

            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable tabela,
                    Object valor,
                    boolean selecionado,
                    boolean foco,
                    int linha,
                    int coluna) {

                super.getTableCellRendererComponent(
                        tabela, valor, selecionado, foco, linha, coluna
                );

                if (!selecionado) { setBackground( linha % 2 == 0? Color.WHITE: new Color(248, 249, 255));

                    setForeground(new Color(55, 57, 75));
                }

                setBorder(new EmptyBorder(0, 12, 0, 12));

                if (coluna == 3 && valor instanceof Perfil) {
                    Perfil perfil = (Perfil) valor;
                    setText(perfil == Perfil.ADMIN ? "Administrador" : "Usuário");

                    setForeground(perfil == Perfil.ADMIN ? AZUL : new Color(105, 110, 135)
                    );

                    setFont(new Font("SansSerif", Font.BOLD, 12));
                }

                return this;
            }
        };

        for (int i = 0; i < tabelaUsuarios.getColumnCount(); i++) {
            tabelaUsuarios.getColumnModel()
                    .getColumn(i)
                    .setCellRenderer(renderizador);
        }

        JScrollPane scrollTabela = new JScrollPane(tabelaUsuarios);
        scrollTabela.setBorder(new LineBorder(new Color(230, 230, 240)));
        scrollTabela.getViewport().setBackground(BRANCO);

        // Cartão da tabela
        JPanel cartaoTabela = new PainelArredondado(BRANCO, new Color(230, 232, 242));
        cartaoTabela.setLayout(new BorderLayout(0, 15));
        cartaoTabela.setBorder(new EmptyBorder(22, 22, 22, 22));

        cartaoTabela.setBackground(BRANCO);
        cartaoTabela.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(230, 230, 240)), new EmptyBorder(18, 18, 18, 18)));

        cartaoTabela.add(topoTabela, BorderLayout.NORTH);
        cartaoTabela.add(scrollTabela, BorderLayout.CENTER);

        // Botões inferiores
        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rodape.setOpaque(false);

        JButton botaoEditar = criarBotao("Editar selecionado", AZUL);
        JButton botaoExcluir = criarBotao("Excluir selecionado", new Color(160, 55, 70));
        JButton botaoAtualizar = criarBotao("Atualizar tabela", new Color(115, 120, 140));

        botaoEditar.addActionListener(e -> editarUsuario());
        botaoExcluir.addActionListener(e -> excluirUsuario());
        botaoAtualizar.addActionListener(e -> {
            limparBusca();
            carregarUsuarios();
        });

        rodape.add(botaoEditar);
        rodape.add(botaoExcluir);
        rodape.add(botaoAtualizar);

        // Organização final
        JPanel topo = new JPanel(new BorderLayout(0, 18));
        topo.setOpaque(false);
        topo.add(cabecalho, BorderLayout.NORTH);
        topo.add(barraBusca, BorderLayout.SOUTH);

        principal.add(topo, BorderLayout.NORTH);
        principal.add(cartaoTabela, BorderLayout.CENTER);
        principal.add(rodape, BorderLayout.SOUTH);

        setContentPane(principal);
    }

    private JButton criarBotao(String texto, Color cor) {

        JButton botao = new JButton(texto);

        botao.setFont(new Font("SansSerif", Font.BOLD, 12));
        botao.setForeground(BRANCO);
        botao.setBackground(cor);
        botao.setFocusPainted(false);
        botao.setBorder(new EmptyBorder(10, 16, 10, 16));
        botao.setCursor(new java.awt.Cursor(
                java.awt.Cursor.HAND_CURSOR
        ));

        return botao;
    }

    private void carregarUsuarios() {

        modeloTabela.setRowCount(0);

        try {

            Usuario[] usuarios = daoUsuario.carregar();

            for (Usuario usuario : usuarios) {

                modeloTabela.addRow(new Object[]{
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getPerfil()
                });
            }

        } catch (PersistenceException e) {
            // O DAO lança essa exceção quando ainda não existem usuários.
            // Nesse caso, a tabela permanece vazia.
            //Teste 

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar usuários:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        labelTotal.setText(
                modeloTabela.getRowCount()
                + (modeloTabela.getRowCount() == 1
                ? " usuário"
                : " usuários")
        );

        modeloTabela.fireTableDataChanged();
    }

    private void buscarUsuario() {

        String texto = campoBusca.getText().trim();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite o ID que deseja buscar."
            );
            return;
        }

        try {

            int id = Integer.parseInt(texto);

            ordenadorTabela.setRowFilter(
                    RowFilter.regexFilter("^" + id + "$", 0)
            );

            if (tabelaUsuarios.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Nenhum usuário encontrado com esse ID."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "O ID precisa ser um número inteiro."
            );
        }
    }

    private void limparBusca() {

        campoBusca.setText("");
        ordenadorTabela.setRowFilter(null);
    }

    private void novoUsuario() {

        int id = proximoId();

        Usuario novo = mostrarFormulario(null, id);

        if (novo == null) {
            return;
        }

        if (emailJaExiste(novo.getEmail(), -1)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Já existe um usuário com esse e-mail."
            );
            return;
        }

        try {

            daoUsuario.salvar(novo);
            daoUsuario.persistir();

            carregarUsuarios();

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário cadastrado com sucesso!"
            );

        } catch (PersistenceException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao cadastrar usuário:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private int proximoId() {

        int maiorId = 0;

        try {

            Usuario[] usuarios = daoUsuario.carregar();

            for (Usuario usuario : usuarios) {
                if (usuario.getId() > maiorId) {
                    maiorId = usuario.getId();
                }
            }

        } catch (PersistenceException e) {
            // Nenhum usuário cadastrado ainda.
        }

        return maiorId + 1;
    }

    private boolean emailJaExiste(String email, int idIgnorado) {

        try {

            Usuario[] usuarios = daoUsuario.carregar();

            for (Usuario usuario : usuarios) {

                if (usuario.getId() != idIgnorado
                        && usuario.getEmail() != null
                        && usuario.getEmail().equalsIgnoreCase(email)) {

                    return true;
                }
            }

        } catch (PersistenceException e) {
            // Nenhum usuário cadastrado.
        }

        return false;
    }

    private Usuario mostrarFormulario(Usuario existente, int id) {

        JTextField campoNome = new JTextField(
                existente == null ? "" : existente.getNome()
        );

        JTextField campoEmail = new JTextField(
                existente == null ? "" : existente.getEmail()
        );

        JPasswordField campoSenha = new JPasswordField();

        JComboBox<Perfil> campoPerfil = new JComboBox<>(Perfil.values());

        if (existente == null) {
            campoPerfil.setSelectedItem(Perfil.USUARIO);
        } else {
            campoPerfil.setSelectedItem(existente.getPerfil());
        }

        JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));

        formulario.add(new JLabel("Nome:"));
        formulario.add(campoNome);

        formulario.add(new JLabel("E-mail:"));
        formulario.add(campoEmail);

        formulario.add(new JLabel(
                existente == null
                        ? "Senha:"
                        : "Nova senha (vazio mantém a atual):"
        ));
        formulario.add(campoSenha);

        formulario.add(new JLabel("Perfil:"));
        formulario.add(campoPerfil);

        int resultado = JOptionPane.showConfirmDialog(
                this,
                formulario,
                existente == null ? "Novo usuário" : "Editar usuário",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (resultado != JOptionPane.OK_OPTION) {
            return null;
        }

        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String senha = new String(campoSenha.getPassword());
        Perfil perfil = (Perfil) campoPerfil.getSelectedItem();

        if (nome.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Preencha o nome e o e-mail."
            );
            return null;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Digite um e-mail válido."
            );
            return null;
        }

        if (existente == null && senha.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Informe uma senha para o novo usuário."
            );
            return null;
        }

        if (existente != null && senha.isEmpty()) {
            senha = existente.getSenha();
        }

        return new Usuario(id, nome, email, senha, perfil);
    }

    private void editarUsuario() {

        int id = obterIdSelecionado();

        if (id == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um usuário na tabela."
            );
            return;
        }

        try {

            Usuario existente = daoUsuario.buscar(id);

            Usuario atualizado = mostrarFormulario(existente, id);

            if (atualizado == null) {
                return;
            }

            if (emailJaExiste(atualizado.getEmail(), id)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Esse e-mail já está sendo utilizado."
                );
                return;
            }

            daoUsuario.atualizar(atualizado);
            daoUsuario.persistir();

            carregarUsuarios();

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário atualizado com sucesso!"
            );

        } catch (PersistenceException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao atualizar usuário:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void excluirUsuario() {

        int id = obterIdSelecionado();

        if (id == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecione um usuário na tabela."
            );
            return;
        }

        if (usuarioLogado != null && usuarioLogado.getId() == id) {
            JOptionPane.showMessageDialog(
                    this,
                    "Você não pode excluir a própria conta enquanto está conectado."
            );
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(
                this,
                "Tem certeza que deseja excluir o usuário de ID " + id + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            daoUsuario.apagar(id);
            daoUsuario.persistir();

            carregarUsuarios();

            JOptionPane.showMessageDialog(
                    this,
                    "Usuário excluído com sucesso!"
            );

        } catch (PersistenceException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao excluir usuário:\n" + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private int obterIdSelecionado() {

        int linhaSelecionada = tabelaUsuarios.getSelectedRow();

        if (linhaSelecionada == -1) {
            return -1;
        }

        int linhaModelo = tabelaUsuarios.convertRowIndexToModel(
                linhaSelecionada
        );

        return (Integer) modeloTabela.getValueAt(linhaModelo, 0);
    }

    private void voltarAoMenu() {

        FrameMenuAdmin menu = new FrameMenuAdmin(usuarioLogado);

        menu.setLocationRelativeTo(null);
        menu.setVisible(true);

        this.dispose();
    }

    private class PainelArredondado extends JPanel {

        private final Color corFundo;
        private final Color corBorda;

        public PainelArredondado(Color fundo, Color borda) {
            this.corFundo = fundo;
            this.corBorda = borda;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(corFundo);
            g2.fillRoundRect(
                    0, 0, getWidth() - 1, getHeight() - 1,
                    20, 20
            );

            g2.setColor(corBorda);
            g2.drawRoundRect(
                    0, 0, getWidth() - 1, getHeight() - 1,
                    20, 20
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 861, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 853, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrameGerenciarUsuarios().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
