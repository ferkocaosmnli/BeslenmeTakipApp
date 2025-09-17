package beslenmeapp.view;

import beslenmeapp.dao.BesinDAO;
import beslenmeapp.model.Besin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BesinTablosuForm extends JPanel {
    private JTable besinTablosu;
    private DefaultTableModel tabloModeli;
    private JTextField txtAdi, txtGram, txtKalori, txtProtein, txtKarbonhidrat, txtYag;
    private JButton ekleButon, guncelleButon, silButon, yenileButon;
    private int seciliBesinId = -1;

    public BesinTablosuForm() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));



        // Tablo
        String[] kolonlar = {"ID", "Besin Adı", "Gram", "Kalori", "Protein", "Karbonhidrat", "Yağ"};
        tabloModeli = new DefaultTableModel(kolonlar, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        besinTablosu = new JTable(tabloModeli);
        besinTablosu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        besinTablosu.getSelectionModel().addListSelectionListener(e -> satirSecildi());
        JScrollPane scrollPane = new JScrollPane(besinTablosu);
        add(scrollPane, BorderLayout.CENTER);

        // Giriş alanları
        JPanel girisPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        txtAdi = new JTextField();
        txtGram = new JTextField();
        txtKalori = new JTextField();
        txtProtein = new JTextField();
        txtKarbonhidrat = new JTextField();
        txtYag = new JTextField();

        girisPanel.add(new JLabel("Besin Adı:"));
        girisPanel.add(new JLabel("Gram:"));
        girisPanel.add(new JLabel("Kalori:"));
        girisPanel.add(new JLabel("Protein:"));
        girisPanel.add(new JLabel("Karbonhidrat:"));
        girisPanel.add(new JLabel("Yağ:"));

        girisPanel.add(txtAdi);
        girisPanel.add(txtGram);
        girisPanel.add(txtKalori);
        girisPanel.add(txtProtein);
        girisPanel.add(txtKarbonhidrat);
        girisPanel.add(txtYag);

        add(girisPanel, BorderLayout.NORTH);

        // Butonlar
        JPanel butonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        ekleButon = new JButton("➕ Ekle");
        guncelleButon = new JButton("🔄 Güncelle");
        silButon = new JButton("🗑️ Sil");
        yenileButon = new JButton("↻ Listeyi Yenile");

        butonPanel.add(ekleButon);
        butonPanel.add(guncelleButon);
        butonPanel.add(silButon);
        butonPanel.add(yenileButon);

        add(butonPanel, BorderLayout.SOUTH);

        // Aksiyonlar
        ekleButon.addActionListener(e -> besinEkle());
        guncelleButon.addActionListener(e -> besinGuncelle());
        silButon.addActionListener(e -> besinSil());
        yenileButon.addActionListener(e -> besinleriYukle());

        // İlk yükleme
        besinleriYukle();
    }

    private void besinleriYukle() {
        tabloModeli.setRowCount(0);
        List<Besin> besinler = BesinDAO.getAllBesinler();
        for (Besin b : besinler) {
            tabloModeli.addRow(new Object[]{
                b.getBesin_id(),
                b.getBesinAdi(),
                b.getBesin_gram(),
                b.getBesin_kalori(),
                b.getBesin_protein(),
                b.getBesin_karbonhidrat(),
                b.getBesin_yag()
            });
        }
    }

    private void satirSecildi() {
        int secili = besinTablosu.getSelectedRow();
        if (secili != -1) {
            seciliBesinId = (int) tabloModeli.getValueAt(secili, 0);
            txtAdi.setText(tabloModeli.getValueAt(secili, 1).toString());
            txtGram.setText(tabloModeli.getValueAt(secili, 2).toString());
            txtKalori.setText(tabloModeli.getValueAt(secili, 3).toString());
            txtProtein.setText(tabloModeli.getValueAt(secili, 4).toString());
            txtKarbonhidrat.setText(tabloModeli.getValueAt(secili, 5).toString());
            txtYag.setText(tabloModeli.getValueAt(secili, 6).toString());
        }
    }

    private void besinEkle() {
        try {
            Besin b = parseBesinInput(0);
            if (BesinDAO.besinEkle(b)) {
                JOptionPane.showMessageDialog(this, "✅ Besin eklendi!");
                temizleForm();
                besinleriYukle();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Hatalı giriş: " + ex.getMessage());
        }
    }

    private void besinGuncelle() {
        if (seciliBesinId == -1) return;
        try {
            Besin b = parseBesinInput(seciliBesinId);
            if (BesinDAO.besinGuncelle(b)) {
                JOptionPane.showMessageDialog(this, "🔄 Güncellendi!");
                temizleForm();
                besinleriYukle();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "⚠️ Güncelleme hatası: " + ex.getMessage());
        }
    }

    private void besinSil() {
        if (seciliBesinId == -1) return;
        int onay = JOptionPane.showConfirmDialog(this, "Silmek istediğinize emin misiniz?", "Onay", JOptionPane.YES_NO_OPTION);
        if (onay == JOptionPane.YES_OPTION) {
            if (BesinDAO.besinSil(seciliBesinId)) {
                JOptionPane.showMessageDialog(this, "🗑️ Silindi.");
                temizleForm();
                besinleriYukle();
            }
        }
    }

    private Besin parseBesinInput(int id) throws Exception {
        String adi = txtAdi.getText().trim();
        int gram = Integer.parseInt(txtGram.getText().trim());
        int kalori = Integer.parseInt(txtKalori.getText().trim());
        float protein = Float.parseFloat(txtProtein.getText().trim());
        float karbonhidrat = Float.parseFloat(txtKarbonhidrat.getText().trim());
        float yag = Float.parseFloat(txtYag.getText().trim());

        if (adi.isEmpty() || gram <= 0 || kalori <= 0)
            throw new Exception("Boş alan bırakmayınız veya sıfır değer girmeyiniz");

        return new Besin(id, adi, gram, kalori, protein, karbonhidrat, yag);
    }

    private void temizleForm() {
        txtAdi.setText("");
        txtGram.setText("");
        txtKalori.setText("");
        txtProtein.setText("");
        txtKarbonhidrat.setText("");
        txtYag.setText("");
        seciliBesinId = -1;
    }
}