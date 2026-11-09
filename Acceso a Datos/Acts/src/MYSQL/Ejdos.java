
package MYSQL;

import java.sql.*;
import java.util.*;

public class Ejdos {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        String url = "jdbc:mysql://192.168.3.171:3306/tienda_completa";
        String user = "lucia";
        String password = "1311";

        // Valores del portátil
        String urlp = "jdbc:mysql://192.168.1.144:3306/tienda_completa";
        String userp = "admin";
        String passwordp = "1311";

        String sqla = "SELECT id, nombre, precio, stock FROM producto";

        String sqlb = "UPDATE producto SET stock = stock + ? WHERE id = ?";

        String sqlc = "UPDATE producto SET precio = ? WHERE id = ?";

        String sqld = "DELETE FROM producto WHERE id = ?";

        String sql3= "SELECT *\n" + //
                        "FROM producto\n" + //
                        "WHERE precio >= ?\n" + //
                        "AND precio <= ?\n" + //
                        "AND stock > ?;";

        try (
            Connection con = DriverManager.getConnection(urlp, userp, passwordp);
            PreparedStatement ps1 = con.prepareStatement(sqla);
            PreparedStatement ps2 = con.prepareStatement(sqlb);
            PreparedStatement ps3 = con.prepareStatement(sqlc);
            PreparedStatement ps4 = con.prepareStatement(sqld);
            PreparedStatement ps5= con.prepareStatement(sql3);
        ) {

            // APARTADO A
            System.out.println("LISTADO DE PRODUCTOS");

            ResultSet rsa = ps1.executeQuery();

            while (rsa.next()) {
                int id = rsa.getInt("id");
                String nombre = rsa.getString("nombre");
                double precio = rsa.getDouble("precio");
                int stock = rsa.getInt("stock");

                System.out.println("ID: " + id +
                    ", Nombre: " + nombre +
                    ", Precio: " + precio +
                    ", Stock: " + stock);
            }

            rsa.close();

            // APARTADO B
            System.out.println("Introduce el ID del producto que quieres actualizar:");
            int idu = sc.nextInt();

            System.out.println("Introduce la cantidad a añadir al stock:");
            int cantidad = sc.nextInt();

            ps2.setInt(1, cantidad);
            ps2.setInt(2, idu);

            int filasB = ps2.executeUpdate();

            if (filasB > 0) {
                System.out.println("Stock actualizado correctamente");
            } else {
                System.out.println("No existe ningún producto con ese ID");
            }

            // APARTADO C
            System.out.println("Introduce el ID del producto que quieres actualizar:");
            int idp = sc.nextInt();

            System.out.println("Introduce el nuevo precio:");
            double nuevoPrecio = sc.nextDouble();

            ps3.setDouble(1, nuevoPrecio);
            ps3.setInt(2, idp);

            int filasC = ps3.executeUpdate();

            if (filasC > 0) {
                System.out.println("Precio actualizado correctamente");
            } else {
                System.out.println("No existe ningún producto con ese ID");
            }

            // APARTADO D
            System.out.println("Introduce el ID del producto que quieres eliminar:");
            int idd = sc.nextInt();

            ps4.setInt(1, idd);

            int filasD = ps4.executeUpdate();

            if (filasD > 0) {
                System.out.println("Producto eliminado correctamente");
            } else {
                System.out.println("No existe ningún producto con ese ID");
            }

            // APARTADO 3
            System.out.println("Introduce el precio mínimo:");
            double precioMin = sc.nextDouble();
            sc.nextLine();

            System.out.println("Introduce el precio máximo:");
            double precioMax = sc.nextDouble();
            sc.nextLine();

            System.out.println("Introduce el stock mínimo:");
            int stockMin = sc.nextInt();

           
            ps5.setDouble(1, precioMin);
            ps5.setDouble(2, precioMax);
            ps5.setInt(3, stockMin);
            
                
            ResultSet rs3 = ps5.executeQuery(); 
            while(rs3.next()){
                int id= rs3.getInt( "id");
                String nombre= rs3.getString("nombre");
                double precio= rs3.getDouble("precio");
                int stock= rs3.getInt("stock");
                

                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Precio: " + precio + ", Stock: " + stock);
            }
            rs3.close();

        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }

        sc.close();
    }
}
