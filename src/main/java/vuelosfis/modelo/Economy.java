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
public class Economy implements TipoVuelo {
    
    @Override
    public String getNombre() {
        return "Economy";
    }
    
    @Override
    public double getMultiplicadorPrecio(){
        return 1.0;
    }
}
