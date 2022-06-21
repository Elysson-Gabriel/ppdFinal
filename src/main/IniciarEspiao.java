/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.swing.JOptionPane;
import org.apache.activemq.ActiveMQConnectionFactory;
import pages.ChatEspiao;

/**
 *
 * @author elysson
 */
public class IniciarEspiao extends javax.swing.JFrame {
    
    private ServerSocket serverSocket;
    private boolean espiaoEntrou;
    private String msg;
    private Publisher publisher = null;
    
    /**
     * Creates new form UsuarioCadastro
     * @throws java.lang.ClassNotFoundException
     */
    public IniciarEspiao() throws ClassNotFoundException {
        initComponents();
        
        System.out.println("----- Server Socket -----");
        espiaoEntrou = false;
        
    }
    
    public void permiteConexoes(int porta) throws ClassNotFoundException{
        try{
            System.out.println("Aguardando espião...");
            
            while(!espiaoEntrou){
                
                new ChatEspiao(porta).setVisible(true);
                
                //Aguarda espião se conectar ao server
                Socket socketEspiao = serverSocket.accept();
                this.dispose();
                System.out.println("Espião se conectou!");
                espiaoEntrou = true;
                
                //Define thread espião que ficará executando
                ConexaoServidor espiao = new ConexaoServidor(socketEspiao);

                Thread threadEspiao = new Thread(espiao);
                threadEspiao.start();

                //Armazena nos atributos locais onde está cada conexão
                //espiaoCon = espiao;
            }

            //Não aceita mais conexões
            System.out.println("O espião já se conectou.");
            
        } catch (IOException ex) {
            System.out.println("Erro em permiteConexoes()");
        }
    }
    
    private class ConexaoServidor implements Runnable {
    
        private Socket socket;
        private DataInputStream entrada;
        private DataOutputStream saida;

        public ConexaoServidor(Socket socket){
            this.socket = socket;

            try {
                entrada = new DataInputStream(socket.getInputStream());
                saida = new DataOutputStream(socket.getOutputStream());
            } catch (IOException ex) {
                System.out.println("Erro no run do socket");
            }
        }

        public void run(){
            try{
                //Execução da thread
                while(true){
                    msg = entrada.readUTF();
                    //System.out.println(msg);
                    enviaMsgTopico(msg);
                }

            } catch (IOException ex) {
                System.out.println("Erro no run do jogador");
                System.exit(0);
            }
        }
        
        public void fechaConexao(){
           try{
                socket.close();
                System.out.println("-----CONEXÃO ENCERRADA-----");
            } catch (IOException ex) {
                System.out.println("Erro no fechaConexao() do Servidor");
            } 
        }
    
    }
    
    private class Publisher {
        /*
         * URL do servidor JMS. DEFAULT_BROKER_URL indica que o servidor está em localhost
        */	

        //private static final String url = ActiveMQConnection.DEFAULT_BROKER_URL;

        private Session session;
        private MessageProducer publisher;
        private Connection connection;

        public Publisher(String topicName, String ip) throws Exception{
            /*
             * Estabelecendo conexão com o Servidor JMS
            */		
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("failover://tcp://" + 
                    ip + ":61616");
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
            * Criando Produtor
            */
            MessageProducer publisher = session.createProducer(dest);

            this.connection = connection;
            this.session = session;
            this.publisher = publisher;

            connection.start();
        }

        public void enviaMensagem(String s)throws JMSException{
            TextMessage msg = session.createTextMessage();
 
            msg.setText(s);

            /*
             * Publicando Mensagem
             */
            publisher.send(msg);
        }

        public void close() throws JMSException{
            publisher.close();
            session.close();
            connection.close();
        }
    
    }
    
    public void enviaMsgTopico(String msg){
   
        try {
            this.publisher.enviaMensagem(msg);
        } catch (JMSException ex) {
            Logger.getLogger(IniciarEspiao.class.getName()).log(Level.SEVERE, null, ex);
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
        jButtonCriar = new javax.swing.JButton();
        jLabelNome1 = new javax.swing.JLabel();
        jTextFieldBroker = new javax.swing.JTextField();
        jLabelNome2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSpinnerPorta = new javax.swing.JSpinner();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanelPrincipal.setBackground(new java.awt.Color(241, 241, 241));

        jLabelTitulo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("Configurações para o Espião");

        jButtonCriar.setText("Iniciar");
        jButtonCriar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCriarActionPerformed(evt);
            }
        });

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

        jLabelNome2.setText("Porta Server Socket:");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/espiao.png"))); // NOI18N

        jSpinnerPorta.setModel(new javax.swing.SpinnerNumberModel(51734, 0, 65536, 1));

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelNome1)
                            .addComponent(jLabelNome2))
                        .addGap(18, 18, 18)
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldBroker, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSpinnerPorta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(56, 56, 56))
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(jLabelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)))
                .addComponent(jLabel1)
                .addContainerGap())
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jButtonCriar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelPrincipalLayout.setVerticalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addGap(11, 11, 11)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNome2)
                    .addComponent(jSpinnerPorta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNome1)
                    .addComponent(jTextFieldBroker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButtonCriar)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCriarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCriarActionPerformed
        int porta = (int) jSpinnerPorta.getValue();
        String ip = jTextFieldBroker.getText();
        
        if(ip != null && !ip.isEmpty()){
            
            try {
                serverSocket = new ServerSocket(porta);
                publisher = new Publisher("MOM-ppdFinal", ip);
                this.permiteConexoes(porta);
            } catch (IOException ex) {
                System.out.println("Erro no construtor do servidor");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(IniciarEspiao.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(IniciarEspiao.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }else{
            JOptionPane.showMessageDialog(this, "Preencha o IP do Broker", 
                    "Tente novamente!", JOptionPane.ERROR_MESSAGE);
        }
 
    }//GEN-LAST:event_jButtonCriarActionPerformed

    private void jTextFieldBrokerFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldBrokerFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBrokerFocusGained

    private void jTextFieldBrokerKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldBrokerKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBrokerKeyReleased

    private void jTextFieldBrokerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBrokerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBrokerActionPerformed

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
            java.util.logging.Logger.getLogger(IniciarEspiao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IniciarEspiao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IniciarEspiao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IniciarEspiao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    new IniciarEspiao().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(IniciarEspiao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCriar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelNome1;
    private javax.swing.JLabel jLabelNome2;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JSpinner jSpinnerPorta;
    private javax.swing.JTextField jTextFieldBroker;
    // End of variables declaration//GEN-END:variables
}
