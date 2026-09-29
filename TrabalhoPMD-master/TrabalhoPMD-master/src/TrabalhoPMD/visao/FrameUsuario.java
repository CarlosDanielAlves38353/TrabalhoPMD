
package TrabalhoPMD.visao;

import java.awt.Color;
import java.awt.Font;


public class FrameUsuario extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrameUsuario.class.getName());

    public FrameUsuario() {
        initComponents();
         jPanel1.setBackground(new Color(201, 214, 255));
         Labeltitulo();
         DesingBotInserir();
         DesingBotALter();
         DesingBotApag();
         DesingBotID();
         DesingBotTds();
         DesingBotVoltar();
    }

  private void Labeltitulo(){
          TituloUsuario.setFont(new Font("Arial", Font.BOLD, 30));
          TituloUsuario.setForeground(Color.white);
          TituloUsuario.setText("Entrar na conta");
}
  
    private void DesingBotInserir(){
      ButIns.setText("Inserir");
      ButIns.setBackground(Color.white);
      ButIns.setFont(new Font("Arial", Font.BOLD, 16));
      ButIns.setBorderPainted(false);
      ButIns.setFocusPainted(false);
   
   }
     private void DesingBotALter(){
      ButAlt.setText("Alterar");
      ButAlt.setBackground(Color.white);
      ButAlt.setFont(new Font("Arial", Font.BOLD, 16));
      ButAlt.setBorderPainted(false);
      ButAlt.setFocusPainted(false);
   
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
        ButAlt = new javax.swing.JButton();
        ButVoltar = new javax.swing.JButton();
        ButApag = new javax.swing.JButton();
        ButID = new javax.swing.JButton();
        ButTDS = new javax.swing.JButton();
        TituloUsuario = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        ButIns.setText("jButton1");

        ButAlt.setText("jButton1");
        ButAlt.addActionListener(this::ButAltActionPerformed);

        ButVoltar.setText("jButton1");

        ButApag.setText("jButton1");

        ButID.setText("jButton1");
        ButID.addActionListener(this::ButIDActionPerformed);

        ButTDS.setText("jButton1");

        TituloUsuario.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ButAlt, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 153, Short.MAX_VALUE)
                        .addComponent(ButID, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ButIns, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(ButApag, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(124, 124, 124))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(ButVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(289, 289, 289)
                        .addComponent(ButTDS, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(311, 311, 311)
                        .addComponent(TituloUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(TituloUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButIns, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButApag, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButAlt, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButID, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addComponent(ButTDS, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addComponent(ButVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
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

    private void ButAltActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButAltActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButAltActionPerformed

    private void ButIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButIDActionPerformed

  
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
        java.awt.EventQueue.invokeLater(() -> new FrameUsuario().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButAlt;
    private javax.swing.JButton ButApag;
    private javax.swing.JButton ButID;
    private javax.swing.JButton ButIns;
    private javax.swing.JButton ButTDS;
    private javax.swing.JButton ButVoltar;
    private javax.swing.JLabel TituloUsuario;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
