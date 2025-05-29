package transaksi.View;

import transaksi.Model.ModelTransaksi;
import transaksi.Controller.ControllerTransaksi;
import transaksi.Listener.ListenerTransaksi;
import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;

public class ViewTransaksi extends JFrame implements ActionListener, ListenerTransaksi {
    private JTextField txtNamaPelanggan, txtNamaObat, txtHargaSatuan, txtJumlahBeli;
    private JLabel lblNamaPelanggan, lblNamaObat, lblHargaSatuan, lblJumlahBeli;
    private JButton btnSave, btnReset, btnEdit, btnDelete;
    private JTable tblTransaksi;
    private DefaultTableModel tableModel;
    private ControllerTransaksi controllerTransaksi;
    private ModelTransaksi modelTransaksi;

    /**
     * Konstruktor untuk inisialisasi gui.
     */
    public ViewTransaksi() {
        setTitle("Aplikasi Apotek");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(null);
        setLocationRelativeTo(null); // Posisi jendela di tengah layar

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
        
        btnSave.setBackground(new Color(34, 139, 34)); // Hijau (simpan)
        btnReset.setBackground(new Color(255, 165, 0)); // Oranye (reset)
        btnEdit.setBackground(new Color(30, 144, 255)); // Biru (edit)
        btnDelete.setBackground(new Color(220, 20, 60)); // Merah (hapus)

        String[] columns = {"ID", "Nama Pelanggan", "Nama Obat", "Harga Satuan", "Jumlah Beli"};
        tableModel = new DefaultTableModel(columns, 0);
        tblTransaksi = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tblTransaksi);

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

        btnSave.addActionListener(this);
        btnReset.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        tblTransaksi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblTransaksi.getSelectedRow();
                if (row >= 0) {
                    // Isi form dengan data transaksi yang dipilih untuk edit
                    txtNamaPelanggan.setText((String) tableModel.getValueAt(row, 1));
                    txtNamaObat.setText((String) tableModel.getValueAt(row, 2));
                    txtHargaSatuan.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                    txtJumlahBeli.setText(String.valueOf(tableModel.getValueAt(row, 4)));
                    // Tampilkan total bayar
                    controllerTransaksi.showTotalBayar(row);
                }
            }
        });

        setVisible(true);

        // Inisialisasi MVC
        modelTransaksi = new ModelTransaksi();
        controllerTransaksi = new ControllerTransaksi();
        modelTransaksi.setTransaksiListener(this);
        controllerTransaksi.setModel(modelTransaksi);

        try {
            controllerTransaksi.loadTransaksiHariIni(this);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat transaksi: " + e.getMessage());
        }
    }

    public JTextField getNamaPelanggan() { return txtNamaPelanggan; }
    public JTextField getNamaObat() { return txtNamaObat; }
    public JTextField getHarga() { return txtHargaSatuan; }
    public JTextField getJumlah() { return txtJumlahBeli; }
    public JTable getTblTransaksi() { return tblTransaksi; }
    public DefaultTableModel getTableModel() { return tableModel; }

    @Override
    public void onChange(ModelTransaksi modelTransaksi) {
        txtNamaPelanggan.setText(modelTransaksi.getNamaPelanggan());
        txtNamaObat.setText(modelTransaksi.getNamaObat());
        txtHargaSatuan.setText(String.valueOf(modelTransaksi.getHarga()));
        txtJumlahBeli.setText(String.valueOf(modelTransaksi.getJumlah()));
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
        SwingUtilities.invokeLater(() -> new ViewTransaksi());
    }
}