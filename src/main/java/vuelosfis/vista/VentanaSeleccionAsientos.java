/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vuelosfis.vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import vuelosfis.modelo.Adulto;
import vuelosfis.modelo.AdultoMayor;
import vuelosfis.modelo.Nino;
import vuelosfis.modelo.Bebe;
import vuelosfis.modelo.Reserva;
import vuelosfis.modelo.DetalleReserva;
import vuelosfis.modelo.Pasajero;
import vuelosfis.modelo.Asiento;
import vuelosfis.enums.TipoViaje;

public class VentanaSeleccionAsientos extends javax.swing.JFrame {
    
    private vuelosfis.controlador.ControladorVuelo controlador; 
    private vuelosfis.modelo.Vuelo vueloActual;

    // --- VARIABLES DE DATOS ---
    private boolean esViajeRedondo, esTramoIda;
    private String origen, destino, fechaVueltaGuardada, cabinaPermitida;
    private int cantAdultos, cantNinos, cantBebes, cantMayores;
    private int pasajerosTotal;
    //Cuántos asientos físicos necesitamos (Total - Bebés)
    private int asientosRequeridos;
    private int pasajeroActual = 1;
    private double precioBaseUnitario, costoAsientosTotal = 0.0;
    
    // Mochila
    private String infoIdaPrev, infoVueltaActual;
    private double precioIdaPrevTotal;
    
    private ArrayList<String> misAsientos = new ArrayList<>();

    public VentanaSeleccionAsientos() {
        initComponents();
        this.setSize(831, 483);
        this.setLocationRelativeTo(null);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
    }

    // --- CARGAR DATOS ---
    public void cargarDatos(vuelosfis.controlador.ControladorVuelo ctrl, 
                            vuelosfis.modelo.Vuelo vuelo, String origen, String destino, 
                            int a, int n, int b, int m, // <--- 4 ENTEROS
                            String cabina, boolean esRedondo, boolean esIda, String fechaVuelta,
                            double precioUnitarioActual, 
                            String infoIdaPrev, double precioIdaPrevTotal, String infoVueltaActual) {
        
        this.controlador = ctrl;
        this.vueloActual = vuelo;
        this.origen = origen;
        this.destino = destino;
        
        this.cantAdultos = a;
        this.cantNinos = n;
        this.cantBebes = b;
        this.cantMayores = m;
        this.pasajerosTotal = a + n + b + m;
        
        this.asientosRequeridos = pasajerosTotal - cantBebes;
        
        this.cabinaPermitida = cabina;
        this.esViajeRedondo = esRedondo;
        this.esTramoIda = esIda;
        this.fechaVueltaGuardada = fechaVuelta;
        this.precioBaseUnitario = precioUnitarioActual;
        this.infoIdaPrev = infoIdaPrev;
        this.precioIdaPrevTotal = precioIdaPrevTotal;
        this.infoVueltaActual = infoVueltaActual;
        
        // Actualizar Textos Visuales
        lblTitulo.setText("<html><center>Eligiendo asiento<br>" + pasajeroActual + " de " + asientosRequeridos + "</center></html>");
        actualizarPrecio();
        
        // Generar los botones DENTRO del panel en Design
        dibujarAsientosEnPanel();
    }

    private void actualizarPrecio() {
        // Obtenemos los descuentos desde las clases
        double descA = new Adulto().getDescuento();      // 0.0
        double descN = new Nino().getDescuento();        // 0.30
        double descM = new AdultoMayor().getDescuento(); // 0.30
        double descB = new Bebe().getDescuento();        // 0.90

        // Aplicamos la fórmula: Cantidad * Precio * (1 - Descuento)
        double totalA = cantAdultos * (precioBaseUnitario * (1 - descA));
        double totalN = cantNinos   * (precioBaseUnitario * (1 - descN));
        double totalM = cantMayores * (precioBaseUnitario * (1 - descM));
        double totalB = cantBebes   * (precioBaseUnitario * (1 - descB));
        
        double totalVueloActual = totalA + totalN + totalM + totalB;
        double granTotal = precioIdaPrevTotal + totalVueloActual + costoAsientosTotal;

        // Texto
        StringBuilder sb = new StringBuilder();
        if (esViajeRedondo && !esTramoIda) sb.append("IDA (Pagado): $ ").append(String.format("%.2f", precioIdaPrevTotal)).append("\n");
        sb.append("VUELO ACTUAL (Base $").append(String.format("%.2f", precioBaseUnitario)).append("):\n");
        
        if(cantAdultos>0) sb.append(" - ").append(cantAdultos).append(" Adulto(s): $").append(String.format("%.2f", totalA)).append("\n");
        if(cantNinos>0)   sb.append(" - ").append(cantNinos).append(" Niño(s) (-30%): $").append(String.format("%.2f", totalN)).append("\n");
        if(cantMayores>0) sb.append(" - ").append(cantMayores).append(" 3ra Edad (-30%): $").append(String.format("%.2f", totalM)).append("\n");
        if(cantBebes>0)   sb.append(" - ").append(cantBebes).append(" Bebé(s) (-90%): $").append(String.format("%.2f", totalB)).append("\n");
        
        sb.append("Asientos: $").append(String.format("%.2f", costoAsientosTotal));
        
        txtDetalle.setText(sb.toString()); 
        lblTotal.setText("Total: $" + String.format("%.2f", granTotal));
    }

