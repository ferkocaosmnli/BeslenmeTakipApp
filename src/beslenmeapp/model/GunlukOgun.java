package beslenmeapp.model;

import java.sql.Date;
import java.sql.Time;

public class GunlukOgun {
    private int id;
    private Date tarih;
    private Time saat;
    private String ogun_turu;
    private String besin_adi;
    private int miktar_gr;
    private int kalori;
    private float protein;
    private float karbonhidrat;
    private float yag;

    public GunlukOgun(int id, Date tarih, Time saat, String ogun_turu,
                      String besin_adi, int miktar_gr, int kalori,
                      float protein, float karbonhidrat, float yag) {
        this.id = id;
        this.tarih = tarih;
        this.saat = saat;
        this.ogun_turu = ogun_turu;
        this.besin_adi = besin_adi;
        this.miktar_gr = miktar_gr;
        this.kalori = kalori;
        this.protein = protein;
        this.karbonhidrat = karbonhidrat;
        this.yag = yag;
    }

    public int getId() {
        return id;
    }

    public Date getTarih() {
        return tarih;
    }

    public Time getSaat() {
        return saat;
    }

    public String getOgun_turu() {
        return ogun_turu;
    }

    public String getBesin_adi() {
        return besin_adi;
    }

    public int getMiktar_gr() {
        return miktar_gr;
    }

    public int getKalori() {
        return kalori;
    }

    public float getProtein() {
        return protein;
    }

    public float getKarbonhidrat() {
        return karbonhidrat;
    }

    public float getYag() {
        return yag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTarih(Date tarih) {
        this.tarih = tarih;
    }

    public void setSaat(Time saat) {
        this.saat = saat;
    }

    public void setOgun_turu(String ogun_turu) {
        this.ogun_turu = ogun_turu;
    }

    public void setBesin_adi(String besin_adi) {
        this.besin_adi = besin_adi;
    }

    public void setMiktar_gr(int miktar_gr) {
        this.miktar_gr = miktar_gr;
    }

    public void setKalori(int kalori) {
        this.kalori = kalori;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public void setKarbonhidrat(float karbonhidrat) {
        this.karbonhidrat = karbonhidrat;
    }

    public void setYag(float yag) {
        this.yag = yag;
    }
}