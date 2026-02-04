package vuelosfis.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Locale; 
import java.util.Random;

public class GeneradorVuelos {

    private static final String[] CIUDADES = {"UIO", "GYE", "CUE", "MEC", "GPS"};
    private static final String[] AVIONES = {"Boeing 737", "Airbus A320", "Embraer 190"};

    public static void main(String[] args) {
        generarVuelosMasivos();
    }

    public static void generarVuelosMasivos() {
        String archivo = "vuelos.txt";
        int contadorVuelos = 0;
        Random random = new Random();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            
            LocalDate fechaInicio = LocalDate.now(); 
            int diasGenerar = 30; 

            System.out.println("REPARANDO VUELOS.TXT...");
            System.out.println("Orden obligatorio: COD,ORG,DEST,DURACION,FECHA,HORA,PRECIO,MODELO,ASIENTOS");

            for (int i = 0; i < diasGenerar; i++) {
                LocalDate fechaActual = fechaInicio.plusDays(i);

                for (String origen : CIUDADES) {
                    for (String destino : CIUDADES) {
                        if (origen.equals(destino)) continue;

                        LocalTime[] horariosBase = {
                            LocalTime.of(6, 30), LocalTime.of(9, 15), LocalTime.of(13, 0), 
                            LocalTime.of(17, 45), LocalTime.of(21, 0)
                        };

                        for (LocalTime hora : horariosBase) {
                            String codigo = "AV" + (1000 + random.nextInt(9000));
                            String modeloAvion = AVIONES[random.nextInt(AVIONES.length)];
                            int duracion = destino.equals("GPS") || origen.equals("GPS") ? 120 : 45 + random.nextInt(15);
                            double precioBase = destino.equals("GPS") || origen.equals("GPS") ? 150.0 : 60.0;
                            double precioFinal = precioBase + random.nextInt(40); 

                            // --- EL ORDEN CORREGIDO ---
                            // [0] Codigo
                            // [1] Origen
                            // [2] Destino
                            // [3] DURACIÓN (Entero)
                            // [4] FECHA (YYYY-MM-DD)
                            // [5] HORA (HH:MM)
                            // [6] PRECIO (Double con punto)
                            // [7] Modelo
                            // [8] Asientos
                            
                            String linea = String.format(Locale.US, "%s,%s,%s,%d,%s,%s,%.2f,%s,%d",
                                    codigo,         // 0
                                    origen,         // 1
                                    destino,        // 2
                                    duracion,       // 3
                                    fechaActual,    // 4 
                                    hora,           // 5
                                    precioFinal,    // 6
                                    modeloAvion,    // 7
                                    150             // 8
                            );

                            writer.write(linea);
                            writer.newLine();
                            contadorVuelos++;
                        }
                    }
                }
            }
            System.out.println("Ejecuta ahora el Main.");

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}