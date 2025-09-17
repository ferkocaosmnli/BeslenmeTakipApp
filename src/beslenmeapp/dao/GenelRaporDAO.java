package beslenmeapp.dao;

import java.sql.*;
import java.util.*;

public class GenelRaporDAO {

    // 1. Günlük toplam makrolar
    public static Map<String, Object> getGunlukToplamlar(java.sql.Date tarih) {
        String sql = "SELECT SUM(kalori) as toplam_kalori, " +
                     "SUM(protein) as toplam_protein, " +
                     "SUM(karbonhidrat) as toplam_karbonhidrat, " +
                     "SUM(yag) as toplam_yag " +
                     "FROM gunlukogunler WHERE tarih = ?";

        Map<String, Object> map = new HashMap<>();

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, tarih);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                map.put("kalori", rs.getInt("toplam_kalori"));
                map.put("protein", rs.getFloat("toplam_protein"));
                map.put("karbonhidrat", rs.getFloat("toplam_karbonhidrat"));
                map.put("yag", rs.getFloat("toplam_yag"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return map;
    }

    // 2. Son 7 günün kalori bilgisi (grafik için)
    public static List<Map<String, Object>> getSon7GunKalorileri() {
        String sql = "SELECT tarih, SUM(kalori) as toplam_kalori " +
                     "FROM gunlukogunler " +
                     "WHERE tarih >= CURDATE() - INTERVAL 6 DAY " +
                     "GROUP BY tarih ORDER BY tarih ASC";

        List<Map<String, Object>> liste = new ArrayList<>();

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("tarih", rs.getDate("tarih"));
                row.put("kalori", rs.getInt("toplam_kalori"));
                liste.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return liste;
    }

    // 3. Son 7 günün özet tablosu (kalori + makrolar)
    public static List<Map<String, Object>> getHaftalikOzet() {
        String sql = "SELECT tarih, " +
                     "SUM(kalori) as toplam_kalori, " +
                     "SUM(protein) as toplam_protein, " +
                     "SUM(karbonhidrat) as toplam_karbonhidrat, " +
                     "SUM(yag) as toplam_yag " +
                     "FROM gunlukogunler " +
                     "WHERE tarih >= CURDATE() - INTERVAL 6 DAY " +
                     "GROUP BY tarih ORDER BY tarih ASC";

        List<Map<String, Object>> liste = new ArrayList<>();

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("tarih", rs.getDate("tarih"));
                row.put("kalori", rs.getInt("toplam_kalori"));
                row.put("protein", rs.getFloat("toplam_protein"));
                row.put("karbonhidrat", rs.getFloat("toplam_karbonhidrat"));
                row.put("yag", rs.getFloat("toplam_yag"));
                liste.add(row);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return liste;
    }
}
