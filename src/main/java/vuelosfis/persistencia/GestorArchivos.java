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
    public static ArrayList<Vuelo> cargarVuelos() {
        ArrayList<Vuelo> listaVuelos = new ArrayList<>();

        // Usamos "try-with-resources" (el paréntesis después del try).
        // Esto cierra el archivo automáticamente aunque haya error. ¡Es muy seguro!
        try (BufferedReader br = new BufferedReader(new FileReader(RUTA_VUELOS))) {
            
            String linea;
            while ((linea = br.readLine()) != null) {
                // Si la línea está vacía o es un comentario, la saltamos
                if (linea.trim().isEmpty() || linea.startsWith("#")) {
                    continue; 
                }

                // 1. SEPARAR LOS DATOS (Split)
                // Formato esperado: Codigo,Origen,Destino,Duracion,Fecha,Hora,Precio,Modelo
                String[] datos = linea.split(",");

                // Validación básica: Si no tiene 8 campos, la línea está rota
                if (datos.length < 8) {
                    System.out.println("Línea inválida (faltan datos): " + linea);
                    continue;
                }

                // 2. EXTRAER Y CONVERTIR (Parsing)
                // Convertimos de String a lo que necesitemos (int, double, fechas)
                String codigo = datos[0].trim();
                String codOrigen = datos[1].trim();
                String codDestino = datos[2].trim();
                int duracion = Integer.parseInt(datos[3].trim());
                LocalDate fecha = LocalDate.parse(datos[4].trim());
                LocalTime hora = LocalTime.parse(datos[5].trim());
                double precio = Double.parseDouble(datos[6].trim());
                // El modelo del avión (datos[7]) lo usa el constructor de Vuelo internamente
                
                // 3. RECONSTRUIR OBJETOS
                // Como no guardamos los nombres completos de ciudades, ponemos el código como nombre temporal
                Ciudad origen = new Ciudad(codOrigen, codOrigen); 
                Ciudad destino = new Ciudad(codDestino, codDestino);
                
                Ruta ruta = new Ruta(origen, destino, duracion);
                
                // 4. CREAR EL VUELO
                Vuelo nuevoVuelo = new Vuelo(codigo, ruta, fecha, hora, precio);
                
                // 5. AGREGAR A LA LISTA
                listaVuelos.add(nuevoVuelo);
            }
            
            System.out.println("Se cargaron " + listaVuelos.size() + " vuelos correctamente desde el archivo.");

        } catch (IOException e) {
            System.err.println("Error al leer el archivo de vuelos: " + e.getMessage());
        } catch (NumberFormatException | java.time.format.DateTimeParseException e) {
            System.err.println("Error del formato en el archivo (revisa fechas o números): " + e.getMessage());
        }

        return listaVuelos;
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
