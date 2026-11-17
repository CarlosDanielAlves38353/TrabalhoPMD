
package TrabalhoPMD.visao;
import java.awt.Color;
import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import TrabalhoPMD.dados.BancoDados;
import TrabalhoPMD.dados.EntidadeDAO;
import TrabalhoPMD.dados.PersistenceException;
import TrabalhoPMD.modelo.Usuario;
import TrabalhoPMD.modelo.Perfil;

import javax.swing.JOptionPane;

public class FrameCadastro extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrameCadastro.class.getName());

   
    public FrameCadastro() {
        initComponents();
          jPanel1.setBackground(new Color(201, 214, 255));
          Labeltitulo();
          Labelemail();
          LabelNome();
          labelsenha();
    }
private void Labeltitulo() {

    TituloCadastro.setFont(
            new Font("Arial", Font.BOLD, 24)
    );

    TituloCadastro.setForeground(Color.white);
    TituloCadastro.setText("Criar conta");
}

 private void LabelNome(){
          LabelNome.setFont(new Font("Arial", Font.BOLD, 24));
          LabelNome.setForeground(Color.white);
          LabelNome.setText("Nome:");
 }
    private void Labelemail(){
          LabelEmail.setFont(new Font("Arial", Font.BOLD, 24));
          LabelEmail.setForeground(Color.white);
          LabelEmail.setText("Email:");
    }
    private void labelsenha(){
          LabelSenha.setFont(new Font("Arial", Font.BOLD, 24));
          LabelSenha.setForeground(Color.white);
          LabelSenha.setText("Senha:");
    }
    
    private void cadastrarUsuario() {

    String nome = jTextField3.getText().trim();
    String email = jTextField1.getText().trim();

    String senha = new String(
            jPasswordField1.getPassword()
    );

    // Verifica campos vazios
    if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Preencha todos os campos.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    // Verifica formato básico do e-mail
    if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {

        JOptionPane.showMessageDialog(
                this,
                "Digite um e-mail válido.",
                "Atenção",
                JOptionPane.WARNING_MESSAGE
        );

        return;
    }

    BancoDados banco = BancoDados.getInstancia();

    EntidadeDAO<Usuario> daoUsuario =
            banco.getDAO(Usuario.class);

    try {

        Usuario[] usuarios;

        // Carrega os usuários existentes
        try {

            usuarios = daoUsuario.carregar();

        } catch (PersistenceException e) {

            // Conjunto vazio significa que ainda não existem usuários.
            if ("CARREGAR TODOS".equals(e.getOperacao())) {

                usuarios = new Usuario[0];

            } else {
                throw e;
            }
        }

        int maiorId = 0;
        boolean existeAdmin = false;

        // Verifica e-mail duplicado e identifica administradores
        for (Usuario usuario : usuarios) {

            if (usuario.getEmail() != null
                    && usuario.getEmail().equalsIgnoreCase(email)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Este e-mail já está cadastrado.",
                        "Cadastro",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            if (usuario.getId() > maiorId) {
                maiorId = usuario.getId();
            }

            if (usuario.getPerfil() == Perfil.ADMIN) {
                existeAdmin = true;
            }
        }

        // Primeiro usuário será administrador.
        Perfil perfil = existeAdmin
                ? Perfil.USUARIO
                : Perfil.ADMIN;

        // Cria o usuário com ID automático
        Usuario usuario = new Usuario(
                maiorId + 1,
                nome,
                email,
                senha,
                perfil
        );

        // Salva no conjunto
        daoUsuario.salvar(usuario);

        JOptionPane.showMessageDialog(
                this,
                "Conta criada com sucesso!\n"
                + "Seu ID: " + usuario.getId()
                + "\nPerfil: " + perfil,
                "Cadastro realizado",
                JOptionPane.INFORMATION_MESSAGE
        );

        // Limpa os campos
        jTextField3.setText("");
        jTextField1.setText("");
        jPasswordField1.setText("");

    } catch (PersistenceException e) {

        JOptionPane.showMessageDialog(
                this,
                "Não foi possível cadastrar o usuário:\n"
                + e.getMessage(),
                "Erro de persistência",
                JOptionPane.ERROR_MESSAGE
        );
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

        TituloEntrar = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        TituloCadastro = new javax.swing.JLabel();
        LabelNome = new javax.swing.JLabel();
        LabelEmail = new javax.swing.JLabel();
        LabelSenha = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        Voltar = new javax.swing.JButton();
        CriarConta = new javax.swing.JButton();

        TituloEntrar.setText("jLabel1");

        jLabel3.setText("jLabel3");

        jLabel1.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(880, 665));

        TituloCadastro.setText("Entrar na Conta");

        LabelNome.setText("Nome");

        LabelEmail.setText("Email");

        LabelSenha.setText("Senha");

        jTextField1.addActionListener(this::jTextField1ActionPerformed);

        jTextField3.addActionListener(this::jTextField3ActionPerformed);

        jPasswordField1.addActionListener(this::jPasswordField1ActionPerformed);

        Voltar.setText("Voltar");
        Voltar.addActionListener(this::VoltarActionPerformed);

        CriarConta.setText("Criar Conta");
        CriarConta.addActionListener(this::CriarContaActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(254, 254, 254)
                        .addComponent(TituloCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(LabelEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelNome, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(101, 101, 101)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
                            .addComponent(jTextField3)
                            .addComponent(jTextField1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(Voltar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(221, 221, 221)
                        .addComponent(CriarConta, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(240, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(TituloCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelNome, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(59, 59, 59)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LabelSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(CriarConta, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(Voltar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void VoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VoltarActionPerformed
        // TODO add your handling code here:
        
        FramePrincipal tela = new FramePrincipal();

    tela.setLocationRelativeTo(null);
    tela.setVisible(true);

    this.dispose();
    }//GEN-LAST:event_VoltarActionPerformed

    private void CriarContaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CriarContaActionPerformed
        cadastrarUsuario();
    }//GEN-LAST:event_CriarContaActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrameCadastro().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CriarConta;
    private javax.swing.JLabel LabelEmail;
    private javax.swing.JLabel LabelNome;
    private javax.swing.JLabel LabelSenha;
    private javax.swing.JLabel TituloCadastro;
    private javax.swing.JLabel TituloEntrar;
    private javax.swing.JButton Voltar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
