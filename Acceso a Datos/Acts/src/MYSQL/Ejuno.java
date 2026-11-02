package MYSQL;

import java.util.*;
import java.sql.*;

public class Ejuno {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String url = "jdbc:mysql://192.168.3.171:3306/tienda";
        String user = "lucia";
        String password = "1311";

        try (
                Connection con = DriverManager.getConnection(url, user, password);
                Statement st = con.createStatement();
                PreparedStatement ps1= con.prepareStatement("SELECT * FROM producto ;");
                PreparedStatement ps2= con.prepareStatement("SELECT stock FROM producto ;");

        ) {
            String insertSQL = "INSERT INTO producto (nombre, precio, stock) VALUES " +
                    "('Teclado mecánico', 59.90, 15), " +
                    "('Ratón inalámbrico', 24.50, 30), " +
                    "('Monitor 24 pulgadas', 149.99, 8), " +
                    "('Auriculares Bluetooth', 39.95, 20)";
            int filasInsertadas = st.executeUpdate(insertSQL);
            System.out.println("¡" + filasInsertadas + " productos han sido insertados correctamente!");

            String updateSQL = "UPDATE producto " +
                    "SET precio = CASE WHEN nombre = 'Ratón inalámbrico' THEN 29.90 ELSE precio END, " +
                    "    stock = CASE WHEN nombre = 'Auriculares Bluetooth' THEN 10 ELSE stock END " +
                    "WHERE nombre IN ('Ratón inalámbrico', 'Auriculares Bluetooth')";

            int filasActualizadas = st.executeUpdate(updateSQL);
            System.out.println("¡Productos actualizados correctamente! (Filas afectadas: " + filasActualizadas + ")");

            ResultSet rs= ps1.executeQuery();

            System.out.println("\n--- LISTADO DE PRODUCTOS ---");
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");
                int stock = rs.getInt("stock");

                System.out.println(
                        "ID: " + id + " | Nombre: " + nombre + " | Precio: " + precio + " € | Stock: " + stock);
            }




        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}