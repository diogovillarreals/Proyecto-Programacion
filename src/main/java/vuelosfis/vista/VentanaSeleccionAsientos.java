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

public class VentanaSeleccionAsientos extends javax.swing.JFrame {
    
    private vuelosfis.controlador.ControladorVuelo controlador; 
    private vuelosfis.modelo.Vuelo vueloActual;

    // --- VARIABLES DE DATOS ---
    private boolean esViajeRedondo, esTramoIda;
    private String origen, destino, fechaVueltaGuardada, cabinaPermitida;
    private int pasajerosTotal, pasajeroActual = 1;
    private double precioBaseUnitario, costoAsientosTotal = 0.0;
    
    // Mochila
    private String infoIdaPrev, infoVueltaActual;
    private double precioIdaPrevTotal;
    
    private ArrayList<String> misAsientos = new ArrayList<>();

    public VentanaSeleccionAsientos() {
        initComponents(); // Carga tu diseño de NetBeans
        this.setSize(800, 479);
        this.setLocationRelativeTo(null);
    }

    // --- CARGAR DATOS ---
    public void cargarDatos(vuelosfis.controlador.ControladorVuelo ctrl, 
                            vuelosfis.modelo.Vuelo vuelo, // <--- NUEVO
                            String origen, String destino, int pasajeros, String cabina,
                            boolean esRedondo, boolean esIda, String fechaVuelta,
                            double precioUnitarioActual, 
                            String infoIdaPrev, double precioIdaPrevTotal, String infoVueltaActual) {
        
        this.controlador = ctrl;
        this.vueloActual = vuelo;
        this.origen = origen;
        this.destino = destino;
        this.pasajerosTotal = pasajeros;
        this.cabinaPermitida = cabina;
        this.esViajeRedondo = esRedondo;
        this.esTramoIda = esIda;
        this.fechaVueltaGuardada = fechaVuelta;
        this.precioBaseUnitario = precioUnitarioActual;
        
        this.infoIdaPrev = infoIdaPrev;
        this.precioIdaPrevTotal = precioIdaPrevTotal;
        this.infoVueltaActual = infoVueltaActual;
        
        // Actualizar Textos Visuales
        lblTitulo.setText("Pasajero " + pasajeroActual + " de " + pasajerosTotal);
        actualizarPrecio();
        
        // Generar los botones DENTRO del panel que creaste en Design
        dibujarAsientosEnPanel();
    }

