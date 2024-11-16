/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.projekakhir;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author infinix
 */
public class Detail extends javax.swing.JFrame {

    String namaSubject;
    String levelPilihan;
    String finishDate;
    public int id;
    public String currUsername;
    public boolean isDone;
    public Quiz qs;

    /**
     * Creates new form Detail
     */
    public Detail(int id, String username, boolean isDone) {
        String tenggat = null;
        this.isDone = isDone;
        this.id = id;
        this.currUsername = username;
        initComponents();
        if(isDone){
            play.setEnabled(false);
            play.setBackground(new Color(175, 175, 175));
        }
        getTask();
        if (finishDate != null) {
            int spasi = finishDate.indexOf(" ");
            if (spasi != -1) {
                String datePart = finishDate.substring(0, spasi); // Mengambil bagian tanggal
                String timePart = finishDate.substring(spasi + 1); // Mengambil bagian waktu
                tenggat = datePart + " | " + timePart; // Menambahkan '|' sebagai pemisah
            }
        }
        subject.setText("Subject = " + (namaSubject != null ? namaSubject : ""));
        level.setText("Level = " + (levelPilihan != null ? levelPilihan : ""));
        dueTo.setText("Finish Date = " + (tenggat != null ? tenggat : ""));
    }

    public void getTask() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = Koneksi.getKoneksi();
            String sql = "SELECT subject, level, finishDate FROM task WHERE id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, this.id);
            rs = ps.executeQuery();
            System.out.println("querry sukses");

            if (rs.next()) { // Check if result set has data
                namaSubject = rs.getString("subject");
                levelPilihan = rs.getString("level");
                finishDate = rs.getString("finishDate");
            }

        } catch (SQLException e) {
            System.out.println("Gagal dengan eror : " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Gagal menutup koneksi: " + e.getMessage());
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

        panel = new javax.swing.JPanel();
        subject = new javax.swing.JLabel();
        level = new javax.swing.JLabel();
        dueTo = new javax.swing.JLabel();
        play = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        panel.setBackground(new java.awt.Color(249, 247, 228));

        subject.setFont(new java.awt.Font("JetBrains Mono", 1, 12)); // NOI18N

        level.setFont(new java.awt.Font("JetBrains Mono", 1, 12)); // NOI18N

        dueTo.setFont(new java.awt.Font("JetBrains Mono", 1, 12)); // NOI18N

        play.setBackground(new java.awt.Color(0, 255, 255));
        play.setText("PLAY");

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(play, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dueTo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(subject, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE)
                    .addComponent(level, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(subject, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(level, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(dueTo, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(play, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void QuizTask() {
        try (Connection conn = Koneksi.getKoneksi()) {
            String select = "SELECT subject, level FROM task WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(select);
            ps.setInt(1, this.id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String sbj = rs.getString("subject");
                String lvl = rs.getString("level");
                qs = new Quiz(lvl, sbj, currUsername, this);
                qs.isTask = true;
                qs.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, currUsername);
            }

        } catch (SQLException e) {
            System.out.println("Eror dengan pesan " + e.getMessage());
        }
    }

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
            java.util.logging.Logger.getLogger(Detail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Detail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Detail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Detail.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Detail().setVisible(true);
//            }
//        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dueTo;
    private javax.swing.JLabel level;
    private javax.swing.JPanel panel;
    private javax.swing.JButton play;
    private javax.swing.JLabel subject;
    // End of variables declaration//GEN-END:variables
}
