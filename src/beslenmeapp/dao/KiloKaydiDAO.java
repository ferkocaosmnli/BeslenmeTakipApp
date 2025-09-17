package beslenmeapp.dao;

import beslenmeapp.model.KiloKaydi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KiloKaydiDAO {

    // 1. Kayıt ekle
    public static boolean kiloEkle(KiloKaydi kayit) {
        String sql = "INSERT INTO kilo_kaydi (tarih, kilo, notlar) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, kayit.getTarih()); // Timestamp kullanılıyor
            stmt.setDouble(2, kayit.getKilo());
            stmt.setString(3, kayit.getNotlar());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Kayıt güncelle
    public static boolean kiloGuncelle(KiloKaydi kayit) {
        String sql = "UPDATE kilo_kaydi SET tarih = ?, kilo = ?, notlar = ? WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, kayit.getTarih());
            stmt.setDouble(2, kayit.getKilo());
            stmt.setString(3, kayit.getNotlar());
            stmt.setInt(4, kayit.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Kayıt sil
    public static boolean kiloSil(int id) {
        String sql = "DELETE FROM kilo_kaydi WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Tüm kayıtları getir (saat dahil olacak şekilde sıralı)
    public static List<KiloKaydi> getTumKayitlar() {
        List<KiloKaydi> kayitlar = new ArrayList<>();
        String sql = "SELECT * FROM kilo_kaydi ORDER BY tarih DESC";

        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                KiloKaydi k = new KiloKaydi(
                        rs.getInt("id"),
                        rs.getTimestamp("tarih"),
                        rs.getDouble("kilo"),
                        rs.getString("notlar")
                );
                kayitlar.add(k);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return kayitlar;
    }

    // 5. Belirli tarihte kayıt var mı (tam zaman eşleşmesi aranıyorsa)
    public static KiloKaydi getKayitByTarih(Timestamp tarih) {
        String sql = "SELECT * FROM kilo_kaydi WHERE tarih = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, tarih);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new KiloKaydi(
                            rs.getInt("id"),
                            rs.getTimestamp("tarih"),
                            rs.getDouble("kilo"),
                            rs.getString("notlar")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}