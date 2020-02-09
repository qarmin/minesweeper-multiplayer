package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
 
public class TopListJDBC {
    static String JdbcURL = "jdbc:mysql://localhost:3306/";
    static String Username = "java";
    static String password = "javapass";
    static Connection connection = null;
    static Statement s = null;
    static ResultSet r = null;
 
    private static void printDataFromQuery(ResultSet r) {
        ResultSetMetaData rsmd;
        try {
            rsmd = r.getMetaData();
 
            int numcols = rsmd.getColumnCount();
            for (int i = 1; i <= numcols; i++) {
                System.out.print("\t" + rsmd.getColumnLabel(i) + "\t|");
            }
            System.out.print("\n____________________________________________________________________________\n");
            while (r.next()) {
                for (int i = 1; i <= numcols; i++) {
                    Object obj = r.getObject(i);
                    if (obj != null)
                        System.out.print("\t" + obj.toString() + "\t|");
                    else
                        System.out.print("\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Bl�d odczytu z bazy! " + e.toString());
            System.exit(3);
        }
    }
 
    private static ResultSet executeQuery(Statement s, String sql) {
        try {
            return s.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static Statement createStatement(Connection connection) {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    private static void closeConnection(Connection connection, Statement s) {
        try {
            s.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Błąd przy zamykaniu polączenia " + e.toString());
            System.exit(4);
        }
    }
 
      /* public static void main(String[] args) {
          try {
             System.out.println("Connecting to database..............."+JdbcURL);
             connection=DriverManager.getConnection(JdbcURL, Username, password);
             System.out.println("Connection is successful!!!!!!");

             s = createStatement(connection);
             r = executeQuery(s, "CREATE DATABASE IF NOT EXISTS TEST;");
             //printDataFromQuery(r);
             r = executeQuery(s, "USE DATABASE TEST;");
             //printDataFromQuery(r);
             r = s.executeQuery("SHOW TABLES LIKE 'Persons';");
             if(r.getMetaData().getColumnCount() == 0)
            	 r = executeQuery(s, "CREATE TABLE Persons (Numer int,    Czas int,    Zwyciezca varchar(255),    Przegrany varchar(255),    Typ varchar(255));");
             //printDataFromQuery(r);
          }
          catch(Exception e) {
             e.printStackTrace();
          }
       }*/
    
}

