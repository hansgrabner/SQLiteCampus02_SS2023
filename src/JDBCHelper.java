import java.sql.*;

public class JDBCHelper {
    private Connection connection;

    public JDBCHelper(String url) {
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



    public void printAllBewertungen() {
        try {
            ResultSet rs = executeQuery("SELECT BewertungsId, Punkte, Kommentar FROM Bewertungen");
            while (rs.next()) {
                System.out.printf("ID %d Punkte %d Kommentar %s %n",
                        rs.getInt("BewertungsID"),
                        rs.getInt("Punkte"),
                        rs.getString("Kommentar")
                );
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printAlleKommentare() {
        try {
            //alle Kommentare aus der Tabelle KommentareZuBewertungen sollen ausgegeben werden
            ResultSet rs = executeQuery("SELECT BewertungsId, Punkte, Kommentar FROM Bewertungen");
            while (rs.next()) {
                System.out.printf("ID %d Punkte %d Kommentar %s %n",
                        rs.getInt("BewertungsID"),
                        rs.getInt("Punkte"),
                        rs.getString("Kommentar")
                );
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printKategorien() {
        try {
            ResultSet rs = executeQuery("SELECT Kategorie FROM Urlaubskategorien");
            while (rs.next()) {
                System.out.printf("Kategorie %s %n",
                        rs.getString("Kategorie")
                );
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printTableMetadata() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[] {"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Tabelle gefunden: " + tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}