package beslenmeapp.view;

import beslenmeapp.dao.BesinDAO;
import beslenmeapp.dao.GunlukOgunDAO;
import beslenmeapp.model.Besin;
import beslenmeapp.model.GunlukOgun;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class OgunEkleForm extends JPanel {
    private JComboBox<String> cmbBesinAdi;
    private JTextField txtGram;
    private JButton btnEkle, btnGuncelle, btnSil;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblToplamKalori, lblToplamProtein, lblToplamKarbonhidrat, lblToplamYag;
    private float toplamKalori = 0;
    private float toplamProtein = 0;
    private float toplamKarbonhidrat = 0;
    private float toplamYag = 0;
    private int seciliOgunId = -1;
    private JDateChooser tarihSecici;

    public OgunEkleForm() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("\uD83E\uDD57 Öğün Girişi"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Besin Adı
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Besin Adı:"), gbc);
        gbc.gridx = 1;
        cmbBesinAdi = new JComboBox<>();
        yukleBesinAdlari();
        formPanel.add(cmbBesinAdi, gbc);

        // Gram
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Gram (g):"), gbc);
        gbc.gridx = 1;
        txtGram = new JTextField(8);
        formPanel.add(txtGram, gbc);

        // Butonlar
        gbc.gridy = 2; gbc.gridx = 0;
        btnEkle = new JButton("➕ Ekle");
        formPanel.add(btnEkle, gbc);

        gbc.gridx = 1;
        btnGuncelle = new JButton("🔄 Güncelle");
        formPanel.add(btnGuncelle, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        btnSil = new JButton("🗑️ Sil");
        formPanel.add(btnSil, gbc);

        // Tarih Seçici
        gbc.gridx = 1;
        JPanel tarihPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        tarihPanel.add(new JLabel("📅 Tarih:"));
        tarihSecici = new JDateChooser();
        tarihSecici.setPreferredSize(new Dimension(170, 45));
        tarihSecici.setDate(Date.valueOf(LocalDate.now()));
        tarihSecici.getDateEditor().addPropertyChangeListener(evt -> {
            if ("date".equals(evt.getPropertyName())) {
                ogunleriSecilenTariheGoreYukle();
            }
        });
        tarihPanel.add(tarihSecici);
        formPanel.add(tarihPanel, gbc);

        btnGuncelle.setEnabled(false);
        btnSil.setEnabled(false);
        btnEkle.addActionListener(e -> ogunEkle());
        btnGuncelle.addActionListener(e -> ogunGuncelle());
        btnSil.addActionListener(e -> ogunSil());

        add(formPanel, BorderLayout.NORTH);

        // Tablo
        String[] kolonlar = {"ID", "Besin", "Gram", "Kalori", "Protein", "Karbonhidrat", "Yağ", "Saat"};
        tableModel = new DefaultTableModel(kolonlar, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.removeColumn(table.getColumnModel().getColumn(0));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                seciliOgunId = (int) tableModel.getValueAt(table.getSelectedRow(), 0);
                cmbBesinAdi.setSelectedItem(tableModel.getValueAt(table.getSelectedRow(), 1));
                txtGram.setText(tableModel.getValueAt(table.getSelectedRow(), 2).toString());
                btnGuncelle.setEnabled(true);
                btnSil.setEnabled(true);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        add(scrollPane, BorderLayout.CENTER);

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
        add(toplamWrapper, BorderLayout.SOUTH);

        ogunleriSecilenTariheGoreYukle();
    }

    private void yukleBesinAdlari() {
        List<Besin> besinler = BesinDAO.getAllBesinler();
        for (Besin b : besinler) {
            cmbBesinAdi.addItem(b.getBesinAdi());
        }
    }

    private void ogunEkle() {
        String ad = (String) cmbBesinAdi.getSelectedItem();
        int gram;

        try {
            gram = Integer.parseInt(txtGram.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Geçerli gram girin.");
            return;
        }

        Besin b = BesinDAO.getBesinByAdi(ad);
        if (b == null) {
            JOptionPane.showMessageDialog(this, "⚠️ Besin bulunamadı.");
            return;
        }

        float oran = gram / 100f;
        float kalori = b.getBesin_kalori() * oran;
        float protein = b.getBesin_protein() * oran;
        float karbonhidrat = b.getBesin_karbonhidrat() * oran;
        float yag = b.getBesin_yag() * oran;

        Date secilenTarih = new Date(tarihSecici.getDate().getTime());

        GunlukOgun ogun = new GunlukOgun(
                seciliOgunId,
                secilenTarih,
                Time.valueOf(LocalTime.now()),
                "",
                ad,
                gram,
                (int) kalori,
                protein,
                karbonhidrat,
                yag
        );

        if (GunlukOgunDAO.ogunEkle(ogun)) {
            ogunleriSecilenTariheGoreYukle();
            txtGram.setText("");
        }
    }

    private void ogunGuncelle() {
        if (seciliOgunId == -1) return;

        String ad = (String) cmbBesinAdi.getSelectedItem();
        int gram;

        try {
            gram = Integer.parseInt(txtGram.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "⚠️ Geçerli gram girin.");
            return;
        }

        Besin b = BesinDAO.getBesinByAdi(ad);
        if (b == null) return;

        float oran = gram / 100f;
        float kalori = b.getBesin_kalori() * oran;
        float protein = b.getBesin_protein() * oran;
        float karbonhidrat = b.getBesin_karbonhidrat() * oran;
        float yag = b.getBesin_yag() * oran;

        Date secilenTarih = new Date(tarihSecici.getDate().getTime());

        GunlukOgun ogun = new GunlukOgun(
                seciliOgunId,
                secilenTarih,
                Time.valueOf(LocalTime.now()),
                "",
                ad,
                gram,
                (int) kalori,
                protein,
                karbonhidrat,
                yag
        );

        if (GunlukOgunDAO.guncelleOgun(ogun)) {
            ogunleriSecilenTariheGoreYukle();
            txtGram.setText("");
            seciliOgunId = -1;
            btnGuncelle.setEnabled(false);
            btnSil.setEnabled(false);
        }
    }

    private void ogunSil() {
        if (seciliOgunId == -1) return;

        if (GunlukOgunDAO.silOgun(seciliOgunId)) {
            ogunleriSecilenTariheGoreYukle();
            txtGram.setText("");
            seciliOgunId = -1;
            btnGuncelle.setEnabled(false);
            btnSil.setEnabled(false);
        }
    }

    private void ogunleriSecilenTariheGoreYukle() {
        Date secilenTarih = new Date(tarihSecici.getDate().getTime());
        List<GunlukOgun> ogunler = GunlukOgunDAO.getOgunlerByTarih(secilenTarih);
        tableModel.setRowCount(0);
        toplamKalori = toplamProtein = toplamKarbonhidrat = toplamYag = 0;

        for (GunlukOgun o : ogunler) {
            Object[] row = {
                    o.getId(),
                    o.getBesin_adi(),
                    o.getMiktar_gr(),
                    o.getKalori(),
                    o.getProtein(),
                    o.getKarbonhidrat(),
                    o.getYag(),
                    o.getSaat()
            };
            tableModel.addRow(row);
            toplamKalori += o.getKalori();
            toplamProtein += o.getProtein();
            toplamKarbonhidrat += o.getKarbonhidrat();
            toplamYag += o.getYag();
        }
        guncelleToplamEtiketleri();
    }

    private void guncelleToplamEtiketleri() {
        lblToplamKalori.setText("Toplam Kalori: " + Math.round(toplamKalori) + " kcal");
        lblToplamProtein.setText(String.format("Toplam Protein: %.1f g", toplamProtein));
        lblToplamKarbonhidrat.setText(String.format("Toplam Karbonhidrat: %.1f g", toplamKarbonhidrat));
        lblToplamYag.setText(String.format("Toplam Yağ: %.1f g", toplamYag));
    }
}
