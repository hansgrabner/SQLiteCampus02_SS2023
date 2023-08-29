import models.Bewertung;
import models.Kunde;
import models.Urlaubskategorien;
import models.Urlaub;

import java.sql.*;
import java.util.ArrayList;

public class eLearning29 {
    private Connection connection;

    public eLearning29(String url) {
        try {
            connection = DriverManager.getConnection(url);

            connection.createStatement().execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public  int deleteKunde(int kundenIdToDelete) {

        String deleteKunde = "DELETE FROM Kunden where KundenId=?;";
        try {

            PreparedStatement pStmt = connection.prepareStatement(deleteKunde);
            pStmt.setInt(1, kundenIdToDelete);

            int rowsAffected = pStmt.executeUpdate();

            return rowsAffected;

        } catch (SQLException ex) {
            System.out.printf("Fehler %s", ex.getStackTrace());
        }
        return 0;
    }

    public  int increaseBonus(double percentIncrease, double maxBonuspunkte) {

        String deleteKunde = "UPDATE Kunden set Bonuspunkte = Bonuspunkte * ? WHERE Bonuspunkte < ?";
        try {
            PreparedStatement pStmt = connection.prepareStatement(deleteKunde);
            pStmt.setDouble(1, percentIncrease);
            pStmt.setDouble(2, maxBonuspunkte);

            int rowsAffected = pStmt.executeUpdate();

            return rowsAffected;

        } catch (SQLException ex) {
            System.out.printf("%s", ex.getStackTrace());
        }
        return 0;
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double getAVGBonuspunkte(){


        double avgBonuspunkte=0;
        try {
            ResultSet rs= executeQuery("SELECT AVG(BONUSPUNKTE) AS durchschnitt FROM Kunden");
            rs.next();
            avgBonuspunkte=rs.getDouble("durchschnitt");
        } catch (SQLException e) {

            return 0;
        }
        return  avgBonuspunkte;
    }

    public void createTableKunden(){
        String createStmt= "CREATE TABLE Kunden\n" +
                "(\n" +
                "    KundenId INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    Vorname varchar(50),\n" +
                "    Bonuspunkte double\n" +
                ")";

        try
        {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(createStmt);
        }
        catch (SQLException ex){
            System.out.printf("%s", ex.getStackTrace());
        }

    }

    public Kunde insertKunde(Kunde k) {

        String insertKunden="INSERT INTO Kunden (\n" +
                "                    \n" +
                "                       Vorname,\n" +
                "                       Bonuspunkte\n" +
                "                   )\n" +
                "                   VALUES (\n" +
                "                    \n" +
                "                       ?,\n" +
                "                       ?\n" +
                "                   );\n";
        try
        {
            PreparedStatement pStmt = connection.prepareStatement(insertKunden);
            pStmt.setString(1,k.getVorname());
            pStmt.setDouble(2,k.getBonuspunkte());
            pStmt.executeUpdate();

            //lese automatisch vergebenen Autoincrement Wert


            int id = getLastInsertId();

            k.setKundenid(id);

        }
        catch (SQLException ex){
            System.out.printf("%s", ex.getStackTrace());
        }

        return  k;
    }

    public Kunde getKundeById(int id) {

        String selectKundeById="SELECT KundenId, Vorname, Bonuspunkte FROM Kunden WHERE KundenId=?";

        Kunde k=new Kunde();
        k.setKundenid(id);

        try
        {
            PreparedStatement pStmt = connection.prepareStatement(selectKundeById);
            pStmt.setInt(1,id);
            ResultSet rs = pStmt.executeQuery();

            if (rs.next()){
                k.setVorname(rs.getString("Vorname"));
                k.setBonuspunkte(rs.getDouble("Bonuspunkte"));
            } else {
                k.setVorname("Kunde nicht vorhanden");
            }


        }
        catch (SQLException ex){
            System.out.printf("%s", ex.getStackTrace());
        }
        return  k;
    }

    private int getLastInsertId() throws SQLException {
        String readLastId="SELECT last_insert_rowid() as rowid ";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(readLastId);
        rs.next();
        int id = rs.getInt("rowid");
        return id;
    }

    public ArrayList<Kunde> getAllKunden() {
        String selectKunden="SELECT KundenId, Vorname, Bonuspunkte FROM Kunden";
        ArrayList<Kunde> meineKunden= new ArrayList<Kunde>();
        try
        {
            ResultSet rs = connection.createStatement().executeQuery(selectKunden);
            while (rs.next()) {
                meineKunden.add(getKundeById(rs.getInt("KundenId")));
            }
        }
        catch (SQLException ex){
            System.out.printf("%s", ex.getStackTrace());
        }
        return  meineKunden;
    }


    public ArrayList<Kunde> getAllKundenMitTelefon() {
        String selectKunden="SELECT DISTINCT k.KundenId\n" +
                "FROM Kunden k JOIN Telefonnummern t\n" +
                "on k.KundenId=t.kundenId\n";


        ArrayList<Kunde> meineKunden= new ArrayList<Kunde>();
        try
        {
            //ResultSet rs = connection.createStatement().executeQuery(selectKunden);
            ResultSet rs = executeQuery(selectKunden); //call helper
            while (rs.next()) {
                meineKunden.add(getKundeById(rs.getInt("KundenId")));
            }
        }
        catch (SQLException ex){
            System.out.printf("%s", ex.getStackTrace());
        }
        return  meineKunden;
    }

    public Kunde getKundeMitMeistenBonuspunkten() {
        String selectKunden="SELECT KundenId\n" +
                " FROM Kunden ";

        Kunde kundeMitMaxBonuspunkten =new Kunde();
        kundeMitMaxBonuspunkten.setBonuspunkte(-1);
        try
        {
            ResultSet rs = connection.createStatement().executeQuery(selectKunden);
            while (rs.next()) {
                if (kundeMitMaxBonuspunkten==null){
                    kundeMitMaxBonuspunkten = getKundeById(rs.getInt("KundenId"));
                } else {
                    Kunde kDummy = getKundeById(rs.getInt("KundenId"));

                    if (kDummy.getBonuspunkte()>kundeMitMaxBonuspunkten.getBonuspunkte()){
                        kundeMitMaxBonuspunkten = kDummy;
                    }
                }

            }
        }
        catch (SQLException ex){
            System.out.printf("%s", ex.getStackTrace());
        }
        return  kundeMitMaxBonuspunkten;

    }

    public Kunde getKundeMitMeistenBonuspunktenVarianteMitOrderBy() {
        String selectKunden="SELECT KundenId\n" +
                " FROM Kunden ORDER BY Bonuspunkte DESC";

        Kunde kundeMitMaxBonuspunkten =new Kunde();

        try {
            ResultSet rs = connection.createStatement().executeQuery(selectKunden);
            rs.next();
            kundeMitMaxBonuspunkten = getKundeById(rs.getInt("KundenId"));
        }
        catch (SQLException ex){
            System.out.printf("%s", ex.getStackTrace());
        }
        return  kundeMitMaxBonuspunkten;

    }
}