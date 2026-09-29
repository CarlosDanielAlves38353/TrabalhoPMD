
package TrabalhoPMD.visao;

import java.awt.Color;
import java.awt.Font;


public class FrameMenuAdmin extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrameMenuAdmin.class.getName());

   
    public FrameMenuAdmin() {
        initComponents();
        jPanel1.setBackground(new Color(201, 214, 255));
        Labeltitulo();
        DesingBotUsuario();
        DesingBotSkin();
        DesingBotaval();
        DesingBotCol();
        DesingBotEx();
        DesingBotCol();
        DesingBotAdm();
    }
private void Labeltitulo(){
          TituloAdm.setFont(new Font("Arial", Font.BOLD, 30));
          TituloAdm.setForeground(Color.white);
          TituloAdm.setText("Entrar na conta");
}
   private void DesingBotUsuario(){
      ButUsuario.setText("Gerenciar Usuários");
      ButUsuario.setBackground(Color.white);
      ButUsuario.setFont(new Font("Arial", Font.BOLD, 16));
      ButUsuario.setBorderPainted(false);
      ButUsuario.setFocusPainted(false);
   
   }
    private void DesingBotSkin(){
      Butskin.setText("Gerenciar Skins");
      Butskin.setBackground(Color.white);
      Butskin.setFont(new Font("Arial", Font.BOLD, 16));
      Butskin.setBorderPainted(false);
      Butskin.setFocusPainted(false);
   }
       private void DesingBotaval(){
      Butavali.setText("Gerenciar Avaliações");
      Butavali.setBackground(Color.white);
      Butavali.setFont(new Font("Arial", Font.BOLD, 16));
      Butavali.setBorderPainted(false);
      Butavali.setFocusPainted(false);
   }
      private void DesingBotCol(){
      Butcol.setText("Gerenciar Coleções");
      Butcol.setBackground(Color.white);
      Butcol.setFont(new Font("Arial", Font.BOLD, 16));
      Butcol.setBorderPainted(false);
      Butcol.setFocusPainted(false);
   }
      
       private void DesingBotEx(){
      Butexplo.setText("Explorar Skins");
      Butexplo.setBackground(Color.white);
      Butexplo.setFont(new Font("Arial", Font.BOLD, 16));
      Butexplo.setBorderPainted(false);
      Butexplo.setFocusPainted(false);
   }
      private void DesingBotAdm(){
      Butcriaradm.setText("Criar administrador");
      Butcriaradm.setBackground(Color.white);
      Butcriaradm.setFont(new Font("Arial", Font.BOLD, 16));
      Butcriaradm.setBorderPainted(false);
      Butcriaradm.setFocusPainted(false);
   }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TituloCadastro = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        TituloAdm = new javax.swing.JLabel();
        Butexplo = new javax.swing.JButton();
        Butavali = new javax.swing.JButton();
        Butcol = new javax.swing.JButton();
        ButUsuario = new javax.swing.JButton();
        Butskin = new javax.swing.JButton();
        Butcriaradm = new javax.swing.JButton();

        TituloCadastro.setText("jLabel1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TituloAdm.setText("jLabel1");

        Butexplo.setText("jButton1");
        Butexplo.addActionListener(this::ButexploActionPerformed);

        Butavali.setText("jButton1");

        Butcol.setText("jButton1");

        ButUsuario.setText("jButton1");

        Butskin.setText("jButton1");
        Butskin.addActionListener(this::ButskinActionPerformed);

        Butcriaradm.setText("jButton1");
        Butcriaradm.addActionListener(this::ButcriaradmActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Butexplo, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Butcol, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(150, 150, 150))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(Butavali, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(101, 101, 101)
                        .addComponent(Butcriaradm, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(323, 323, 323)
                        .addComponent(TituloAdm, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(150, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(125, 125, 125)
                    .addComponent(ButUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(504, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(127, 127, 127)
                    .addComponent(Butskin, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(502, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(TituloAdm, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51)
                .addComponent(Butcol, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(Butexplo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(70, 70, 70)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Butavali, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Butcriaradm, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(134, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(135, 135, 135)
                    .addComponent(ButUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(372, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(242, 242, 242)
                    .addComponent(Butskin, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(265, Short.MAX_VALUE)))
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

    private void ButexploActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButexploActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButexploActionPerformed

    private void ButskinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButskinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButskinActionPerformed

    private void ButcriaradmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButcriaradmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButcriaradmActionPerformed

   
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
        java.awt.EventQueue.invokeLater(() -> new FrameMenuAdmin().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButUsuario;
    private javax.swing.JButton Butavali;
    private javax.swing.JButton Butcol;
    private javax.swing.JButton Butcriaradm;
    private javax.swing.JButton Butexplo;
    private javax.swing.JButton Butskin;
    private javax.swing.JLabel TituloAdm;
    private javax.swing.JLabel TituloCadastro;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