  // --- DIBUJAR LOS BOTONES EN TU PANEL DE DISEÑO ---
    private void dibujarAsientosEnPanel() {
        pnlAsientos.removeAll(); 
        
        if (this.controlador != null) {
            this.controlador.getControladorReserva().cargarDatosIniciales();
        }
        
        if (this.controlador != null) {
            // Verificar si el historial está vacío. Si sí, obligamos a leer el archivo
            
            System.out.println(" Verificando memoria de reservas para el vuelo: " + this.vueloActual.getCodigo());
            
            // Forzamos la carga de datos del archivo 'reservas.csv' en este instante
            // Esto arregla el problema de que la Ida llegue vacía.
            this.controlador.getControladorReserva().cargarDatosIniciales(); 
        } else {
             System.out.println(" ERROR CRÍTICO: El controlador es NULL en Asientos.");
        }
        
        // Forzar cuadrícula de 5 columnas
        pnlAsientos.setLayout(new java.awt.GridLayout(0, 5, 10, 10)); 
        
        // Cabeceras (A B - C D)
        String[] headers = {"A", "B", "", "C", "D"};
        for(String h : headers) {
            JLabel lbl = new JLabel(h, SwingConstants.CENTER);
            pnlAsientos.add(lbl);
        }

        String[] letras = {"A", "B", "C", "D"};

        // --- BUCLE PARA CREAR LAS 10 FILAS ---
        for (int fila = 1; fila <= 10; fila++) {
            for (int col = 0; col < 5; col++) {
                
                // Si es la columna del medio (2), ponemos el número de fila (Pasillo)
                if (col == 2) {
                    JLabel lblFila = new JLabel(String.valueOf(fila), SwingConstants.CENTER);
                    lblFila.setForeground(Color.GRAY);
                    pnlAsientos.add(lblFila);
                    continue; 
                }
                
                // Calcular letra del asiento
                int index = (col > 2) ? col - 1 : col;
                String numeroAsiento = fila + letras[index];
                
                JButton btn = new JButton(numeroAsiento);
                btn.setPreferredSize(new java.awt.Dimension(50, 40)); 
                
                // --- 1. DETERMINAR CLASE DEL ASIENTO POR FILA ---
                String tipoAsiento;
                double precio;
                Color colorBtn;
                
                if (fila <= 3) { 
                    tipoAsiento = "Business"; precio = 50.00; colorBtn = new Color(255, 153, 153); // Rojo
                } else if (fila <= 5) {
                    tipoAsiento = "Premium"; precio = 25.00; colorBtn = new Color(153, 153, 255); // Azul
                } else {
                    tipoAsiento = "Economy"; precio = 10.00; colorBtn = new Color(153, 255, 153); // Verde
                }
                
                btn.setBackground(colorBtn);
                
                boolean estaOcupado = false;
                if (this.controlador != null) {

                     estaOcupado = this.controlador.getControladorReserva().verificarAsientoOcupado(
                            this.vueloActual.getCodigo(), 
                            numeroAsiento
                     );
                }

                if (estaOcupado) {
                    btn.setEnabled(false);
                    btn.setBackground(Color.DARK_GRAY); // Gris oscuro
                    btn.setText("X"); // Marcar con X
                }

// --- 2. LÓGICA DE BLOQUEO ---
                boolean permitido = false;
                String miCabina = (cabinaPermitida != null) ? cabinaPermitida.toUpperCase() : "ECONOMY";

                if (miCabina.contains("BUSINESS")) {
                    permitido = true; // Business puede elegir todo
                } else if (miCabina.contains("PREMIUM")) {
                    // Premium puede elegir Premium y Economy, pero NO Business
                    if (!tipoAsiento.equals("Business")) permitido = true;
                } else {
                    // Economy solo puede elegir Economy
                    if (tipoAsiento.equals("Economy")) permitido = true;
                }
                
                // --- 3. APLICAR EL ESTADO AL BOTÓN ---
                if (!permitido) {
                    btn.setEnabled(false); // Desactivar clic
                    btn.setBackground(Color.LIGHT_GRAY); // Poner gris
                    btn.setToolTipText("Tu boleto " + cabinaPermitida + " no permite este asiento.");
                } else {
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btn.addActionListener(e -> seleccionarAsiento(btn, numeroAsiento, precio));
                }
                
                pnlAsientos.add(btn);
            }
        }
        
        pnlAsientos.revalidate();
        pnlAsientos.repaint();
    }

