//programa para contar las letras de un fichero
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Pract {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);

        // leer
        
        try (
                FileReader fr = new FileReader("alumnos.txt");
                BufferedReader lector = new BufferedReader(fr);
            ) {
            
            System.out.println("inserte la letra que desee buscar en el fichero: ");
            String letra= sc.nextLine();

            
            int contador= 0;        
            String linea;
            while ((linea = lector.readLine()) != null) {
               
                if(linea.contains(letra)){
                    //contador ++;      no me va servir ya que me contara las lineas que tienen la letra
                    for( int i=0; i< linea.length(); i++){
                        if(linea.charAt(i)== letra.charAt(0)){
                            contador++;
                        }

                    }
                }
                System.out.println(linea);
            }
            System.out.println("El total de letras encontradas en el fichero es: "+ contador);

        } catch (IOException e) {
            System.out.println("Error al leer el fichero.");
        }

                
        

    }
}