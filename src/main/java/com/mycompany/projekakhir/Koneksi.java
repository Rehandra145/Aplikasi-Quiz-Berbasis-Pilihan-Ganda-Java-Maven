/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekakhir;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author infinix
 */
public class Koneksi {
    private static Connection koneksi;

    public static Connection getKoneksi() {
        try {
            // Periksa apakah koneksi sudah diinisialisasi atau tertutup
            if (koneksi == null || koneksi.isClosed()) {
                String url = "jdbc:mysql://localhost:3306/pbo"; 
                String user = "root"; 
                String password = "SUZULYTDR"; 
                koneksi = DriverManager.getConnection(url, user, password);
                System.out.println("Koneksi berhasil dibuka kembali!");
            }
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
        return koneksi;
    }
    
    public static void closeKoneksi() {
        if (koneksi != null) {
            try {
                koneksi.close();
                koneksi = null; // Set ke null agar bisa diinisialisasi ulang
                System.out.println("Koneksi berhasil ditutup.");
            } catch (SQLException e) {
                System.out.println("Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
 }
