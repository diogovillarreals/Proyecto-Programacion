
package vuelosfis.interfaces;

public interface TipoVuelo {
   // Devuelve el nombre de la categoría (ej: "Economy")
    String getNombre();
   // Devuelve por cuánto se multiplica el precio base
    double getMultiplicadorPrecio();
}
