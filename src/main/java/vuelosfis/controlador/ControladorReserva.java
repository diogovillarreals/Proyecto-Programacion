package vuelosfis.controlador;

import java.util.ArrayList;
import vuelosfis.modelo.Asiento;
import vuelosfis.modelo.Vuelo;
import vuelosfis.modelo.DetalleReserva; // Importamos DetalleReserva
import vuelosfis.persistencia.GestorArchivos;

public class ControladorReserva {
    
    private ArrayList<Vuelo> catalogoVuelos;
    private ArrayList<String> historialReservas; // Aquí vive la memoria de lo vendido
    
    public ControladorReserva() {
        this.catalogoVuelos = new ArrayList<>();
        this.historialReservas = new ArrayList<>();
    }

    /**
     * MÉTODOS DE INICIO 
     */
    public void cargarDatosIniciales() {
        System.out.println("🔄 Cargando sistema...");

        // PASO 1: Cargar los aviones
        this.catalogoVuelos = GestorArchivos.cargarVuelos();

        if (catalogoVuelos.isEmpty()) {
            System.out.println("⚠️ ALERTA: No hay vuelos en 'vuelos.txt'.");
            return;
        }

        // PASO 2: RECUPERAR MEMORIA
        actualizarMemoriaDesdeArchivo(); // <--- Usamos un método centralizado
        
        System.out.println("✅ Sistema sincronizado. " + historialReservas.size() + " reservas activas.");
    }
    
    // !!! NUEVO MÉTODO AUXILIAR PARA NO REPETIR CÓDIGO
    public void actualizarMemoriaDesdeArchivo() {
        // Leemos el archivo físico y llenamos la RAM
        this.historialReservas = GestorArchivos.leerHistorialReservas();
    }

    /**
     * MÉTODOS DE BÚSQUEDA 
     */
    public ArrayList<Vuelo> buscarVuelos(String origen, String destino, java.time.LocalDate fechaBuscada) {
        ArrayList<Vuelo> resultados = new ArrayList<>();
        
        for (Vuelo v : catalogoVuelos) {
            boolean matchOrigen = v.getRuta().getOrigen().getNombre().equalsIgnoreCase(origen);
            boolean matchDestino = v.getRuta().getDestino().getNombre().equalsIgnoreCase(destino);
            boolean matchFecha = v.getFecha().isEqual(fechaBuscada);

            if (matchOrigen && matchDestino && matchFecha) {
                resultados.add(v);
            }
        }
        return resultados;
    }

    /**
     * MÉTODOS DE TRANSACCIÓN
     */
    public void finalizarReserva(vuelosfis.modelo.Reserva nuevaReserva) {
        if (nuevaReserva == null || nuevaReserva.getListaDetalles().isEmpty()) {
            System.out.println("❌ Error: Intentando guardar reserva vacía.");
            return;
        }

        // 1. Guardar en Archivo Físico (PERSISTENCIA)
        GestorArchivos.guardarReserva(nuevaReserva);

        // 2. !!! CORRECCIÓN CRÍTICA: ACTUALIZAR MEMORIA RAM !!!
        // Antes faltaba esto. Ahora obligamos a releer el archivo inmediatamente.
        // Así, 'verificarAsientoOcupado' sabrá que el asiento ya se vendió.
        actualizarMemoriaDesdeArchivo(); 

        // 3. (Opcional) Bloqueo visual inmediato de objetos si usas el mismo puntero
        for(DetalleReserva det : nuevaReserva.getListaDetalles()) {
             if(det.getAsiento() != null) {
                 // Esto es solo visual por si la ventana no se cierra
                 // Pero lo importante es el paso 2
             }
        }
        
        System.out.println("💾 Reserva guardada y memoria actualizada. Total reservas: " + historialReservas.size());
    }

    public ArrayList<Vuelo> getCatalogoVuelos() {
        return catalogoVuelos;
    }
    
    // --- VERIFICACIÓN DE ASIENTOS ---
    public boolean verificarAsientoOcupado(String codigoVuelo, String numeroAsiento) {
        
        // Protección contra nulos
        if (this.historialReservas == null || this.historialReservas.isEmpty()) {
            return false; 
        }

        // Recorremos las líneas de texto de la memoria (que ahora está sincronizada)
        for (String linea : this.historialReservas) {
            try {
                String[] datos = linea.split(",");
                // Formato esperado: RES-ID, VUELO, CLIENTE, CEDULA, ASIENTO, PRECIO
                
                if (datos.length >= 5) {
                    String vueloEnArchivo = datos[1].trim();   // Columna 1: ID Vuelo
                    String asientoEnArchivo = datos[4].trim(); // Columna 4: Asiento (ej: 6A)

                    // Comparamos ignorando mayúsculas/minúsculas y espacios
                    if (vueloEnArchivo.equalsIgnoreCase(codigoVuelo.trim()) && 
                        asientoEnArchivo.equalsIgnoreCase(numeroAsiento.trim())) {
                        
                        // DEBUG: Para saber si encontró algo
                        // System.out.println("🔒 Asiento ocupado encontrado: " + numeroAsiento + " en vuelo " + codigoVuelo);
                        return true;
                    }
                }
            } catch (Exception e) {
                continue; // Ignorar líneas corruptas
            }
        }
        return false; 
    }
}