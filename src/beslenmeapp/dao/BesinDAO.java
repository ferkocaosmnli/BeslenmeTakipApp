package beslenmeapp.dao;

import beslenmeapp.model.Besin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BesinDAO {

    // Tüm besinleri getir
    public static List<Besin> getAllBesinler() {
        List<Besin> besinList = new ArrayList<>();
        String sql = "SELECT * FROM besinler";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Besin besin = new Besin(
                        rs.getInt("besin_id"),
                        rs.getString("besin_adi"),
                        rs.getInt("besin_gram"),
                        rs.getInt("besin_kalori"),
                        rs.getFloat("besin_protein"),
                        rs.getFloat("besin_karbonhidrat"),
                        rs.getFloat("besin_yag")
                );
                besinList.add(besin);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return besinList;
    }

    // Besin ekle
    public static boolean besinEkle(Besin besin) {
        String sql = "INSERT INTO besinler (besin_adi, besin_gram, besin_kalori, besin_protein, besin_karbonhidrat, besin_yag) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, besin.getBesinAdi());
            stmt.setInt(2, besin.getBesin_gram());
            stmt.setInt(3, besin.getBesin_kalori());
            stmt.setFloat(4, besin.getBesin_protein());
            stmt.setFloat(5, besin.getBesin_karbonhidrat());
            stmt.setFloat(6, besin.getBesin_yag());

            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Besin güncelle
    public static boolean besinGuncelle(Besin besin) {
        String sql = "UPDATE besinler SET besin_adi=?, besin_gram=?, besin_kalori=?, besin_protein=?, besin_karbonhidrat=?, besin_yag=? WHERE besin_id=?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, besin.getBesinAdi());
            stmt.setInt(2, besin.getBesin_gram());
            stmt.setInt(3, besin.getBesin_kalori());
            stmt.setFloat(4, besin.getBesin_protein());
            stmt.setFloat(5, besin.getBesin_karbonhidrat());
            stmt.setFloat(6, besin.getBesin_yag());
            stmt.setInt(7, besin.getBesin_id());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Besin sil
    public static boolean besinSil(int besinId) {
        String sql = "DELETE FROM besinler WHERE besin_id=?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, besinId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Adına göre besin getir
    public static Besin getBesinByAdi(String besinAdi) {
        String sql = "SELECT * FROM besinler WHERE besin_adi = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, besinAdi);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Besin(
                        rs.getInt("besin_id"),
                        rs.getString("besin_adi"),
                        rs.getInt("besin_gram"),
                        rs.getInt("besin_kalori"),
                        rs.getFloat("besin_protein"),
                        rs.getFloat("besin_karbonhidrat"),
                        rs.getFloat("besin_yag")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}