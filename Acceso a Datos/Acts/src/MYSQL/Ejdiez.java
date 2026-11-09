package MYSQL;
import java.sql.*;
import java.util.*;

public class Ejdiez {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        String url = "jdbc:mysql://192.168.3.171:3306/tienda_completa";
        String user = "lucia";
        String password = "1311";


        // Valores del portátil
        String urlp = "jdbc:mysql://192.168.1.144:3306/banco";
        String userp = "admin";
        String passwordp = "1311";

        String sql= "{CALL retirar_dinero (?, ?)}";
        String sql2= "SELECT COUNT(*) FROM cuenta WHERE numero = ? AND activa = ?;";
        String sql3= "SELECT saldo FROM cuenta WHERE numero = ?;";
        try (
            Connection con = DriverManager.getConnection(urlp, userp, passwordp);
            PreparedStatement ps= con.prepareStatement(sql2);
            PreparedStatement ps2= con.prepareStatement(sql3);
            CallableStatement cs = con.prepareCall(sql);
        ) {
           
            System.out.println("Introduce el número de cuenta: ");
            String num= sc.nextLine();
            System.out.println("Introduce la cantidad a retirar: ");
            double cantidad= sc.nextDouble();

            // Verificar si el número de cuenta existe
            ps.setString(1, num);
            ps.setInt(2, 1); // Verificar si la cuenta está activa
            
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("El número de cuenta " + num + " existe en la base de datos.");

                if(rs.getInt(1) != 0){
                    System.out.println("La cuenta " + num + " está activa.");

                    ps2.setString(1, num);
                    ResultSet rs2 = ps2.executeQuery();
                    if(rs2.next()){
                        double saldoActual = rs2.getDouble("saldo");
                        if(saldoActual < 0){
                            System.out.println("Saldo NEGATIVO. No se puede realizar la operación.");
                            return;
                        }

                        if(saldoActual <= cantidad){
                            System.out.println("Saldo insuficiente. no se puede realizar la operación. Introduce una cantidad menor o igual al saldo actual: " + saldoActual);
                            double nuevaCantidad = sc.nextDouble();
                            cs.setString(1, num);
                            cs.setDouble(2, nuevaCantidad);
                            cs.registerOutParameter(2, Types.DOUBLE);
                            cs.executeUpdate();
                            double saldoActualizado = cs.getDouble(2);
                            System.out.println("Dinero retirado correctamente. Saldo actualizado: " + saldoActualizado);
                            return;

                            
                        }else{
                            System.out.println("Saldo suficiente. Se puede realizar la operación.");
                        }
                    }
                    cs.setString(1, num);
                    cs.setDouble(2, cantidad);
                    cs.registerOutParameter(2, Types.DOUBLE);
                    
                    cs.executeUpdate();
                    double saldoActualizado = cs.getDouble(2);
                    System.out.println("Dinero retirado correctamente. Saldo actualizado: " + saldoActualizado);

                }else{
                    System.out.println("La cuenta " + num + " no está activa. No se puede realizar la operación.");
                    return;
                }
                
            }else{                
                System.out.println("El número de cuenta " + num + " no existe en la base de datos.");
                return;
            }



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}