package transaksi.Model;

import transaksi.View.ViewTransaksi;
import transaksi.Listener.ListenerTransaksi;
import javax.swing.JOptionPane;

public class ModelTransaksi {
    private int idTransaksi;
    private String nama_pelanggan;
    private String nama_obat;
    private double harga;
    private int jumlah;
    private ListenerTransaksi listenerTransaksi;

    protected void fireOnChange() {
        if (listenerTransaksi != null) {
            listenerTransaksi.onChange(this);
        }
    }

    public ListenerTransaksi getTransaksiListener() {
        return listenerTransaksi;
    }

    public void setTransaksiListener(ListenerTransaksi listenerTransaksi) {
        this.listenerTransaksi = listenerTransaksi;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
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


    public double hitungTotalBayar() {
        double total = harga * jumlah;
        if (jumlah > 5) {
            total *= 0.9; // Diskon 10%
        }
        return total;
    }

    public void resetForm() {
        setNamaPelanggan("");
        setNamaObat("");
        setHarga(0.0);
        setJumlah(0);
    }

    public void submitForm(ViewTransaksi view) {
        JOptionPane.showMessageDialog(view, "Transaksi untuk " + view.getNamaPelanggan().getText() + 
            " (" + view.getNamaObat().getText() + ") berhasil disimpan!");
    }
}