/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.projekakhir;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import org.bson.Document;

/**
 *
 * @author infinix
 */
public class Admin extends javax.swing.JFrame {

    public int id;
    public int selectedRow = 0;

    private final int noColumnIndex = 0;
    private final int noColumnWidth = 50;

    private final int idColumnIndex = 1;
    private final int idColumnWidth = 50;

    private final int lvlColumnIndex = 4;
    private final int lvlColumnWidth = 80;

    private final int noSoalIndex = 0;
    private final int noSoalWidth = 40;

    private final int soalSoalIndex = 1;
    private final int soalSoalWidth = 300;

    private final int sbjSoalIndex = 2;
    private final int sbjSoalWidth = 80;

    private final int lvlSoalIndex = 3;
    private final int lvlSoalWidth = 70;

    private MongoDatabase mongodb;
    DefaultTableModel tblMdl = new DefaultTableModel(new Object[]{"No", "Soal", "Subject", "Level", "Action"}, 0);

    /**
     * Creates new form Admin
     */
    public Admin() {
        FlatLightLaf.setup();
        initComponents();
        welcome.setVisible(true);
        addTask.setVisible(false);
        manageUser.setVisible(false);
        manageTask.setVisible(false);
        manageSoal.setVisible(false);
        jPanel1.add(addTask);
    }

    private void setSoalWidth() {
        TableColumnModel columnModel = soal.getColumnModel();
        columnModel.getColumn(noSoalIndex).setPreferredWidth(noSoalWidth);
        columnModel.getColumn(noSoalIndex).setMaxWidth(noSoalWidth);
        columnModel.getColumn(noSoalIndex).setMinWidth(noSoalWidth);

        columnModel.getColumn(soalSoalIndex).setPreferredWidth(soalSoalWidth);
        columnModel.getColumn(soalSoalIndex).setMaxWidth(soalSoalWidth);
        columnModel.getColumn(soalSoalIndex).setMinWidth(soalSoalWidth);

        columnModel.getColumn(sbjSoalIndex).setPreferredWidth(sbjSoalWidth);
        columnModel.getColumn(sbjSoalIndex).setMaxWidth(sbjSoalWidth);
        columnModel.getColumn(sbjSoalIndex).setMinWidth(sbjSoalWidth);

        columnModel.getColumn(lvlSoalIndex).setPreferredWidth(lvlSoalWidth);
        columnModel.getColumn(lvlSoalIndex).setMaxWidth(lvlSoalWidth);
        columnModel.getColumn(lvlSoalIndex).setMinWidth(lvlSoalWidth);
    }

    private void setColumnWidth() {
        TableColumnModel columnModel = taskList.getColumnModel();
        columnModel.getColumn(noColumnIndex).setPreferredWidth(noColumnWidth);
        columnModel.getColumn(noColumnIndex).setMaxWidth(noColumnWidth);
        columnModel.getColumn(noColumnIndex).setMinWidth(noColumnWidth);

        columnModel.getColumn(idColumnIndex).setPreferredWidth(idColumnWidth);
        columnModel.getColumn(idColumnIndex).setMaxWidth(idColumnWidth);
        columnModel.getColumn(idColumnIndex).setMinWidth(idColumnWidth);

        columnModel.getColumn(idColumnIndex).setPreferredWidth(idColumnWidth);
        columnModel.getColumn(idColumnIndex).setMaxWidth(idColumnWidth);
        columnModel.getColumn(idColumnIndex).setMinWidth(idColumnWidth);
    }

