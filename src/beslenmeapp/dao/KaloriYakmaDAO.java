package beslenmeapp.dao;

import beslenmeapp.model.KaloriYakma;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KaloriYakmaDAO {

    // 1. Kayıt ekle
    public static boolean kaloriEkle(KaloriYakma kayit) {
        String sql = "INSERT INTO kalori_yakma (tarih, aktivite, sure_dk, yakilan_kalori, notlar) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, kayit.getTarih());
            stmt.setString(2, kayit.getAktivite());
            stmt.setInt(3, kayit.getSureDk());
            stmt.setDouble(4, kayit.getYakilanKalori());
            stmt.setString(5, kayit.getNotlar());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Kayıt güncelle
    public static boolean kaloriGuncelle(KaloriYakma kayit) {
        String sql = "UPDATE kalori_yakma SET tarih = ?, aktivite = ?, sure_dk = ?, yakilan_kalori = ?, notlar = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, kayit.getTarih());
            stmt.setString(2, kayit.getAktivite());
            stmt.setInt(3, kayit.getSureDk());
            stmt.setDouble(4, kayit.getYakilanKalori());
            stmt.setString(5, kayit.getNotlar());
            stmt.setInt(6, kayit.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Kayıt sil
    public static boolean kaloriSil(int id) {
        String sql = "DELETE FROM kalori_yakma WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Tüm kayıtları getir
    public static List<KaloriYakma> getTumKayitlar() {
        List<KaloriYakma> kayitlar = new ArrayList<>();
        String sql = "SELECT * FROM kalori_yakma ORDER BY tarih DESC";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                KaloriYakma k = new KaloriYakma(
                        rs.getInt("id"),
                        rs.getTimestamp("tarih"),
                        rs.getString("aktivite"),
                        rs.getInt("sure_dk"),
                        rs.getDouble("yakilan_kalori"),
                        rs.getString("notlar")
                );
                kayitlar.add(k);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return kayitlar;
    }

    // 5. Belirli tarihe göre sorgula (opsiyonel)
    public static List<KaloriYakma> getKayitlarByTarih(java.sql.Date date) {
        List<KaloriYakma> liste = new ArrayList<>();
        String sql = "SELECT * FROM kalori_yakma WHERE DATE(tarih) = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    KaloriYakma k = new KaloriYakma(
                            rs.getInt("id"),
                            rs.getTimestamp("tarih"),
                            rs.getString("aktivite"),
                            rs.getInt("sure_dk"),
                            rs.getDouble("yakilan_kalori"),
                            rs.getString("notlar")
                    );
                    liste.add(k);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }
}