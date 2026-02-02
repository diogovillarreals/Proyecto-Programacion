package vuelosfis.persistencia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import vuelosfis.modelo.Ciudad;
import vuelosfis.modelo.Ruta;
import vuelosfis.modelo.Vuelo;

public class GestorArchivos {

    // Ruta del archivo (al estar en la raíz del proyecto, solo ponemos el nombre)
    private static final String RUTA_VUELOS = "vuelos.txt";

    /**
     * Lee el archivo de texto y devuelve una lista de objetos Vuelo.
     * @return ArrayList de Vuelos cargados desde el disco.
     */
    public static java.util.ArrayList<vuelosfis.modelo.Vuelo> cargarVuelos() {
        java.util.ArrayList<vuelosfis.modelo.Vuelo> lista = new java.util.ArrayList<>();
        java.io.File archivo = new java.io.File("vuelos.txt");

        if (!archivo.exists()) {
            System.out.println("⚠️ ALERTA: No existe vuelos.txt. Ejecuta GeneradorVuelos.");
            return lista;
        }

        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(archivo))) {
            String linea;
            int numeroLinea = 0;
            
            while ((linea = br.readLine()) != null) {
                numeroLinea++;
                if (linea.trim().isEmpty()) continue;

                String[] datos = linea.split(",");
                
                // Si la línea no tiene 8 datos, la saltamos para evitar errores
                if (datos.length < 8) {
                    System.out.println("⚠️ Saltando línea " + numeroLinea + " (Datos incompletos): " + linea);
                    continue; 
                }

                try {
                    // --- ZONA DE PELIGRO (Aquí es donde daba el error) ---
                    // El error "AirbusA320" pasaba porque se leía el índice incorrecto.
                    // Aseguramos leer los índices exactos del Generador:
                    
                    // Indice 3 = Duración (Número)
                    int duracion = Integer.parseInt(datos[3].trim()); 
                    
                    // Indice 6 = Precio (Número con decimales)
                    // (Si tu código viejo leía datos[7] aquí, explotaba)
                    double precio = Double.parseDouble(datos[6].trim()); 

                    // --- FIN ZONA DE PELIGRO ---

                    // Leemos el resto
                    String codigo = datos[0].trim();
                    String codOrigen = datos[1].trim();
                    String codDestino = datos[2].trim();
                    java.time.LocalDate fecha = java.time.LocalDate.parse(datos[4].trim());
                    java.time.LocalTime hora = java.time.LocalTime.parse(datos[5].trim());
                    
                    // Reconstruimos
                    vuelosfis.modelo.Ciudad orig = new vuelosfis.modelo.Ciudad(codOrigen, codOrigen);
                    vuelosfis.modelo.Ciudad dest = new vuelosfis.modelo.Ciudad(codDestino, codDestino);
                    vuelosfis.modelo.Ruta ruta = new vuelosfis.modelo.Ruta(orig, dest, duracion);
                    
                    lista.add(new vuelosfis.modelo.Vuelo(codigo, ruta, fecha, hora, precio));

                } catch (NumberFormatException e) {
                    System.out.println("❌ ERROR MATEMÁTICO en línea " + numeroLinea + ": " + linea);
                    System.out.println("   -> Intentó convertir texto a número y falló: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    // Ruta para el archivo de reservas (se creará solo si no existe)
    private static final String RUTA_RESERVAS = "reservas.csv";

    /**
     * Guarda una reserva completa en el archivo CSV.
     * @param reserva El objeto Reserva con todos sus detalles.
     */
    public static void guardarReserva(vuelosfis.modelo.Reserva reserva) {
        // Estructura del CSV: CodigoReserva,Vuelo,Pasajero,Cedula,Asiento,TotalPagado
        
        // El 'true' en FileWriter es VITAL: significa "Append" (Agregar al final).
        // Si pones false, borrará todo el archivo cada vez que guardes algo nuevo.
        try (java.io.FileWriter fw = new java.io.FileWriter(RUTA_RESERVAS, true);
             java.io.BufferedWriter bw = new java.io.BufferedWriter(fw);
             java.io.PrintWriter out = new java.io.PrintWriter(bw)) {

            for (vuelosfis.modelo.DetalleReserva detalle : reserva.getListaDetalles()) {
                StringBuilder linea = new StringBuilder();
                
                // 1. Código de Reserva
                linea.append(reserva.getCodigoReserva()).append(",");
                
                // 2. Código de Vuelo
                linea.append(detalle.getVuelo().getCodigo()).append(",");
                
                // 3. Datos Pasajero    
                linea.append(detalle.getPasajero().getNombre()).append(",");
                linea.append(detalle.getPasajero().getCedula()).append(",");
                
                // 4. Asiento (Validamos si es null/bebé)
                String asientoInfo = (detalle.getAsiento() != null) ? detalle.getAsiento().getCodigo() : "REGAZO";
                linea.append(asientoInfo).append(",");
                
                // 5. Precio individual
                linea.append(detalle.calcularSubtotal());

                // Escribir la línea en el archivo
                out.println(linea.toString());
            }
            
            System.out.println("Reserva " + reserva.getCodigoReserva() + " guardada en " + RUTA_RESERVAS);

        } catch (IOException e) {
            System.err.println("Error al guardar la reserva: " + e.getMessage());
        }
    }
    public static ArrayList<String> leerHistorialReservas() {
        ArrayList<String> lineas = new ArrayList<>();
        java.io.File archivo = new java.io.File(RUTA_RESERVAS); // Asegúrate que esta constante exista

        if (!archivo.exists()) return lineas;

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                lineas.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Error leyendo historial: " + e.getMessage());
        }
        return lineas;
    }
    }
