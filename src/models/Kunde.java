package models;

public class Kunde {
    private int Kundenid;

    private int AnzahlTelefonnummern;
    private String Vorname;

    public int getAnzahlTelefonnummern() {
        return AnzahlTelefonnummern;
    }

    public void setAnzahlTelefonnummern(int anzahlTelefonnummern) {
        AnzahlTelefonnummern = anzahlTelefonnummern;
    }

    @Override
    public String toString() {
        return "Kunde{" +
                "Kundenid=" + Kundenid +
                ", Vorname='" + Vorname + '\'' +
                ", Anzahl Telefonnummern=" + AnzahlTelefonnummern +
                ", Bonuspunkte=" + Bonuspunkte +
                '}';
    }

    public int getKundenid() {
        return Kundenid;
    }

    public void setKundenid(int kundenid) {
        Kundenid = kundenid;
    }

    public String getVorname() {
        return Vorname;
    }

    public void setVorname(String vorname) {
        Vorname = vorname;
    }

    public double getBonuspunkte() {
        return Bonuspunkte;
    }

    public void setBonuspunkte(double bonuspunkte) {
        Bonuspunkte = bonuspunkte;
    }

    private double Bonuspunkte;

}
