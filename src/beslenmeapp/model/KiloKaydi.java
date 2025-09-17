package beslenmeapp.model;

import java.sql.Timestamp;

public class KiloKaydi {
    private int id;
    private Timestamp tarih;
    private double kilo;
    private String notlar;

    // Constructorlar
    public KiloKaydi() {}

    public KiloKaydi(int id, Timestamp tarih, double kilo, String notlar) {
        this.id = id;
        this.tarih = tarih;
        this.kilo = kilo;
        this.notlar = notlar;
    }

    public KiloKaydi(Timestamp tarih, double kilo, String notlar) {
        this.tarih = tarih;
        this.kilo = kilo;
        this.notlar = notlar;
    }

    // Getter ve Setter'lar
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTarih() {
        return tarih;
    }

    public void setTarih(Timestamp tarih) {
        this.tarih = tarih;
    }

    public double getKilo() {
        return kilo;
    }

    public void setKilo(double kilo) {
        this.kilo = kilo;
    }

    public String getNotlar() {
        return notlar;
    }

    public void setNotlar(String notlar) {
        this.notlar = notlar;
    }
}