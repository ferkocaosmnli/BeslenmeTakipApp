package beslenmeapp.dao;

import java.sql.*;
import java.time.LocalDate;

public class KisiselBilgiDAO {

    private static final int BMR = 2400; // Sabit BMR değeri (günlük kalori ihtiyacı)

    public static int getBMR() {
        return BMR;
    }

    // ✅ Bugünkü alınan kalori
    public static int getBugunkuAlinanKalori() {
        String sql = "SELECT SUM(kalori) FROM gunlukogunler WHERE tarih = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ✅ Haftalık alınan kalori
    public static int getHaftalikAlinanKalori() {
        String sql = "SELECT SUM(kalori) FROM gunlukogunler WHERE tarih >= ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDate yediGunOnce = LocalDate.now().minusDays(6);
            stmt.setDate(1, Date.valueOf(yediGunOnce));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ✅ Bugünkü yakılan kalori
    public static int getBugunkuYakilanKalori() {
    String sql = "SELECT SUM(yakilan_kalori) FROM kalori_yakma WHERE DATE(tarih) = ?";
    try (Connection conn = DBConnection.connect();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setDate(1, Date.valueOf(LocalDate.now()));
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) return rs.getInt(1);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

    // ✅ Haftalık yakılan kalori
    public static int getHaftalikYakilanKalori() {
        String sql = "SELECT SUM(yakilan_kalori) FROM kalori_yakma WHERE tarih >= ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDate yediGunOnce = LocalDate.now().minusDays(6);
            stmt.setDate(1, Date.valueOf(yediGunOnce));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // ✅ Günlük kalori açığı
    public static int getGunlukKaloriAcigi() {
        return (getBMR() + getBugunkuYakilanKalori()) - getBugunkuAlinanKalori();
    }

    // ✅ Haftalık kalori açığı
    public static int getHaftalikKaloriAcigi() {
    int haftalikAlinan = getHaftalikAlinanKalori();
    int haftalikYakilan = getHaftalikYakilanKalori();
    int toplamHarcama = (getBMR() * 7) + haftalikYakilan;

    return toplamHarcama - haftalikAlinan;
}

    // ✅ Haftalık tahmini kilo kaybı (1 kg = 7700 kcal)
    public static float getHaftalikTahminiKiloKaybi() {
        return getHaftalikKaloriAcigi() / 7700f;
    }

    // ✅ Aylık tahmini kilo kaybı (4 hafta üzerinden)
    public static float getAylikTahminiKiloKaybi() {
        return (getHaftalikKaloriAcigi() * 4) / 7700f;
    }

    // ✅ Alternatif isimlendirme: Bugünkü alınan kalori
    public static int getGunlukAlinanKalori() {
        return getBugunkuAlinanKalori();
    }

    // ✅ Alternatif isimlendirme: Bugünkü yakılan kalori
    public static int getGunlukYakilanKalori() {
        return getBugunkuYakilanKalori();
    }
}