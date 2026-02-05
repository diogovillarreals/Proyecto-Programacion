/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vuelosfis.modelo;

import vuelosfis.interfaces.TipoPasajero;

/**
 *
 * @author Diogo
 */
public class Bebe implements TipoPasajero{
    @Override 
    public String getDescripcion(){
        return "Infante/Bebe Menor 2 años)" ;
    }
    
    @Override
    public double getDescuento(){
        return 0.90;
    }
}
