package vuelosfis.vista;

public class VentanaSeleccionTarifa extends javax.swing.JFrame {
    
    private vuelosfis.controlador.ControladorVuelo controlador; 
    private vuelosfis.modelo.Vuelo vueloActual;

    // Variables de control de navegación
    private boolean esViajeRedondo;
    private boolean esTramoIda;

    // Variables de datos
    private String origen, destino, fechaVueltaGuardada;
    private int pasajeros;
    
    // --- AGREGADO: Variable para recordar la cabina original ---
    private String cabinaOriginal; 
    // ----------------------------------------------------------

    private String infoIdaPrev;      
    private double precioIdaPrev = 0.0;
    private String infoVueltaActual;
    
    public VentanaSeleccionTarifa() {
        initComponents();
        this.setLocationRelativeTo(null);
    }

    /**
     * CONFIGURACIÓN INICIAL Recibe el precio base y calcula las 3 opciones
     * originales: 1. Básica 2. Con Maletas 3. Con Prioridad
     * * --- MODIFICADO: Ahora recibe 'cabina' al final ---
     */
    public void inicializar(vuelosfis.controlador.ControladorVuelo ctrl,vuelosfis.modelo.Vuelo vuelo,String origen, String destino, String fecha, String precioBaseStr,
            int pasajeros, boolean esRedondo, boolean esIda, String fechaVuelta, String cabina) { // <--- AÑADIDO
        
        this.controlador = ctrl;
        this.vueloActual = vuelo;
        this.origen = origen;
        this.destino = destino;
        this.pasajeros = pasajeros;
        this.esViajeRedondo = esRedondo;
        this.esTramoIda = esIda;
        this.fechaVueltaGuardada = fechaVuelta;
        
        // --- AGREGADO: Guardamos la cabina ---
        this.cabinaOriginal = cabina;
        // -------------------------------------
        
        if (this.controlador == null) {
            System.out.println("⚠️ ALERTA: Controlador llegó nulo a Tarifas. Creando uno de emergencia...");
            this.controlador = new vuelosfis.controlador.ControladorVuelo(); 
            // Esto asegura que al menos podamos leer el archivo y bloquear asientos
            this.controlador.getControladorReserva().cargarDatosIniciales();
        }
        
        // Título informativo
        lblTitulo.setText("Elige tu tarifa para: " + origen + " - " + destino + " (" + cabina + ")");

        // 1. Limpiar el precio para hacer matemáticas ($ 100.00 -> 100.00)
        double base = Double.parseDouble(precioBaseStr.replace("$", "").replace(" ", "").trim());

        // 2. CÁLCULO DE LAS 3 TARIFAS (Según lo acordado)
        // A. BÁSICA: Precio tal cual sale en la tabla
        double pBasic = base;

        // B. ESTÁNDAR (Más mochilas): Sumamos $40 por la maleta extra
        double pEstandar = base + 40.00;

        // C. PREMIUM (Prioridad): Sumamos $90 por embarque prioritario y VIP
        double pPremium = base + 90.00;

        // 3. Mostrar precios en pantalla
        lblBasic.setText("$ " + String.format("%.2f", pBasic));
        lblEstandar.setText("$ " + String.format("%.2f", pEstandar));
        lblPremium.setText("$ " + String.format("%.2f", pPremium));
    }

    /**
     * 2. RECIBIR DATOS PREVIOS
     */
    public void setDatosPrevios(String infoIda, double precioIda, String infoVuelta) {
        this.infoIdaPrev = infoIda;
        this.precioIdaPrev = precioIda;
        this.infoVueltaActual = infoVuelta;
    }
    
