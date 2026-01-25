
package vuelosfis.controlador;

import java.util.ArrayList;
import vuelosfis.modelo.Asiento;
import vuelosfis.modelo.Reserva;
import vuelosfis.modelo.Vuelo;
import vuelosfis.persistencia.GestorArchivos;

public class ControladorReserva {
    private ArrayList<Vuelo> catalogoVuelos;

    public ControladorReserva() {
        this.catalogoVuelos = new ArrayList<>();
    }

    /**
     * MÉTODOS DE INICIO 
     */
    public void cargarDatosIniciales() {
        System.out.println("Cargando sistema...");

        // PASO 1: Cargar los aviones (Vienen vacíos)
        this.catalogoVuelos = GestorArchivos.cargarVuelos();

        if (catalogoVuelos.isEmpty()) {
            System.out.println("⚠️ ALERTA: No hay vuelos en 'vuelos.txt'.");
            return;
        }

        // PASO 2: RECUPERAR MEMORIA (Anti-Amnesia)
        // Pedimos al Gestor las líneas del archivo 'reservas.csv'
        ArrayList<String> historial = GestorArchivos.leerHistorialReservas();
        
        System.out.println("Restaurando " + historial.size() + " tickets vendidos anteriormente...");

        for (String linea : historial) {
            procesarLineaHistorial(linea);
        }
        
        System.out.println("✅ Sistema sincronizado correctamente.");
    }

    // Método auxiliar para no ensuciar el código principal
    private void procesarLineaHistorial(String linea) {
        try {
            String[] datos = linea.split(",");
            // Formato esperado: COD_RES, VUELO_ID, PASAJERO, CEDULA, ASIENTO_ID, PRECIO
            
            if (datos.length < 5) return; 

            String vueloID = datos[1].trim();
            String asientoID = datos[4].trim();

                        if (asientoID.equalsIgnoreCase("REGAZO")) {
                return; 
            }

            // --- BÚSQUEDA Y OCUPACIÓN ---
            for (Vuelo v : catalogoVuelos) {
                if (v.getCodigo().equalsIgnoreCase(vueloID)) {
                    // Encontramos el vuelo, ahora buscamos el asiento
                    for (Asiento a : v.getAvion().getListaAsientos()) {
                        if (a.getCodigo().equalsIgnoreCase(asientoID)) {
                            a.ocuparAsiento(); // <--- AQUÍ RECUPERAMOS EL ESTADO
                            break;
                        }
                    }
                    break; 
                }
            }
        } catch (Exception e) {
            System.out.println("Error procesando línea histórica: " + linea);
        }
    }

    /**
     * MÉTODOS DE BÚSQUEDA 
     */
    public ArrayList<Vuelo> buscarVuelos(String origen, String destino) {
        ArrayList<Vuelo> resultados = new ArrayList<>();
        
        for (Vuelo v : catalogoVuelos) {
            // Buscamos ignorando mayúsculas/minúsculas
            boolean matchOrigen = v.getRuta().getOrigen().getNombre().equalsIgnoreCase(origen);
            boolean matchDestino = v.getRuta().getDestino().getNombre().equalsIgnoreCase(destino);

            if (matchOrigen && matchDestino) {
                resultados.add(v);
            }
        }
        return resultados;
    }

    /**
     * MÉTODOS DE TRANSACCIÓN
     */
    public void finalizarReserva(Reserva nuevaReserva) {
        if (nuevaReserva.getListaDetalles().isEmpty()) {
            System.out.println("Error: Reserva vacía.");
            return;
        }
        
        // Guardamos en disco duro
        GestorArchivos.guardarReserva(nuevaReserva);
        System.out.println("Reserva finalizada y guardada con éxito.");
    }

    // Getter necesario para las pruebas
    public ArrayList<Vuelo> getCatalogoVuelos() {
        return catalogoVuelos;
    }
}
