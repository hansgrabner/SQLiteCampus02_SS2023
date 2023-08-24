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

    public PreparedStatement prepareStatement(String query) throws SQLException {
        return connection.prepareStatement(query);
    }
    public void displayUrlaubMitID(String query, int id) {
        try {
            PreparedStatement  statement = prepareStatement(query);
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                System.out.printf("Schlagwort %s", rs.getString("Schlagwort"));
            }

        } catch (SQLException e) {
            e.printStackTrace();

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

    public void printAllBewertungenMinPunkteHoeherAls(int mindestPunkteanzahl) {

        //es sollen nur Bewertungen ausgegeben werden,
        //bei denen die Punkteanzahl größer oder gleich der Variable "mindestPunkteanzahl" ist

        String dynamicQuery="SELECT BewertungsId,\n" +
                "       UrlaubsId,\n" +
                "       Punkte,\n" +
                "       Kommentar\n" +
                "  FROM Bewertungen\n" +
                "  WHERE Punkte >= " + mindestPunkteanzahl;

        String likeExpression = " WHERE SCHLAGWORT LIKE '%" + "suchTExt" + "%'";

        //Vorsicht SQL Injection!!!

        System.out.println(dynamicQuery);

        try {
            ResultSet rs = executeQuery(dynamicQuery);
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

    public void printAllBewertungenMinPunkteHoeherAlsMitPreparedStatement(int mindestPunkteanzahl) {

        //es sollen nur Bewertungen ausgegeben werden,
        //bei denen die Punkteanzahl größer oder gleich der Variable "mindestPunkteanzahl" ist

        String dynamicQuery="SELECT BewertungsId,\n" +
                "       UrlaubsId,\n" +
                "       Punkte,\n" +
                "       Kommentar\n" +
                "  FROM Bewertungen\n" +
                "  WHERE Punkte >= ?";
        try {

        PreparedStatement pStmt = connection.prepareStatement(dynamicQuery);
        pStmt.setInt(1,mindestPunkteanzahl); //alle Parameter müssen vor der Ausführung bestimmt werden

            //pStmt.setString, pStmt.setDouble


            ResultSet rs = pStmt.executeQuery();
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
            ResultSet rs = executeQuery("SELECT * FROM KommentareZuBewertung");
            while (rs.next()) {
                System.out.printf("IKommentar %s %n",

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