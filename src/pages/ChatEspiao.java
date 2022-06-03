/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pages;

import main.*;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import tuplas.Espiao;
import tuplas.Message;
import tuplas.Usuario;
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
    
    /**
     * Creates new form SalasListagem
     */
    public ChatEspiao() throws ClassNotFoundException {
        initComponents();
        
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
        
        AtualizaChat chat = new AtualizaChat();
        chat.start();
    }
    
    private class AtualizaChat extends Thread{

        public AtualizaChat() {
        }

        @Override
        public void run(){
            while(true){
                Message template = new Message();
                template.ordem = msgAtualPub;
                template.validada = false;
                Message msg;
                try {
                    msg = (Message) space.take(template, null, Lease.FOREVER);
                    
                    if(msg != null){
                        msg.validada = true;
                        msgAtualPub += 1;
                        chatArea.setText(chatArea.getText() + "\n" + msg.usuario + ": " + msg.content);
                        chatArea.setCaretPosition(chatArea.getDocument().getLength());
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
        jListSalas = new javax.swing.JList<>();
        jLabelNome1 = new javax.swing.JLabel();
        jButtonAdd = new javax.swing.JButton();
        mensagem = new javax.swing.JTextField();
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

        jListSalas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSalas.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSalasValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jListSalas);

        jLabelNome1.setText("Palavras \"suspeitas\":");

        jButtonAdd.setText("+");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        mensagem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                mensagemKeyPressed(evt);
            }
        });

        jButtonRmv.setText("-");
        jButtonRmv.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRmvActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                            .addComponent(jLabelNome1, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(92, 92, 92))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPrincipalLayout.createSequentialGroup()
                            .addComponent(mensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButtonRmv)
                            .addContainerGap()))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
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
                            .addComponent(mensagem, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonAdd)
                            .addComponent(jButtonRmv))))
                .addContainerGap(40, Short.MAX_VALUE))
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

    private void jListSalasValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSalasValueChanged
        /*String select = this.jListSalas.getSelectedValue();
        listaUsuarios.removeAllElements();

        if(select != null && !select.isEmpty()){
            Sala s = null;
            Usuario uTemplate = new Usuario();
            uTemplate.sala = select;

            Usuario u = null;

            this.sala = new Sala();
            this.sala.nome = select;

            try {
                s = (Sala) space.read(this.sala, null, Lease.FOREVER);
            } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException ex) {
                Logger.getLogger(SalaCadastro.class.getName()).log(Level.SEVERE, null, ex);
            }

            ArrayList<String> usuarios = new ArrayList<String>();
            ArrayList<Integer> qtdMsgUsu = new ArrayList<Integer>();

            for (int i = 0; i < s.qtdUsu; i++) {
                try {
                    u = (Usuario) space.take(uTemplate, null, Lease.FOREVER);
                    usuarios.add(u.nome);
                    qtdMsgUsu.add(u.qtdMsg);
                    listaUsuarios.addElement(u.nome);
                } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException ex) {
                    Logger.getLogger(SalasListagem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            for (int i = 0; i < usuarios.size(); i++) {
                uTemplate.nome = usuarios.get(i);
                uTemplate.qtdMsg = qtdMsgUsu.get(i);
                try {
                    this.space.write(uTemplate, null, Lease.FOREVER);
                } catch (TransactionException | RemoteException ex) {
                    Logger.getLogger(SalasListagem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            this.jListUsuarios.setModel(listaUsuarios);
        }*/
    }//GEN-LAST:event_jListSalasValueChanged

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButtonAddActionPerformed

    private void mensagemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_mensagemKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode() == evt.VK_ENTER){
            //enviarMensagemChat();
        }
    }//GEN-LAST:event_mensagemKeyPressed

    private void jButtonRmvActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRmvActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonRmvActionPerformed

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
                try {
                    new ChatEspiao().setVisible(true);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ChatEspiao.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea chatArea;
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonRmv;
    private javax.swing.JLabel jLabelNome1;
    private javax.swing.JLabel jLabelTitulo;
    private javax.swing.JList<String> jListSalas;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField mensagem;
    // End of variables declaration//GEN-END:variables
}