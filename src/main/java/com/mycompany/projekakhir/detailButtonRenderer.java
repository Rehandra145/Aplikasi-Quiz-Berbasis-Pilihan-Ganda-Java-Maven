/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekakhir;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.LayoutStyle;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author infinix
 */
public class detailButtonRenderer extends JPanel implements TableCellRenderer{
    private JButton detailButton;
    
    public detailButtonRenderer() {
        detailButton = new JButton(new FlatSVGIcon("SVGICON/detail.svg", 0.35f));

        // Menggunakan BorderLayout agar tombol mengisi seluruh panel
        setLayout(new BorderLayout());
        add(detailButton, BorderLayout.CENTER);

        // Menambahkan padding/gap dengan EmptyBorder (top, left, bottom, right)
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));       
    }

    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (isSelected) {
            setBackground(table.getSelectionBackground());
            detailButton.setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
            detailButton.setBackground(table.getBackground());
        }
        
        return this;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1); 
    }
    
}
