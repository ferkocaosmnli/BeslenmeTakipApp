package beslenmeapp.view;

import beslenmeapp.dao.KaloriYakmaDAO;
import beslenmeapp.model.KaloriYakma;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class KaloriYakmaForm extends JPanel {
    private JTextField txtSure, txtKalori;
    private JTextArea txtAciklama;
    private JButton btnEkle, btnSil;
    private JTable table;
    private DefaultTableModel tableModel;
    private int seciliId = -1;

    public KaloriYakmaForm() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 30, 20, 30));

       

        JPanel girisPanel = new JPanel(new GridBagLayout());
        girisPanel.setBorder(BorderFactory.createTitledBorder("📋 Yeni Kayıt"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        girisPanel.add(new JLabel("Süre (dk):"), gbc);
        gbc.gridx = 1;
        txtSure = new JTextField(10);
        girisPanel.add(txtSure, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        girisPanel.add(new JLabel("Kalori (kcal):"), gbc);
        gbc.gridx = 1;
        txtKalori = new JTextField(10);
        girisPanel.add(txtKalori, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        girisPanel.add(new JLabel("Açıklama:"), gbc);
        gbc.gridx = 1;
        txtAciklama = new JTextArea(3, 20);
        txtAciklama.setLineWrap(true);
        txtAciklama.setWrapStyleWord(true);
        girisPanel.add(new JScrollPane(txtAciklama), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        btnEkle = new JButton("➕ Ekle");
        girisPanel.add(btnEkle, gbc);

        gbc.gridx = 1;
        btnSil = new JButton("🗑️ Sil");
        btnSil.setEnabled(false);
        girisPanel.add(btnSil, gbc);

        add(girisPanel, BorderLayout.WEST);

        tableModel = new DefaultTableModel(new Object[]{"Tarih", "Süre (dk)", "Kalori", "Açıklama"}, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        tabloyuYenile();

        btnEkle.addActionListener(e -> kayitEkle());
        btnSil.addActionListener(e -> kayitSil());

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                seciliId = KaloriYakmaDAO.getTumKayitlar().get(row).getId();
                txtSure.setText(table.getValueAt(row, 1).toString());
                txtKalori.setText(table.getValueAt(row, 2).toString());
                txtAciklama.setText(table.getValueAt(row, 3).toString());
                btnSil.setEnabled(true);
            }
        });
    }

    private void kayitEkle() {
        try {
            int sure = Integer.parseInt(txtSure.getText().trim());
            double kalori = Double.parseDouble(txtKalori.getText().trim());
            String aciklama = txtAciklama.getText().trim();
            Timestamp simdi = Timestamp.valueOf(LocalDateTime.now());

            KaloriYakma kayit = new KaloriYakma(simdi, "Aktivite", sure, kalori, aciklama);
            boolean basarili = KaloriYakmaDAO.kaloriEkle(kayit);

            if (basarili) {
                tabloyuYenile();
                txtSure.setText("");
                txtKalori.setText("");
                txtAciklama.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Kayıt eklenemedi.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Lütfen geçerli sayılar girin.");
        }
    }

    private void kayitSil() {
        if (seciliId == -1) return;
        int onay = JOptionPane.showConfirmDialog(this, "Kaydı silmek istiyor musunuz?", "Onay", JOptionPane.YES_NO_OPTION);
        if (onay == JOptionPane.YES_OPTION) {
            if (KaloriYakmaDAO.kaloriSil(seciliId)) {
                tabloyuYenile();
                txtSure.setText("");
                txtKalori.setText("");
                txtAciklama.setText("");
                seciliId = -1;
                btnSil.setEnabled(false);
            }
        }
    }

    private void tabloyuYenile() {
        tableModel.setRowCount(0);
        List<KaloriYakma> liste = KaloriYakmaDAO.getTumKayitlar();
        for (KaloriYakma k : liste) {
            tableModel.addRow(new Object[]{
                    k.getTarih(),
                    k.getSureDk(),
                    k.getYakilanKalori(),
                    k.getNotlar()
            });
        }
    }
}
