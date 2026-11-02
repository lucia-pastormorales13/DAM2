package MYSQL;

import java.util.*;
import java.sql.*;

public class Ejsiete {
   public static void main(String[] args) {

      Scanner sc = new Scanner(System.in);
      String url = "jdbc:mysql://192.168.3.171:3306/banco";
      String user = "lucia";
      String password = "1311";
      String sqlpi = "{CALL ingresar_dinero(?, ?)}";
      String sqlo= "{CALL obtener_saldo(?, ?)}";
      String sqlfc= "{?, ? CALL calcular_comisión(?)}";
       

      try (Connection conexion = DriverManager.getConnection(url, user, password);
         CallableStatement sentencia = conexion.prepareCall(sqlp);
      ) {

      } catch (Exception e) {
         // TODO: handle exception
      }
   }

}