package beslenmeapp.model;

import java.sql.Timestamp;

public class KaloriYakma {
    private int id;
    private Timestamp tarih;
    private String aktivite;
    private int sureDk;
    private double yakilanKalori;
    private String notlar;

    public KaloriYakma() {}

    public KaloriYakma(int id, Timestamp tarih, String aktivite, int sureDk, double yakilanKalori, String notlar) {
        this.id = id;
        this.tarih = tarih;
        this.aktivite = aktivite;
        this.sureDk = sureDk;
        this.yakilanKalori = yakilanKalori;
        this.notlar = notlar;
    }

    public KaloriYakma(Timestamp tarih, String aktivite, int sureDk, double yakilanKalori, String notlar) {
        this.tarih = tarih;
        this.aktivite = aktivite;
        this.sureDk = sureDk;
        this.yakilanKalori = yakilanKalori;
        this.notlar = notlar;
    }

    public int getId() {
        return id;
    }

    public Timestamp getTarih() {
        return tarih;
    }

    public String getAktivite() {
        return aktivite;
    }

    public int getSureDk() {
        return sureDk;
    }

    public double getYakilanKalori() {
        return yakilanKalori;
    }

    public String getNotlar() {
        return notlar;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTarih(Timestamp tarih) {
        this.tarih = tarih;
    }

    public void setAktivite(String aktivite) {
        this.aktivite = aktivite;
    }

    public void setSureDk(int sureDk) {
        this.sureDk = sureDk;
    }

    public void setYakilanKalori(double yakilanKalori) {
        this.yakilanKalori = yakilanKalori;
    }

    public void setNotlar(String notlar) {
        this.notlar = notlar;
    }
}