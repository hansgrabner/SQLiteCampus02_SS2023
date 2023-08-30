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

    public  void transferBonuspunkte(double points, int fromKundenId, int toKundenId) {

        String update1 = "UPDATE Kunden set Bonuspunkte = Bonuspunkte - ? WHERE KundenId = ?";
        String update2 = "UPDATE Kunden set Bonuspunkte = Bonuspunkte + ? WHERE KundenId = ?";

        try {

            connection.setAutoCommit( false ); //es gibt kein AutoCommit mehr, alle Transatktionen müssen mit commit abgeschlossen werden
            PreparedStatement pStmt1 = connection.prepareStatement(update1);
            pStmt1.setDouble(1, points);
            pStmt1.setInt(2, fromKundenId);
            pStmt1.executeUpdate(); //Bei Autocomiit wird jede Ausführung sofort "festgeschrieben", kann nich rückgängig gemacht werde

            PreparedStatement pStmt2 = connection.prepareStatement(update2);
            pStmt2.setDouble(1, points);
            pStmt2.setInt(2, toKundenId);

            pStmt2.executeUpdate();

            //Wenn alles erfolgreich war, dann dauerhaft "festschreiben"
            connection.commit(); //Alle Änderungen sind in der DB, es gibt kein zurück


        } catch (SQLException ex) {
            try
            {
                connection.rollback(); //alle Änderungen rückgängig machen
            }
            catch (SQLException transactionFehler){
                System.out.printf("%s", ex.getStackTrace());
            }


        }
        ;
    }

    public  void deleteTelefonnummernAndKunde(int kundenId) {

        String update1 = "DELETE FROM Telefonnummern WHERE KundenId = ?";
        String update2 = "DELETE FROMX Kunden WHERE KundenId = ?";

        try {

            connection.setAutoCommit( false ); //es gibt kein AutoCommit mehr, alle Transatktionen müssen mit commit abgeschlossen werden

            PreparedStatement pStmt1 = connection.prepareStatement(update1);
            pStmt1.setInt(1, kundenId);
            pStmt1.executeUpdate(); //Bei Autocomiit wird jede Ausführung sofort "festgeschrieben", kann nich rückgängig gemacht werde

            PreparedStatement pStmt2 = connection.prepareStatement(update2);
            pStmt2.setInt(1, kundenId);
            pStmt2.executeUpdate();

            //Wenn alles erfolgreich war, dann dauerhaft "festschreiben"
            connection.commit(); //Alle Änderungen sind in der DB, es gibt kein zurück
            connection.setAutoCommit(true);


        } catch (SQLException ex) {
            System.out.printf("Rollback %s", ex.getStackTrace());
            try
            {
                connection.rollback(); //alle Änderungen rückgängig machen
            }
            catch (SQLException transactionFehler){
                System.out.printf("%s", ex.getStackTrace());
            }


        }
        ;
    }

    public void printKundenColumns(){
        try {
            ResultSet rs = executeQuery( "SELECT * FROM Kunden" );
            ResultSetMetaData meta = rs.getMetaData();

            int numerics = 0;

            for ( int i = 1; i <= meta.getColumnCount(); i++ )
            {
                System.out.printf( "%-20s %-20s%n", meta.getColumnLabel( i ),
                        meta.getColumnTypeName( i ) );

                if ( meta.isSigned( i ) )
                    numerics++;
            }

            System.out.println();
            System.out.println( "Spalten: " + meta.getColumnCount() +
                    ", Numerisch: " + numerics );

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}