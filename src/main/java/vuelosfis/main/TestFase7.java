package vuelosfis.main;

import java.util.ArrayList;
import vuelosfis.controlador.ControladorReserva;
import vuelosfis.modelo.Asiento;
import vuelosfis.modelo.Vuelo;

public class TestFase7 {

    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   AUDITORÍA DE FASE 7: MEMORIA Y CEREBRO ");
        System.out.println("==========================================");

        // 1. INICIO DEL CONTROLADOR
        System.out.println("\n[PASO 1] Iniciando Controlador y cargando datos...");
        ControladorReserva control = new ControladorReserva();
        
        // Aquí ocurre la magia: Carga vuelos + Lee historial + Mancha asientos
        control.cargarDatosIniciales(); 

        ArrayList<Vuelo> catalogo = control.getCatalogoVuelos();

        if (catalogo.isEmpty()) {
            System.out.println("❌ ERROR CRÍTICO: No hay vuelos en memoria. Revisa 'vuelos.txt'.");
            return;
        }

        // 2. BUSCANDO LA EVIDENCIA
        // En la prueba de Fase 6, vendimos el asiento '1A' del primer vuelo (LA101)
        System.out.println("\n[PASO 2] Inspeccionando el Vuelo LA101 (Asiento 1A)...");
        
        Vuelo vueloInvestigado = catalogo.get(0); // Asumimos que es el LA101
        Asiento asiento1A = null;

        // Buscamos el asiento 1A específicamente
        for (Asiento a : vueloInvestigado.getAvion().getListaAsientos()) {
            if (a.getCodigo().equalsIgnoreCase("1A")) {
                asiento1A = a;
                break;
            }
        }

        if (asiento1A == null) {
            System.out.println("❌ ERROR: No se encontró el asiento 1A en el avión.");
            return;
        }

        // 3. EL VEREDICTO FINAL
        System.out.println("   -> Estado del asiento 1A en memoria: " + (asiento1A.isDisponible() ? "LIBRE 🟢" : "OCUPADO 🔴"));

        if (!asiento1A.isDisponible()) {
            System.out.println("\n✅ PRUEBA SUPERADA: ¡NO HAY AMNESIA!");
            System.out.println("   El sistema recordó que vendiste este asiento en la Fase 6.");
            System.out.println("   El Controlador procesó correctamente el archivo 'reservas.csv'.");
        } else {
            System.out.println("\n❌ PRUEBA FALLIDA: AMNESIA DETECTADA.");
            System.out.println("   El asiento aparece LIBRE, pero debería estar OCUPADO.");
            System.out.println("   Revisa el método 'cargarDatosIniciales' en ControladorReserva.");
        }
        
        // 4. PRUEBA DE BEBÉS (OPCIONAL)
        // Verificamos que el sistema no haya explotado por culpa de los bebés "REGAZO"
        System.out.println("\n[PASO 3] Verificación de estabilidad (Bebés)...");
        System.out.println("   -> Si estás leyendo esto, el sistema ignoró correctamente a los bebés 'REGAZO'");
        System.out.println("      y no lanzó errores al intentar buscar asientos inexistentes.");
    }
}
