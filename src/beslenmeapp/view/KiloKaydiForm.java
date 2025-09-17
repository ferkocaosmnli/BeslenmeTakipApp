package beslenmeapp.view;

import beslenmeapp.dao.KiloKaydiDAO;
import beslenmeapp.model.KiloKaydi;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class KiloKaydiForm extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField kiloField;
    private JTextArea notlarArea;
    private JButton btnEkle, btnGuncelle, btnSil;
    private int seciliId = -1;

    public KiloKaydiForm() {
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 40, 20, 40));

        

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Yeni Kilo Kaydı"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Kilo
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Kilo (kg):"), gbc);

        gbc.gridx = 1;
        kiloField = new JTextField(10);
        formPanel.add(kiloField, gbc);

        // Notlar
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Notlar:"), gbc);

        gbc.gridx = 1;
        notlarArea = new JTextArea(3, 20);
        notlarArea.setLineWrap(true);
        notlarArea.setWrapStyleWord(true);
        JScrollPane notScroll = new JScrollPane(notlarArea);
        formPanel.add(notScroll, gbc);

        // Butonlar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnEkle = new JButton("Ekle");
        btnGuncelle = new JButton("Güncelle");
        btnSil = new JButton("Sil");

        btnGuncelle.setEnabled(false);
        btnSil.setEnabled(false);

        buttonPanel.add(btnEkle);
        buttonPanel.add(btnGuncelle);
        buttonPanel.add(btnSil);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.WEST);

        // Tablo
        tableModel = new DefaultTableModel(new String[]{"Tarih", "Kilo", "Notlar"}, 0);
        table = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Kayıt Listesi"));
        add(tableScroll, BorderLayout.CENTER);

        // Tabloyu doldur
        tabloyuYukle();

        // Seçim olayı
        table.getSelectionModel().addListSelectionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected != -1) {
                Timestamp tarih = (Timestamp) tableModel.getValueAt(selected, 0);
                double kilo = (double) tableModel.getValueAt(selected, 1);
                String notlar = (String) tableModel.getValueAt(selected, 2);

                KiloKaydi secili = KiloKaydiDAO.getKayitByTarih(tarih);
                if (secili != null) {
                    seciliId = secili.getId();
                    kiloField.setText(String.valueOf(kilo));
                    notlarArea.setText(notlar);
                    btnGuncelle.setEnabled(true);
                    btnSil.setEnabled(true);
                }
            }
        });

        // Buton olayları
        btnEkle.addActionListener(e -> kiloEkle());
        btnGuncelle.addActionListener(e -> kiloGuncelle());
        btnSil.addActionListener(e -> kiloSil());
    }

    private void tabloyuYukle() {
        tableModel.setRowCount(0);
        List<KiloKaydi> kayitlar = KiloKaydiDAO.getTumKayitlar();
        for (KiloKaydi k : kayitlar) {
            tableModel.addRow(new Object[]{k.getTarih(), k.getKilo(), k.getNotlar()});
        }
    }

    private void kiloEkle() {
        try {
            double kilo = Double.parseDouble(kiloField.getText().trim());
            String not = notlarArea.getText().trim();
            Timestamp simdi = Timestamp.valueOf(LocalDateTime.now());
            KiloKaydi yeni = new KiloKaydi(simdi, kilo, not);
            if (KiloKaydiDAO.kiloEkle(yeni)) {
                tabloyuYukle();
                temizle();
            } else {
                JOptionPane.showMessageDialog(this, "Kayıt eklenemedi.");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Geçerli bir kilo girin.");
        }
    }

    private void kiloGuncelle() {
        if (seciliId == -1) return;
        try {
            double kilo = Double.parseDouble(kiloField.getText().trim());
            String not = notlarArea.getText().trim();
            Timestamp simdi = Timestamp.valueOf(LocalDateTime.now());
            KiloKaydi guncel = new KiloKaydi(seciliId, simdi, kilo, not);
            if (KiloKaydiDAO.kiloGuncelle(guncel)) {
                tabloyuYukle();
                temizle();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Geçerli bir kilo girin.");
        }
    }

    private void kiloSil() {
        if (seciliId == -1) return;
        int onay = JOptionPane.showConfirmDialog(this, "Silmek istediğinize emin misiniz?", "Silme", JOptionPane.YES_NO_OPTION);
        if (onay == JOptionPane.YES_OPTION) {
            if (KiloKaydiDAO.kiloSil(seciliId)) {
                tabloyuYukle();
                temizle();
            }
        }
    }

    private void temizle() {
        kiloField.setText("");
        notlarArea.setText("");
        seciliId = -1;
        btnGuncelle.setEnabled(false);
        btnSil.setEnabled(false);
        table.clearSelection();
    }
}
