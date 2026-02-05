
package vuelosfis.modelo;

import vuelosfis.interfaces.TipoPasajero;


public class AdultoMayor implements TipoPasajero{
    @Override
    public String getDescripcion(){
        return "Tercera Edad";
    }
    
    @Override
    public double getDescuento() {
        return 0.30;
    }
}
