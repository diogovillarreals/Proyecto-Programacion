
package vuelosfis.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Vuelo {
   private String codigo;
   private Ruta ruta;
   private LocalDate fecha;
   private LocalTime hora;
   private double precioBase;
   private Avion avion;

    public Vuelo(String codigo, Ruta ruta, LocalDate fecha, LocalTime hora, 
           double precioBase) {
        this.codigo = codigo;
        this.ruta = ruta;
        this.fecha = fecha;
        this.hora = hora;
        this.precioBase = precioBase;
        this.avion = new Avion("Airbus A320");      
    }

    public String getCodigo() {
        return codigo;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public Avion getAvion() {
        return avion;
    }

    @Override
    public String toString() {
       return codigo + ": " + ruta.toString() + " (" + fecha + " " + hora + ")";
    }
      
}
