package MYSQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class Ejseis{
    public static void main(String[] args){
        Scanner sc= new Scanner(System.in);

        // Valores del portátil
        String urlp = "jdbc:mysql://192.168.1.144:3306/banco";
        String userp = "admin";
        String passwordp = "1311";

        String sql1="SELECT saldo FROM cuenta WHERE id= ?;";
        String sql2="UPDATE cuenta SET saldo= saldo- ? WHERE id= ?;";
        String sql3="UPDATE cuenta SET saldo= saldo+ ? WHERE id= ?;";

        try( Connection con= DriverManager.getConnection(urlp, userp, passwordp);
        PreparedStatement ps1 = con.prepareStatement(sql1);
        PreparedStatement ps2 = con.prepareStatement(sql2);
        PreparedStatement ps3 = con.prepareStatement(sql3);
        ){
            con.setAutoCommit(false);
            System.out.println("Introduce el id de la cuenta:");
            int id= sc.nextInt();
            System.out.println("Introduce la cantidad a retirar:");
            double cantidad= sc.nextDouble();
            sc.nextLine(); // Consumir el salto de línea
            System.out.println("Introduce el id de la cuenta destino:");
            int idd= sc.nextInt();

            //paso 1: comprobar saldo
            ps1.setInt(1, id);
            ResultSet rs= ps1.executeQuery();
            if(rs.next()){
                double saldo= rs.getDouble("saldo");
                if(saldo>=cantidad){
                    System.out.println("Saldo suficiente, se puede realizar la transferencia");
                    System.out.println("Saldo actual: " + saldo);
                    
                } else{
                   System.out.println("Saldo insuficiente, no se puede realizar la transferencia");
                    return;
                }
            }

            //paso 2: eliminar dinero de la cuenta origen
            ps2.setDouble(1, cantidad);
            ps2.setInt(2, id);
            ps2.executeUpdate();
            System.out.println("Dinero retirado de la cuenta origen");
            con.commit(); // Confirmar la operación de retirada
            
            //paso 3: añadir dinero a la cuenta destino
            ps3.setDouble(1, cantidad);
            ps3.setInt(2, idd);
            ps3.executeUpdate();
            System.out.println("Dinero añadido a la cuenta destino");
            con.commit(); // Confirmar la operación de adición
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
}