package UAP;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class bangunDatarDenganTabell {
    private static ImageIcon backgroundImage = null;
    private static JLabel imageLabel = new JLabel();

    public static void main(String[] args) {

        JFrame frame = new JFrame("Kalkulator Bangun Datar dengan Tabel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600); // Ukuran frame lebih besar
        frame.setLayout(new BorderLayout());


        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());


        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2, 10, 10));
        inputPanel.setOpaque(false);


        JLabel labelBangunDatar = new JLabel("Pilih Bangun Datar:");
        labelBangunDatar.setForeground(Color.WHITE);
        String[] bangunDatar = {"Persegi", "Persegi Panjang", "Lingkaran"};
        JComboBox<String> comboBangunDatar = new JComboBox<>(bangunDatar);

        JLabel labelParameter1 = new JLabel("Sisi / Panjang / Jari-jari:");
        labelParameter1.setForeground(Color.WHITE);
        JTextField textParameter1 = new JTextField();

        JLabel labelParameter2 = new JLabel("Lebar (opsional):");
        labelParameter2.setForeground(Color.WHITE);
        JTextField textParameter2 = new JTextField();

        JButton buttonHitung = new JButton("Hitung");
        JButton buttonHapus = new JButton("Hapus");
        JButton buttonPilihGambar = new JButton("Pilih Gambar");

        inputPanel.add(labelBangunDatar);
        inputPanel.add(comboBangunDatar);
        inputPanel.add(labelParameter1);
        inputPanel.add(textParameter1);
        inputPanel.add(labelParameter2);
        inputPanel.add(textParameter2);
        inputPanel.add(buttonHitung);
        inputPanel.add(buttonHapus);
        inputPanel.add(buttonPilihGambar);

        String[] columnNames = {"Bangun Datar", "Lebar Bangunan", "Tinggi Bangunan", "Luas", "Keliling"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);

        JScrollPane tableScrollPane = new JScrollPane(resultTable);


        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);


                if (isSelected) {
                    cell.setBackground(new Color(51, 153, 255));
                    cell.setForeground(Color.WHITE);
                } else {
                    if (row % 2 == 0) {
                        cell.setBackground(new Color(230, 240, 255));
                    } else {
                        cell.setBackground(new Color(240, 240, 240));
                    }
                    cell.setForeground(Color.BLACK); // Teks hitam
                }
                return cell;
            }
        };

        resultTable.setDefaultRenderer(Object.class, cellRenderer);


        buttonHitung.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBangun = (String) comboBangunDatar.getSelectedItem();
                String parameter1 = textParameter1.getText();
                String parameter2 = textParameter2.getText();

                try {
                    double p1 = Double.parseDouble(parameter1);
                    double p2 = parameter2.isEmpty() ? 0 : Double.parseDouble(parameter2);
                    double luas = 0, keliling = 0;

                    switch (selectedBangun) {
                        case "Persegi":
                            luas = p1 * p1;
                            keliling = 4 * p1;
                            tableModel.addRow(new Object[]{selectedBangun, p1, "-", luas, keliling});
                            break;

                        case "Persegi Panjang":
                            if (parameter2.isEmpty()) {
                                JOptionPane.showMessageDialog(frame, "Lebar harus diisi untuk persegi panjang!", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            luas = p1 * p2;
                            keliling = 2 * (p1 + p2);
                            tableModel.addRow(new Object[]{selectedBangun, p1, p2, luas, keliling});
                            break;

                        case "Lingkaran":
                            luas = Math.PI * p1 * p1;
                            keliling = 2 * Math.PI * p1;
                            tableModel.addRow(new Object[]{selectedBangun, p1, "-", luas, keliling});
                            break;

                        default:
                            JOptionPane.showMessageDialog(frame, "Pilih bangun datar!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    textParameter1.setText("");
                    textParameter2.setText("");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Masukkan angka yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        buttonHapus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int confirm = JOptionPane.showConfirmDialog(frame, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        tableModel.removeRow(selectedRow);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Pilih baris yang ingin dihapus!", "Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });


        buttonPilihGambar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Pilih Gambar");
                fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Gambar JPG & PNG", "jpg", "png"));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {

                        backgroundImage = new ImageIcon(ImageIO.read(selectedFile));
                        imageLabel.setIcon(backgroundImage);
                        backgroundPanel.add(imageLabel, BorderLayout.SOUTH);
                        backgroundPanel.repaint();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(frame, "Gagal memuat gambar!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        backgroundPanel.add(inputPanel, BorderLayout.NORTH);
        backgroundPanel.add(tableScrollPane, BorderLayout.CENTER);

        frame.add(backgroundPanel);

        frame.setVisible(true);
    }
}