/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekakhir;

import com.formdev.flatlaf.FlatLightLaf;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.AbstractCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author infinix
 */
public class deleteButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener{
    JPanel panel;
    JButton deleteButton;
    JTable table;
    Admin adm;
    public deleteButtonEditor(Admin adm, JTable table, JButton deleteButton) {
        this.table = table;
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
                        .addComponent(deleteButton)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
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
        fireEditingStopped();
        int selectedName = table.getSelectedRow();
        if (selectedName >= 0) {
            String nama = (String) table.getValueAt(selectedName, 1);
            if(e.getSource() == deleteButton){
            int result = JOptionPane.showConfirmDialog(adm , "Apakah anda yakin ingin menghapus", "konfirmasi", JOptionPane.YES_NO_OPTION);
            if(result == JOptionPane.YES_OPTION){
                try(Connection conn = Koneksi.getKoneksi()){
                    String sql = "DELETE FROM users WHERE username = ? AND role = 'siswa'";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, nama);
                    int hasil = ps.executeUpdate();
                    if (hasil>0){
                        JOptionPane.showMessageDialog(adm, "User berhasil dihapus");
                        adm.loadUser();
                        return;
                    }
                }catch(SQLException g){
                    
                }
            }
            }
        }
    
    }
}
