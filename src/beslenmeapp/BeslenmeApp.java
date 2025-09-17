package beslenmeapp;

import beslenmeapp.view.OgunEkleForm;
import beslenmeapp.view.KiloKaydiForm;
import beslenmeapp.view.BesinTablosuForm;
import beslenmeapp.view.KaloriYakmaForm;
import beslenmeapp.view.RaporlamaForm;
import beslenmeapp.view.BeslenmeProgramiForm;
import beslenmeapp.view.KisiselBilgiForm;

import javax.swing.*;
import java.awt.*;

public class BeslenmeApp extends JFrame {

    public BeslenmeApp() {
        setTitle("Beslenme Takip Uygulaması");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 700); // Geniş ekran
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Sekmeli Panel
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("🏠 Haftalık Program", new BeslenmeProgramiForm());
        tabbedPane.addTab("🍽️ Yediklerim", new OgunEkleForm());
        tabbedPane.addTab("⚖️ Kilo Ölçümlerim", new KiloKaydiForm());
        tabbedPane.addTab("🔥 Kalori Yakım Takibi", new KaloriYakmaForm());
        tabbedPane.addTab("📋 Besin Listesi", new BesinTablosuForm());
        tabbedPane.addTab("📊 Raporlama", new RaporlamaForm());
        tabbedPane.addTab("👤 Kişisel Bilgi", new KisiselBilgiForm());


        add(tabbedPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        // Nimbus görünümü
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            System.out.println("Tema yüklenemedi.");
        }

        SwingUtilities.invokeLater(() -> new BeslenmeApp().setVisible(true));
    }
}