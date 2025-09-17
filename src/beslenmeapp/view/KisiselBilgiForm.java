package beslenmeapp.view;

import beslenmeapp.dao.KisiselBilgiDAO;

import javax.swing.*;
import java.awt.*;

public class KisiselBilgiForm extends JPanel {

    private final JPanel infoPanel;
    private final JPanel kaloriPanel;
    private final JPanel sonucPanel;
    private final JTextArea motivasyon;

    public KisiselBilgiForm() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // 👤 Kişisel Bilgiler
        infoPanel = new JPanel(new GridLayout(4, 1, 10, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("👤 Kişisel Bilgiler"));
        add(infoPanel);

        // 📊 Kalori Takibi
        kaloriPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        kaloriPanel.setBorder(BorderFactory.createTitledBorder("📊 Kalori Takibi"));
        add(Box.createVerticalStrut(10));
        add(kaloriPanel);

        // 🔻 Kalori Açığı ve Tahmini Kayıp
        sonucPanel = new JPanel(new GridLayout(4, 1, 10, 5));
        sonucPanel.setBorder(BorderFactory.createTitledBorder("🔻 Kalori Açığı & Kilo Kaybı"));
        add(Box.createVerticalStrut(10));
        add(sonucPanel);

        // 📝 Motivasyon
        motivasyon = new JTextArea();
        motivasyon.setWrapStyleWord(true);
        motivasyon.setLineWrap(true);
        motivasyon.setEditable(false);
        motivasyon.setOpaque(false);
        motivasyon.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        add(Box.createVerticalStrut(10));
        add(motivasyon);

        // 🔄 Yenile Butonu
        JButton btnYenile = new JButton("🔄 Yenile");
        btnYenile.addActionListener(e -> verileriYenile());
        btnYenile.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10));
        add(btnYenile);

        // İlk veri yükleme
        verileriYenile();
    }

    private void verileriYenile() {
        infoPanel.removeAll();
        kaloriPanel.removeAll();
        sonucPanel.removeAll();

        // 👤 Kişisel Bilgiler
        infoPanel.add(new JLabel("Ad Soyad: Ferhat Kocaosmanli"));
        infoPanel.add(new JLabel("Yaş / Boy / Kilo: 21 yaş / 177 cm / 92 kg"));
        infoPanel.add(new JLabel("Günlük Kalori İhtiyacı (BMR): " + KisiselBilgiDAO.getBMR() + " kcal"));
        infoPanel.add(new JLabel("Hedef: Kilo Vermek"));

        // 📊 Kalori Takibi
        kaloriPanel.add(new JLabel("🍽️ Günlük Alınan Kalori: " + KisiselBilgiDAO.getGunlukAlinanKalori() + " kcal"));
        kaloriPanel.add(new JLabel("🔥 Günlük Yakılan Kalori: " + KisiselBilgiDAO.getGunlukYakilanKalori() + " kcal"));
        kaloriPanel.add(new JLabel("🍽️ Haftalık Alınan Kalori: " + KisiselBilgiDAO.getHaftalikAlinanKalori() + " kcal"));
        kaloriPanel.add(new JLabel("🔥 Haftalık Yakılan Kalori: " + KisiselBilgiDAO.getHaftalikYakilanKalori() + " kcal"));

        // 🔻 Açık ve Tahmini Kayıp
        sonucPanel.add(new JLabel("🔻 Bugün Kalori Açığı: " + KisiselBilgiDAO.getGunlukKaloriAcigi() + " kcal"));
        sonucPanel.add(new JLabel("🔻 Haftalık Kalori Açığı: " + KisiselBilgiDAO.getHaftalikKaloriAcigi() + " kcal"));
        sonucPanel.add(new JLabel("📆 Haftalık Tahmini Kilo Kaybı: " +
                String.format("%.2f", KisiselBilgiDAO.getHaftalikTahminiKiloKaybi()) + " kg"));
        sonucPanel.add(new JLabel("📅 Aylık Tahmini Kilo Kaybı: " +
                String.format("%.2f", KisiselBilgiDAO.getAylikTahminiKiloKaybi()) + " kg"));

        // 📝 Motivasyon Notu
        motivasyon.setText("📝 Motive Edici Not: Bu şekilde devam edersen bu ay yaklaşık " +
                String.format("%.2f", KisiselBilgiDAO.getAylikTahminiKiloKaybi()) +
                " kg yağ yakımı sağlayabilirsin! 💪");

        revalidate();
        repaint();
    }
}
