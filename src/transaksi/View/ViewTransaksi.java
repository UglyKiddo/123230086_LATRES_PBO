package transaksi.View;

import transaksi.Controller.ControllerTransaksi;
import transaksi.Listener.ListenerTransaksi;
import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import transaksi.Model.ModelTransaksi;
import transaksi.Model.Transaksi;

/**
 * Kelas ViewTransaksi menyediakan antarmuka pengguna untuk aplikasi apotek.
 * Menampilkan form input dan tabel transaksi aktif.
 */
public abstract class ViewTransaksi extends JFrame implements ActionListener, ListenerTransaksi {
    private JTextField txtNamaPelanggan, txtNamaObat, txtHargaSatuan, txtJumlahBeli;
    private JLabel lblNamaPelanggan, lblNamaObat, lblHargaSatuan, lblJumlahBeli;
    private JButton btnSave, btnReset, btnEdit, btnDelete;
    private JTable tblTransaksi;
    private DefaultTableModel tableModel;
    private ControllerTransaksi controllerTransaksi;
    private Transaksi modelTransaksi;

    public ViewTransaksi() {
        setTitle("Aplikasi Apotek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(null);

        // Inisialisasi komponen form
        lblNamaPelanggan = new JLabel("Nama Pelanggan");
        txtNamaPelanggan = new JTextField(20);
        lblNamaObat = new JLabel("Nama Obat");
        txtNamaObat = new JTextField(20);
        lblHargaSatuan = new JLabel("Harga Satuan");
        txtHargaSatuan = new JTextField(20);
        lblJumlahBeli = new JLabel("Jumlah Beli");
        txtJumlahBeli = new JTextField(20);
        btnSave = new JButton("Simpan");
        btnReset = new JButton("Reset");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Hapus");

        // Inisialisasi tabel transaksi
        String[] columns = {"ID", "Nama Pelanggan", "Nama Obat", "Harga Satuan", "Jumlah Beli", "Tanggal"};
        tableModel = new DefaultTableModel(columns, 0);
        tblTransaksi = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblTransaksi);

        // Atur posisi komponen
        lblNamaPelanggan.setBounds(10, 10, 120, 20);
        txtNamaPelanggan.setBounds(130, 10, 200, 20);
        lblNamaObat.setBounds(10, 35, 120, 20);
        txtNamaObat.setBounds(130, 35, 200, 20);
        lblHargaSatuan.setBounds(10, 60, 120, 20);
        txtHargaSatuan.setBounds(130, 60, 200, 20);
        lblJumlahBeli.setBounds(10, 85, 120, 20);
        txtJumlahBeli.setBounds(130, 85, 200, 20);
        btnSave.setBounds(130, 110, 80, 20);
        btnReset.setBounds(220, 110, 80, 20);
        btnEdit.setBounds(310, 110, 80, 20);
        btnDelete.setBounds(400, 110, 80, 20);
        scrollPane.setBounds(10, 140, 560, 200);

        // Tambah komponen ke frame
        add(lblNamaPelanggan);
        add(txtNamaPelanggan);
        add(lblNamaObat);
        add(txtNamaObat);
        add(lblHargaSatuan);
        add(txtHargaSatuan);
        add(lblJumlahBeli);
        add(txtJumlahBeli);
        add(btnSave);
        add(btnReset);
        add(btnEdit);
        add(btnDelete);
        add(scrollPane);

        // Tambah listener untuk tombol
        btnSave.addActionListener(this);
        btnReset.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        tblTransaksi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblTransaksi.getSelectedRow();
                if (row >= 0) {
                    controllerTransaksi.showTotalBayar(row);
                }
            }
        });

        setVisible(true);

        // Inisialisasi MVC
        modelTransaksi = new Transaksi();
        controllerTransaksi = new ControllerTransaksi();
        modelTransaksi.setTransaksiListener(this);
        controllerTransaksi.setModel(modelTransaksi);

        // Muat transaksi hari ini
        try {
            controllerTransaksi.loadTransaksiHariIni(this);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat transaksi: " + e.getMessage());
        }
    }

    // Getter untuk input
    public JTextField getNamaPelanggan() { return txtNamaPelanggan; }
    public JTextField getNamaObat() { return txtNamaObat; }
    public JTextField getHarga() { return txtHargaSatuan; }
    public JTextField getJumlah() { return txtJumlahBeli; }
    public JTable getTblTransaksi() { return tblTransaksi; }
    public DefaultTableModel getTableModel() { return tableModel; }

    // Implementasi ListenerTransaksi
    @Override
    public void onChange(Transaksi modelTransaksi) {
        txtNamaPelanggan.setText(modelTransaksi.getNamaPelanggan());
        txtNamaObat.setText(modelTransaksi.getNamaObat());
        txtHargaSatuan.setText(String.valueOf(modelTransaksi.getHargaSatuan()));
        txtJumlahBeli.setText(String.valueOf(modelTransaksi.getJumlahBeli()));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSave) {
            controllerTransaksi.submitForm(this);
        } else if (e.getSource() == btnReset) {
            controllerTransaksi.resetForm(this);
        } else if (e.getSource() == btnEdit) {
            controllerTransaksi.editForm(this);
        } else if (e.getSource() == btnDelete) {
            controllerTransaksi.deleteForm(this);
        }
    }

    public static void main(String[] args) {
        new ViewTransaksi() {
            @Override
            public void onChange(ModelTransaksi ModelTransaksi) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
        };
    }
}