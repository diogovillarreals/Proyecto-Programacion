
package vuelosfis.modelo;

import vuelosfis.interfaces.TipoPasajero;

public class Pasajero {
    private String nombre;
    private String cedula;
    private String correo;
    private TipoPasajero tipoPasajero;

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public TipoPasajero getTipoPasajero() {
        return tipoPasajero;
    }
    
    

    public Pasajero(String nombre, String cedula, String correo, TipoPasajero tipoPasajero) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.correo = correo;
        this.tipoPasajero = tipoPasajero;
    }
    
    public double obtenerDescuento() {
        return tipoPasajero.getDescuento();
    }
    
    @Override
    public String toString() {
        return nombre + " (" + tipoPasajero.getDescripcion() + ")";
    }
}