    private void seleccionarAsiento(JButton btn, String numero, double precio) {
        
        // --- FRENO: SI YA ESTÁ LLENO, NO DEJA CLICKAR ---
        if (misAsientos.size() >= asientosRequeridos) { 
            JOptionPane.showMessageDialog(this, "Ya seleccionaste los asientos necesarios.\n(Los bebés van en regazo)."); 
            return;
        }
        // --------------------------------------------------

        int r = JOptionPane.showConfirmDialog(this, "¿Asignar " + numero + " (+$" + precio + ")?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (r == JOptionPane.YES_OPTION) {
            misAsientos.add(numero);
            costoAsientosTotal += precio;
            
            actualizarPrecio(); // Actualiza la lista
            
            // Marcar visualmente
            btn.setEnabled(false);
            btn.setBackground(Color.DARK_GRAY);
            btn.setForeground(Color.WHITE);
            
            pasajeroActual++;
            
            if (pasajeroActual > pasajerosTotal) {
                lblTitulo.setText("<html><font color='green'>¡COMPLETO!</font></html>");
                btnConfirmar.setEnabled(true); 
            } else {
                lblTitulo.setText("<html><center>Eligiendo asiento<br>" + 
                        pasajeroActual + " de " + asientosRequeridos + "</center></html>");
                
            }
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        lblTotal = new javax.swing.JLabel();
        btnConfirmar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDetalle = new javax.swing.JTextArea();
        scrollpane = new javax.swing.JScrollPane();
        pnlAsientos = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.white, java.awt.Color.white, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)), "Referencias", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(150, 0));

        jLabel1.setText("Business ($50)");

        jLabel2.setText("Premium ($25)");

        jLabel3.setText("Economy ($10)");

        jLabel4.setText("Ocupado");

        jPanel1.setBackground(new java.awt.Color(255, 51, 51));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(102, 153, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(0, 204, 0));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        jPanel5.setBackground(new java.awt.Color(204, 204, 204));
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 28, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(19, 19, 19)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(69, 69, 69)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(67, 67, 67)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(80, 80, 80)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_START);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED, null, null, new java.awt.Color(204, 204, 204), new java.awt.Color(204, 204, 204)), "Tu Selección", javax.swing.border.TitledBorder.LEADING, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jPanel6.setPreferredSize(new java.awt.Dimension(250, 0));

        lblTitulo.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(0, 51, 153));
        lblTitulo.setText("Pasajero 1");

        jLabel5.setText("Total acumulado:");

        lblTotal.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(0, 102, 0));
        lblTotal.setText("Total: $");

        btnConfirmar.setBackground(new java.awt.Color(0, 255, 0));
        btnConfirmar.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        btnConfirmar.setForeground(new java.awt.Color(255, 255, 255));
        btnConfirmar.setText("Confirmar asientos");
        btnConfirmar.addActionListener(this::btnConfirmarActionPerformed);

        txtDetalle.setColumns(20);
        txtDetalle.setRows(5);
        txtDetalle.setEnabled(false);
        txtDetalle.setPreferredSize(new java.awt.Dimension(220, 84));
        jScrollPane1.setViewportView(txtDetalle);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnConfirmar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(0, 132, Short.MAX_VALUE))
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblTitulo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnConfirmar, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(34, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel6, java.awt.BorderLayout.LINE_END);

        scrollpane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Selecciona en el mapa", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N

        pnlAsientos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.ABOVE_TOP, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        pnlAsientos.setLayout(new java.awt.GridLayout(0, 5, 5, 5));
        scrollpane.setViewportView(pnlAsientos);

        getContentPane().add(scrollpane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmarActionPerformed
        // 1. Validación
        if(misAsientos.size() < asientosRequeridos) { 
            JOptionPane.showMessageDialog(this, "Faltan elegir asientos."); 
            return; 
        }
        
        // 2. Cálculos finales
        double descA=new Adulto().getDescuento(), descN=new Nino().getDescuento(), descM=new AdultoMayor().getDescuento(), descB=new Bebe().getDescuento();
        double totalVuelo = (cantAdultos * precioBaseUnitario * (1-descA)) + (cantNinos * precioBaseUnitario * (1-descN)) + (cantMayores * precioBaseUnitario * (1-descM)) + (cantBebes * precioBaseUnitario * (1-descB));
        double totalEsteTramo = totalVuelo + costoAsientosTotal;
        
        // 3. Construcción de Información para Resumen
        
        // A. Parte del Vuelo actual y sus Asientos
        String infoVueloYAsientos = "Vuelo " + vueloActual.getCodigo() + " (" + origen + "-" + destino + ") Asientos: " + misAsientos;
        if (cantBebes > 0) infoVueloYAsientos += " + " + cantBebes + " Bebé(s) en regazo";

        // B. Parte de la Tarifa y Fecha (Recuperada de la ventana anterior)
        String baseInfo = (esTramoIda && esViajeRedondo) ? infoIdaPrev : infoVueltaActual;
        
        // C. Info Completa Unificada
        String infoEsteTramoCompleta = baseInfo + " | " + infoVueloYAsientos;

        // ====================================================================
        // LÓGICA DE NAVEGACIÓN
        // ====================================================================

        // --- CAMINO A: IR A LA VUELTA ---
        if (esViajeRedondo && esTramoIda) {
            this.setVisible(false);
            VentanaVuelosVuelta vVuelta = new VentanaVuelosVuelta();
            
            vVuelta.recibirDatos(
                controlador, destino, origen, fechaVueltaGuardada, 
                cantAdultos, cantNinos, cantBebes, cantMayores, 
                infoEsteTramoCompleta, // Pasamos la info de IDA completa
                totalEsteTramo, 
                cabinaPermitida
            );
            vVuelta.setVisible(true);
        } 
        
        // --- CAMINO B: FINALIZAR Y GUARDAR TODO ---
        else {
            this.setVisible(false);
            
            // 1. Crear Reserva
            TipoViaje tipo = esViajeRedondo ? TipoViaje.IDA_Y_VUELTA : TipoViaje.SOLO_IDA;
            Reserva reservaFinal = new Reserva("RES-" + System.currentTimeMillis(), tipo);
            Pasajero paxDummy = new Pasajero("Cliente", "999", "x", new Adulto());

            // 2. Agregar detalles de ESTE vuelo (Vuelta o Ida única)
            for (String asientoCod : misAsientos) {
                 Asiento aObj = new Asiento(asientoCod, 1, cabinaPermitida, precioBaseUnitario);
                 aObj.ocuparAsiento(); // Forzar ocupación en memoria
                 DetalleReserva det = new DetalleReserva(vueloActual, paxDummy, aObj, new vuelosfis.modelo.Economy(), true);
                 reservaFinal.agregarDetalle(det);
            }
            if (cantBebes > 0) {
                Pasajero paxBebe = new Pasajero("Bebé", "000", "x", new Bebe());
                for (int i = 0; i < cantBebes; i++) {
                    reservaFinal.agregarDetalle(new DetalleReserva(vueloActual, paxBebe, null, new vuelosfis.modelo.Economy(), false));
                }
            }

            // 3. Variables para el Resumen Visual
            String textoFinalIda = "";
            String textoFinalVuelta = "";
            double granTotal = totalEsteTramo;

            if (esViajeRedondo) {
                granTotal += precioIdaPrevTotal;
                textoFinalIda = infoIdaPrev; 
                textoFinalVuelta = infoEsteTramoCompleta;
                
                // --- 4. RECONSTRUCCIÓN CRÍTICA DE LA IDA PARA BASE DE DATOS ---
                try {
                    String codVueloIda = "";
                    ArrayList<String> asientosIda = new ArrayList<>();
                    
                    // a. Buscar Código de Vuelo en el texto de la Ida (busca la palabra "Vuelo")
                    int idxVuelo = infoIdaPrev.indexOf("Vuelo ");
                    if (idxVuelo != -1) {
                        // Busca el final del código (espacio o barra)
                        int idxFin = infoIdaPrev.indexOf("|", idxVuelo);
                        if (idxFin == -1) idxFin = infoIdaPrev.indexOf(")", idxVuelo); // Por si acaso
                        if (idxFin == -1) idxFin = infoIdaPrev.length();
                        
                        String sucio = infoIdaPrev.substring(idxVuelo + 6, idxFin).trim();
                        // Limpiar paréntesis ej: "AV123 (UIO-GYE)"
                        if (sucio.contains("(")) codVueloIda = sucio.substring(0, sucio.indexOf("(")).trim();
                        else codVueloIda = sucio;
                    }

                    // b. Buscar Asientos en el texto: "Asientos: [1A, 2B]"
                    int idxAsientos = infoIdaPrev.indexOf("Asientos: ");
                    if (idxAsientos != -1) {
                        int idxAbre = infoIdaPrev.indexOf("[", idxAsientos);
                        int idxCierra = infoIdaPrev.indexOf("]", idxAbre);
                        if (idxAbre != -1 && idxCierra != -1) {
                            String contenido = infoIdaPrev.substring(idxAbre + 1, idxCierra);
                            String[] parts = contenido.split(",");
                            for (String s : parts) if (!s.trim().isEmpty()) asientosIda.add(s.trim());
                        }
                    }

                    // c. Buscar Objeto Vuelo
                    vuelosfis.modelo.Vuelo vueloIda = null;
                    if(controlador != null && !codVueloIda.isEmpty()) {
                        for (vuelosfis.modelo.Vuelo v : controlador.getControladorReserva().getCatalogoVuelos()) {
                            if (v.getCodigo().equalsIgnoreCase(codVueloIda)) { vueloIda = v; break; }
                        }
                    }

                    // d. AGREGAR A LA RESERVA (ADULTOS + BEBÉS)
                    if (vueloIda != null && !asientosIda.isEmpty()) {
                        // Agregar Adultos/Niños
                        for (String cod : asientosIda) {
                            vuelosfis.modelo.Asiento aIda = new vuelosfis.modelo.Asiento(cod, 1, "Economy", 0.0);
                            aIda.ocuparAsiento(); // <--- IMPORTANTE: Marcar ocupado para que se guarde
                            reservaFinal.agregarDetalle(new DetalleReserva(vueloIda, paxDummy, aIda, new vuelosfis.modelo.Economy(), true));
                        }
                        
                        // Agregar Bebés a la Ida también
                        if (cantBebes > 0) {
                            Pasajero paxBebeIda = new Pasajero("Bebé", "000", "x", new Bebe());
                            for (int i = 0; i < cantBebes; i++) {
                                reservaFinal.agregarDetalle(new DetalleReserva(vueloIda, paxBebeIda, null, new vuelosfis.modelo.Economy(), false));
                            }
                        }
                        
                        System.out.println("IDA Reconstruida con éxito en BD: " + codVueloIda);
                    }
                } catch (Exception e) { 
                    System.out.println("Error reconstruyendo Ida: " + e.getMessage()); 
                }
                // -----------------------------------------------------------------

            } else {
                textoFinalIda = infoEsteTramoCompleta; // Solo Ida
            }

            // 5. GUARDAR EN ARCHIVO
            if (controlador != null) {
                controlador.getControladorReserva().finalizarReserva(reservaFinal);
            }

            // 6. MOSTRAR RESUMEN
            VentanaResumen vResumen = new VentanaResumen();
            vResumen.setControlador(this.controlador);
            vResumen.setReserva(reservaFinal); 

            if (esViajeRedondo) {
                vResumen.mostrarResumen(textoFinalIda, precioIdaPrevTotal, textoFinalVuelta, totalEsteTramo);
            } else {
                vResumen.mostrarResumen(textoFinalIda, totalEsteTramo, "", 0.0);
            }
            vResumen.setVisible(true);
        }
    }//GEN-LAST:event_btnConfirmarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VentanaSeleccionAsientos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JPanel pnlAsientos;
    private javax.swing.JScrollPane scrollpane;
    private javax.swing.JTextArea txtDetalle;
    // End of variables declaration//GEN-END:variables
}
