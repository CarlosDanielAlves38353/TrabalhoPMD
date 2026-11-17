
package TrabalhoPMD.visao;

import java.awt.Color;
import java.awt.Font;
import TrabalhoPMD.modelo.Usuario;

public class FrameUsuarioMenu extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrameUsuarioMenu.class.getName());
private Usuario usuarioLogado;

    public FrameUsuarioMenu() {
    this(null);
}

public FrameUsuarioMenu(Usuario usuario) {

    initComponents();

    this.usuarioLogado = usuario;

    setTitle("Skin Collection - Usuário");
    setLocationRelativeTo(null);

    jPanel1.setBackground(new Color(201, 214, 255));

    Labeltitulo();
    DesingBotInserir();
    DesingBotALter();
    DesingBotApag();
    DesingBotID();
    DesingBotTds();
    DesingBotVoltar();
}

 private void Labeltitulo() {

    TituloUsuario.setFont(new Font("Arial", Font.BOLD, 30));
    TituloUsuario.setForeground(Color.white);

    if (usuarioLogado != null) {

        TituloUsuario.setText(
                "Olá, " + usuarioLogado.getNome()
        );

    } else {

        TituloUsuario.setText("Menu do Usuário");
    }
}
  
    private void DesingBotInserir(){
      ButIns.setText("Inserir");
      ButIns.setBackground(Color.white);
      ButIns.setFont(new Font("Arial", Font.BOLD, 16));
      ButIns.setBorderPainted(false);
      ButIns.setFocusPainted(false);
   
   }
     private void DesingBotALter(){
      CaixaBtn.setText("Alterar");
      CaixaBtn.setBackground(Color.white);
      CaixaBtn.setFont(new Font("Arial", Font.BOLD, 16));
      CaixaBtn.setBorderPainted(false);
      CaixaBtn.setFocusPainted(false);
   
   }
       private void DesingBotApag(){
      ButApag.setText("Apagar");
      ButApag.setBackground(Color.white);
      ButApag.setFont(new Font("Arial", Font.BOLD, 16));
      ButApag.setBorderPainted(false);
      ButApag.setFocusPainted(false);
   
   }
      private void DesingBotID(){
      ButID.setText("Visualizar por ID");
      ButID.setBackground(Color.white);
      ButID.setFont(new Font("Arial", Font.BOLD, 16));
      ButID.setBorderPainted(false);
      ButID.setFocusPainted(false);
   
   }
        private void DesingBotTds(){
      ButTDS.setText("Visualizar todos os Usuários");
      ButTDS.setBackground(Color.white);
      ButTDS.setFont(new Font("Arial", Font.BOLD, 16));
      ButTDS.setBorderPainted(false);
      ButTDS.setFocusPainted(false);
   
   }
       private void DesingBotVoltar(){
      ButVoltar.setText("Voltar");
      ButVoltar.setBackground(Color.white);
      ButVoltar.setFont(new Font("Arial", Font.BOLD, 16));
      ButVoltar.setBorderPainted(false);
      ButVoltar.setFocusPainted(false);
   
   }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        ButIns = new javax.swing.JButton();
        CaixaBtn = new javax.swing.JButton();
        ButVoltar = new javax.swing.JButton();
        ButApag = new javax.swing.JButton();
        ButID = new javax.swing.JButton();
        ButTDS = new javax.swing.JButton();
        TituloUsuario = new javax.swing.JLabel();
        ButAlt1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ButIns.setText("jButton1");

        CaixaBtn.setText("caixas");
        CaixaBtn.addActionListener(this::CaixaBtnActionPerformed);

        ButVoltar.setText("jButton1");

        ButApag.setText("jButton1");

        ButID.setText("jButton1");
        ButID.addActionListener(this::ButIDActionPerformed);

        ButTDS.setText("jButton1");

        TituloUsuario.setText("jLabel1");

        ButAlt1.setText("jButton1");
        ButAlt1.addActionListener(this::ButAlt1ActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(ButVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(311, 311, 311)
                        .addComponent(TituloUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(346, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ButIns, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ButApag, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(ButID, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(124, 124, 124))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ButTDS, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CaixaBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(132, 132, 132))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(112, 112, 112)
                    .addComponent(ButAlt1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(518, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(TituloUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ButIns, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ButApag, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(71, 71, 71)
                        .addComponent(ButID, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(ButTDS, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(CaixaBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(86, 86, 86)))
                .addComponent(ButVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(253, 253, 253)
                    .addComponent(ButAlt1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(255, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CaixaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CaixaBtnActionPerformed
        // TODO add your handling code here:
         TelaCaixasVisual tela = new TelaCaixasVisual(usuarioLogado);

        tela.setLocationRelativeTo(null);
        tela.setVisible(true);

        this.dispose();
    }//GEN-LAST:event_CaixaBtnActionPerformed

    private void ButIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButIDActionPerformed

    private void ButAlt1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAlt1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButAlt1ActionPerformed

  
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
        java.awt.EventQueue.invokeLater(() -> new FrameUsuarioMenu().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButAlt1;
    private javax.swing.JButton ButApag;
    private javax.swing.JButton ButID;
    private javax.swing.JButton ButIns;
    private javax.swing.JButton ButTDS;
    private javax.swing.JButton ButVoltar;
    private javax.swing.JButton CaixaBtn;
    private javax.swing.JLabel TituloUsuario;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
