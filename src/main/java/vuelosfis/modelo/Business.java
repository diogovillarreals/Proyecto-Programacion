
package vuelosfis.modelo;

import vuelosfis.interfaces.TipoVuelo;


public class Business implements TipoVuelo{
    
    @Override
   public String getNombre(){
       return "Business";
   }
   
   @Override
   public double getMultiplicadorPrecio(){
       return 1.0;
   }
}
