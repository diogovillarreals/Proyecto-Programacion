/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

import vuelosfis.interfaces.TipoVuelo;

/**
 *
 * @author Diogo
 */
public class Premium implements TipoVuelo{
    
    @Override 
    public String getNombre(){
        return "Premium";
    }
    
    @Override
    public double getMultiplicadorPrecio(){
        return 1.0;
    }
}