    private void actualizarPrecio() {
        // --- 1. CÁLCULOS ---
        double totalVueloActual = precioBaseUnitario * pasajerosTotal;
        double totalAsientos = costoAsientosTotal;
        // El gran total suma: Lo que traías de antes (si es vuelta) + Vuelo Actual + Asientos
        double granTotal = precioIdaPrevTotal + totalVueloActual + totalAsientos;

        // --- 2. CONSTRUIR EL TEXTO DEL DETALLE ---
        StringBuilder sb = new StringBuilder();
        
        // A) PRECIOS DE LOS VUELOS
        if (esViajeRedondo && !esTramoIda) {
            // -- CASO: ESTAMOS EN LA VUELTA --
            // Mostramos lo que costó la Ida (Total acumulado anterior)
            sb.append(" Vuelo IDA (Total): $ ").append(String.format("%.2f", precioIdaPrevTotal)).append("\n");
            // Mostramos el costo de este vuelo de vuelta
            sb.append(" Vuelo VUELTA ($").append(String.format("%.2f", precioBaseUnitario))
              .append(" por persona): $ ").append(String.format("%.2f", totalVueloActual)).append("\n");
        } else {
            // -- CASO: SOLO IDA O PRIMER TRAMO --
            sb.append(" Vuelo IDA ($").append(String.format("%.2f", precioBaseUnitario))
              .append(" por persona): $ ").append(String.format("%.2f", totalVueloActual)).append("\n");
        }
        
        sb.append(" ----------------------------\n");
        
        // B) DETALLE DE ASIENTOS
        if (misAsientos.isEmpty()) {
            sb.append(" (Asientos no seleccionados)\n");
        } else {
            for (String asiento : misAsientos) {
                sb.append(" + Asiento ").append(asiento).append(" .......... [OK]\n");
            }
        }
        
        sb.append(" ----------------------------\n");
        sb.append(" Costo Asientos: +$ ").append(String.format("%.2f", totalAsientos));
        
        // --- 3. MOSTRAR EN PANTALLA ---
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
            // Verificar si el historial está vacío. Si sí, ¡OBLIGAMOS A LEER EL ARCHIVO!
            // (Nota: Esto asume que tienes un método para checar si está vacío o simplemente recargamos)
            
            System.out.println("🔍 Verificando memoria de reservas para el vuelo: " + this.vueloActual.getCodigo());
            
            // Forzamos la carga de datos del archivo 'reservas.csv' en este instante
            // Esto arregla el problema de que la Ida llegue vacía.
            this.controlador.getControladorReserva().cargarDatosIniciales(); 
        } else {
             System.out.println("❌ ERROR CRÍTICO: El controlador es NULL en Asientos.");
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

        // --- BUCLE PARA CREAR LOS 10 FILAS ---
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
                    // Nota: Asegúrate de tener este método o similar en tu controlador.
                    // Si no lo tienes, usa el truco de abajo (*)
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

// --- 2. LÓGICA DE BLOQUEO (Tu requerimiento principal) ---
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
        if (misAsientos.size() >= pasajerosTotal) {
            JOptionPane.showMessageDialog(this, "Ya completaste los " + pasajerosTotal + " pasajeros.\nNo puedes seleccionar más.");
            return; // Se sale del método, no hace nada más
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
                lblTitulo.setText("Pasajero " + pasajeroActual + " de " + pasajerosTotal);
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
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addComponent(jSeparator2)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblTitulo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnConfirmar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(lblTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(lblTitulo)
                .addGap(37, 37, 37)
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
                .addComponent(btnConfirmar, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                .addGap(31, 31, 31))
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
        // 1. Validación de seguridad
        if (misAsientos.size() < pasajerosTotal) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona todos los asientos.");
            return;
        }

        // 2. PREPARAR DATOS DEL TRAMO ACTUAL
        double totalEsteTramo = (precioBaseUnitario * pasajerosTotal) + costoAsientosTotal;
        String infoEsteTramo = "Vuelo " + vueloActual.getCodigo() + " (" + origen + "-" + destino + ") Asientos: " + misAsientos;

        // =====================================================================
        // CAMINO A: IDA Y VUELTA (ESTAMOS EN LA IDA) -> IR A LA VUELTA
        // =====================================================================
        if (esViajeRedondo && esTramoIda) {
            this.setVisible(false);
            vuelosfis.vista.VentanaVuelosVuelta vVuelta = new vuelosfis.vista.VentanaVuelosVuelta();
            
            // Pasamos la estafeta
            vVuelta.recibirDatos(
                this.controlador,       
                this.destino, this.origen, this.fechaVueltaGuardada, this.pasajerosTotal,
                infoEsteTramo, totalEsteTramo, this.cabinaPermitida
            );
            vVuelta.setVisible(true);
        } 
        
        // =====================================================================
        // CAMINO B: FIN DEL VIAJE (SOLO IDA O ESTAMOS EN LA VUELTA) -> GUARDAR
        // =====================================================================
        else {
            this.setVisible(false);

            // 1. CREAMOS LA RESERVA FINAL
            vuelosfis.enums.TipoViaje tipo = esViajeRedondo ? vuelosfis.enums.TipoViaje.IDA_Y_VUELTA : vuelosfis.enums.TipoViaje.SOLO_IDA;
            vuelosfis.modelo.Reserva reservaFinal = new vuelosfis.modelo.Reserva("RES-" + System.currentTimeMillis(), tipo);
            vuelosfis.modelo.Pasajero paxDummy = new vuelosfis.modelo.Pasajero("Cliente", "999", "x", new vuelosfis.modelo.Adulto());

            // 2. AGREGAMOS LOS ASIENTOS DE *ESTE* VUELO (LO QUE ACABAS DE ELEGIR)
            for (String asientoCod : misAsientos) {
                 vuelosfis.modelo.Asiento aObj = new vuelosfis.modelo.Asiento(asientoCod, 1, cabinaPermitida, precioBaseUnitario);
                 vuelosfis.modelo.DetalleReserva det = new vuelosfis.modelo.DetalleReserva(vueloActual, paxDummy, aObj, new vuelosfis.modelo.Economy(), true);
                 reservaFinal.agregarDetalle(det);
            }

        // 3. RECONSTRUCCIÓN DE LA IDA (Solo si es viaje redondo y estamos en la vuelta)
            double granTotal = totalEsteTramo;
            String textoFinal = infoEsteTramo;

            if (esViajeRedondo && !esTramoIda) {
                granTotal += precioIdaPrevTotal;
                textoFinal = infoIdaPrev + "\n" + infoEsteTramo;
                
                try {
                    // Extraer datos de la cadena guardada en el tramo anterior
                    String codVueloIda = infoIdaPrev.split(" ")[1].trim();
                    String asientosStr = infoIdaPrev.substring(infoIdaPrev.indexOf("[") + 1, infoIdaPrev.indexOf("]"));
                    String[] arrayAsientos = asientosStr.split(",");

                    // Buscar el vuelo de ida en el catálogo
                    vuelosfis.modelo.Vuelo vueloIda = null;
                    for (vuelosfis.modelo.Vuelo v : controlador.getControladorReserva().getCatalogoVuelos()) {
                        if (v.getCodigo().equalsIgnoreCase(codVueloIda)) {
                            vueloIda = v;
                            break;
                        }
                    }

                    // Agregar detalles de ida a la reserva final (UNA SOLA VEZ)
                    if (vueloIda != null) {
                        for (String as : arrayAsientos) {
                            vuelosfis.modelo.Asiento aObjIda = new vuelosfis.modelo.Asiento(as.trim(), 1, "Economy", 0.0);
                            reservaFinal.agregarDetalle(new vuelosfis.modelo.DetalleReserva(vueloIda, paxDummy, aObjIda, new vuelosfis.modelo.Economy(), true));
                        }
                    }
                } catch (Exception e) {
                    System.out.println("⚠️ No se pudo reconstruir la reserva de Ida automáticamante: " + e.getMessage());
                }
            }

        // 4. GUARDADO ÚNICO
            controlador.getControladorReserva().finalizarReserva(reservaFinal);

            // 5. MOSTRAR RESUMEN
            vuelosfis.vista.VentanaResumen vResumen = new vuelosfis.vista.VentanaResumen();
            vResumen.setControlador(this.controlador);
            vResumen.setReserva(reservaFinal);
            
            if (esViajeRedondo) {
                // AQUÍ ESTÁ LA MAGIA: Enviamos Ida y Vuelta por separado
                // Arg 1: Info Ida, Arg 2: Precio Ida, Arg 3: Info Vuelta, Arg 4: Precio Vuelta
                vResumen.mostrarResumen(infoIdaPrev, precioIdaPrevTotal, infoEsteTramo, totalEsteTramo);
            } else {
                // Solo Ida
                vResumen.mostrarResumen(infoEsteTramo, totalEsteTramo, "", 0.0);
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
