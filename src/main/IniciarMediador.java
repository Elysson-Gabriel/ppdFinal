/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package main;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.DefaultListModel;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * @author elysson
 */
public class IniciarMediador extends javax.swing.JFrame {

    /**
     * Creates new form IniciarMediador
     * @throws java.lang.Exception
     */
    public IniciarMediador() throws Exception{
        initComponents();
    }
    
    private class Subscriber implements MessageListener{
        /*
         * URL do servidor JMS. DEFAULT_BROKER_URL indica que o servidor está em localhost
        */	

        //private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;

        private Session session;
        private MessageConsumer subscriber;
        private Connection connection;

        public Subscriber(String topicName) throws Exception{
            /*
             * Estabelecendo conexão com o Servidor JMS
            */		
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("failover://tcp://" 
                    + jTextFieldBroker.getText() + ":61616");
            Connection connection = connectionFactory.createConnection();
            connection.start();

            /*
            * Criando Session 
            */
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            /*
            * Criando Topic
            */ 
            Destination dest = session.createTopic(topicName);

            /*
            * Criando Consumidor
            */
            MessageConsumer subscriber = session.createConsumer(dest);

            this.connection = connection;
            this.session = session;
            this.subscriber = subscriber;
            
            jTextArea.setEnabled(true);
            jButtonIniciar.setEnabled(false);
        }

        public void Go(){

            try{
                /*
                * Setando Listener
                */
                subscriber.setMessageListener(this);

            }catch(Exception e){
                e.printStackTrace();
            }       
        }

        @Override
        public void onMessage(Message message) {
            if(message instanceof TextMessage){
                try{
                    TextMessage obj = (TextMessage) message;
                    String msg = (String) obj.getText();
                    
                    jTextArea.setText(jTextArea.getText() + "\n" + msg);
                    jTextArea.setCaretPosition(jTextArea.getText().length());
                }catch(Exception e){
                }
            }
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

        jPanelPrincipal = new javax.swing.JPanel();
        jLabelTitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea = new javax.swing.JTextArea();
        jLabelUnd1 = new javax.swing.JLabel();
        jLabelNome1 = new javax.swing.JLabel();
        jTextFieldBroker = new javax.swing.JTextField();
        jButtonIniciar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);

        jPanelPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        jLabelTitulo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("--- Mediador ---");

        jTextArea.setEditable(false);
        jTextArea.setColumns(15);
        jTextArea.setRows(5);
        jTextArea.setEnabled(false);
        jScrollPane1.setViewportView(jTextArea);

        jLabelUnd1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelUnd1.setText("Mensagens suspeitas recebidas do Espião:");

        jLabelNome1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabelNome1.setText("IP Broker (Active MQ):");

        jTextFieldBroker.setText("127.0.0.1");
        jTextFieldBroker.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldBrokerFocusGained(evt);
            }
        });
        jTextFieldBroker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBrokerActionPerformed(evt);
            }
        });
        jTextFieldBroker.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldBrokerKeyReleased(evt);
            }
        });

        jButtonIniciar.setText("Iniciar");
        jButtonIniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonIniciarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPrincipalLayout.createSequentialGroup()
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jLabelNome1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                .addComponent(jLabelUnd1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldBroker, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonIniciar))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(220, 220, 220))
        );
        jPanelPrincipalLayout.setVerticalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelUnd1))
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(jLabelNome1)
                        .addGap(4, 4, 4)
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldBroker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonIniciar))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addGap(6, 6, 6))
        );

        getContentPane().add(jPanelPrincipal);
        jPanelPrincipal.setBounds(0, 0, 410, 310);

        setBounds(0, 0, 425, 351);
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldBrokerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldBrokerFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBrokerFocusGained

    private void jTextFieldBrokerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBrokerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBrokerActionPerformed

    private void jTextFieldBrokerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBrokerKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBrokerKeyReleased

    private void jButtonIniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonIniciarActionPerformed
        try {
            new Subscriber("MOM").Go();
        } catch (Exception ex) {
            Logger.getLogger(IniciarMediador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButtonIniciarActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IniciarMediador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IniciarMediador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IniciarMediador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IniciarMediador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new IniciarMediador().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(IniciarMediador.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonIniciar;
    private javax.swing.JLabel jLabelNome1;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JLabel jLabelUnd1;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea;
    private javax.swing.JTextField jTextFieldBroker;
    // End of variables declaration//GEN-END:variables
}
