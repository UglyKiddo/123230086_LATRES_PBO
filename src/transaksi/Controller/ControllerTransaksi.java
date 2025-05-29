package transaksi.Controller;

import transaksi.Model.ModelTransaksi;
import transaksi.View.ViewTransaksi;
import transaksi.Database;
import javax.swing.*;
import java.sql.*;

/**
 * Kelas ControllerTransaksi mengelola logika bisnis aplikasi apotek.
 * Menangani input pengguna, validasi, dan interaksi dengan database.
 */
public class ControllerTransaksi {
    private ModelTransaksi model;

    public void setModel(Transaksi model) {
        this.model = model;
    }

    /**
     * Mengosongkan form input jika tidak kosong.
     * @param view ViewTransaksi yang digunakan.
     */
    public void resetForm(ViewTransaksi view) {
        String nama_pelanggan = view.getNamaPelanggan().getText();
        String nama_obat = view.getNamaObat().getText();
        String harga = view.getHarga().getText();
        String jumlah = view.getJumlah().getText();

        if (!nama_pelanggan.equals("") || !nama_obat.equals("") || !harga.equals("") || !jumlah.equals("")) {
            model.resetForm();
        }
    }

    /**
     * Menyimpan transaksi baru ke database dan memperbarui tabel.
     * @param view ViewTransaksi yang digunakan.
     */
    public void submitForm(ViewTransaksi view) {
        try {
            String nama_pelanggan = view.getNamaPelanggan().getText().trim();
            String nama_obat = view.getNamaObat().getText().trim();
            String hargaStr = view.getHarga().getText().trim();
            String jumlahStr = view.getJumlah().getText().trim();

            // Validasi input
            if (nama_pelanggan.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Nama Pelanggan tidak boleh kosong!");
                return;
            }
            if (nama_obat.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Nama Obat tidak boleh kosong!");
                return;
            }
            double harga = Double.parseDouble(hargaStr);
            if (harga <= 0) {
                JOptionPane.showMessageDialog(view, "Harga Satuan harus lebih dari 0!");
                return;
            }
            int jumlah = Integer.parseInt(jumlahStr);
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(view, "Jumlah Beli harus lebih dari 0!");
                return;
            }

            // Simpan ke database
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO transaksi (nama_pelanggan, nama_obat, harga, jumlah) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nama_pelanggan);
            stmt.setString(2, nama_obat);
            stmt.setDouble(3, harga);
            stmt.setInt(4, jumlah);
            stmt.executeUpdate();

            // Dapatkan ID transaksi
            ResultSet rs = stmt.getGeneratedKeys();
            int id_transaksi = 0;
            if (rs.next()) {
                id_transaksi = rs.getInt(1);
            }

            // Tambah ke tabel
            view.getTableModel().addRow(new Object[]{id_transaksi, nama_pelanggan, nama_obat, harga, jumlah});
            model.submitForm(view);
            model.resetForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Harga Satuan atau Jumlah Beli harus berupa angka!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error database: " + e.getMessage());
        }
    }

    /**
     * Mengedit transaksi yang dipilih di tabel.
     * @param view ViewTransaksi yang digunakan.
     */
    public void editForm(ViewTransaksi view) {
        int selectedRow = view.getTblTransaksi().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Pilih transaksi yang akan diedit!");
            return;
        }

        try {
            String nama_pelanggan = view.getNamaPelanggan().getText().trim();
            String nama_obat = view.getNamaObat().getText().trim();
            String hargaStr = view.getHarga().getText().trim();
            String jumlahStr = view.getJumlah().getText().trim();

            // Validasi input
            if (nama_pelanggan.isEmpty() || nama_obat.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Nama Pelanggan dan Nama Obat tidak boleh kosong!");
                return;
            }
            double harga = Double.parseDouble(hargaStr);
            if (harga <= 0) {
                JOptionPane.showMessageDialog(view, "Harga Satuan harus lebih dari 0!");
                return;
            }
            int jumlah = Integer.parseInt(jumlahStr);
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(view, "Jumlah Beli harus lebih dari 0!");
                return;
            }

            int id_transaksi = (int) view.getTableModel().getValueAt(selectedRow, 0);
            Connection conn = Database.getConnection();
            String sql = "UPDATE transaksi SET nama_pelanggan = ?, nama_obat = ?, harga = ?, jumlah = ? WHERE id_transaksi = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama_pelanggan);
            stmt.setString(2, nama_obat);
            stmt.setDouble(3, harga);
            stmt.setInt(4, jumlah);
            stmt.setInt(5, id_transaksi);
            stmt.executeUpdate();

            // Update tabel
            view.getTableModel().setValueAt(nama_pelanggan, selectedRow, 1);
            view.getTableModel().setValueAt(nama_obat, selectedRow, 2);
            view.getTableModel().setValueAt(harga, selectedRow, 3);
            view.getTableModel().setValueAt(jumlah, selectedRow, 4);
            JOptionPane.showMessageDialog(view, "Transaksi berhasil diedit!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Harga Satuan atau Jumlah Beli harus berupa angka!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error database: " + e.getMessage());
        }
    }

    /**
     * Menghapus transaksi yang dipilih dari tabel dan database.
     * @param view ViewTransaksi yang digunakan.
     */
    public void deleteForm(ViewTransaksi view) {
        int selectedRow = view.getTblTransaksi().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Pilih transaksi yang akan dihapus!");
            return;
        }

        try {
            int id_transaksi = (int) view.getTableModel().getValueAt(selectedRow, 0);
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM transaksi WHERE id_transaksi = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_transaksi);
            stmt.executeUpdate();

            // Hapus dari tabel
            view.getTableModel().removeRow(selectedRow);
            JOptionPane.showMessageDialog(view, "Transaksi berhasil dihapus!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error database: " + e.getMessage());
        }
    }

    /**
     * Menampilkan total bayar dengan diskon saat baris tabel diklik.
     * @param row Baris tabel yang dipilih.
     */
    public void showTotalBayar(int row) {
        try {
            double harga = (double) view.getTableModel().getValueAt(row, 3);
            int jumlah = (int) view.getTableModel().getValueAt(row, 4);
            model.setHarga(harga);
            model.setJumlah(jumlah);
            double totalBayar = model.hitungTotalBayar();
            JOptionPane.showMessageDialog(null, "Total Bayar: Rp " + String.format("%.2f", totalBayar));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error menghitung total: " + e.getMessage());
        }
    }

    /**
     * Memuat transaksi hari ini dari database ke tabel.
     * @param view ViewTransaksi yang digunakan.
     * @throws SQLException Jika terjadi error database.
     */
    public void loadTransaksiHariIni(ViewTransaksi view) throws SQLException {
        view.getTableModel().setRowCount(0); // Kosongkan tabel
        Connection conn = Database.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            view.getTableModel().addRow(new Object[]{
                rs.getInt("id_transaksi"),
                rs.getString("nama_pelanggan"),
                rs.getString("nama_obat"),
                rs.getDouble("harga"),
                rs.getInt("jumlah"),
            });
        }
    }

    private static class view {

        public view() {
        }
    }
}