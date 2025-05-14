package proyectoprogra3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ProyectoProgra3 {
    
    public static void main(String[] args) {
        //String archivoCSV = "datos.csv";
        String archivoCSV = "C:/Users/HP/Documents/Angel/Programacion3/pasajeros.csv";
        System.out.println(archivoCSV);
        
        File file = new File(archivoCSV);
        if(file.exists()){
            System.out.println("El archivo existe");
            
            ArbolBusqueda arbol = new ArbolBusqueda();
            
            try{
                BufferedReader br = new BufferedReader(
                                            new InputStreamReader(
                                                new FileInputStream(archivoCSV), 
                                                    StandardCharsets.UTF_8));
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    int id = Integer.parseInt(partes[0]);
                    String dpi = partes[1];
                    String nombre = partes[2];
                    arbol.insertar(new Pasajero(id, dpi, nombre));
                }
            } catch (IOException e) {
                System.out.println("Error al leer CSV: " + e.getMessage());
            }

            arbol.imprimirArbol();
            
            // Pruebas
            System.out.println("\n-- Buscar ID 12345 --");
            arbol.buscar(12345);

            System.out.println("\n-- Actualizar ID 12345 --");
            arbol.actualizar(12345, "Ignacio Pérez", "1234567890333");

            System.out.println("\n-- Eliminar ID 12345 --");
            arbol.eliminar(12345);

            System.out.println("\n-- Árbol final --");
            arbol.imprimirArbol();
        }else{
            System.out.println("Archivo no encontrado");
        }
    }
    
}
