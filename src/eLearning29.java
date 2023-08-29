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
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    private int getLastInsertId() throws SQLException {
        String readLastId="SELECT last_insert_rowid() as rowid ";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(readLastId);
        rs.next();
        int id = rs.getInt("rowid");
        return id;
    }
}