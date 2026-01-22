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
        // ==========================================
        // PARTE 1: PRUEBAS DE LÓGICA (FASE 5)
        // ==========================================
        System.out.println("=== INICIO DE PRUEBAS DE LÓGICA (FASE 5) ===");

        // 1. Infraestructura simulada
        Ciudad uio = new Ciudad("UIO", "Quito");
        Ciudad mad = new Ciudad("MAD", "Madrid");
        Ruta ruta = new Ruta(uio, mad, 660);
        
        // Creamos vuelo con precio base $100.00 para facilitar cálculos
        Vuelo vueloTest = new Vuelo("TEST-001", ruta, LocalDate.now(), LocalTime.of(10, 0), 100.00);

        // Referencias rápidas a asientos
        Asiento ventana1A = vueloTest.getAvion().getListaAsientos().get(0); // Costo $10 (Ventana)
        Asiento centro1B = vueloTest.getAvion().getListaAsientos().get(1);  // Costo $0  (Centro)
        Asiento ventana2A = vueloTest.getAvion().getListaAsientos().get(6); // Costo $10 (Ventana)
        
        // --- PRUEBA 1 ---
        System.out.println("\n--- PRUEBA 1: Asignación Automática (Gratis) ---");
        Pasajero p1 = new Pasajero("Lucky", "101", "lucky@mail.com", new Adulto());
        DetalleReserva t1 = new DetalleReserva(vueloTest, p1, ventana1A, new Economy(), false);

        System.out.println("Asiento asignado: " + t1.getAsiento().getCodigo());
        System.out.println("¿Está ocupado el 1A?: " + !ventana1A.isDisponible()); 
        System.out.println("Ticket: " + t1.toString());

        // --- PRUEBA 2 ---
        System.out.println("\n--- PRUEBA 2: Selección Manual (Con Costo) ---");
        Pasajero p2 = new Pasajero("Ricky", "102", "ricky@mail.com", new Adulto());
        DetalleReserva t2 = new DetalleReserva(vueloTest, p2, ventana2A, new Economy(), true);

        System.out.println("Ticket: " + t2.toString());

        // --- PRUEBA 3 ---
        System.out.println("\n--- PRUEBA 3: Cambio de Asiento (Trueque) ---");
        Pasajero p3 = new Pasajero("Indeciso", "103", "indeciso@mail.com", new Adulto());

        DetalleReserva t3 = new DetalleReserva(vueloTest, p3, centro1B, new Economy(), false);
        System.out.println("Inicio: " + t3.toString()); 
        System.out.println("Estado 1B (Viejo) antes: " + (centro1B.isDisponible() ? "LIBRE" : "OCUPADO"));

        Asiento ventana1F = vueloTest.getAvion().getListaAsientos().get(5);

        System.out.println("... Cambiando de asiento a 1F ...");
        t3.cambiarAsiento(ventana1F);

        System.out.println("Final: " + t3.toString()); 
        System.out.println("Estado 1B (Viejo) despues: " + (centro1B.isDisponible() ? "LIBRE" : "OCUPADO"));
        System.out.println("Estado 1F (Nuevo) despues: " + (ventana1F.isDisponible() ? "LIBRE" : "OCUPADO"));
        
        // --- PRUEBA 4 ---
        System.out.println("\n--- PRUEBA 4: Bebé en Regazo (Realismo Puro) ---");
        Pasajero pBebe = new Pasajero("Bebé Lucas", "505", "n/a", new Bebe());
        DetalleReserva tBebe = new DetalleReserva(vueloTest, pBebe, null, new Economy(), false);

        System.out.println("Ticket Bebé: " + tBebe.toString());
        

        // ==========================================
        // PARTE 2: PRUEBAS DE PERSISTENCIA (FASE 6)
        // ==========================================
        System.out.println("\n=== INICIO DE PRUEBAS DE ARCHIVOS (FASE 6) ===");
        
        // --- PRUEBA DE LECTURA ---
        System.out.println("\n--- A. CARGANDO VUELOS DESDE 'vuelos.txt' ---");
        ArrayList<Vuelo> vuelosCargados = GestorArchivos.cargarVuelos();

        if (vuelosCargados.isEmpty()) {
            System.out.println("ERROR: No se encontraron vuelos. Revisa el archivo vuelos.txt");
            return;
        }

        for (Vuelo v : vuelosCargados) {
            System.out.println("-> Cargado: " + v.toString());
        }
        
        // --- PRUEBA DE ESCRITURA ---
        System.out.println("\n--- B. GUARDANDO RESERVA EN 'reservas.csv' ---");
        
        // 1. Elegimos uno de los vuelos REALES que acabamos de cargar
        Vuelo vueloReal = vuelosCargados.get(0); 
        System.out.println("Usaremos el vuelo: " + vueloReal.getCodigo());

        // 2. Creamos una Reserva
        Reserva reservaFinal = new Reserva("RES-FINAL-01", TipoViaje.SOLO_IDA);
        
        // 3. Agregamos un pasajero a ese vuelo real
        Pasajero pasajeroFinal = new Pasajero("Cliente Archivo", "17999", "cli@archivo.com", new Adulto());
        Asiento asientoReal = vueloReal.getAvion().getListaAsientos().get(2); // Asiento 1C
        
        DetalleReserva ticketFinal = new DetalleReserva(vueloReal, pasajeroFinal, asientoReal, new Economy(), true);
        
        reservaFinal.agregarDetalle(ticketFinal);
        
        // 4. Guardamos
        System.out.println("Guardando reserva de $" + reservaFinal.calcularTotalReserva() + "...");
        GestorArchivos.guardarReserva(reservaFinal);
        
        System.out.println("\n¡PRUEBAS COMPLETAS FINALIZADAS CON ÉXITO!");
    }
}