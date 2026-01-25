package vuelosfis.controlador;

import java.util.ArrayList;
import vuelosfis.modelo.Vuelo;
import vuelosfis.modelo.Reserva;
import vuelosfis.persistencia.GestorArchivos;

public class ControladorReserva {

    // Esta lista es la "Memoria RAM" del sistema.
    // Aquí vivirán los vuelos mientras el programa esté abierto.
    private ArrayList<Vuelo> catalogoVuelos;

    public ControladorReserva() {
        // Inicializamos la lista vacía
        this.catalogoVuelos = new ArrayList<>();
    }

    /**
     * PASO 1: Cargar datos
     * Llama al GestorArchivos para llenar la memoria.
     */
    public void cargarDatosIniciales() {
        this.catalogoVuelos = GestorArchivos.cargarVuelos();
        
        if (catalogoVuelos.isEmpty()) {
            System.out.println("ADVERTENCIA: No se cargaron vuelos. Verifique 'vuelos.txt'.");
        } else {
            System.out.println("Sistema cargado. Vuelos disponibles en memoria: " + catalogoVuelos.size());
        }
    }

    /**
     * PASO 2: Buscar Vuelos
     * Filtra el catálogo según lo que el usuario escriba.
     * @param origen
     * @param destino
     * @return 
     */
    public ArrayList<Vuelo> buscarVuelos(String origen, String destino) {
        ArrayList<Vuelo> resultados = new ArrayList<>();

        for (Vuelo v : catalogoVuelos) {
            // Comparamos ignorando mayúsculas/minúsculas
            boolean coincideOrigen = v.getRuta().getOrigen().getNombre().equalsIgnoreCase(origen);
            boolean coincideDestino = v.getRuta().getDestino().getNombre().equalsIgnoreCase(destino);

            if (coincideOrigen && coincideDestino) {
                resultados.add(v);
            }
        }
        return resultados;
    }

    /**
     * PASO 3: Completar Reserva
     * Recibe la reserva finalizada y la manda a guardar al disco.
     * @param nuevaReserva
     */
    public void finalizarReserva(Reserva nuevaReserva) {
        // 1. Validaciones de negocio (opcional)
        if (nuevaReserva.getListaDetalles().isEmpty()) {
            System.out.println("Error: No se puede guardar una reserva vacía.");
            return;
        }

        // 2. Guardar en archivo
        GestorArchivos.guardarReserva(nuevaReserva);
        
        System.out.println("¡ÉXITO! La reserva se ha procesado y guardado.");
    }

    // Getter para ver todo el catálogo si es necesario
    public ArrayList<Vuelo> getCatalogoVuelos() {
        return catalogoVuelos;
    }
}
