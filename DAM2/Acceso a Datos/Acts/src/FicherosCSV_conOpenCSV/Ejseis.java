package FicherosCSV_conOpenCSV;

import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.*;
import com.opencsv.CSVWriter;

/*Creamos un csv con los datos pedidos, utilizando , como delimitador gastamdo arrays */
class Ejseis {
    public static void main(String[] args) {
        Scanner sc= new Scanner(System.in);
        String[] nombres = new String[3];
        String[] apellidos = new String[3];
        int[] edades = new int[3];
        double[] notas = new double[3];
        
        int cont = 0;
        while(cont < 3) {
            int numAlumno = cont + 1;
            
            System.out.println("Inserte el nombre del alumno " + numAlumno + ": ");
            nombres[cont] = sc.nextLine();
            
            System.out.println("Inserte el apellido del alumno " + numAlumno + ": ");
            apellidos[cont] = sc.nextLine();
            
            System.out.println("Inserte la edad del alumno " + numAlumno + ": ");
            edades[cont] = sc.nextInt();
            
            System.out.println("Inserte la nota del alumno " + numAlumno + ": ");
            notas[cont] = sc.nextDouble();
            sc.nextLine(); // Limpiar el buffer del Enter
            
            cont++;
        }
       
        try (
                FileWriter fw = new FileWriter("alumnos.csv");
                CSVWriter escritor = new CSVWriter(fw)
            ){
            escritor.writeNext(new String[] { "nombre", "Apellido", "edad", "nota" });
           // escritor.writeNext( nombres[1],  apellidos[1], edades[1], notas[1]);
            escritor.writeNext(new String[] { "Carlos", "21", "6.75" });
            escritor.writeNext(new String[] { "María", "19", "9.25" });
        } catch (Exception e) {
            System.out.println("Error al escribir el fichero.");
        }
    }

}