package transaksi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Kelas Database mengelola koneksi ke database MySQL.
 */
public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/apotek";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Mendapatkan koneksi ke database.
     * @return Objek Connection atau null jika gagal.
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Koneksi ke database berhasil!");
        } catch (SQLException e) {
            System.out.println("Koneksi gagal: " + e.getMessage());
        }
        return conn;
    }
}