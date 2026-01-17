package vuelosfis.main;

import java.time.LocalDate;
import java.time.LocalTime;
import vuelosfis.modelo.Adulto;
import vuelosfis.modelo.Asiento;
import vuelosfis.modelo.Ciudad;
import vuelosfis.modelo.DetalleReserva;
import vuelosfis.modelo.Economy;
import vuelosfis.modelo.Pasajero;
import vuelosfis.modelo.Ruta;
import vuelosfis.modelo.Vuelo;

public class Main {

    public static void main(String[] args) {
        System.out.println("--- INICIO DE PRUEBAS DEL SISTEMA DE ASIENTOS ---");

// 1. Infraestructura
        Ciudad uio = new Ciudad("UIO", "Quito");
        Ciudad mad = new Ciudad("MAD", "Madrid");
        Ruta ruta = new Ruta(uio, mad, 660);
        Vuelo vueloTest = new Vuelo("TEST-001", ruta, LocalDate.now(), LocalTime.of(10, 0), 100.00);

// Referencias rápidas a asientos (Recordando: Columna 0 y 5 son Ventanas $10)
        Asiento ventana1A = vueloTest.getAvion().getListaAsientos().get(0); // Costo $10
        Asiento centro1B = vueloTest.getAvion().getListaAsientos().get(1); // Costo $0
        Asiento ventana2A = vueloTest.getAvion().getListaAsientos().get(6); // Costo $10
        
        System.out.println("\n--- PRUEBA 1: Asignación Automática (Gratis) ---");
Pasajero p1 = new Pasajero("Lucky", "101", "lucky@mail.com", new Adulto());

// false = El sistema lo eligió, el usuario NO (Debe salir GRATIS el asiento)
DetalleReserva t1 = new DetalleReserva(vueloTest, p1, ventana1A, new Economy(), false);

System.out.println("Asiento asignado: " + t1.getAsiento().getCodigo());
System.out.println("¿Está ocupado el 1A?: " + !ventana1A.isDisponible()); // Debe ser TRUE
System.out.println("Ticket: " + t1.toString());

System.out.println("\n--- PRUEBA 2: Selección Manual (Con Costo) ---");
Pasajero p2 = new Pasajero("Ricky", "102", "ricky@mail.com", new Adulto());

// true = El usuario dio clic, quiere pagar por ese lugar
DetalleReserva t2 = new DetalleReserva(vueloTest, p2, ventana2A, new Economy(), true);

System.out.println("Ticket: " + t2.toString());

// VALIDACIÓN:
// Precio Base ($100) + Asiento Manual ($10) = $110.00

System.out.println("\n--- PRUEBA 3: Cambio de Asiento (Trueque) ---");
Pasajero p3 = new Pasajero("Indeciso", "103", "indeciso@mail.com", new Adulto());

// 1. Inicia con asiento Centro 1B (Automático - Gratis)
DetalleReserva t3 = new DetalleReserva(vueloTest, p3, centro1B, new Economy(), false);
System.out.println("Inicio: " + t3.toString()); 
// Debe costar $100

// Verificamos estado antes del cambio
System.out.println("Estado 1B (Viejo) antes: " + (centro1B.isDisponible() ? "LIBRE" : "OCUPADO"));

// 2. EL CAMBIO -> Se mueve a la Ventana 2A (que usó Ricky en la prueba 2... ups, está ocupada)
// Usaremos mejor la Ventana 1F (Índice 5) que está vacía
Asiento ventana1F = vueloTest.getAvion().getListaAsientos().get(5);

System.out.println("... Cambiando de asiento a 1F ...");
t3.cambiarAsiento(ventana1F);

// 3. Verificamos resultados
System.out.println("Final: " + t3.toString()); // Debe subir a $110
System.out.println("Estado 1B (Viejo) despues: " + (centro1B.isDisponible() ? "LIBRE" : "OCUPADO"));
System.out.println("Estado 1F (Nuevo) despues: " + (ventana1F.isDisponible() ? "LIBRE" : "OCUPADO"));
    }

}
