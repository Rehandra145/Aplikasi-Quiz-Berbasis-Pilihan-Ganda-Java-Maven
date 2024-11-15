package com.mycompany.projekakhir;

import com.formdev.flatlaf.FlatLightLaf;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;
import javax.swing.AbstractCellEditor;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import org.bson.Document;

public class soalButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    JPanel panel;
    JButton editButton;
    JButton deleteButton;
    private JTable table;
    private DefaultTableModel model;
    private MongoDatabase mongodb; // Database reference
    
    public soalButtonEditor(JTable table, JButton editButton, JButton deleteButton, MongoDatabase mongodb) {
        this.table = table;
        this.model = (DefaultTableModel) table.getModel();
        this.editButton = editButton;
        this.editButton.setActionCommand("Edit");
        this.editButton.addActionListener(this);

        this.deleteButton = deleteButton;
        this.deleteButton.setActionCommand("Delete");
        this.deleteButton.addActionListener(this);

        this.mongodb = mongodb; // Save MongoDB connection
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
        int row = table.getSelectedRow();
        String actionCommand = e.getActionCommand();

        // Mengambil nilai kolom "Soal" dari baris yang dipilih sebagai filter untuk MongoDB
        String soalText = (String) model.getValueAt(row, 1); // Asumsi kolom "Soal" ada di indeks 1
        MongoCollection<Document> collection = mongodb.getCollection("soal");

        if ("Edit".equals(actionCommand)) {
            // Ambil data dari dokumen untuk mendapatkan detail lengkap soal
            Document soalDoc = collection.find(Filters.eq("question", soalText)).first();
            if (soalDoc != null) {
                // Mengambil nilai yang sudah ada dari dokumen soal
                String subject = soalDoc.getString("subject");
                String level = soalDoc.getString("difficulty");
                String review = soalDoc.getString("review");
                
                // Mengambil opsi jawaban dari dokumen
                Document options = soalDoc.get("options", Document.class);
                String option1 = options.getString("option1");
                String option2 = options.getString("option2");
                String option3 = options.getString("option3");
                String option4 = options.getString("option4");
                String correctAnswer = soalDoc.getString("correct_answer");

                // Menyiapkan field input untuk dialog edit
                JTextField soalField = new JTextField(soalText);
                JTextField subjectField = new JTextField(subject);
                
                JComboBox<String> levelField = new JComboBox<>(new String[] {"Mudah", "Sedang", "Sulit"});
                levelField.setSelectedItem(level); // Set level saat ini sebagai default
                
                JTextField option1Field = new JTextField(option1);
                JTextField option2Field = new JTextField(option2);
                JTextField option3Field = new JTextField(option3);
                JTextField option4Field = new JTextField(option4);
                JTextField correctAnswerField = new JTextField(correctAnswer);
                JTextArea reviewField = new JTextArea(review, 5, 20);
                reviewField.setLineWrap(true);
                reviewField.setWrapStyleWord(true);

                Object[] message = {
                    "Soal:", soalField,
                    "Subject:", subjectField,
                    "Level:", levelField,
                    "Option 1:", option1Field,
                    "Option 2:", option2Field,
                    "Option 3:", option3Field,
                    "Option 4:", option4Field,
                    "Correct Answer (e.g., option4):", correctAnswerField,
                    "Review:", reviewField
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Edit Soal", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    String updatedCorrectAnswer = correctAnswerField.getText();
                    if (!Pattern.matches("^option[1-4]$", updatedCorrectAnswer)) {
                        JOptionPane.showMessageDialog(null, "Correct Answer harus dalam format 'option' diikuti angka 1-4 tanpa spasi.", "Input Error", JOptionPane.ERROR_MESSAGE);
                        return; // Kembali tanpa menyimpan perubahan jika validasi gagal
                    }
                    // Mengambil nilai yang diperbarui
                    String updatedSoal = soalField.getText();
                    String updatedSubject = subjectField.getText();
                    String updatedLevel = (String) levelField.getSelectedItem();
                    String updatedOption1 = option1Field.getText();
                    String updatedOption2 = option2Field.getText();
                    String updatedOption3 = option3Field.getText();
                    String updatedOption4 = option4Field.getText();
                    String updatedReview = reviewField.getText();

                    // Update data di tabel
                    model.setValueAt(updatedSoal, row, 1);
                    model.setValueAt(updatedSubject, row, 2);
                    model.setValueAt(updatedLevel, row, 3);

                    // Update data di MongoDB
                    Document updatedOptions = new Document("option1", updatedOption1)
                            .append("option2", updatedOption2)
                            .append("option3", updatedOption3)
                            .append("option4", updatedOption4);
                    collection.updateOne(
                        Filters.eq("question", soalText),
                        new Document("$set", new Document("question", updatedSoal)
                                .append("subject", updatedSubject)
                                .append("difficulty", updatedLevel)
                                .append("options", updatedOptions)
                                .append("correct_answer", updatedCorrectAnswer)
                                .append("review", updatedReview))
                    );
                }
            }
        }

        if ("Delete".equals(actionCommand)) {
            int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                fireEditingStopped();
                collection.deleteOne(Filters.eq("question", soalText));
                model.removeRow(row);
            }
        }

        fireEditingStopped();
    }
}
