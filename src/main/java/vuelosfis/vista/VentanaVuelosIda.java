package vuelosfis.vista;

import javax.swing.table.DefaultTableModel;


public class VentanaVuelosIda extends javax.swing.JFrame {
    
    private vuelosfis.controlador.ControladorVuelo controlador;
    private java.util.ArrayList<vuelosfis.modelo.Vuelo> listaVuelosEncontrados;

    // Variables privadas
    private String origen;
    private String destino;
    private String fechaIda;
    private String fechaVuelta; 
    private int pasajeros;
    private boolean esIdaYVuelta;
    private String cabina;
    private String infoIdaPrev;
    private double precioIdaPrev = 0.0;
    private String infoVueltaActual; // Solo se usa si estamos en el paso final

    public VentanaVuelosIda() {
        initComponents();
        this.setLocationRelativeTo(null);
        
        // 1. Obtenemos el "modelo de selección" de la tabla
        javax.swing.ListSelectionModel selector = tblVuelos.getSelectionModel();
    }

    /**
     * INYECCIÓN DE DATOS
     */
    public void recibirDatos(String origen, String destino, String fechaIda, String fechaVuelta, 
            int pasajeros, boolean esRedondo, String cabina) {
        this.origen = origen;
        this.destino = destino;
        this.fechaIda = fechaIda;
        this.fechaVuelta = fechaVuelta; // Guardamos la fecha de vuelta
        this.pasajeros = pasajeros;
        this.esIdaYVuelta = esRedondo;  // Guardamos si es viaje redondo
        this.cabina = cabina;
        
        // Ejecutamos nuestra lógica visual
        actualizarTitulo();
        cargarTablaEstiloLATAM();
    }

    /**
     * Método 1: Actualiza el texto de arriba
     */
    private void actualizarTitulo() {
        // Usamos el Label(lblTitulo)
        lblTitulo.setText("Vuelos encontrados: " + origen + " - " + destino);
        
        // También cambiamos el título de la ventana flotante
        this.setTitle("Resultados para " + pasajeros + " pasajero(s) - Fecha: " + fechaIda);
    }

    /**
     * Método 2: Crea la tabla bonita y la llena de datos falsos
     */
    private void cargarTablaEstiloLATAM() {
        // 1. Definimos las columnas
        String[] columnas = {
            "Vuelo",    // Col 0
            "Salida",   // Col 1
            "Duración", // Col 2
            "Precio",   // Col 3
            "Avión"     // Col 4
        };

        // 2. Modelo de datos
        javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        try {
            // A. Convertimos la fecha
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.LocalDate fechaBuscada = java.time.LocalDate.parse(this.fechaIda, fmt);

            // B. Buscamos en el cerebro
            this.listaVuelosEncontrados = this.controlador.getControladorReserva().buscarVuelos(origen, destino, fechaBuscada); 
            java.util.ArrayList<vuelosfis.modelo.Vuelo> resultados = this.listaVuelosEncontrados;
            
            if (resultados.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "No hay vuelos para esta fecha.");
            }

            // C. Llenamos la tabla (ALINEADO PERFECTAMENTE)
            for (vuelosfis.modelo.Vuelo v : resultados) {
                Object[] fila = {
                    v.getCodigo(),                      // Vuelo (Col 0)
                    v.getHora().toString(),             // Salida (Col 1)
                    v.getRuta().getDuracion() + " min", // Duración (Col 2)
                    "$" + v.getPrecioBase(),            // Precio (Col 3)
                    "Airbus A320"                       // Avión (Col 4)
                };
                modelo.addRow(fila);
            }

        } catch (Exception e) {
            System.out.println("Error al cargar vuelos en tabla: " + e.getMessage());
            e.printStackTrace();
        }
        
        // 3. Asignar el modelo a la tabla visual
        tblVuelos.setModel(modelo);
        tblVuelos.getTableHeader().setResizingAllowed(false);
        tblVuelos.getTableHeader().setReorderingAllowed(false);
        
        // Ajuste estético de anchos
        tblVuelos.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblVuelos.getColumnModel().getColumn(2).setPreferredWidth(70);
    }
    
    public void setDatosPrevios(String infoIda, double precioIda, String infoVuelta) {
        this.infoIdaPrev = infoIda;
        this.precioIdaPrev = precioIda;
        this.infoVueltaActual = infoVuelta;
    }
    
    public void setControlador(vuelosfis.controlador.ControladorVuelo controladorRecibido) {
        this.controlador = controladorRecibido;
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
        lblTitulo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblVuelos = new javax.swing.JTable();
        btnSeleccionar = new javax.swing.JButton();
        btnAtras = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        lblTitulo.setFont(new java.awt.Font("Lucida Bright", 0, 24)); // NOI18N
        lblTitulo.setText("Vuelos encontrados:");

        tblVuelos.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
        tblVuelos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Vuelo", "Salida", "Duración", "Precio", "Avión"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblVuelos);

        btnSeleccionar.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        btnSeleccionar.setText("Continuar");
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });

        btnAtras.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        btnAtras.setText("Atrás");
        btnAtras.setToolTipText("");
        btnAtras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtrasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 775, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAtras)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSeleccionar))
                    .addComponent(lblTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSeleccionar)
                    .addComponent(btnAtras))
                .addContainerGap(18, Short.MAX_VALUE))
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

    private void btnAtrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtrasActionPerformed
        // 1. Crear la nueva instancia de la pantalla principal
        vuelosfis.vista.VentanaPrincipal ventana1 = new vuelosfis.vista.VentanaPrincipal();
        
        // 2. Pasarle el controlador para que recupere la memoria
        ventana1.setControlador(this.controlador); 
        
        // 3. Mostrarla y cerrar la actual
        ventana1.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAtrasActionPerformed

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        int fila = tblVuelos.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un vuelo.");
            return;
        }

               
        String precioTexto = tblVuelos.getValueAt(fila, 3).toString(); // Columna 3 = Precio
        String precioLimpio = precioTexto.replace("$", "").trim(); // Quitamos el $
        
        // Pasamos los datos a la siguiente ventana
        this.setVisible(false);       
        vuelosfis.modelo.Vuelo vueloElegido = this.listaVuelosEncontrados.get(fila);
        
        VentanaSeleccionTarifa vTarifas = new VentanaSeleccionTarifa();
        
        // Pasamos 'precioLimpio' en vez de precioTexto
        vTarifas.inicializar(this.controlador,vueloElegido,origen, destino, fechaIda, precioLimpio, pasajeros, esIdaYVuelta, true, fechaVuelta, this.cabina);
        
        vTarifas.setVisible(true);
    }//GEN-LAST:event_btnSeleccionarActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new VentanaVuelosIda().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtras;
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTable tblVuelos;
    // End of variables declaration//GEN-END:variables
}
