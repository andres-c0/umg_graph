package arbol_busqueda;

import javax.swing.SwingUtilities;

public class ProyectoProgra3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true); // ? Abre la ventana
        });
    }
}
