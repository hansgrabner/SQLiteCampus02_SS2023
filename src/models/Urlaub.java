package models;

public class Urlaub {

    private int UrlaubsId;

    @Override
    public String toString() {
        return "Urlaub{" +
                "UrlaubsId=" + UrlaubsId +
                ", Schlagwort='" + Schlagwort + '\'' +
                '}';
    }

    public int getUrlaubsId() {
        return UrlaubsId;
    }

    public void setUrlaubsId(int urlaubsId) {
        UrlaubsId = urlaubsId;
    }

    public String getSchlagwort() {
        return Schlagwort;
    }

    public void setSchlagwort(String schlagwort) {
        Schlagwort = schlagwort;
    }

    private String Schlagwort;

}