  private void procesarSeleccion(String tarifaNombre, String precioStr) {
        // 1. Convertir precio
        double precioActual = Double.parseDouble(precioStr.replace("$", "").replace(" ", "").replace(",", "."));

        this.setVisible(false);
        VentanaSeleccionAsientos vAsientos = new VentanaSeleccionAsientos();
        
        String cabinaAsignada = "Economy"; // Valor por defecto
        
        // --- LÓGICA ESTRICTA: SOLO IMPORTA LA CABINA ORIGINAL ---
        // Ignoramos totalmente qué tarifa (Básica/Premium) eligió el usuario para los asientos.
        
        String cabinaLimpia = (cabinaOriginal != null) ? cabinaOriginal.trim().toUpperCase() : "";

        if (cabinaLimpia.contains("BUSINESS")) {
            cabinaAsignada = "Business"; 
        } 
        else if (cabinaLimpia.contains("PREMIUM")) { 
            cabinaAsignada = "Premium"; 
        } 
        else {
            cabinaAsignada = "Economy";
        }
        
        System.out.println("Cabina Original: " + cabinaOriginal);
        System.out.println("Tarifa: " + tarifaNombre + " (IGNORADA para bloqueo de asientos)");
        System.out.println("-> Cabina Final Asignada: " + cabinaAsignada);
        // --------------------------------------------------------

        // Enviar datos
        if (esViajeRedondo && esTramoIda) {
            // FASE 1: IDA
            String infoActual = "Ida: " + origen + " - " + destino + " | Tarifa: " + tarifaNombre;
            vAsientos.cargarDatos(this.controlador,this.vueloActual,origen, destino, pasajeros, cabinaAsignada, esViajeRedondo, true, fechaVueltaGuardada,
                                  precioActual, infoActual, 0.0, ""); 
        } else {
            // FASE 2: VUELTA O SOLO IDA
            if (esViajeRedondo) {
                String infoVuelta = "Vuelta: " + origen + " - " + destino + " | Tarifa: " + tarifaNombre;
                vAsientos.cargarDatos(this.controlador,this.vueloActual,origen, destino, pasajeros, cabinaAsignada, true, false, null,
                                      precioActual, infoIdaPrev, precioIdaPrev, infoVuelta);
            } else {
                String infoActual = "Ida: " + origen + " - " + destino + " | Tarifa: " + tarifaNombre;
                vAsientos.cargarDatos(this.controlador,this.vueloActual,origen, destino, pasajeros, cabinaAsignada, false, false, null,
                                      precioActual, infoActual, 0.0, "");
            }
        }
        vAsientos.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        pnlBasic = new javax.swing.JPanel();
        lblB = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnBasic = new javax.swing.JButton();
        lblBasic = new javax.swing.JLabel();
        pnlEstandar = new javax.swing.JPanel();
        lblE = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnEstandar = new javax.swing.JButton();
        lblEstandar = new javax.swing.JLabel();
        pnlPremium = new javax.swing.JPanel();
        lblP = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btnPremium = new javax.swing.JButton();
        lblPremium = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        pnlBasic.setBackground(new java.awt.Color(204, 255, 255));
        pnlBasic.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblB.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        lblB.setText("Basic");

        jLabel3.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        jLabel3.setText("Viaje ligero.");

        jLabel6.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        jLabel6.setText("• Solo equipaje de mano.");

        btnBasic.setFont(new java.awt.Font("Lucida Bright", 0, 14)); // NOI18N
        btnBasic.setForeground(new java.awt.Color(255, 51, 0));
        btnBasic.setText("Elegir");
        btnBasic.addActionListener(this::btnBasicActionPerformed);

        lblBasic.setFont(new java.awt.Font("Lucida Bright", 0, 14)); // NOI18N
        lblBasic.setText("jLabel1");

        javax.swing.GroupLayout pnlBasicLayout = new javax.swing.GroupLayout(pnlBasic);
        pnlBasic.setLayout(pnlBasicLayout);
        pnlBasicLayout.setHorizontalGroup(
            pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBasicLayout.createSequentialGroup()
                .addGroup(pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBasicLayout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addComponent(lblB))
                    .addGroup(pnlBasicLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlBasicLayout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(btnBasic))
                    .addGroup(pnlBasicLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6))
                    .addGroup(pnlBasicLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblBasic, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        pnlBasicLayout.setVerticalGroup(
            pnlBasicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBasicLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblB, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblBasic, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnBasic)
                .addGap(20, 20, 20))
        );

        pnlEstandar.setBackground(new java.awt.Color(204, 255, 204));
        pnlEstandar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblE.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        lblE.setText("Estándar");

        jLabel5.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        jLabel5.setText("Lleva más cosas.");

        jLabel4.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        jLabel7.setText("• Equipaje de mano.");

        jLabel8.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        jLabel8.setText("• 1 maleta en bodega.");

        btnEstandar.setFont(new java.awt.Font("Lucida Bright", 0, 14)); // NOI18N
        btnEstandar.setForeground(new java.awt.Color(255, 51, 0));
        btnEstandar.setText("Elegir");
        btnEstandar.addActionListener(this::btnEstandarActionPerformed);

