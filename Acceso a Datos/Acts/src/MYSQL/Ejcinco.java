package MYSQL;

import java.sql.*;
import java.util.*;

public class Ejcinco {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String urlp = "jdbc:mysql://192.168.1.144:3306/banco";
        String userp = "admin";
        String passwordp = "1311";

        String sqli= "UPDATE cuenta\n" + //
                      "SET saldo = saldo + ?\n" + //
                      "WHERE id = ?;";

         String sqlr= "UPDATE cuenta\n" + //
                      "SET saldo = saldo - ?\n" + //
                      "WHERE id = ?;";

         String sqls= "SELECT saldo FROM cuenta\n" + //
                      "WHERE id = ?;";

        try (Connection con = DriverManager.getConnection(urlp, userp, passwordp);
            PreparedStatement ps1 = con.prepareStatement(sqli);
            PreparedStatement ps2 = con.prepareStatement(sqlr);
            PreparedStatement ps3 = con.prepareStatement(sqls)) {

            int opcion;

            do {

                System.out.println("============ MENU ============");
                System.out.println("1. Comprobar autocommit");
                System.out.println("2. Activar el autocommit");
                System.out.println("3. Desactivar el autocommit");
                System.out.println("4. Realizar un ingreso");
                System.out.println("5. Realizar una retirada");
                System.out.println("6. Consultar saldo");
                System.out.println("7. Hacer commit de las operaciones");
                System.out.println("8. Salir");

                System.out.print("Introduce una opción: ");
                opcion = sc.nextInt();

                switch (opcion) {

                    case 1:
                        boolean autocommit = con.getAutoCommit();

                        System.out.println("El autocommit está " +
                            (autocommit ? "activado" : "desactivado"));
                        break;

                    case 2:
                        con.setAutoCommit(true);
                        System.out.println("Autocommit activado");
                        break;

                    case 3:
                        con.setAutoCommit(false);
                        System.out.println("Autocommit desactivado");
                        break;

                    case 4:
                        // Realizar un ingreso
                        System.out.print("Introduce el ID de la cuenta: ");
                        int idCuentaIngreso = sc.nextInt();
                        System.out.print("Introduce la cantidad a ingresar: ");
                        double cantidadIngreso = sc.nextDouble();

                        ps1.setDouble(1, cantidadIngreso);
                        ps1.setInt(2, idCuentaIngreso);
                        ps1.executeUpdate();
                        System.out.println("Ingreso realizado correctamente");


                        break;

                    case 5:
                         System.out.print("Introduce el ID de la cuenta: ");
                         int idCuentaRetirada = sc.nextInt();
                         System.out.print("Introduce la cantidad a retirar: ");
                         double cantidadRetirada = sc.nextDouble();

                        ps2.setDouble(1, cantidadRetirada);
                        ps2.setInt(2, idCuentaRetirada);
                        ps2.executeUpdate();
                        System.out.println("Retiro realizado correctamente");

                        break;

                    case 6:
                        // Consultar saldo
                        System.out.print("Introduce el ID de la cuenta: ");
                        int idCuentaSaldo = sc.nextInt();

                        ps3.setInt(1, idCuentaSaldo);
                        ResultSet rs = ps3.executeQuery();
                        if (rs.next()) {
                           double saldo = rs.getDouble("saldo");
                           System.out.println("El saldo de la cuenta con ID " + idCuentaSaldo + " es: " + saldo);
                        }
                        break;

                    case 7:
                     autocommit = con.getAutoCommit();
                     if (autocommit) {
                         System.out.println("El autocommit está activado, no es necesario hacer commit");
                     }
                     else {
                         con.commit();
                         System.out.println("Operaciones confirmadas");
                     }
                        break;

                    case 8:
                        System.out.println("Saliendo del programa...");
                        return;

                    default:
                        System.out.println("Opción inválida");
                }

            } while (opcion != 8);

        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }

        sc.close();
    }
}