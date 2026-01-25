package vuelosfis.main;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// Imports del Modelo
import vuelosfis.modelo.Adulto;
import vuelosfis.modelo.Bebe;
import vuelosfis.modelo.Asiento;
import vuelosfis.modelo.Ciudad;
import vuelosfis.modelo.DetalleReserva;
import vuelosfis.modelo.Economy;
import vuelosfis.modelo.Pasajero;
import vuelosfis.modelo.Ruta;
import vuelosfis.modelo.Vuelo;
import vuelosfis.modelo.Reserva; // <--- Nuevo
import vuelosfis.enums.TipoViaje; // <--- Nuevo

// Import de Persistencia
import vuelosfis.persistencia.GestorArchivos;

public class Pruebas {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   AUDITORÍA DE FASE 6: PERSISTENCIA      ");
        System.out.println("==========================================");

        // -----------------------------------------------------------
        // PRUEBA 1: Lectura de Itinerarios (vuelos.txt)
        // -----------------------------------------------------------
        System.out.println("\n[PRUEBA 1] Intentando leer 'vuelos.txt'...");
        
        ArrayList<Vuelo> vuelosCargados = GestorArchivos.cargarVuelos();

        if (vuelosCargados.isEmpty()) {
            System.out.println("❌ FALLO: La lista está vacía.");
            System.out.println("   -> Verifica que exista el archivo 'vuelos.txt' en la raíz.");
            System.out.println("   -> Verifica que el archivo tenga datos (formato: COD,ORIGEN,DESTINO...).");
        } else {
            System.out.println("✅ ÉXITO: Se cargaron " + vuelosCargados.size() + " vuelos.");
            System.out.println("   -> Primer vuelo detectado: " + vuelosCargados.get(0).getCodigo());
            System.out.println("   -> Ruta: " + vuelosCargados.get(0).getRuta().getOrigen().getNombre() 
                                     + " - " + vuelosCargados.get(0).getRuta().getDestino().getNombre());
        }

        // Si falló la carga, no podemos seguir con las pruebas de escritura usando datos reales
        if (vuelosCargados.isEmpty()) return;


        // -----------------------------------------------------------
        // PRUEBA 2: Escritura en Disco (reservas.csv)
        // -----------------------------------------------------------
        System.out.println("\n[PRUEBA 2] Intentando escribir una reserva nueva...");

        // A. Preparamos datos falsos pero coherentes
        Vuelo vueloReal = vuelosCargados.get(0); // Usamos el primer vuelo que encontramos
        Asiento asientoPrueba = vueloReal.getAvion().getListaAsientos().get(0); // El primer asiento (ej. 1A)
        Pasajero pasajeroTest = new Pasajero("Tester Fase6", "1700000000", "test@qa.com", new Adulto());
        
        // B. Creamos la reserva
        Reserva reservaTest = new Reserva("RES-TEST-" + System.currentTimeMillis(), vuelosfis.enums.TipoViaje.SOLO_IDA);
        DetalleReserva detalle = new DetalleReserva(vueloReal, pasajeroTest, asientoPrueba, new Economy(), true);
        reservaTest.agregarDetalle(detalle);

        // C. Guardamos (Esto debería crear/añadir línea en reservas.csv)
        try {
            GestorArchivos.guardarReserva(reservaTest);
            System.out.println("✅ ÉXITO: Método ejecutado sin errores (Excepciones).");
            System.out.println("   -> Se intentó guardar la reserva: " + reservaTest.getCodigoReserva());
        } catch (Exception e) {
            System.out.println("❌ FALLO: El método guardarReserva lanzó error: " + e.getMessage());
        }


        // -----------------------------------------------------------
        // PRUEBA 3: Lectura de Historial (Round-Trip)
        // -----------------------------------------------------------
        System.out.println("\n[PRUEBA 3] Verificando si el archivo guardó los datos (Lectura)...");
        
        // Aquí llamamos al método NUEVO que agregaste (leerHistorialReservas o similar)
        // Nota: Ajusta el nombre del método si le pusiste otro.
        ArrayList<String> historial = GestorArchivos.leerHistorialReservas();

        if (historial.isEmpty()) {
            System.out.println("❌ FALLO: El historial volvió vacío."); 
            System.out.println("   -> O no se guardó nada en la Prueba 2, o el lector no funciona.");
        } else {
            // Buscamos nuestra reserva de prueba en el archivo
            boolean encontrada = false;
            String ultimaLinea = "";
            
            for (String linea : historial) {
                if (linea.contains(reservaTest.getCodigoReserva())) {
                    encontrada = true;
                    ultimaLinea = linea;
                }
            }

            if (encontrada) {
                System.out.println("✅ ÉXITO TOTAL: La reserva fue encontrada en el disco duro.");
                System.out.println("   -> Datos leídos: " + ultimaLinea);
                System.out.println("   -> Conclusión: El GestorArchivos sabe LEER y ESCRIBIR.");
            } else {
                System.out.println("❌ FALLO DE INTEGRIDAD: Se leyeron líneas, pero no la que acabamos de guardar.");
            }
        }
    }
}