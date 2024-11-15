/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekakhir;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.AbstractCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.table.TableCellEditor;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    JPanel panel;
    JButton seeButton;
    JButton editButton;
    JButton deleteButton;
    private JTable table; // Tambahkan referensi ke JTable
    private Admin adm;

    public ButtonEditor(JTable table, JButton seeButton, JButton editButton, JButton deleteButton, Admin adm) {
        this.table = table;
        this.seeButton = seeButton;
        this.seeButton.addActionListener(this);
        this.editButton = editButton;
        this.editButton.addActionListener(this);
        this.deleteButton = deleteButton;
        this.deleteButton.addActionListener(this);
        this.adm = adm;
        panel = new JPanel();

        FlatLightLaf.setup();

        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(seeButton)
                        .addGap(10)
                        .addComponent(editButton)
                        .addGap(10)
                        .addComponent(deleteButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(seeButton)
                .addComponent(editButton)
                .addComponent(deleteButton)
        );
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) table.getValueAt(selectedRow, 1);
            if (e.getSource() == seeButton) {
                See see = new See(id);
                see.setVisible(true);
            } else if (e.getSource() == editButton) {
                Edit edit = new Edit(id);
                edit.setVisible(true);
            } else if (e.getSource() == deleteButton) {
                int conf = JOptionPane.showConfirmDialog(adm, "Anda yakin ingin menghapus?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION){
                    try(Connection conn = Koneksi.getKoneksi()){
                        String delete = "DELETE FROM task WHERE id = ?";
                        PreparedStatement ps = conn.prepareStatement(delete);
                        ps.setInt(1, id);
                        int sukses = ps.executeUpdate();
                        if(sukses > 0){
                            JOptionPane.showMessageDialog(adm, "Tugas berhasil dihapus");
                            adm.loadAllTask();
                            return;
                        }
                    }catch(SQLException f){
                        
                    }
                }
            }

            fireEditingStopped();
        }

    }
}