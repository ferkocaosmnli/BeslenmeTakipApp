package beslenmeapp.model;

public class Besin {
    private int besin_id;
    private String besin_adi;
    private int besin_gram;
    private int besin_kalori;
    private float besin_protein;
    private float besin_karbonhidrat;
    private float besin_yag;

    public Besin(int besin_id, String besin_adi, int besin_gram, int besin_kalori,
                 float besin_protein, float besin_karbonhidrat, float besin_yag) {
        this.besin_id = besin_id;
        this.besin_adi = besin_adi;
        this.besin_gram = besin_gram;
        this.besin_kalori = besin_kalori;
        this.besin_protein = besin_protein;
        this.besin_karbonhidrat = besin_karbonhidrat;
        this.besin_yag = besin_yag;
    }

    // Getter'lar
    public int getBesin_id() { return besin_id; }
    public String getBesinAdi() { return besin_adi; }
    public int getBesin_gram() { return besin_gram; }
    public int getBesin_kalori() { return besin_kalori; }
    public float getBesin_protein() { return besin_protein; }
    public float getBesin_karbonhidrat() { return besin_karbonhidrat; }
    public float getBesin_yag() { return besin_yag; }

    // Setter'lar
    public void setBesin_id(int besin_id) { this.besin_id = besin_id; }
    public void setBesinAdi(String besin_adi) { this.besin_adi = besin_adi; }
    public void setBesin_gram(int besin_gram) { this.besin_gram = besin_gram; }
    public void setBesin_kalori(int besin_kalori) { this.besin_kalori = besin_kalori; }
    public void setBesin_protein(float besin_protein) { this.besin_protein = besin_protein; }
    public void setBesin_karbonhidrat(float besin_karbonhidrat) { this.besin_karbonhidrat = besin_karbonhidrat; }
    public void setBesin_yag(float besin_yag) { this.besin_yag = besin_yag; }
}