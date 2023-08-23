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

Connect.displayBewertungenOrderByBewertung();


        //Connect.connect();
        //  Connect.readKategorien();
        //  Connect.printMetadata();

        String url = "jdbc:sqlite:C:/LVs/DBP2023/Campus02JDBC.db";

        System.out.printf("%nmit Helper %n");

        try {

            JDBCHelper helper = new JDBCHelper(url);
            ResultSet rs = helper.executeQuery("SELECT UrlaubsID, Schlagwort, UrlaubskategorieID FROM Urlaube");
            while (rs.next()) {
                System.out.printf("%s %n", rs.getString("Schlagwort"));
            }
            helper.closeConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}