        lblEstandar.setFont(new java.awt.Font("Lucida Bright", 0, 14)); // NOI18N
        lblEstandar.setText("jLabel1");

        javax.swing.GroupLayout pnlEstandarLayout = new javax.swing.GroupLayout(pnlEstandar);
        pnlEstandar.setLayout(pnlEstandarLayout);
        pnlEstandarLayout.setHorizontalGroup(
            pnlEstandarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstandarLayout.createSequentialGroup()
                .addGroup(pnlEstandarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlEstandarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(pnlEstandarLayout.createSequentialGroup()
                            .addGap(77, 77, 77)
                            .addComponent(lblE))
                        .addGroup(pnlEstandarLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel4))
                        .addGroup(pnlEstandarLayout.createSequentialGroup()
                            .addGap(90, 90, 90)
                            .addComponent(btnEstandar))
                        .addGroup(pnlEstandarLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlEstandarLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(pnlEstandarLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(pnlEstandarLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblEstandar, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        pnlEstandarLayout.setVerticalGroup(
            pnlEstandarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlEstandarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblE, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblEstandar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(btnEstandar)
                .addGap(19, 19, 19))
        );

        pnlPremium.setBackground(new java.awt.Color(204, 204, 255));
        pnlPremium.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblP.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        lblP.setText("Premium ");

        jLabel9.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        jLabel9.setText("Viaja primero.");

        jLabel10.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        jLabel10.setText("• Equipaje de mano.");

        jLabel11.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        jLabel11.setText("• 1 maleta en bodega.");

        jLabel12.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        jLabel12.setText("• Prioridad de embarque.");

        btnPremium.setFont(new java.awt.Font("Lucida Bright", 0, 14)); // NOI18N
        btnPremium.setForeground(new java.awt.Color(255, 51, 0));
        btnPremium.setText("Elegir");
        btnPremium.addActionListener(this::btnPremiumActionPerformed);

        lblPremium.setFont(new java.awt.Font("Lucida Bright", 0, 14)); // NOI18N
        lblPremium.setText("jLabel1");

        javax.swing.GroupLayout pnlPremiumLayout = new javax.swing.GroupLayout(pnlPremium);
        pnlPremium.setLayout(pnlPremiumLayout);
        pnlPremiumLayout.setHorizontalGroup(
            pnlPremiumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPremiumLayout.createSequentialGroup()
                .addGroup(pnlPremiumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlPremiumLayout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(lblP))
                    .addGroup(pnlPremiumLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlPremiumLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10))
                    .addGroup(pnlPremiumLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11))
                    .addGroup(pnlPremiumLayout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(btnPremium))
                    .addGroup(pnlPremiumLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12))
                    .addGroup(pnlPremiumLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblPremium, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        pnlPremiumLayout.setVerticalGroup(
            pnlPremiumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPremiumLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblP, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(lblPremium, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(btnPremium)
                .addGap(18, 18, 18))
        );

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 0, 24)); // NOI18N
        lblTitulo.setText("3 Tarifas Disponibles");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pnlBasic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlEstandar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pnlPremium, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPremium, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlEstandar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlBasic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBasicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBasicActionPerformed
        procesarSeleccion("BÁSICA", lblBasic.getText());
    }//GEN-LAST:event_btnBasicActionPerformed

    private void btnEstandarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEstandarActionPerformed
        procesarSeleccion("ESTÁNDAR", lblEstandar.getText());
    }//GEN-LAST:event_btnEstandarActionPerformed

    private void btnPremiumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPremiumActionPerformed
        procesarSeleccion("PREMIUM", lblPremium.getText());
    }//GEN-LAST:event_btnPremiumActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new VentanaSeleccionTarifa().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBasic;
    private javax.swing.JButton btnEstandar;
    private javax.swing.JButton btnPremium;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblB;
    private javax.swing.JLabel lblBasic;
    private javax.swing.JLabel lblE;
    private javax.swing.JLabel lblEstandar;
    private javax.swing.JLabel lblP;
    private javax.swing.JLabel lblPremium;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlBasic;
    private javax.swing.JPanel pnlEstandar;
    private javax.swing.JPanel pnlPremium;
    // End of variables declaration//GEN-END:variables
}