    public void loadAllTask() {
        // Buat model tabel dengan kolom yang sesuai
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"No", "id", "Task name", "Subject", "Level", "Action"}, 0); // Set 0 untuk memulai dengan tabel kosong
        taskList.setModel(tableModel);
        try {
            // Mendapatkan detail task
            Connection conn = Koneksi.getKoneksi();
            String sql = "SELECT taskName, subject, level, id FROM task WHERE isDone = '0' ORDER BY id ASC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            setColumnWidth();
            int i = 1; // variabel untuk nomor
            while (rs.next()) {
                String namaQuiz = rs.getString("taskName");
                String subjectQuiz = rs.getString("subject");
                String levelQuiz = rs.getString("level");
                int id = rs.getInt("id");

                // menambahkan data ke tabel
                tableModel.addRow(new Object[]{i, id, namaQuiz, subjectQuiz, levelQuiz});
                i++; // increment nomor
            }

            taskList.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
            taskList.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(taskList, new JButton(new FlatSVGIcon("SVGICON/see.svg", 0.35f)),
                    new JButton(new FlatSVGIcon("SVGICON/edit.svg", 0.35f)),
                    new JButton(new FlatSVGIcon("SVGICON/delete.svg", 0.35f)), this));
        } catch (SQLException e) {
            System.out.println("Eror dengan pesan: " + e.getMessage());
        }
    }

    public void createNewSoal() {
        // Membuat field input untuk soal baru
        mongodb = new NoKoneksi().getDatabase();
        soal.setModel(tblMdl);
        if (mongodb == null) {
            JOptionPane.showMessageDialog(this, "Koneksi ke database gagal. Pastikan MongoDB sudah terhubung.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JTextField soalField = new JTextField();
        JTextField subjectField = new JTextField();
        JComboBox<String> levelField = new JComboBox<>(new String[]{"Mudah", "Sedang", "Sulit"});
        JTextField option1Field = new JTextField();
        JTextField option2Field = new JTextField();
        JTextField option3Field = new JTextField();
        JTextField option4Field = new JTextField();
        JTextField correctAnswerField = new JTextField();
        JTextArea reviewField = new JTextArea(5, 20);
        reviewField.setLineWrap(true);
        reviewField.setWrapStyleWord(true);

        // Menampilkan form input untuk soal baru
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

        int option = JOptionPane.showConfirmDialog(null, message, "Tambah Soal Baru", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Validasi format correct answer dengan regex
            String correctAnswer = correctAnswerField.getText();
            if (!Pattern.matches("^option[1-4]$", correctAnswer)) {
                JOptionPane.showMessageDialog(null, "Correct Answer harus dalam format 'option' diikuti angka 1-4 tanpa spasi.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return; // Kembali tanpa menyimpan perubahan jika validasi gagal
            }

            // Mengambil nilai yang diperbarui
            String soalText = soalField.getText();
            String subject = subjectField.getText();
            String level = (String) levelField.getSelectedItem();
            String option1 = option1Field.getText();
            String option2 = option2Field.getText();
            String option3 = option3Field.getText();
            String option4 = option4Field.getText();
            String review = reviewField.getText();

            // Membuat dokumen baru untuk MongoDB
            Document newSoal = new Document("question", soalText)
                    .append("subject", subject)
                    .append("difficulty", level)
                    .append("options", new Document("option1", option1)
                            .append("option2", option2)
                            .append("option3", option3)
                            .append("option4", option4))
                    .append("correct_answer", correctAnswer)
                    .append("review", review);

            // Menyimpan dokumen baru ke MongoDB
            MongoCollection<Document> collection = mongodb.getCollection("soal");
            collection.insertOne(newSoal);

            // Menambahkan data baru ke tabel
            tblMdl.addRow(new Object[]{tblMdl.getRowCount() + 1, soalText, subject, level});
        }
    }

    public void loadUser() {
        DefaultTableModel tbm = new DefaultTableModel(new Object[]{"No", "Nama", "Password", "Action"}, 0);
        usersTable.setModel(tbm);
        try (Connection conn = Koneksi.getKoneksi()) {
            String sql = "SELECT * FROM users WHERE role = 'siswa'";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            int i = 1;
            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("username");
                String pw = rs.getString("password");

                tbm.addRow(new Object[]{i, nama, pw});
                i++;
            }
            usersTable.getColumnModel().getColumn(3).setCellRenderer(new deleteButtonRenderer());
            usersTable.getColumnModel().getColumn(3).setCellEditor(new deleteButtonEditor(this, usersTable, new JButton(new FlatSVGIcon("SVGICON/delete.svg", 0.35f))));

        } catch (SQLException g) {

        }
    }

    public void loadSoal() {
        soal.setModel(tblMdl);
        setSoalWidth();

        try {
            MongoDatabase mongodb = new NoKoneksi().getDatabase();
            MongoCollection<Document> collection = mongodb.getCollection("soal");
            List<Document> soalList = collection.find().into(new ArrayList<>());
            int i = 1;
            for (Document doc : soalList) {
                String soalText = doc.getString("question");
                String subject = doc.getString("subject");
                String level = doc.getString("difficulty");

                tblMdl.addRow(new Object[]{i, soalText, subject, level, "Edit/Delete"});
                i++;
            }
            soal.getColumnModel().getColumn(4).setCellRenderer(new soalButtonRenderer());
            soal.getColumnModel().getColumn(4).setCellEditor(new soalButtonEditor(soal, new JButton(new FlatSVGIcon("SVGICON/edit.svg", 0.35f)), new JButton(new FlatSVGIcon("SVGICON/delete.svg", 0.35f)), mongodb));
        } catch (Exception o) {
            o.printStackTrace();
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

        subject = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        addTask = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        sejarah = new javax.swing.JRadioButton();
        Subject = new javax.swing.JLabel();
        economics = new javax.swing.JRadioButton();
        geography = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        Date = new com.toedter.calendar.JDateChooser();
        Add = new javax.swing.JButton();
        Level = new javax.swing.JComboBox<>();
        taskName = new javax.swing.JTextField();
        manageTask = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        taskList = new javax.swing.JTable();
        manageSoal = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        soal = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        manageUser = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        welcome = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setFocusable(false);

        jPanel2.setBackground(new java.awt.Color(102, 255, 255));

        jLabel1.setFont(new java.awt.Font("JetBrains Mono", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ADMIN");

        jButton1.setText("Manage user");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Manage soal");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Manage Task");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Add Task");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton6.setText("EXIT");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(32, 32, 32))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jButton6)
                .addGap(43, 43, 43))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addGap(34, 34, 34))
        );

        addTask.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("JetBrains Mono", 1, 24)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("ADD TASK");

        subject.add(sejarah);
        sejarah.setText("Sejarah");

        Subject.setFont(new java.awt.Font("JetBrains Mono", 0, 12)); // NOI18N
        Subject.setText("Subject");

        subject.add(economics);
        economics.setText("Ekonomi");

        subject.add(geography);
        geography.setText("Geografi");

        jLabel5.setFont(new java.awt.Font("JetBrains Mono", 0, 12)); // NOI18N
        jLabel5.setLabelFor(Level);
        jLabel5.setText("Level");

        jLabel6.setFont(new java.awt.Font("JetBrains Mono", 0, 12)); // NOI18N
        jLabel6.setText("Due To");

        Add.setBackground(new java.awt.Color(51, 255, 255));
        Add.setText("Add");
        Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddActionPerformed(evt);
            }
        });

        Level.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Mudah", "Sedang", "Sulit" }));
        Level.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LevelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addTaskLayout = new javax.swing.GroupLayout(addTask);
        addTask.setLayout(addTaskLayout);
        addTaskLayout.setHorizontalGroup(
            addTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addTaskLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
            .addGroup(addTaskLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(addTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addTaskLayout.createSequentialGroup()
                        .addGap(248, 248, 248)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(addTaskLayout.createSequentialGroup()
                        .addGroup(addTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(taskName, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Level, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(addTaskLayout.createSequentialGroup()
                                    .addComponent(sejarah)
                                    .addGap(18, 18, 18)
                                    .addComponent(geography)
                                    .addGap(18, 18, 18)
                                    .addComponent(economics))
                                .addComponent(Subject, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(Date, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel6)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        addTaskLayout.setVerticalGroup(
            addTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addTaskLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(taskName, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(Subject)
                .addGap(18, 18, 18)
                .addGroup(addTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sejarah)
                    .addComponent(geography)
                    .addComponent(economics))
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(Level, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71))
        );

        taskList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "title 5"
            }
        ));
        taskList.setRowHeight(40);
        taskList.setShowGrid(true);
        taskList.setShowHorizontalLines(true);
        taskList.setShowVerticalLines(true);
        jScrollPane3.setViewportView(taskList);

        javax.swing.GroupLayout manageTaskLayout = new javax.swing.GroupLayout(manageTask);
        manageTask.setLayout(manageTaskLayout);
        manageTaskLayout.setHorizontalGroup(
            manageTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageTaskLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
                .addContainerGap())
        );
        manageTaskLayout.setVerticalGroup(
            manageTaskLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageTaskLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
                .addContainerGap())
        );

        soal.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        soal.setRowHeight(40);
        soal.setShowGrid(true);
        jScrollPane4.setViewportView(soal);

        jButton5.setBackground(new java.awt.Color(0, 255, 255));
        jButton5.setText("ADD");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout manageSoalLayout = new javax.swing.GroupLayout(manageSoal);
        manageSoal.setLayout(manageSoalLayout);
        manageSoalLayout.setHorizontalGroup(
            manageSoalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageSoalLayout.createSequentialGroup()
                .addGap(548, 548, 548)
                .addComponent(jButton5)
                .addContainerGap(41, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manageSoalLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        manageSoalLayout.setVerticalGroup(
            manageSoalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manageSoalLayout.createSequentialGroup()
                .addContainerGap(77, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        usersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        usersTable.setRowHeight(40);
        usersTable.setShowGrid(true);
        jScrollPane1.setViewportView(usersTable);

        javax.swing.GroupLayout manageUserLayout = new javax.swing.GroupLayout(manageUser);
        manageUser.setLayout(manageUserLayout);
        manageUserLayout.setHorizontalGroup(
            manageUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(manageUserLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(67, Short.MAX_VALUE))
        );
        manageUserLayout.setVerticalGroup(
            manageUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, manageUserLayout.createSequentialGroup()
                .addContainerGap(77, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );

        welcome.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("JetBrains Mono", 3, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Welcome Admin :)");

        jLabel3.setFont(new java.awt.Font("JetBrains Mono", 2, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 153, 153));
        jLabel3.setText("Cick button to do an action..");

        javax.swing.GroupLayout welcomeLayout = new javax.swing.GroupLayout(welcome);
        welcome.setLayout(welcomeLayout);
        welcomeLayout.setHorizontalGroup(
            welcomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomeLayout.createSequentialGroup()
                .addContainerGap(172, Short.MAX_VALUE)
                .addGroup(welcomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomeLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(160, 160, 160))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, welcomeLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(144, 144, 144))))
        );
        welcomeLayout.setVerticalGroup(
            welcomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(welcomeLayout.createSequentialGroup()
                .addGap(193, 193, 193)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(324, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(welcome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(394, Short.MAX_VALUE)
                    .addComponent(addTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(0, 222, Short.MAX_VALUE)
                    .addComponent(manageTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(0, 202, Short.MAX_VALUE)
                    .addComponent(manageSoal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addGap(0, 204, Short.MAX_VALUE)
                    .addComponent(manageUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(welcome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(addTask, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(manageTask, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(manageSoal, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(manageUser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        welcome.setVisible(false);
        manageTask.setVisible(false);
        manageUser.setVisible(false);
        manageSoal.setVisible(false);
        jPanel1.add(addTask);
        addTask.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
        // TODO add your handling code here:
        try {
            String title = (String) taskName.getText();
            String selectedSubject = null;
            String selectedLevel = (String) Level.getSelectedItem();
            Date date = Date.getDate();
            LocalDate taskDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();
            if (taskDate.isBefore(today)) {
                JOptionPane.showMessageDialog(this, "Tanggal invalid");
                return;
            }
            SimpleDateFormat fmtr = new SimpleDateFormat("yyyy-MM-dd");
            String dueTo = fmtr.format(date);

            Connection conn = Koneksi.getKoneksi();
            if (title == null) {
                JOptionPane.showMessageDialog(this, "Judul tidak boleh kosong");
            }

            if (sejarah.isSelected()) {
                selectedSubject = "Sejarah";
            } else if (economics.isSelected()) {
                selectedSubject = "Ekonomi";
            } else if (geography.isSelected()) {
                selectedSubject = "Geografi";
            }

            String insert = "INSERT INTO task (taskName, subject, level, finishDate) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insert);

            ps.setString(1, title);
            ps.setString(2, selectedSubject);
            ps.setString(3, selectedLevel);
            ps.setString(4, dueTo);

            int updateStatus = ps.executeUpdate();
            if (updateStatus > 0) {
                JOptionPane.showMessageDialog(this, "Tugas berhasil ditambahkan!");
                subject.clearSelection();
                taskName.setText("");
                Date.setCalendar(null);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menambahkan tugas.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }//GEN-LAST:event_AddActionPerformed

    private void LevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LevelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LevelActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        welcome.setVisible(false);
        addTask.setVisible(false);
        manageUser.setVisible(false);
        manageSoal.setVisible(false);
        loadAllTask();
        manageTask.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        welcome.setVisible(false);
        addTask.setVisible(false);
        manageTask.setVisible(false);
        manageSoal.setVisible(false);
        loadUser();
        manageUser.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        welcome.setVisible(false);
        addTask.setVisible(false);
        manageTask.setVisible(false);
        manageUser.setVisible(false);
        loadSoal();
        manageSoal.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        createNewSoal();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        dispose();
        Auth at = new Auth();
        at.setVisible(true);
    }//GEN-LAST:event_jButton6ActionPerformed

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
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private com.toedter.calendar.JDateChooser Date;
    private javax.swing.JComboBox<String> Level;
    private javax.swing.JLabel Subject;
    private javax.swing.JPanel addTask;
    private javax.swing.JRadioButton economics;
    private javax.swing.JRadioButton geography;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel manageSoal;
    public javax.swing.JPanel manageTask;
    private javax.swing.JPanel manageUser;
    private javax.swing.JRadioButton sejarah;
    private javax.swing.JTable soal;
    private javax.swing.ButtonGroup subject;
    private javax.swing.JTable taskList;
    private javax.swing.JTextField taskName;
    private javax.swing.JTable usersTable;
    private javax.swing.JPanel welcome;
    // End of variables declaration//GEN-END:variables

}
