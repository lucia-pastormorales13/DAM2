
package MYSQL;

import java.sql.*;
import java.util.*;

public class Ejsiete {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Valores del portátil
        String urlp = "jdbc:mysql://192.168.1.144:3306/banco";
        String userp = "admin";
        String passwordp = "1311";

        String sqlp2 = "{CALL ingresar_dinero(?, ?)}";

        String sql1 = "SELECT saldo FROM cuenta WHERE numero = ?";

        try (
            Connection conexion = DriverManager.getConnection(urlp, userp, passwordp);
            PreparedStatement ps = conexion.prepareStatement(sql1);
            CallableStatement cs = conexion.prepareCall(sqlp2);
            PreparedStatement ps2 = conexion.prepareStatement(sql1);
        ) {

            System.out.print("Introduce el número de cuenta: ");
            String numero = sc.nextLine();

            System.out.print("Introduce la cantidad a ingresar: ");
            double cantidad = sc.nextDouble();

            // Consultar y mostrar el saldo actual
            ps.setString(1, numero);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double saldoActual = rs.getDouble("saldo");
                System.out.println("Saldo actual: " + saldoActual);
            } else {
                System.out.println("No se encontró la cuenta: " + numero);
                return;
            }

            rs.close();

            // Ejecutar el procedimiento ingresar_dinero
            cs.setString(1, numero);
            cs.setDouble(2, cantidad);

            cs.executeUpdate();

            System.out.println("Dinero ingresado correctamente");

            // Consultar y mostrar el saldo actualizado
            ps2.setString(1, numero);

            ResultSet rs2 = ps2.executeQuery();

            if (rs2.next()) {
                double saldoActual2 = rs2.getDouble("saldo");
                System.out.println("Saldo actualizado: " + saldoActual2);
            } else {
                System.out.println("No se encontró la cuenta: " + numero);
            }

            rs2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        sc.close();
    }
}