/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekakhir;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author infinix
 */
public class detailButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    JButton detailButton;
    JPanel panel;
    private String username;
    private HomeStudent homeStudent;

    public detailButtonEditor(HomeStudent hs, JTable table, JButton detailButton) {
        this.detailButton = detailButton;
        this.detailButton.addActionListener(this);
        this.homeStudent = hs;

        // Membuat panel dengan BorderLayout
        panel = new JPanel(new BorderLayout());

        // Menambahkan tombol ke panel dengan BorderLayout.CENTER
        panel.add(detailButton, BorderLayout.CENTER);

        // Menambahkan padding/gap dengan EmptyBorder (top, left, bottom, right)
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 5 pixel gap di semua sisi

    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        // Mengatur latar belakang saat sel dipilih
        if (isSelected) {
            panel.setBackground(table.getSelectionBackground());
            detailButton.setBackground(table.getSelectionBackground());
        } else {
            panel.setBackground(table.getBackground());
            detailButton.setBackground(table.getBackground());
        }
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        fireEditingStopped();
        int id = homeStudent.taskSelected(); // Mengambil id dari HomeStudent
        username = homeStudent.currentUsername; // Mengambil username dari HomeStudent
        System.out.println("id yang didapatkan " + id);

        boolean isDone = false; // Inisialisasi variabel isDone
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "SELECT taskDone.username "
                    + "FROM taskDone "
                    + "JOIN task ON taskDone.idTask = task.id "
                    + "WHERE taskDone.idTask = ? AND taskDone.username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id); // Mengatur parameter idTask
            ps.setString(2, username); // Mengatur parameter username
            ResultSet rs = ps.executeQuery();

            if (rs.next()) { // Memeriksa apakah ada hasil
                isDone = true; // Jika ada hasil, berarti tugas sudah selesai
            }
        } catch (SQLException f) {
            System.out.println("Gagal dengan eror: " + f.getMessage());
        }

        // Membuat dan menampilkan jendela Detail
        Detail dt = new Detail(id, username, isDone);
        System.out.println("username yang didapatkan " + username);
        dt.setVisible(true);
    }

}
