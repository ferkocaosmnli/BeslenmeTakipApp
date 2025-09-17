package beslenmeapp.model;

public class BeslenmePlani {
    private int id;
    private String gun;
    private String ogunTuru;
    private String besinAdi;
    private int miktarGr;
    private int kalori;
    private float protein;
    private float karbonhidrat;
    private float yag;

    // Constructor (Tüm alanlarla)
    public BeslenmePlani(int id, String gun, String ogunTuru, String besinAdi,
                         int miktarGr, int kalori, float protein, float karbonhidrat, float yag) {
        this.id = id;
        this.gun = gun;
        this.ogunTuru = ogunTuru;
        this.besinAdi = besinAdi;
        this.miktarGr = miktarGr;
        this.kalori = kalori;
        this.protein = protein;
        this.karbonhidrat = karbonhidrat;
        this.yag = yag;
    }

    // Constructor (id olmadan, ekleme için)
    public BeslenmePlani(String gun, String ogunTuru, String besinAdi,
                         int miktarGr, int kalori, float protein, float karbonhidrat, float yag) {
        this(-1, gun, ogunTuru, besinAdi, miktarGr, kalori, protein, karbonhidrat, yag);
    }

    // Getter'lar
    public int getId() {
        return id;
    }

    public String getGun() {
        return gun;
    }

    public String getOgunTuru() {
        return ogunTuru;
    }

    public String getBesinAdi() {
        return besinAdi;
    }

    public int getMiktarGr() {
        return miktarGr;
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

    // Setter'lar
    public void setId(int id) {
        this.id = id;
    }

    public void setGun(String gun) {
        this.gun = gun;
    }

    public void setOgunTuru(String ogunTuru) {
        this.ogunTuru = ogunTuru;
    }

    public void setBesinAdi(String besinAdi) {
        this.besinAdi = besinAdi;
    }

    public void setMiktarGr(int miktarGr) {
        this.miktarGr = miktarGr;
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
