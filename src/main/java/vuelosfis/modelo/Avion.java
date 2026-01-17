package vuelosfis.modelo;

import java.util.ArrayList;

public class Avion {

    public static final int FILAS = 7;
    public static final int COLUMNAS = 6;

    private String modelo;
    private ArrayList<Asiento> listaAsientos;

    public Avion(String modelo) {
        this.modelo = modelo;
        this.listaAsientos = new ArrayList<>();
        generarAsientos();
    }
    
    public ArrayList<Asiento> getListaAsientos() {
        return listaAsientos;
    }

    public String getModelo() {
        return modelo;
    }
    
    public int getFilas() { 
        return FILAS; 
    }
    public int getColumnas() { 
        return COLUMNAS; 
    }

    private void generarAsientos() {
        String letras = "ABCDEF";

        for (int i = 1; i<=FILAS; i++){
            for (int j = 0; j<COLUMNAS; j++){
                String letraColumna = String.valueOf(letras.charAt(j));
                String codigoAsiento = i + letraColumna;
                double precioExtra = 0;
                
                if (j==0 || j==COLUMNAS-1){
                    precioExtra = 10.0;
                }
                
                Asiento nuevoAsiento = new Asiento(codigoAsiento, i, letraColumna,
                precioExtra);
                listaAsientos.add(nuevoAsiento);
            }
        }
    }
   
}
