package MYSQL;

import java.sql.*;

public class MySQLTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://192.168.3.171:3306/ejemplo";
        String user = "lucia";
        String password = "1311";

        try (Connection con = DriverManager.getConnection(url, user, password);) {

            System.out.println("Conexión realizada correctamente");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}