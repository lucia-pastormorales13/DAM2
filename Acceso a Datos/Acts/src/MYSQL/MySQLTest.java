package MYSQL;
import java.util.*;
import java.sql.*;

public class MySQLTest {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        String url = "jdbc:mysql://192.168.3.171:3306/tienda_completa";
        String user = "lucia";
        String password = "1311";

        try (
            Connection con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();
            PreparedStatement ps= con.prepareStatement("SELECT * FROM cliente WHERE nombre = ? ;")

        ) {
            System.out.println("dime tu nombre: ");
            String nombres= sc.nextLine();
            ps.setString(1, nombres);
            System.out.println("Conexión realizada correctamente");
            ResultSet rs= ps.executeQuery();

            while(rs.next()){
                int id= rs.getInt("id");
                String nombre= rs.getString("nombre");
                String email= rs.getString("email");
                String ciudad= rs.getString("ciudad");

                System.out.println("ID:"+ id+"\n"+
                "Nombre:"+ nombre+"\n"+
                "Email:"+ email+"\n"+
                "ciudad:"+ ciudad
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}