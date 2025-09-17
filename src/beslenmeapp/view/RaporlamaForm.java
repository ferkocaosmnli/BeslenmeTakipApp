package beslenmeapp.view;

import beslenmeapp.dao.GenelRaporDAO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RaporlamaForm extends JPanel {
    private JTable ozetTablo;

    public RaporlamaForm() {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#f9f9f9"));
        setBorder(new EmptyBorder(20, 20, 20, 20));

 

        // Orta Panel (grafikler)
        JPanel grafikPanel = new JPanel();
        grafikPanel.setLayout(new GridLayout(2, 1, 20, 20));
        grafikPanel.setBackground(Color.WHITE);

        ChartPanel kaloriChart = createBarChartKalori();
        kaloriChart.setBorder(BorderFactory.createTitledBorder("🔥 Günlük Kalori Dağılımı"));
        grafikPanel.add(kaloriChart);

        ChartPanel proteinChart = createLineChartProtein();
        proteinChart.setBorder(BorderFactory.createTitledBorder("💪 Günlük Protein Değişimi"));
        grafikPanel.add(proteinChart);

        add(grafikPanel, BorderLayout.CENTER);

        // Tablo Paneli (küçültülmüş versiyon)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("📋 Haftalık Makro Tablosu"));

        ozetTablo = new JTable();
        ozetTablo.setFont(new Font("Segoe UI", Font.PLAIN, 12));         // daha küçük yazı
        ozetTablo.setRowHeight(16);                                      // satır yüksekliği düşürüldü
        ozetTablo.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(ozetTablo);
        scrollPane.setPreferredSize(new Dimension(800, 100));            // yüksekliği 120 px'e düşürüldü

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.setPreferredSize(new Dimension(800, 160));            // panelin toplam yüksekliği azaldı

        add(tablePanel, BorderLayout.SOUTH);

        // Verileri yükle
        verileriYukle();
    }

private void verileriYukle() {
    List<Map<String, Object>> list = GenelRaporDAO.getHaftalikOzet();

    // Tarihe göre en yeni en üstte olacak şekilde sırala
    list.sort((a, b) -> {
        Date tarihA = (Date) a.get("tarih");
        Date tarihB = (Date) b.get("tarih");
        return tarihB.compareTo(tarihA); // tersten karşılaştırma
    });

    DefaultTableModel model = new DefaultTableModel(
            new String[]{"Tarih", "Kalori", "Protein", "Karbonhidrat", "Yağ"}, 0
    );
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    for (Map<String, Object> row : list) {
        model.addRow(new Object[]{
                sdf.format(row.get("tarih")),
                row.get("kalori"),
                row.get("protein"),
                row.get("karbonhidrat"),
                row.get("yag")
        });
    }

    ozetTablo.setModel(model);
}
    private ChartPanel createBarChartKalori() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map<String, Object>> veri = GenelRaporDAO.getSon7GunKalorileri();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");

        for (Map<String, Object> row : veri) {
            int kalori = (int) row.get("kalori");
            Date tarih = (Date) row.get("tarih");
            dataset.addValue(kalori, "Kalori", sdf.format(tarih));
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "", "Tarih", "Kalori (kcal)", dataset,
                PlotOrientation.VERTICAL, false, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineVisible(false);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(135, 206, 250));
        renderer.setMaximumBarWidth(0.08);

        return new ChartPanel(chart);
    }

    private ChartPanel createLineChartProtein() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<Map<String, Object>> veri = GenelRaporDAO.getHaftalikOzet();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");

        for (Map<String, Object> row : veri) {
            float protein = Float.parseFloat(row.get("protein").toString());
            Date tarih = (Date) row.get("tarih");
            dataset.addValue(protein, "Protein", sdf.format(tarih));
        }

        JFreeChart chart = ChartFactory.createLineChart(
                "", "Tarih", "Protein (g)", dataset,
                PlotOrientation.VERTICAL, false, true, false
        );

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlineVisible(false);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesPaint(0, new Color(255, 99, 132)); // soft kırmızı
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        
        plot.setRenderer(renderer);

        return new ChartPanel(chart);
    }
}