import java.sql.*;

public class Connect {
    /**
     * Connect to a sample database
     */
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            //1. Project Structure -- add Referenz to sqlite jar driver
            //2. Define connection string jdbc:subprotocol
            String url = "jdbc:sqlite:C:/LVs/DBP2023/Campus02JDBC.db";
            //Wie ist ein connection-String aufgebaut - DriverName:Filename
            // create a connection to the database
            //3. Get Connection
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void connectXYZ() {
        Connection conn = null;
        try {
            // db parameters
            //1. Project Structure -- add Referenz to sqlite jar driver
            //2. Define connection string jdbc:subprotocol
            String url = "jdbc:sqlite:C:/LVs/DBP2023/Campus02JDBC.db";
            //Wie ist ein connection-String aufgebaut - DriverName:Filename
            // create a connection to the database
            //3. Get Connection
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static void readKategorien() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:C:/LVs/DBP2023/Campus02JDBC.db";
            //Wie ist ein connection-String aufgebaut - DriverName:Filename
            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            Statement stmt = conn.createStatement();

            String query = "SELECT UrlaubsID, Schlagwort, UrlaubskategorieID FROM Urlaube";
            ResultSet rs = stmt.executeQuery( query );

            //rs ist ein Zeiger auf eine virtuelle Tabelle,
            // zeigt nach dem Öffnen auf den Datensatz - 1

            while ( rs.next() ) {
                System.out.printf("Schlagwort: %s die UrlaubsID ist: %d %n",
                        rs.getString("Schlagwort"),
                        rs.getInt("UrlaubsID"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


}