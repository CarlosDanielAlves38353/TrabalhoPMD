
package TrabalhoPMD.visao;
import java.awt.Color;
import java.awt.Font;
public class FramePrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FramePrincipal.class.getName());


    public FramePrincipal() {
   
        initComponents();
             jPanel1.setBackground(new Color(201, 214, 255));
          AlterarbotaoCriar();
          Alterarbotaologin();
    }

private void AlterarbotaoCriar(){
      botaocriar.setText("Criar Conta");
      botaocriar.setBackground(Color.white);
      botaocriar.setFont(new Font("Arial", Font.BOLD, 24));
      botaocriar.setBorderPainted(false);
      botaocriar.setFocusPainted(false);
      botaocriar.setForeground(new Color(32, 25, 95));
}
private void Alterarbotaologin(){
      botaologin.setText("Entrar na conta");
      botaologin.setBackground(Color.white);
      botaologin.setFont(new Font("Arial", Font.BOLD, 24));
      botaologin.setBorderPainted(false);
      botaologin.setFocusPainted(false);
      botaologin.setForeground(new Color(32, 25, 95));
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        botaologin = new javax.swing.JButton();
        botaocriar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(201, 214, 255));

        botaologin.setText("jButton1");
        botaologin.addActionListener(this::botaologinActionPerformed);

        botaocriar.setText("jButton1");
        botaocriar.addActionListener(this::botaocriarActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(283, 283, 283)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botaologin, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botaocriar, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(320, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(146, Short.MAX_VALUE)
                .addComponent(botaocriar, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(135, 135, 135)
                .addComponent(botaologin, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(162, 162, 162))
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

    private void botaologinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaologinActionPerformed
        // TODO add your handling code here:
      
     
        
    }//GEN-LAST:event_botaologinActionPerformed

    private void botaocriarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaocriarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botaocriarActionPerformed

    public static void main(String args[]) {
      
        java.awt.EventQueue.invokeLater(() -> new FramePrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaocriar;
    private javax.swing.JButton botaologin;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
