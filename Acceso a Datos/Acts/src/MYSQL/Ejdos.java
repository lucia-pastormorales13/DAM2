package MYSQL;

import java.util.*;
import java.sql.*;

public class Ejdos {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String url = "jdbc:mysql://192.168.3.171:3306/tienda_completa";
        String user = "lucia";
        String password = "1311";

        try (
                Connection con = DriverManager.getConnection(url, user, password);
                Statement st = con.createStatement();
                PreparedStatement ps1 = con.prepareStatement("SELECT * FROM producto ;");
                PreparedStatement ps2 = con.prepareStatement("SELECT stock FROM producto ;");

        ) {
        } catch (SQLException e) {

        }
    }

}