package beslenmeapp.view;

import beslenmeapp.dao.BesinDAO;
import beslenmeapp.dao.BeslenmePlaniDAO;
import beslenmeapp.model.Besin;
import beslenmeapp.model.BeslenmePlani;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BeslenmeProgramiForm extends JPanel {
    private JList<String> gunListesi;
    private JTable tablo;
    private DefaultTableModel tabloModeli;
    private JComboBox<String> ogunTuruCombo, besinCombo;
    private JTextField miktarField;
    private JButton ekleButon, silButon, guncelleButon;
    private JLabel lblToplamKalori, lblToplamProtein, lblToplamKarbonhidrat, lblToplamYag;
    private int seciliPlanId = -1;

    public BeslenmeProgramiForm() {
        setLayout(new BorderLayout(10, 10));

        // Sol: Gün listesi
        gunListesi = new JList<>(new String[]{
            "Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi", "Pazar"
        });
        gunListesi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gunListesi.setSelectedIndex(0);
        gunListesi.addListSelectionListener(e -> seciliGunuYukle());
        add(new JScrollPane(gunListesi), BorderLayout.WEST);

        // Orta: Tablo
        tabloModeli = new DefaultTableModel(
            new String[]{"ID", "Öğün", "Besin", "Miktar (g)", "Kalori", "Protein", "Karbonhidrat", "Yağ"}, 0
        );
        tablo = new JTable(tabloModeli);
        tablo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablo.removeColumn(tablo.getColumnModel().getColumn(0)); // ID gizle
        tablo.getSelectionModel().addListSelectionListener(e -> satirSecildi());

        JScrollPane scrollPane = new JScrollPane(tablo);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        add(scrollPane, BorderLayout.CENTER);

        // Alt: Besin Ekleme Paneli
        JPanel altPanel = new JPanel(new FlowLayout());

        ogunTuruCombo = new JComboBox<>(new String[]{"Kahvaltı", "Öğle", "Akşam", "Ara Öğün"});
        besinCombo = new JComboBox<>();
        miktarField = new JTextField(5);
        ekleButon = new JButton("➕ Ekle");
        silButon = new JButton("🗑️ Sil");
        guncelleButon = new JButton("🔄 Güncelle");

        altPanel.add(new JLabel("Öğün:"));
        altPanel.add(ogunTuruCombo);
        altPanel.add(new JLabel("Besin:"));
        altPanel.add(besinCombo);
        altPanel.add(new JLabel("Gram:"));
        altPanel.add(miktarField);
        altPanel.add(ekleButon);
        altPanel.add(guncelleButon);
        altPanel.add(silButon);

        add(altPanel, BorderLayout.SOUTH);

        JPanel toplamPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        lblToplamKalori = new JLabel("Toplam Kalori: 0 kcal");
        lblToplamProtein = new JLabel("Toplam Protein: 0 g");
        lblToplamKarbonhidrat = new JLabel("Toplam Karbonhidrat: 0 g");
        lblToplamYag = new JLabel("Toplam Yağ: 0 g");

        toplamPanel.add(lblToplamKalori);
        toplamPanel.add(lblToplamProtein);
        toplamPanel.add(lblToplamKarbonhidrat);
        toplamPanel.add(lblToplamYag);

        JPanel toplamWrapper = new JPanel(new BorderLayout());
        toplamWrapper.setBorder(BorderFactory.createTitledBorder("🔢 Günlük Toplam"));
        toplamWrapper.add(toplamPanel, BorderLayout.CENTER);
        add(toplamWrapper, BorderLayout.NORTH);

        ekleButon.addActionListener(e -> planEkle());
        guncelleButon.addActionListener(e -> planGuncelle());
        silButon.addActionListener(e -> planSil());

        besinleriYukle();
        seciliGunuYukle();
    }

    private void besinleriYukle() {
        List<Besin> besinler = BesinDAO.getAllBesinler();
        besinCombo.removeAllItems();
        for (Besin b : besinler) {
            besinCombo.addItem(b.getBesinAdi());
        }
    }

    private void seciliGunuYukle() {
        String gun = gunListesi.getSelectedValue();
        tabloModeli.setRowCount(0);
        List<BeslenmePlani> liste = BeslenmePlaniDAO.getPlanlarByGun(gun);
        float toplamKal = 0, toplamPr = 0, toplamKar = 0, toplamYag = 0;
        for (BeslenmePlani p : liste) {
            tabloModeli.addRow(new Object[]{
                p.getId(),
                p.getOgunTuru(),
                p.getBesinAdi(),
                p.getMiktarGr(),
                p.getKalori(),
                p.getProtein(),
                p.getKarbonhidrat(),
                p.getYag()
            });
            toplamKal += p.getKalori();
            toplamPr += p.getProtein();
            toplamKar += p.getKarbonhidrat();
            toplamYag += p.getYag();
        }
        lblToplamKalori.setText("Toplam Kalori: " + (int) toplamKal + " kcal");
        lblToplamProtein.setText(String.format("Toplam Protein: %.1f g", toplamPr));
        lblToplamKarbonhidrat.setText(String.format("Toplam Karbonhidrat: %.1f g", toplamKar));
        lblToplamYag.setText(String.format("Toplam Yağ: %.1f g", toplamYag));
    }

    private void satirSecildi() {
        int seciliRow = tablo.getSelectedRow();
        if (seciliRow != -1) {
            seciliPlanId = (int) tabloModeli.getValueAt(seciliRow, 0);
            ogunTuruCombo.setSelectedItem(tabloModeli.getValueAt(seciliRow, 1));
            besinCombo.setSelectedItem(tabloModeli.getValueAt(seciliRow, 2));
            miktarField.setText(tabloModeli.getValueAt(seciliRow, 3).toString());
        }
    }

    private void planEkle() {
        String gun = gunListesi.getSelectedValue();
        String ogun = (String) ogunTuruCombo.getSelectedItem();
        String besinAdi = (String) besinCombo.getSelectedItem();
        int miktar;

        try {
            miktar = Integer.parseInt(miktarField.getText().trim());
            if (miktar <= 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Geçerli bir miktar giriniz.");
            return;
        }

        Besin b = BesinDAO.getBesinByAdi(besinAdi);
        if (b == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Besin bulunamadı.");
            return;
        }

        float oran = miktar / 100f;
        int kalori = Math.round(b.getBesin_kalori() * oran);
        float protein = b.getBesin_protein() * oran;
        float karbonhidrat = b.getBesin_karbonhidrat() * oran;
        float yag = b.getBesin_yag() * oran;

        BeslenmePlani plan = new BeslenmePlani(0, gun, ogun, besinAdi, miktar, kalori, protein, karbonhidrat, yag);

        if (BeslenmePlaniDAO.planiEkle(plan)) {
            miktarField.setText("");
            seciliPlanId = -1;
            seciliGunuYukle();
        }
    }

    private void planGuncelle() {
        if (seciliPlanId == -1) return;

        String gun = gunListesi.getSelectedValue();
        String ogun = (String) ogunTuruCombo.getSelectedItem();
        String besinAdi = (String) besinCombo.getSelectedItem();
        int miktar;

        try {
            miktar = Integer.parseInt(miktarField.getText().trim());
            if (miktar <= 0) throw new Exception();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Geçerli bir miktar giriniz.");
            return;
        }

        Besin b = BesinDAO.getBesinByAdi(besinAdi);
        if (b == null) return;

        float oran = miktar / 100f;
        int kalori = Math.round(b.getBesin_kalori() * oran);
        float protein = b.getBesin_protein() * oran;
        float karbonhidrat = b.getBesin_karbonhidrat() * oran;
        float yag = b.getBesin_yag() * oran;

        BeslenmePlani plan = new BeslenmePlani(seciliPlanId, gun, ogun, besinAdi, miktar, kalori, protein, karbonhidrat, yag);

        if (BeslenmePlaniDAO.planiGuncelle(plan)) {
            miktarField.setText("");
            seciliPlanId = -1;
            seciliGunuYukle();
        }
    }

    private void planSil() {
        if (seciliPlanId == -1) return;

        if (BeslenmePlaniDAO.planiSil(seciliPlanId)) {
            miktarField.setText("");
            seciliPlanId = -1;
            seciliGunuYukle();
        }
    }
}
