package beslenmeapp.dao;

import beslenmeapp.model.GunlukOgun;
import java.time.LocalDate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GunlukOgunDAO {

    public static boolean ogunEkle(GunlukOgun ogun) {
        String sql = "INSERT INTO gunlukogunler (tarih, saat, ogun_turu, besin_adi, miktar_gr, kalori, protein, karbonhidrat, yag) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, ogun.getTarih());
            pstmt.setTime(2, ogun.getSaat());
            pstmt.setString(3, ogun.getOgun_turu());
            pstmt.setString(4, ogun.getBesin_adi());
            pstmt.setInt(5, ogun.getMiktar_gr());
            pstmt.setInt(6, ogun.getKalori());
            pstmt.setFloat(7, ogun.getProtein());
            pstmt.setFloat(8, ogun.getKarbonhidrat());
            pstmt.setFloat(9, ogun.getYag());

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<GunlukOgun> getBugunkuOgunler() {
        return getOgunlerByTarih(Date.valueOf(LocalDate.now()));
    }

    public static List<GunlukOgun> getOgunlerByTarih(Date tarih) {
        List<GunlukOgun> ogunList = new ArrayList<>();
        String sql = "SELECT * FROM gunlukogunler WHERE tarih = ? ORDER BY saat ASC";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, tarih);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                GunlukOgun ogun = new GunlukOgun(
                        rs.getInt("id"),
                        rs.getDate("tarih"),
                        rs.getTime("saat"),
                        rs.getString("ogun_turu"),
                        rs.getString("besin_adi"),
                        rs.getInt("miktar_gr"),
                        rs.getInt("kalori"),
                        rs.getFloat("protein"),
                        rs.getFloat("karbonhidrat"),
                        rs.getFloat("yag")
                );
                ogunList.add(ogun);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ogunList;
    }

    public static boolean guncelleOgun(GunlukOgun ogun) {
        String sql = "UPDATE gunlukogunler SET miktar_gr = ?, kalori = ?, protein = ?, karbonhidrat = ?, yag = ? WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ogun.getMiktar_gr());
            pstmt.setInt(2, ogun.getKalori());
            pstmt.setFloat(3, ogun.getProtein());
            pstmt.setFloat(4, ogun.getKarbonhidrat());
            pstmt.setFloat(5, ogun.getYag());
            pstmt.setInt(6, ogun.getId());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean silOgun(int id) {
        String sql = "DELETE FROM gunlukogunler WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}