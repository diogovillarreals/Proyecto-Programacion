
package vuelosfis.modelo;

import vuelosfis.interfaces.TipoPasajero;


public class Nino implements TipoPasajero{
    @Override
    public String getDescripcion() {
        return "Niño (2-12 años)";
    }
    
    @Override
    public double getDescuento() {
        return 0.25; // 25% de descuento
    }
}
