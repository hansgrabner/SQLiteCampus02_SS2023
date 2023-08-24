package models;

public class Bewertung {

    private int BewertungsId;

    @Override
    public String toString() {
        return "Bewertung{" +
                "BewertungsId=" + BewertungsId +
                ", UrlaubsId=" + UrlaubsId +
                ", Punkte=" + Punkte +
                ", Kommentar=" + Kommentar +
                '}';
    }

    public int getBewertungsId() {
        return BewertungsId;
    }

    public Bewertung() {

    }
    public Bewertung(int bewertungsId, int urlaubsId, int punkte, String kommentar) {
        BewertungsId = bewertungsId;
        UrlaubsId = urlaubsId;
        Punkte = punkte;
        Kommentar = kommentar;
    }

    public void setBewertungsId(int bewertungsId) {
        BewertungsId = bewertungsId;
    }

    public int getUrlaubsId() {
        return UrlaubsId;
    }

    public void setUrlaubsId(int urlaubsId) {
        UrlaubsId = urlaubsId;
    }

    public int getPunkte() {
        return Punkte;
    }

    public void setPunkte(int punkte) {
        Punkte = punkte;
    }

    public String getKommentar() {
        return Kommentar;
    }

    public void setKommentar(String kommentar) {
        Kommentar = kommentar;
    }

    private int UrlaubsId;
    private int Punkte;

    private String Kommentar;
}
