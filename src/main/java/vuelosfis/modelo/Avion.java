package vuelosfis.modelo;

import java.util.ArrayList;

public class Avion {

    public static final int FILAS = 10;
    public static final int COLUMNAS = 4;

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
        String letras = "ABCD"; 

        for (int f = 1; f <= FILAS; f++) {
            for (int i = 0; i < COLUMNAS; i++) {
                
                // A. Preparar datos individuales
                char letraChar = letras.charAt(i);
                String columnaStr = String.valueOf(letraChar); // "A", "B"...
                String codigo = f + columnaStr;                // "1A", "1B"...

                // B. Definir precio según la fila (para que coincida con tu vista)
                double precio = 0.0;
                if (f <= 3) {
                    precio = 50.0; // Business
                } else if (f <= 5) {
                    precio = 25.0; // Premium
                } else {
                    precio = 10.0; // Economy
                }

                // C. INSTANCIAR EL ASIENTO
                Asiento asiento = new Asiento(codigo, f, columnaStr, precio);
                
                listaAsientos.add(asiento);
            }
        }
    }
}

