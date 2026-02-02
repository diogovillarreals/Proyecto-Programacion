package vuelosfis.vista;

import vuelosfis.controlador.ControladorVuelo;

public class VentanaPrincipal extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaPrincipal.class.getName());
    private ControladorVuelo controlador;
    // Variable para guardar la lista maestra de ciudades
    private java.util.ArrayList<String> todasLasCiudades = new java.util.ArrayList<>();
    
    /**
     * Creates new form VentanaPrincipal
     */
    public VentanaPrincipal() {
        initComponents();
        this.setLocationRelativeTo(null);
        cargarDatos();
        // BLOQUEO DE FECHAS (Hoy hasta un año)

        // 1. Calculamos los límites
        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.LocalDate maximo = hoy.plusYears(1); // La fecha límite (hoy + 365 días)

        // 2. Aplicamos el rango a ambos calendarios
        // (Mínimo: hoy, Máximo: maximo)
        dpIda.getSettings().setDateRangeLimits(hoy, maximo);
        dpVuelta.getSettings().setDateRangeLimits(hoy, maximo);

        // Vincula Ida con Vuelta
        dpIda.addDateChangeListener((dateChangeEvent) -> {
            java.time.LocalDate fechaSeleccionada = dateChangeEvent.getNewDate();
            if (fechaSeleccionada != null) {
                // Al cambiar la Ida, la Vuelta ahora tiene:
                // - Mínimo: La fecha de ida (no puedes volver antes de ir)
                // - Máximo: El mismo límite de 1 año general
                dpVuelta.getSettings().setDateRangeLimits(fechaSeleccionada, maximo);

                // Si la vuelta seleccionada quedó fuera de rango (antes de la ida), la borramos
                java.time.LocalDate fechaVueltaActual = dpVuelta.getDate();
                if (fechaVueltaActual != null && fechaVueltaActual.isBefore(fechaSeleccionada)) {
                    dpVuelta.setDate(null);
                }
            } else {
                // Si borran la fecha de ida, reseteamos la vuelta a los límites originales
                dpVuelta.getSettings().setDateRangeLimits(hoy, maximo);
            }
        });
    }
    
    private void cargarDatos() {
    // Llenar cabinas
    cbCabina.addItem("Economy");
    cbCabina.addItem("Premium Economy");
    cbCabina.addItem("Business");
    
    // Configurar pasajeros
    spAdultos.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));
    spNinos.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));
    spBebes.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));
    spMayores.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));
    ((javax.swing.JSpinner.DefaultEditor) spAdultos.getEditor()).getTextField().setEditable(false);
    }
    
    public void setControlador(ControladorVuelo controladorRecibido) {
        this.controlador = controladorRecibido;
        // Esto es lo que rellena los combos cuando regresas de VentanaVuelosIda
        if (this.controlador != null) {
            cargarCiudadesDisponibles();
    }
    }
    
    public ControladorVuelo getControlador() {
        return this.controlador;
    }
    
    private void cargarCiudadesDisponibles() {
        cbOrigen.removeAllItems();
        cbDestino.removeAllItems();
        todasLasCiudades.clear();

        if (this.controlador == null) {
            return;
        }

        // 1. Obtener ciudades y guardarlas ordenadas
        java.util.TreeSet<String> setCiudades = new java.util.TreeSet<>();
        for (vuelosfis.modelo.Vuelo v : this.controlador.getControladorReserva().getCatalogoVuelos()) {
            setCiudades.add(v.getRuta().getOrigen().getNombre());
            setCiudades.add(v.getRuta().getDestino().getNombre());
        }
        todasLasCiudades.addAll(setCiudades);

        // 2. Limpiar listeners viejos para no causar errores
        for (java.awt.event.ActionListener al : cbOrigen.getActionListeners()) {
            cbOrigen.removeActionListener(al);
        }

        // 3. Llenar Origen
        for (String ciudad : todasLasCiudades) {
            cbOrigen.addItem(ciudad);
        }

        // 4. Agregar el evento: Si cambia Origen -> Actualiza Destino
        cbOrigen.addActionListener(evt -> actualizarComboDestino());

        // 5. Ejecutar una vez al inicio
        if (cbOrigen.getItemCount() > 0) {
            cbOrigen.setSelectedIndex(0);
            actualizarComboDestino();
        }
    }
    
    private void actualizarComboDestino() {
    String origenSeleccionado = (String) cbOrigen.getSelectedItem();
    if (origenSeleccionado == null) return;

    // 1. Guardar selección previa
    String destinoPrevio = (String) cbDestino.getSelectedItem();

    // 2. Recargar destino excluyendo el origen
    cbDestino.removeAllItems();
    for (String ciudad : todasLasCiudades) {
        if (!ciudad.equals(origenSeleccionado)) {
            cbDestino.addItem(ciudad);
        }
    }

    // 3. Intentar mantener la selección anterior si aún es válida
    if (destinoPrevio != null && !destinoPrevio.equals(origenSeleccionado)) {
        // Verificar si existe en la nueva lista antes de seleccionarlo
        boolean existe = false;
        for(int i=0; i<cbDestino.getItemCount(); i++) {
            if(cbDestino.getItemAt(i).equals(destinoPrevio)) { 
                existe = true; break; 
            }
        }
        if(existe) cbDestino.setSelectedItem(destinoPrevio);
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanelPrincipal = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbOrigen = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbDestino = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        rbIdaVuelta = new javax.swing.JRadioButton();
        rbSoloIda = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        cbCabina = new javax.swing.JComboBox<>();
        jPanelFechas = new javax.swing.JPanel();
        separadorFechas = new javax.swing.JSeparator();
        dpIda = new com.github.lgooddatepicker.components.DatePicker();
        dpVuelta = new com.github.lgooddatepicker.components.DatePicker();
        lblTituloVuelta = new javax.swing.JLabel();
        lblIda = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        spAdultos = new javax.swing.JSpinner();
        spNinos = new javax.swing.JSpinner();
        spBebes = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        spMayores = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(640, 480));

        jPanelPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        jPanelPrincipal.setPreferredSize(new java.awt.Dimension(640, 480));

        jLabel1.setText("Bienvenidos a VuelosFIS");
        jLabel1.setFont(new java.awt.Font("Lucida Bright", 0, 24)); // NOI18N

        jLabel2.setText("Origen:");
        jLabel2.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N

        cbOrigen.addActionListener(this::cbOrigenActionPerformed);

        jLabel3.setText("¿A dónde quieres volar?");
        jLabel3.setFont(new java.awt.Font("Lucida Bright", 0, 22)); // NOI18N

        jLabel4.setText("Destino:");
        jLabel4.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(52, 17));

        btnBuscar.setText("Buscar Vuelos");
        btnBuscar.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        buttonGroup1.add(rbIdaVuelta);
        rbIdaVuelta.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        rbIdaVuelta.setSelected(true);
        rbIdaVuelta.setText("Ida y vuelta");
        rbIdaVuelta.addActionListener(this::rbIdaVueltaActionPerformed);

        buttonGroup1.add(rbSoloIda);
        rbSoloIda.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N
        rbSoloIda.setText("Solo ida");
        rbSoloIda.addActionListener(this::rbSoloIdaActionPerformed);

        jLabel5.setText("Cabina:");
        jLabel5.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N

        jPanelFechas.setBackground(new java.awt.Color(255, 255, 255));
        jPanelFechas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jPanelFechas.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        lblTituloVuelta.setText("Fecha de Vuelta");

        lblIda.setText("Fecha de Ida");

        javax.swing.GroupLayout jPanelFechasLayout = new javax.swing.GroupLayout(jPanelFechas);
        jPanelFechas.setLayout(jPanelFechasLayout);
        jPanelFechasLayout.setHorizontalGroup(
            jPanelFechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(separadorFechas)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelFechasLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanelFechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblIda)
                    .addComponent(lblTituloVuelta, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelFechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(dpIda, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dpVuelta, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        jPanelFechasLayout.setVerticalGroup(
            jPanelFechasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelFechasLayout.createSequentialGroup()
                .addComponent(lblIda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dpIda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(separadorFechas, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTituloVuelta, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dpVuelta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        jLabel8.setText("Pasajeros:");
        jLabel8.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N

        spAdultos.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));

        spNinos.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));

        spBebes.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));

        jLabel9.setText("Adultos (De 12 a 64 años)");
        jLabel9.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N

        jLabel10.setText("Niños (De 2 a 11 años)");
        jLabel10.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N

        jLabel11.setText("Bebés (Menores de 2 años)");
        jLabel11.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N

        jLabel12.setText("Fechas:");
        jLabel12.setFont(new java.awt.Font("Lucida Bright", 0, 18)); // NOI18N

        jLabel13.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N

        spMayores.setModel(new javax.swing.SpinnerNumberModel(0, 0, 10, 1));

        jLabel14.setText("Adulto mayor (65 años en adelante)");
        jLabel14.setFont(new java.awt.Font("Lucida Bright", 0, 12)); // NOI18N
        jLabel14.setToolTipText("");

        javax.swing.GroupLayout jPanelPrincipalLayout = new javax.swing.GroupLayout(jPanelPrincipal);
        jPanelPrincipal.setLayout(jPanelPrincipalLayout);
        jPanelPrincipalLayout.setHorizontalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(146, 146, 146))
                            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                        .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(96, 96, 96))
                                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(50, 50, 50)))
                                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spNinos, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spAdultos, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22))
                            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel14)
                                .addGap(38, 38, 38)
                                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(spBebes, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                                    .addComponent(spMayores))
                                .addGap(22, 22, 22))))
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbCabina, 0, 282, Short.MAX_VALUE)
                            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(70, 70, 70))))
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel3)))
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addComponent(btnBuscar))
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                                .addComponent(rbIdaVuelta)
                                .addGap(18, 18, 18)
                                .addComponent(rbSoloIda, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 614, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanelFechas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(229, 229, 229))
        );
        jPanelPrincipalLayout.setVerticalGroup(
            jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(rbIdaVuelta)
                    .addComponent(rbSoloIda))
                .addGap(33, 33, 33)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelPrincipalLayout.createSequentialGroup()
                        .addComponent(cbCabina, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addGap(16, 16, 16)
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spAdultos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(spNinos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)))
                    .addComponent(jPanelFechas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spBebes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spMayores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel13)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 658, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cbOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbOrigenActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // 1. Capturar ciudades
        String origen = (String) cbOrigen.getSelectedItem();
        String destino = (String) cbDestino.getSelectedItem();
        
        // CAPTURAR LA CABINA 
        String cabinaSeleccionada = cbCabina.getSelectedItem().toString();
        
        // 2. Capturar fechas 
        java.time.LocalDate fechaIdaObj = dpIda.getDate();
        java.time.LocalDate fechaVueltaObj = dpVuelta.getDate();

        // --- VALIDACIONES DE FECHAS ---
        // A) Validar que la fecha de IDA no esté vacía
        if (fechaIdaObj == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione fecha de ida",
                    "Fecha incompleta",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // B) Validar fecha de VUELTA (Solo si es Ida y Vuelta)
        if (rbIdaVuelta.isSelected()) {
            if (fechaVueltaObj == null) {
                javax.swing.JOptionPane.showMessageDialog(this,
                        "Por favor, seleccione fecha de vuelta",
                        "Fecha incompleta",
                        javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

        }

        // 3. Preparar datos para la siguiente ventana
        java.time.format.DateTimeFormatter formato = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
        VentanaVuelosIda ventana2 = new VentanaVuelosIda();
        ventana2.setControlador(this.controlador);

        // Convertimos Ida
        String fechaIdaTexto = fechaIdaObj.format(formato);

        // Convertimos Vuelta (Si existe, si no, la dejamos vacía)
        String fechaVueltaTexto = "";
        if (rbIdaVuelta.isSelected() && fechaVueltaObj != null) {
            fechaVueltaTexto = fechaVueltaObj.format(formato);
        }

        // 4. Capturar y validar pasajeros
        int nAdultos = (int) spAdultos.getValue();
        int nNinos = (int) spNinos.getValue();
        int nBebes = (int) spBebes.getValue();
        int nMayores = (int) spMayores.getValue();

        int totalPasajeros = nAdultos + nNinos + nBebes + nMayores;
        int totalResponsables = nAdultos + nMayores;
        int totalDependientes = nNinos + nBebes;

        // A) Validación: ¿Hay alguien viajando?
        if (totalPasajeros == 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Por favor, indique cuantos pasajeros irán en el viaje",
                    "Faltan pasajeros",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // B) Validación: ¿Los niños viajan acompañados?
        if (totalDependientes > 0 && totalResponsables == 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Mínimo debe viajar un adulto o un adulto mayor",
                    "Aviso de Seguridad",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 5. Abrir ventana de ida
        this.setVisible(false);
     
        // Variable para saber si es redondo
        boolean esRedondo = rbIdaVuelta.isSelected();
        
        // Pasamos los 7 datos necesarios:
        // Origen, Destino, FechaIda, FechaVuelta, TotalPasajeros, esIdaYVuelta, cabinaSeleccionada
        ventana2.recibirDatos(origen, destino, fechaIdaTexto, fechaVueltaTexto, totalPasajeros, esRedondo, cabinaSeleccionada);

        ventana2.setVisible(true);
        ventana2.setLocationRelativeTo(null);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void rbIdaVueltaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbIdaVueltaActionPerformed
        // MOSTRAR todo de nuevo
        lblTituloVuelta.setVisible(true);

        // Usamos la variable dpVuelta
        dpVuelta.setVisible(true);

        //Separador
        separadorFechas.setVisible(true);

        // Poner el foco en el nuevo calendario para invitar a seleccionar
        dpVuelta.requestFocus();
    }//GEN-LAST:event_rbIdaVueltaActionPerformed

    private void rbSoloIdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbSoloIdaActionPerformed
        // OCULTAR la etiqueta y el calendario de vuelta
        lblTituloVuelta.setVisible(false);

        //Usamos la variable dpVuelta
        dpVuelta.setVisible(false);

        // Separador
        separadorFechas.setVisible(false);
    }//GEN-LAST:event_rbSoloIdaActionPerformed

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
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VentanaPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbCabina;
    private javax.swing.JComboBox<String> cbDestino;
    private javax.swing.JComboBox<String> cbOrigen;
    private com.github.lgooddatepicker.components.DatePicker dpIda;
    private com.github.lgooddatepicker.components.DatePicker dpVuelta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanelFechas;
    private javax.swing.JPanel jPanelPrincipal;
    private javax.swing.JLabel lblIda;
    private javax.swing.JLabel lblTituloVuelta;
    private javax.swing.JRadioButton rbIdaVuelta;
    private javax.swing.JRadioButton rbSoloIda;
    private javax.swing.JSeparator separadorFechas;
    private javax.swing.JSpinner spAdultos;
    private javax.swing.JSpinner spBebes;
    private javax.swing.JSpinner spMayores;
    private javax.swing.JSpinner spNinos;
    // End of variables declaration//GEN-END:variables
}
