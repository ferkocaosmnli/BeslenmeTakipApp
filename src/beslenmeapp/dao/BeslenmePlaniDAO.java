package beslenmeapp.dao;

import beslenmeapp.model.BeslenmePlani;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BeslenmePlaniDAO {

    // 1. Ekleme
    public static boolean planiEkle(BeslenmePlani plan) {
        String sql = "INSERT INTO beslenme_plani (gun, ogun_turu, besin_adi, miktar_gr, kalori, protein, karbonhidrat, yag) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, plan.getGun());
            pstmt.setString(2, plan.getOgunTuru());
            pstmt.setString(3, plan.getBesinAdi());
            pstmt.setInt(4, plan.getMiktarGr());
            pstmt.setInt(5, plan.getKalori());
            pstmt.setFloat(6, plan.getProtein());
            pstmt.setFloat(7, plan.getKarbonhidrat());
            pstmt.setFloat(8, plan.getYag());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Seçilen güne göre planları getir
    public static List<BeslenmePlani> getPlanlarByGun(String gun) {
        List<BeslenmePlani> list = new ArrayList<>();
        String sql = "SELECT * FROM beslenme_plani WHERE gun = ? ORDER BY ogun_turu";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, gun);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BeslenmePlani plan = new BeslenmePlani(
                        rs.getInt("id"),
                        rs.getString("gun"),
                        rs.getString("ogun_turu"),
                        rs.getString("besin_adi"),
                        rs.getInt("miktar_gr"),
                        rs.getInt("kalori"),
                        rs.getFloat("protein"),
                        rs.getFloat("karbonhidrat"),
                        rs.getFloat("yag")
                );
                list.add(plan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // 3. Planı sil (ID'ye göre)
    public static boolean planiSil(int id) {
        String sql = "DELETE FROM beslenme_plani WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Tüm planları getir (İsteğe bağlı)
    public static List<BeslenmePlani> getTumPlanlar() {
        List<BeslenmePlani> list = new ArrayList<>();
        String sql = "SELECT * FROM beslenme_plani ORDER BY gun, ogun_turu";

        try (Connection conn = DBConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BeslenmePlani plan = new BeslenmePlani(
                        rs.getInt("id"),
                        rs.getString("gun"),
                        rs.getString("ogun_turu"),
                        rs.getString("besin_adi"),
                        rs.getInt("miktar_gr"),
                        rs.getInt("kalori"),
                        rs.getFloat("protein"),
                        rs.getFloat("karbonhidrat"),
                        rs.getFloat("yag")
                );
                list.add(plan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public static boolean planiGuncelle(BeslenmePlani plan) {
    String sql = "UPDATE beslenme_plani SET ogun_turu=?, besin_adi=?, miktar_gr=?, kalori=?, protein=?, karbonhidrat=?, yag=? WHERE id=?";

    try (Connection conn = DBConnection.connect();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, plan.getOgunTuru());
        stmt.setString(2, plan.getBesinAdi());
        stmt.setInt(3, plan.getMiktarGr());
        stmt.setInt(4, plan.getKalori());
        stmt.setFloat(5, plan.getProtein());
        stmt.setFloat(6, plan.getKarbonhidrat());
        stmt.setFloat(7, plan.getYag());
        stmt.setInt(8, plan.getId());

        return stmt.executeUpdate() > 0;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}
