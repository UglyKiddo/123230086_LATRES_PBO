package transaksi.Model;

import transaksi.View.ViewTransaksi;
import transaksi.Listener.ListenerTransaksi;
import javax.swing.JOptionPane;

/**
 * Kelas Transaksi merepresentasikan data transaksi penjualan obat.
 * Mengelola atribut transaksi dan perhitungan total bayar dengan diskon.
 */
public class ModelTransaksi {
    private int id_transaksi;
    private String nama_pelanggan;
    private String nama_obat;
    private double harga;
    private int jumlah;
    private ListenerTransaksi listenerTransaksi;

    // Mengatur listener untuk memberi tahu View tentang perubahan
    protected void fireOnChange() {
        if (listenerTransaksi != null) {
            listenerTransaksi.onChange(this);
        }
    }

    // Getter dan Setter untuk listener
    public ListenerTransaksi getTransaksiListener() {
        return listenerTransaksi;
    }

    public void setTransaksiListener(ListenerTransaksi listenerTransaksi) {
        this.listenerTransaksi = listenerTransaksi;
    }

    // Getter dan Setter untuk atribut
    public int getIdTransaksi() {
        return id_transaksi;
    }

    public void setIdTransaksi(int id_transaksi) {
        this.id_transaksi = id_transaksi;
        fireOnChange();
    }

    public String getNamaPelanggan() {
        return nama_pelanggan;
    }

    public void setNamaPelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
        fireOnChange();
    }

    public String getNamaObat() {
        return nama_obat;
    }

    public void setNamaObat(String nama_obat) {
        this.nama_obat = nama_obat;
        fireOnChange();
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
        fireOnChange();
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
        fireOnChange();
    }

    /**
     * Menghitung total harga dengan diskon 10% jika jumlah beli lebih dari 5.
     * @return Total bayar setelah diskon.
     */
    public double hitungTotalBayar() {
        double total = harga * jumlah;
        if (jumlah > 5) {
            total *= 0.9; // Diskon 10%
        }
        return total;
    }

    /**
     * Mengosongkan data transaksi di form.
     */
    public void resetForm() {
        setNamaPelanggan("");
        setNamaObat("");
        setHarga(0.0);
        setJumlah(0);
    }

    /**
     * Menampilkan pesan konfirmasi saat transaksi disimpan.
     * @param viewTransaksi View yang digunakan untuk mendapatkan data input.
     */
    public void submitForm(ViewTransaksi viewTransaksi) {
        JOptionPane.showMessageDialog(null, "Transaksi untuk " + viewTransaksi.getNamaPelanggan().getText() + 
            " (" + viewTransaksi.getNamaObat().getText() + ") berhasil disimpan!");
    }
}