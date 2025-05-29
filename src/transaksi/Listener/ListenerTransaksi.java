package transaksi.Listener;

import transaksi.Model.ModelTransaksi;

/**
 * Antarmuka untuk menangani perubahan pada model Transaksi.
 */
public interface ListenerTransaksi {
    void onChange(ModelTransaksi ModelTransaksi);
}