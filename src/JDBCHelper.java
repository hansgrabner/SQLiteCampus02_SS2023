import models.Bewertung;
import models.Urlaubskategorien;

import java.sql.*;
import java.util.ArrayList;

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
            PreparedStatement statement = prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printAllBewertungenMinPunkteHoeherAls(int mindestPunkteanzahl) {

        //es sollen nur Bewertungen ausgegeben werden,
        //bei denen die Punkteanzahl größer oder gleich der Variable "mindestPunkteanzahl" ist

        String dynamicQuery = "SELECT BewertungsId,\n" +
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void printAllBewertungenMinPunkteHoeherAlsMitPreparedStatement(int mindestPunkteanzahl) {

        //es sollen nur Bewertungen ausgegeben werden,
        //bei denen die Punkteanzahl größer oder gleich der Variable "mindestPunkteanzahl" ist

        String dynamicQuery = "SELECT BewertungsId,\n" +
                "       UrlaubsId,\n" +
                "       Punkte,\n" +
                "       Kommentar\n" +
                "  FROM Bewertungen\n" +
                "  WHERE Punkte >= ?";
        try {

            PreparedStatement pStmt = connection.prepareStatement(dynamicQuery);
            pStmt.setInt(1, mindestPunkteanzahl); //alle Parameter müssen vor der Ausführung bestimmt werden

            //pStmt.setString, pStmt.setDouble


            ResultSet rs = pStmt.executeQuery();
            while (rs.next()) {
                System.out.printf("ID %d Punkte %d Kommentar %s %n",
                        rs.getInt("BewertungsID"),
                        rs.getInt("Punkte"),
                        rs.getString("Kommentar")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void printBewertungskommentare(String person) {
        //PreparedStatement     FROM KommentareZuBewertung WHERE PERSON ='Johann'
        //Auflösung um 09:45 - bitte auch Pause einplanen
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
        } catch (SQLException e) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printTableMetadata() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, null, new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("Tabelle gefunden: " + tableName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateKategorie(int id, String geaenderteKategorie) {
        //UPDATE Urlaubskategorien SET Kategorie='neu geändert hello' where ID = 2

        //Wenn keine Zeile geändert wurde soll eine entsprechende Meldung ausgegeben werden
        //Kategorie wurde geändert
        //oder Kategorie mit der Id xyz wurde nicht gefunden

        /*
            if (affectedRows==1)
                //geändert

           if (affectedRows==0)
              //nicht gefunden
                    */
        try {
            String updateText = "UPDATE Urlaubskategorien SET Kategorie=? where ID = ?";
            PreparedStatement pStmt = connection.prepareStatement(updateText);
            pStmt.setString(1, geaenderteKategorie);
            pStmt.setInt(2, id);

            int affectedRows = pStmt.executeUpdate();

            if (affectedRows == 1) {
                System.out.println("Kategorie wurde erfolgreich geändert");
            } else {
                System.out.printf("die Kategorie mit der ID %d wurde nicht gefunden ", id);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertKategorie(String kategorie) {
        String insertString = "INSERT INTO Urlaubskategorien (\n" +
                "                                  Kategorie\n" +
                "                              )\n" +
                "                              VALUES (\n" +
                "                                  ?\n" +
                "                              );";

        try {
            PreparedStatement pStmt = connection.prepareStatement(insertString);
            pStmt.setString(1, kategorie);

            int affectedRows = pStmt.executeUpdate();


            System.out.printf("Es waren %d Datensätze betroffen", affectedRows);


        } catch (SQLException ex) {

        }


    }

    public void insertKategorie(Urlaubskategorien kategorie) {
        String insertString = "INSERT INTO Urlaubskategorien (\n" +
                "                                  Kategorie\n" +
                "                              )\n" +
                "                              VALUES (\n" +
                "                                  ?\n" +
                "                              );";

        try {
            PreparedStatement pStmt = connection.prepareStatement(insertString);
            pStmt.setString(1, kategorie.getKategorie());

            int affectedRows = pStmt.executeUpdate();


            System.out.printf("Es waren %d Datensätze betroffen", affectedRows);


        } catch (SQLException ex) {

        }


    }


    public void insertBewertung(Bewertung b1) {
        String insertBewertungSQL = "INSERT INTO Bewertungen (                            \n" +
                "                            UrlaubsId, Punkte, Kommentar\n" +
                "                        )\n" +
                "                        VALUES (                           \n" +
                "                            ?,?,?\n" +
                "                        );";

        try {
            PreparedStatement pInsertBewergung = connection.prepareStatement(insertBewertungSQL);
            pInsertBewergung.setInt(1, b1.getUrlaubsId());
            pInsertBewergung.setInt(2, b1.getPunkte());
            pInsertBewergung.setString(3, b1.getKommentar());

            pInsertBewergung.executeUpdate();

            ResultSet rsLastAutoincrement = executeQuery("SELECT last_insert_rowid() as rowid");
            rsLastAutoincrement.next();

            int lastAutoIncrement = rsLastAutoincrement.getInt("rowid");

            b1.setBewertungsId(lastAutoIncrement);


        } catch (SQLException ex) {
            System.out.println(ex.getStackTrace());
        }


    }

    public ArrayList<Bewertung> getAlleBewertungen() {
        ArrayList<Bewertung> alleBewertungen = new ArrayList<Bewertung>();

        try {

            ResultSet rs = executeQuery("SELECT BewertungsID FROM Bewertungen");
            while (rs.next()) {
                alleBewertungen.add(getBewertungById(rs.getInt("BewertungsID")));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getStackTrace());
        }

        return  alleBewertungen;
    }

    public Bewertung getBewertungById(int bewertungsId) {
        Bewertung b = new Bewertung();
        String sqlSucheBewertungById = "SELECT BewertungsId, UrlaubsId, Punkte, Kommentar " +
                " FROM Bewertungen WHERE BewertungsId=?";

        try {
            PreparedStatement pSucheByID = connection.prepareStatement(sqlSucheBewertungById);
            pSucheByID.setInt(1, bewertungsId);

            ResultSet rs = pSucheByID.executeQuery();
            b.setBewertungsId(bewertungsId);
            if (rs.next()) {//falls Bewertung mit dieser ID exisitert lesen

                b.setUrlaubsId(rs.getInt("UrlaubsId"));
                b.setPunkte(rs.getInt("Punkte"));
                b.setKommentar(rs.getString("Kommentar"));
            } else {
                b.setKommentar(rs.getString("wurde nicht gefunden"));
            }
            return b;

        } catch (SQLException ex) {
            System.out.println(ex.getStackTrace());
        }
        return b;

    }
}
