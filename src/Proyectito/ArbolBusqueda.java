package Proyectito;

public class ArbolBusqueda {
    Pasajero raiz;
    
    // Inserta un nuevo pasajero al árbol
    public void insertar(Pasajero nuevo) {
        raiz = insertarRec(raiz, nuevo);
    }
    // Método recursivo para insertar un pasajero
    private Pasajero insertarRec(Pasajero actual, Pasajero nuevo) {
        if (actual == null){ // Si el nodo está vacío, se inserta aquí
            return nuevo;
        }
        // Comparación por ID de pasajero
        if (nuevo.idPasajero < actual.idPasajero){ 
            actual.izquierda = insertarRec(actual.izquierda, nuevo); // Inserta en subárbol izquierdo
        }
        else if (nuevo.idPasajero > actual.idPasajero){
            actual.derecha = insertarRec(actual.derecha, nuevo); // Inserta en subárbol derecho
        }
        return actual; // Retorna el nodo actual
    }
    
    
    // Busca un pasajero por ID, e imprime el resultado junto con los saltos y tiempo
   public String buscar(int id) {
       long inicio = System.nanoTime(); // Tiempo de inicio
       int saltos = 0;
       Pasajero actual = raiz;

    while (actual != null) {
        saltos++; // Contador de saltos
        if (id == actual.idPasajero) {
            long tiempo = System.nanoTime() - inicio;
            return "Encontrado: " + actual.getPasajero() +
                   "\nSaltos: " + saltos +
                   "\nTiempo: " + tiempo + " ms";
        } else if (id < actual.idPasajero) {
            actual = actual.izquierda; // Buscar en subárbol izquierdo
        } else {
            actual = actual.derecha; // Buscar en subárbol derecho
        }
    }
    return "Pasajero no encontrado.";
    }
    
    
    // Actualiza el nombre y DPI de un pasajero por su ID
    public void actualizar(int id, String nuevoNombre, String nuevoDPI) {
        Pasajero nodo = buscarNodo(raiz, id); // Busca el nodo por ID
        if (nodo != null) {
            nodo.nombre = nuevoNombre;
            nodo.dpi = nuevoDPI;
            System.out.println("Actualizado: " + nodo.getPasajero());
        } else {
            System.out.println("Pasajero no encontrado.");
        }
    }
    // Método auxiliar para buscar un nodo directamente sin imprimir
    public Pasajero buscarNodo(Pasajero actual, int id) {
        while (actual != null) {
            if (id == actual.idPasajero){
                return actual;
            }
            else if (id < actual.idPasajero){ 
                actual = actual.izquierda;
            }
            else{ 
                actual = actual.derecha;
            }
        }
        return null;
    }
    
    
    // Elimina un pasajero por ID y muestra saltos y tiempo
    public void eliminar(int id) {
        long inicio = System.nanoTime();
        int[] saltos = new int[1]; // Usamos arreglo para pasar por referencia
        raiz = eliminarRec(raiz, id, saltos);
        long tiempo = System.nanoTime() - inicio;
        System.out.println("Saltos: " + saltos[0] + ", Tiempo: " + tiempo + " ns");
    }
    // Método recursivo para eliminar un nodo
    private Pasajero eliminarRec(Pasajero nodo, int id, int[] saltos) {
        if (nodo == null){ // No encontrado
            return null;
        }
        saltos[0]++; // Contar salto

        if (id < nodo.idPasajero) {
            nodo.izquierda = eliminarRec(nodo.izquierda, id, saltos); // Buscar a la izquierda
        } else if (id > nodo.idPasajero) {
            nodo.derecha = eliminarRec(nodo.derecha, id, saltos); // Buscar a la derecha
        } else { 
            // Nodo encontrado, ahora eliminamos
            if (nodo.izquierda == null){
                return nodo.derecha; // Caso 1: sin hijo izquierdo
            }
            if (nodo.derecha == null){
                return nodo.izquierda; // Caso 2: sin hijo derecho
            }

            // Caso 3: tiene ambos hijos
            Pasajero sucesor = encontrarMinimo(nodo.derecha); // Buscar el mínimo del lado derecho
            nodo.idPasajero = sucesor.idPasajero; // Reemplazar datos
            nodo.nombre = sucesor.nombre;
            nodo.dpi = sucesor.dpi;
            // Eliminar el nodo duplicado que movimos arriba
            nodo.derecha = eliminarRec(nodo.derecha, sucesor.idPasajero, saltos);
        }

        return nodo;
    }
    // Encuentra el nodo con el valor mínimo (más a la izquierda
    private Pasajero encontrarMinimo(Pasajero nodo) {
        while (nodo.izquierda != null)
            nodo = nodo.izquierda;
        return nodo;
    }
    
    
    // Imprime el árbol en forma jerárquica
    public void imprimirArbol() {
        imprimir(raiz, "", true);
    }
    // Método recursivo para impresión visual del árbol
    private void imprimir(Pasajero nodo, String prefijo, boolean esUltimo) {
        if (nodo != null) {
            System.out.println(prefijo + (esUltimo ? "+-- " : "|-- ") +
                               "ID: " + nodo.idPasajero + ", Nombre: " + nodo.nombre);

            if (nodo.izquierda != null || nodo.derecha != null) {
                if (nodo.izquierda != null && nodo.derecha != null) {
                    imprimir(nodo.izquierda, prefijo + (esUltimo ? "    " : "|   "), false);
                    imprimir(nodo.derecha, prefijo + (esUltimo ? "    " : "|   "), true);
                } else if (nodo.izquierda != null) {
                    imprimir(nodo.izquierda, prefijo + (esUltimo ? "    " : "|   "), true);
                } else {
                    imprimir(nodo.derecha, prefijo + (esUltimo ? "    " : "|   "), true);
                }
            }
        }
    }
}
