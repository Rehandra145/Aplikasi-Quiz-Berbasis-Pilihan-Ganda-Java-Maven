/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projekakhir;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class NoKoneksi {
    private static final String CONNECTION_URI = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "pbo"; // Ganti dengan nama database Anda
    private MongoClient mongoClient;
    private MongoDatabase database;

    // Konstruktor untuk menginisialisasi koneksi ke MongoDB
    public NoKoneksi() {
        try {
            mongoClient = MongoClients.create(CONNECTION_URI);
            database = mongoClient.getDatabase(DATABASE_NAME);
            System.out.println("=> Connection to MongoDB successful");
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }

    // Method untuk mendapatkan instance dari database
    public MongoDatabase getDatabase() {
        return this.database;
    }

    // Method untuk menutup koneksi
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("=> MongoDB connection closed");
        }
    }

    // Main method untuk testing koneksi
    public static void main(String[] args) {
        NoKoneksi noKoneksi = new NoKoneksi();
        if (noKoneksi.getDatabase() != null) {
            System.out.println("Database connection is ready to use.");
        }
        noKoneksi.closeConnection();
    }
}

