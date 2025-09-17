package beslenmeapp.model;

public class Egzersiz {
    private int id;
    private String ad;
    private String bolge;
    private String resimPath;

    public Egzersiz(int id, String ad, String bolge, String resimPath) {
        this.id = id;
        this.ad = ad;
        this.bolge = bolge;
        this.resimPath = resimPath;
    }

    public Egzersiz(String ad, String bolge, String resimPath) {
        this.ad = ad;
        this.bolge = bolge;
        this.resimPath = resimPath;
    }

    public int getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public String getBolge() {
        return bolge;
    }

    public String getResimPath() {
        return resimPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public void setBolge(String bolge) {
        this.bolge = bolge;
    }

    public void setResimPath(String resimPath) {
        this.resimPath = resimPath;
    }

    @Override
    public String toString() {
        return ad + " (" + bolge + ")";
    }
}
