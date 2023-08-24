import java.sql.ResultSet;
import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Press Alt+Eingabe with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        System.out.printf("Hello and welcome to JDBC!");

        int i = 12;

        int erg = 23 + i;

        System.out.println("Aufgabe Mittwoch");
        Connect.AufgabeNachmittagMittwoch();

Connect.displayBewertungenOrderByBewertung();


        //Connect.connect();
        //  Connect.readKategorien();
        //  Connect.printMetadata();

        String url = "jdbc:sqlite:C:/LVs/DBP2023/Campus02JDBC.db";

        System.out.printf("%nmit Helper %n");

        //try {

            JDBCHelper helper = new JDBCHelper(url);
            System.out.printf("%n alle Bewertugen mit Helper %n");
            helper.printAllBewertungen();

        System.out.printf("%n alle Kategorien mit Helper %n");
            helper.printKategorien();


        System.out.printf("%n printAlleKommentare mit Helper %n");
        helper.printAlleKommentare();

        System.out.printf("%n Urlaub mit Nummer  %n");
        helper.displayUrlaubMitID("SELECT Schlagwort FROM URLAUBE WHERE UrlaubsID=?",1);

        System.out.printf("%n Bewertungen mit Parametern  %n");
        helper.printAllBewertungenMinPunkteHoeherAls(6);


            /*
            ResultSet rs = helper.executeQuery("SELECT UrlaubsID, Schlagwort, UrlaubskategorieID FROM Urlaube");
            while (rs.next()) {
                System.out.printf("%s %n", rs.getString("Schlagwort"));
            }*/

            helper.closeConnection();
        //} catch (SQLException e) {
          //  throw new RuntimeException(e);
        }


    }
