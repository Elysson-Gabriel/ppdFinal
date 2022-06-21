/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pages;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import tuplas.Espiao;
import tuplas.Message;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import util.Lookup;

/**
 *
 * @author elysson
 */
public class ChatEspiao extends javax.swing.JFrame {
    
    private JavaSpace space;
    private Espiao espiao;
    private int msgAtualPub;
    private DefaultListModel palavras = null;
    private ConexaoEspiao clienteEspiao;
    private int porta;
    
    /**
     * Creates new form ChatEspiao
     */
    public ChatEspiao(){

    }
    
    public ChatEspiao(int porta) throws ClassNotFoundException {
        initComponents();
        
        this.porta = porta;
        System.out.println("Procurando pelo servico JavaSpace...");
        Lookup finder = new Lookup(JavaSpace.class);
        this.space = (JavaSpace) finder.getService();
        if (this.space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                System.exit(-1);
        } 
        System.out.println("O servico JavaSpace foi encontrado.");
        System.out.println(this.space);
        
        Espiao template = new Espiao();
        Espiao esp = null;
        
        try {
            esp = (Espiao) space.read(template, null, 500);
        } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException ex) {
            Logger.getLogger(ChatEspiao.class.getName()).log(Level.SEVERE, null, ex);
        }
        esp = null;//RMV
        if (esp == null) {
            esp = new Espiao();
            esp.qtdMsg = 0;
        }else{
            JOptionPane.showMessageDialog(this, "Só pode existir um espião.", 
                    "Espião já está na sala!", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        
        this.espiao = esp;
        
        try {
            this.space.write(this.espiao, null, Lease.FOREVER);
        } catch (TransactionException | RemoteException ex) {
            Logger.getLogger(ChatEspiao.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.msgAtualPub = this.espiao.qtdMsg + 1;
        palavras = new DefaultListModel();
        
        AtualizaChat chat = new AtualizaChat();
        chat.start();
        
        this.conectaServidor();
    }
    
    public void conectaServidor(){
        clienteEspiao = new ConexaoEspiao();
    }
    
    private class ConexaoEspiao {
    
        private Socket socket;
        private DataInputStream entrada;
        private DataOutputStream saida;
        
        public ConexaoEspiao(){
            System.out.println("----- Espião -----");

            try{
                //Se conecta via socket ao servidor
                socket = new Socket("localhost", porta);
                entrada = new DataInputStream(socket.getInputStream());
                saida = new DataOutputStream(socket.getOutputStream());
                System.out.println("Espião conectado ao servidor.");
            } catch (IOException ex) {
                System.out.println("Erro no construtor do ConexaoEspiao");
            }
        }
        
        public void enviaSuspeita(String msg){
            try{
                saida.writeUTF(msg);
                saida.flush();
            } catch (IOException ex) {
                System.out.println("Erro no enviaSuspeita() do Espião");
            }
        }
        
        public void fechaConexao(){
           try{
                socket.close();
                System.out.println("-----CONEXÃO ENCERRADA-----");
            } catch (IOException ex) {
                System.out.println("Erro no fechaConexao() do Cliente");
            } 
        }
        
    }
    
    private class AtualizaChat extends Thread{

        public AtualizaChat() {
        }

        @Override
        public void run(){
            while(true){
                boolean suspeito;
                String conteudo;
                String exibicao;
                Message template = new Message();
                template.ordem = msgAtualPub;
                template.validada = false;
                Message msg;
                try {
                    msg = (Message) space.take(template, null, Lease.FOREVER);
                    
                    if(msg != null){
                        suspeito = false;
                        conteudo = msg.content;
                        
                        for(int i = 0; i< jListPalavras.getModel().getSize(); i++){
                            if(conteudo.toLowerCase().contains(jListPalavras.getModel().getElementAt(i))){
                                suspeito = true;
                            }
                        }
                        msg.validada = true;
                        
                        if(suspeito){
                            exibicao = conteudo + " [***Bloqueada***]";
                            msg.content = "--- Mensgaem bloqueada ---";
                            clienteEspiao.enviaSuspeita(msg.usuario + ": " + conteudo);
                        }else{
                            exibicao = conteudo;
                        }
                        
                        chatArea.setText(chatArea.getText() + "\n" + msg.usuario + ": " + exibicao);
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
                        
                        try {
                            space.write(msg, null, Lease.FOREVER);
                        } catch (TransactionException | RemoteException ex) {
                            Logger.getLogger(ChatEspiao.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        msgAtualPub += 1;
                        Thread.sleep(10);
                    }
                    
                    try {
                        space.write(msg, null, Lease.FOREVER);
                    } catch (TransactionException | RemoteException ex) {
                        Logger.getLogger(ChatEspiao.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
    
    public void addPalavra(){
        String palavra = this.txtPalavra.getText();
        boolean duplicada = false;
        
        if(palavra != null && !palavra.isEmpty()){ 
            
            for(int i = 0; i< jListPalavras.getModel().getSize(); i++){
                if(palavra.equalsIgnoreCase(jListPalavras.getModel().getElementAt(i))){
                    duplicada = true;
                }
            }
            
            if(!duplicada){
                palavras.addElement(palavra);
                this.jListPalavras.setModel(palavras);
            }

        }
        
        this.txtPalavra.setText("");
    }
    
    public void rmvPalavra(){
        String palavra = this.jListPalavras.getSelectedValue();

        if(palavra != null && !palavra.isEmpty()){ 

            palavras.removeElement(palavra);

            this.jListPalavras.setModel(palavras);
        }
        
        this.jButtonRmv.setEnabled(false);
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
        jScrollPane3 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListPalavras = new javax.swing.JList<>();
        jLabelNome1 = new javax.swing.JLabel();
        jButtonAdd = new javax.swing.JButton();
        txtPalavra = new javax.swing.JTextField();
        jButtonRmv = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("----- ESPIÃO -----");
        setResizable(false);

        jPanelPrincipal.setBackground(new java.awt.Color(241, 241, 241));

        jLabelTitulo.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabelTitulo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelTitulo.setText("----- ESPIÃO -----");

        chatArea.setEditable(false);
        chatArea.setColumns(20);
        chatArea.setRows(5);
        chatArea.setFocusable(false);
        jScrollPane3.setViewportView(chatArea);

        jListPalavras.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListPalavras.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListPalavrasValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jListPalavras);

        jLabelNome1.setText("Palavras \"suspeitas\":");

        jButtonAdd.setText("+");
        jButtonAdd.setEnabled(false);
        jButtonAdd.setPreferredSize(new java.awt.Dimension(41, 24));
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        txtPalavra.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPalavraFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPalavraFocusLost(evt);
            }
        });
        txtPalavra.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPalavraKeyPressed(evt);
            }
        });

        jButtonRmv.setText("-");
        jButtonRmv.setEnabled(false);
        jButtonRmv.setPreferredSize(new java.awt.Dimension(41, 24));
        jButtonRmv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRmvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(jLabelNome1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(txtPalavra, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonRmv, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addComponent(jLabelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanelPrincipalLayout.setVerticalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(jLabelNome1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPalavra, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonRmv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(46, Short.MAX_VALUE))
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

    private void jButtonRmvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRmvActionPerformed
        rmvPalavra();
    }//GEN-LAST:event_jButtonRmvActionPerformed

    private void txtPalavraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPalavraKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){
            addPalavra();
        }
    }//GEN-LAST:event_txtPalavraKeyPressed

    private void txtPalavraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPalavraFocusLost
        // TODO add your handling code here:

    }//GEN-LAST:event_txtPalavraFocusLost

    private void txtPalavraFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPalavraFocusGained
        // TODO add your handling code here:
        this.jListPalavras.clearSelection();
        this.jButtonAdd.setEnabled(true);
        this.jButtonRmv.setEnabled(false);
    }//GEN-LAST:event_txtPalavraFocusGained

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO add your handling code here:
        addPalavra();
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jListPalavrasValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListPalavrasValueChanged
        String select = this.jListPalavras.getSelectedValue();

        if(select != null && !select.isEmpty()){
            this.jButtonAdd.setEnabled(false);
            this.jButtonRmv.setEnabled(true);
        }
    }//GEN-LAST:event_jListPalavrasValueChanged

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
            java.util.logging.Logger.getLogger(ChatEspiao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatEspiao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatEspiao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatEspiao.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatEspiao().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatArea;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonRmv;
    private javax.swing.JLabel jLabelNome1;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JList<String> jListPalavras;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField txtPalavra;
    // End of variables declaration//GEN-END:variables
}
