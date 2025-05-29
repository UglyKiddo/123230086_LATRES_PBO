package transaksi.Controller;

import transaksi.Model.ModelTransaksi;
import transaksi.View.ViewTransaksi;
import transaksi.Database;
import javax.swing.*;
import java.sql.*;
import java.text.DecimalFormat;

public class ControllerTransaksi {
    private ModelTransaksi model;
    private ViewTransaksi view; // Simpan referensi ke ViewTransaksi

   
    public void setModel(ModelTransaksi model) {
        this.model = model;
    }

    public void resetForm(ViewTransaksi view) {
        this.view = view; // Simpan referensi
        String namaPelanggan = view.getNamaPelanggan().getText();
        String namaObat = view.getNamaObat().getText();
        String harga = view.getHarga().getText();
        String jumlah = view.getJumlah().getText();

        if (!namaPelanggan.equals("") || !namaObat.equals("") || !harga.equals("") || !jumlah.equals("")) {
            model.resetForm();
        }
    }

    public void submitForm(ViewTransaksi view) {
        this.view = view; // Simpan referensi
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
            if (conn == null) {
                JOptionPane.showMessageDialog(view, "Gagal terhubung ke database!");
                return;
            }
            String sql = "INSERT INTO transaksi (nama_pelanggan, nama_obat, harga, jumlah) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nama_pelanggan);
            stmt.setString(2, nama_obat);
            stmt.setDouble(3, harga);
            stmt.setInt(4, jumlah);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int id_transaksi = 0;
            if (rs.next()) {
                id_transaksi = rs.getInt(1);
            }

            view.getTableModel().addRow(new Object[]{id_transaksi, nama_pelanggan, nama_obat, harga, jumlah});
            model.submitForm(view);
            model.resetForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Harga Satuan atau Jumlah Beli harus berupa angka!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error database: " + e.getMessage());
        }
    }


    public void editForm(ViewTransaksi view) {
        this.view = view; // Simpan referensi
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

            int idTransaksi = (int) view.getTableModel().getValueAt(selectedRow, 0);
            Connection conn = Database.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(view, "Gagal terhubung ke database!");
                return;
            }
            String sql = "UPDATE transaksi SET nama_pelanggan = ?, nama_obat = ?, harga = ?, jumlah = ? WHERE id_transaksi = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nama_pelanggan);
            stmt.setString(2, nama_obat);
            stmt.setDouble(3, harga);
            stmt.setInt(4, jumlah);
            stmt.setInt(5, idTransaksi);
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
    
    public void deleteForm(ViewTransaksi view) {
        this.view = view; // Simpan referensi
        int selectedRow = view.getTblTransaksi().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Pilih transaksi yang akan dihapus!");
            return;
        }

        try {
            int idTransaksi = (int) view.getTableModel().getValueAt(selectedRow, 0);
            Connection conn = Database.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(view, "Gagal terhubung ke database!");
                return;
            }
            String sql = "DELETE FROM transaksi WHERE id_transaksi = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idTransaksi);
            stmt.executeUpdate();

            // Hapus dari tabel
            view.getTableModel().removeRow(selectedRow);
            JOptionPane.showMessageDialog(view, "Transaksi berhasil dihapus!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error database: " + e.getMessage());
        }
    }

    /**
     * Menampilkan total bayar dengan diskon saat output diklik.
     */
    public void showTotalBayar(int row) {
        try {
            if (view == null) {
                System.out.println("View is null in showTotalBayar!");
                JOptionPane.showMessageDialog(null, "View belum diinisialisasi! Silakan lakukan aksi lain (misalnya, muat data) terlebih dahulu.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double harga = (double) view.getTableModel().getValueAt(row, 3);
            int jumlah = (int) view.getTableModel().getValueAt(row, 4);
            model.setHarga(harga);
            model.setJumlah(jumlah);

            // Hitung total
            double totalSebelumDiskon = harga * jumlah;
            double diskon = (jumlah > 5) ? totalSebelumDiskon * 0.1 : 0;
            double totalBayar = totalSebelumDiskon - diskon;

            // Format angka tanpa desimal jika bulat
            DecimalFormat formatter = new DecimalFormat("#,###");
            String hargaStr = "Rp " + formatter.format(harga);
            String totalSebelumDiskonStr = formatter.format(totalSebelumDiskon);
            String diskonStr = formatter.format(diskon);
            String totalBayarStr = "Rp " + formatter.format(totalBayar);

            // Buat pesan pop-up
            String message = String.format(
                "Harga: %s\n" +
                "Jumlah: %d\n" +
                "Total: %s × %d = %s\n" +
                "%s: %s × 10%% = %s\n" +
                "Total bayar: %s",
                hargaStr, jumlah, hargaStr, jumlah, totalSebelumDiskonStr,
                (jumlah > 5) ? "Diskon 10%" : "Diskon 10% (tidak berlaku)", totalSebelumDiskonStr, diskonStr, totalBayarStr
            );

            JOptionPane.showMessageDialog(view, message, "Detail Total Bayar", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace(); // Cetak stack trace untuk debugging
            JOptionPane.showMessageDialog(view, "Error menghitung total: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @throws SQLException Jika terjadi error database.
     */
    public void loadTransaksiHariIni(ViewTransaksi view) throws SQLException {
        this.view = view; // Simpan referensi
        view.getTableModel().setRowCount(0); // Kosongkan tabel
        Connection conn = Database.getConnection();
        if (conn == null) {
            throw new SQLException("Gagal terhubung ke database!");
        }
        String sql = "SELECT id_transaksi, nama_pelanggan, nama_obat, harga, jumlah FROM transaksi";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            view.getTableModel().addRow(new Object[]{
                rs.getInt("id_transaksi"),
                rs.getString("nama_pelanggan"),
                rs.getString("nama_obat"),
                rs.getDouble("harga"),
                rs.getInt("jumlah")
            });
        }
    }
}