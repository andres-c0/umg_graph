/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Proyectito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class VentanaPrincipal extends javax.swing.JFrame {

    private ArbolBusqueda arbol = new ArbolBusqueda();
    private ArbolGrafico ventanaArbol = null;
    private static final String RUTA_CONFIG = "config.txt";
    private DefaultTableModel modeloTabla;
    private JTable tablaPasajeros;
    private JTextArea areaArbol;

    private JTextField campoID, campoNombre, campoDPI, campoBuscar;
    private File archivoActual = null; // ? Para recordar el archivo CSV cargado

    public VentanaPrincipal() {
        setTitle("Gestión de Pasajeros");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Inicializar componentes de la interfaz
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "DPI", "Nombre"}, 0);
        tablaPasajeros = new JTable(modeloTabla);
        areaArbol = new JTextArea(10, 50);
        areaArbol.setEditable(false);

        campoID = new JTextField(5);
        campoDPI = new JTextField(10);
        campoNombre = new JTextField(10);
        campoBuscar = new JTextField(10);

        JScrollPane scrollTabla = new JScrollPane(tablaPasajeros);
        JScrollPane scrollArbol = new JScrollPane(areaArbol);
        scrollArbol.setBorder(BorderFactory.createTitledBorder("Árbol Binario"));

        // Panel superior con botones y campos
        JPanel panelTop = new JPanel(new GridLayout(3, 1));

        // Fila 1: Cargar CSV y Buscar
        JPanel fila1 = new JPanel();
        JButton btnCargarCSV = new JButton("Cargar CSV");
        JButton btnBuscar = new JButton("Buscar por ID");
        fila1.add(btnCargarCSV);
        fila1.add(new JLabel("ID:"));
        fila1.add(campoBuscar);
        fila1.add(btnBuscar);

        // Fila 2: Insertar pasajero
        JPanel fila2 = new JPanel();
        JButton btnInsertar = new JButton("Insertar");
        fila2.add(new JLabel("ID:"));
        fila2.add(campoID);
        fila2.add(new JLabel("DPI:"));
        fila2.add(campoDPI);
        fila2.add(new JLabel("Nombre:"));
        fila2.add(campoNombre);
        fila2.add(btnInsertar);

        // Fila 3: Eliminar, Actualizar y Mostrar Árbol
        JPanel fila3 = new JPanel();
        JButton btnEliminar = new JButton("Eliminar por ID");
        JButton btnActualizar = new JButton("Actualizar por ID");
        JButton btnMostrarArbol = new JButton("Mostrar Árbol");
        fila3.add(btnEliminar);
        fila3.add(btnActualizar);
        fila3.add(btnMostrarArbol);

        panelTop.add(fila1);
        panelTop.add(fila2);
        panelTop.add(fila3);

        // Agregar todo al layout principal
        add(panelTop, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(scrollArbol, BorderLayout.SOUTH);

        // Luego de que todo está inicializado, se intenta cargar archivo desde config.txt
        File config = new File(RUTA_CONFIG);
        if (config.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(config))) {
                String rutaGuardada = br.readLine();
                if (rutaGuardada != null) {
                    File archivo = new File(rutaGuardada);
                    if (archivo.exists()) {
                        archivoActual = archivo;
                        cargarDesdeArchivo(archivo);  // Ya es seguro
                    }
                }
            } catch (IOException e) {
                System.out.println("No se pudo leer la configuración: " + e.getMessage());
            }
        }

        // Acción: Cargar CSV
        btnCargarCSV.addActionListener(e -> cargarCSV());

        // Acción: Insertar pasajero
        btnInsertar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(campoID.getText());
                String dpi = campoDPI.getText();
                String nombre = campoNombre.getText();
                Pasajero p = new Pasajero(id, dpi, nombre);
                arbol.insertar(p);
                modeloTabla.addRow(new Object[]{id, dpi, nombre});
                limpiarCampos();
                guardarCSV();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID debe ser numérico.");
            }
        });

        // Acción: Buscar
        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(campoBuscar.getText());
                String resultado = arbol.buscar(id); // ahora devuelve mensaje
                JOptionPane.showMessageDialog(this, resultado);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        });

        // Acción: Eliminar
        btnEliminar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(campoID.getText());
                arbol.eliminar(id);
                actualizarTabla();
                guardarCSV();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        });

        // Acción: Actualizar pasajero
        btnActualizar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(campoID.getText());
                String nombre = campoNombre.getText();
                String dpi = campoDPI.getText();
                arbol.actualizar(id, nombre, dpi);
                actualizarTabla();
                guardarCSV();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.");
            }
        });

        // Acción: Mostrar árbol
        btnMostrarArbol.addActionListener(e -> {
            if (ventanaArbol == null || !ventanaArbol.isDisplayable()) {
                ventanaArbol = new ArbolGrafico(arbol.raiz);
                ventanaArbol.setVisible(true);
            } else {
                ventanaArbol.toFront(); // Trae la ventana al frente si ya está abierta
            }
        });
    }

    private void cargarCSV() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            archivoActual = fileChooser.getSelectedFile();
            guardarRutaCSV(archivoActual.getAbsolutePath());
            arbol = new ArbolBusqueda();
            modeloTabla.setRowCount(0);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(archivoActual), StandardCharsets.UTF_8))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    int id = Integer.parseInt(partes[0]);
                    String dpi = partes[1];
                    String nombre = partes[2];
                    Pasajero p = new Pasajero(id, dpi, nombre);
                    arbol.insertar(p);
                    modeloTabla.addRow(new Object[]{id, dpi, nombre});
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage());
            }
        }
    }

    private void guardarCSV() {
        if (archivoActual == null) {
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(archivoActual), StandardCharsets.UTF_8))) {
            guardarNodoCSV(arbol.raiz, writer);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar CSV: " + e.getMessage());
        }
    }

    private void guardarNodoCSV(Pasajero nodo, BufferedWriter writer) throws IOException {
        if (nodo != null) {
            guardarNodoCSV(nodo.izquierda, writer);
            writer.write(nodo.idPasajero + "," + nodo.dpi + "," + nodo.nombre);
            writer.newLine();
            guardarNodoCSV(nodo.derecha, writer);
        }
    }

    private void guardarRutaCSV(String ruta) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RUTA_CONFIG))) {
            writer.write(ruta);
        } catch (IOException e) {
            System.out.println("No se pudo guardar la ruta del archivo CSV: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        campoID.setText("");
        campoDPI.setText("");
        campoNombre.setText("");
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        recorrerYAgregar(arbol.raiz);
    }

    private void recorrerYAgregar(Pasajero nodo) {
        if (nodo != null) {
            recorrerYAgregar(nodo.izquierda);
            modeloTabla.addRow(new Object[]{nodo.idPasajero, nodo.dpi, nodo.nombre});
            recorrerYAgregar(nodo.derecha);
        }
    }

    private void cargarDesdeArchivo(File archivo) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(archivo), StandardCharsets.UTF_8))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                int id = Integer.parseInt(partes[0]);
                String dpi = partes[1];
                String nombre = partes[2];
                Pasajero p = new Pasajero(id, dpi, nombre);
                arbol.insertar(p);
                modeloTabla.addRow(new Object[]{id, dpi, nombre});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 711, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 485, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new VentanaPrincipal().setVisible(true));
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
