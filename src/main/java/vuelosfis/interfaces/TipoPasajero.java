
package vuelosfis.interfaces;

public interface TipoPasajero {
    // Devuelve el tipo de pasajero (ej: "Adulto", "Niño")
    String getDescripcion();
    
    // Devuelve el porcentaje de descuento (0.0 a 1.0)
    double getDescuento();
}
