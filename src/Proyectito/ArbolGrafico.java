package Proyectito;

import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import javax.swing.*;

public class ArbolGrafico extends JFrame {
    public ArbolGrafico(Pasajero raiz) {
        setTitle("Visualización del Árbol");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana

        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {
            dibujarNodo(graph, parent, raiz, 400, 20, 100);
        } finally {
            graph.getModel().endUpdate();
        }

        mxCompactTreeLayout layout = new mxCompactTreeLayout(graph, false); // Horizontal = false ? Árbol vertical
        layout.execute(parent);

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        getContentPane().add(graphComponent);
    }

    private Object dibujarNodo(mxGraph graph, Object parent, Pasajero nodo, int x, int y, int offsetX) {
        if (nodo == null) return null;

        String label = nodo.idPasajero + "\n" + nodo.nombre;
        Object actual = graph.insertVertex(parent, null, label, x, y, 80, 40);

        if (nodo.izquierda != null) {
            Object hijoIzq = dibujarNodo(graph, parent, nodo.izquierda, x - offsetX, y + 80, offsetX / 2);
            graph.insertEdge(parent, null, "", actual, hijoIzq);
        }

        if (nodo.derecha != null) {
            Object hijoDer = dibujarNodo(graph, parent, nodo.derecha, x + offsetX, y + 80, offsetX / 2);
            graph.insertEdge(parent, null, "", actual, hijoDer);
        }

        return actual;
    }
}
