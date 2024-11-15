/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekakhir;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
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
public class soalButtonRenderer extends JPanel implements TableCellRenderer {

    public soalButtonRenderer() {
        setBackground(Color.WHITE);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        removeAll();
        FlatLightLaf.setup();
        JButton editButton = new JButton(new FlatSVGIcon("SVGICON/edit.svg", 0.35f));
        JButton deleteButton = new JButton(new FlatSVGIcon("SVGICON/delete.svg", 0.35f));

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(editButton)
                        .addGap(10)
                        .addComponent(deleteButton)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
                                GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(editButton)
                .addComponent(deleteButton)
        );

        return this;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
    }
}
