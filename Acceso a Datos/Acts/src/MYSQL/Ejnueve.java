package MYSQL;

import java.sql.*;
import java.util.*;

public class Ejnueve {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Valores del portátil
        String urlp = "jdbc:mysql://192.168.1.144:3306/banco";
        String userp = "admin";
        String passwordp = "1311";

        String sql = "{? = CALL calcular_comision(?, ?)}";

        try (Connection con= DriverManager.getConnection(urlp, userp, passwordp);
            CallableStatement cs = con.prepareCall(sql)) {

                System.out.println("Inserte la cantidad ddel importe:");
                Double importe = sc.nextDouble();
                sc.nextLine();
                System.err.println("Inserte el porcentaque que se corresponda a la comision:");
                Double porcentaje = sc.nextDouble();

                cs.registerOutParameter(1, Types.DOUBLE);
                cs.setDouble(2, importe);
                cs.setDouble(3, porcentaje);
                cs.execute();
                Double comision = cs.getDouble(1);
                System.out.println("La comision calculada es: " + comision);
                Double total = importe + comision;
                System.out.println("El total a pagar es: " + total);

        }catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: "+ e.getMessage());
        }

    }
}