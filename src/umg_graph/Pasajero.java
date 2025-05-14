package proyectoprogra3;

public class Pasajero {
    int idPasajero;
    String dpi;
    String nombre;
    Pasajero izquierda;
    Pasajero derecha;

    public Pasajero(int idPasajero, String dpi, String nombre) {
        this.idPasajero = idPasajero;
        this.dpi = dpi;
        this.nombre = nombre;
    }
    
    
    public String getPasajero() {
        return "ID: " + idPasajero + ", DPI: " + dpi + ", Nombre: " + nombre;
    }
}
