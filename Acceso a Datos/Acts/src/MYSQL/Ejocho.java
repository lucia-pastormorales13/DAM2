package MYSQL;

import java.sql.*;
import java.util.*;

public class Ejocho {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Valores del portátil
        String urlp = "jdbc:mysql://192.168.1.144:3306/banco";
        String userp = "admin";
        String passwordp = "1311";

        String sql= "SELECT COUNT(*)\n" + //
                        "FROM cuenta\n" + //
                        "WHERE numero = ?;"; //COUNT(*) devuelve el número de filas que cumplen la condición especificada en la cláusula WHERE. En este caso, se está contando cuántas filas existen en la tabla cuentas donde el número de cuenta coincide con el valor proporcionado como parámetro.
        String sql1 = "{CALL obtener_saldo(?, ?)}";

        try (Connection con = DriverManager.getConnection(urlp, userp, passwordp);
            PreparedStatement ps = con.prepareStatement(sql);
            CallableStatement cs = con.prepareCall(sql1)) {

            System.out.print("Introduce el número de cuenta: ");
            String numero = sc.nextLine();

            //ver si existe numero de cuenta
            ps.setString(1, numero);
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                int count = rs.getInt(1);
                System.out.println("El número de cuenta " + numero + " existe en la base de datos.");
 
                cs.setString(1, numero);
                cs.registerOutParameter(2, Types.DOUBLE);
                cs.execute(); 
                
                double saldo = cs.getDouble(2);
                System.out.println("El saldo de la cuenta " + numero +" es: " + saldo);

            }else {
                System.out.println("El número de cuenta " + numero + " no existe en la base de datos.");
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: "+ e.getMessage());
        }

        sc.close();
    }
}