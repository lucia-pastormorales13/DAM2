package MYSQL;
import java.sql.*;

public class Ejuno{
    public static void main(String[] args){
        //valores de clase
        String urlc = "jdbc:mysql://192.168.3.171:3306/tienda";
        String userc = "lucia";
        String passwordc = "1311";

        //valores del portatil
        String urlp = "jdbc:mysql://192.168.1.144:3306/tienda";
        String userp = "admin";
        String passwordp = "1311";

        String sqlt= "CREATE TABLE IF NOT EXISTS producto (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "nombre VARCHAR(100) NOT NULL," +
                "precio DECIMAL(10,2) NOT NULL," +
                "stock INT NOT NULL" +
                ");";

        String sqle= "INSERT INTO producto (nombre, precio, stock)\n" + 
                        "VALUES\n" + 
                        "('Teclado mecánico', 59.90, 15),\n" + 
                        "('Ratón inalámbrico', 24.50, 30),\n" + 
                        "('Monitor 24 pulgadas', 149.99, 8),\n" + 
                        "('Auriculares Bluetooth', 39.95, 20);";

        String sqls= "UPDATE producto\n" + 
                        "SET stock = 10\n" + 
                        "WHERE nombre = 'Auriculares Bluetooth';";

        String sqlv= "SELECT *\n" + //
                        "FROM producto\n" + //
                        "WHERE nombre IN ('Ratón inalámbrico', 'Auriculares Bluetooth');";

        try (Connection con = DriverManager.getConnection(urlp, userp, passwordp);
            Statement st= con.createStatement();
            PreparedStatement ps1= con.prepareStatement(sqle);
            PreparedStatement ps2= con.prepareStatement(sqls);
            PreparedStatement ps3= con.prepareStatement(sqlv);
        ){
            st.executeUpdate(sqlt);
            System.out.println("Tabla creada correctamente");
            ps1.executeUpdate();
            System.out.println("Datos insertados correctamente");
            ps2.executeUpdate();
            System.out.println("Datos actualizados correctamente");
            ResultSet rs= ps3.executeQuery();
            while(rs.next()){
                int id= rs.getInt("id");
                String nombre= rs.getString("nombre");
                double precio= rs.getDouble("precio");
                int stock= rs.getInt("stock");
                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Precio: " + precio + ", Stock: " + stock);
            }


        }catch (SQLException e){
            System.out.println("Error al crear la tabla: " + e.getMessage());

    }
    }

}