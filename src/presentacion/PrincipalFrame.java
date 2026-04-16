package presentacion;

import entidades.Registro;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;
import logica.ParqueoService;
import logica.ResultadoOperacion;

public class PrincipalFrame extends JFrame {
    private final ParqueoService parqueoService;
    private final JTextField txtPlaca;
    private final JComboBox<String> cmbTipo;
    private final JLabel lblEstado;
    private final JLabel lblResumen;
    private final JTable tablaActivos;
    private final JTable tablaHistorial;
    private final RegistroTableModel modeloActivos;
    private final RegistroTableModel modeloHistorial;

    public PrincipalFrame() {
        this.parqueoService = new ParqueoService();
        this.txtPlaca = new JTextField(14);
        this.cmbTipo = new JComboBox<>(new String[]{"", "Carro", "Moto"});
        this.lblEstado = new JLabel("Sistema listo.");
        this.lblResumen = new JLabel("Tarifa vigente: CRC 500 por hora o fraccion.");
        this.modeloActivos = new RegistroTableModel(false);
        this.modeloHistorial = new RegistroTableModel(true);
        this.tablaActivos = new JTable(modeloActivos);
        this.tablaHistorial = new JTable(modeloHistorial);

        configurarVentana();
        construirInterfaz();
        cargarTablas();
    }

    private void configurarVentana() {
        setTitle("Sistema de Parqueo Publico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 680));
        setLocationRelativeTo(null);
    }

    private void construirInterfaz() {
        JPanel contenedor = new JPanel(new BorderLayout(14, 14));
        contenedor.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        contenedor.setBackground(new Color(245, 247, 250));

        JLabel titulo = new JLabel("Gestion de Parqueo Publico");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JPanel superior = new JPanel(new BorderLayout(10, 10));
        superior.setOpaque(false);
        superior.add(titulo, BorderLayout.NORTH);
        superior.add(crearPanelFormulario(), BorderLayout.CENTER);
        superior.add(crearPanelEstado(), BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, crearPanelTablaActivos(), crearPanelTablaHistorial());
        splitPane.setResizeWeight(0.45);
        splitPane.setBorder(null);

        contenedor.add(superior, BorderLayout.NORTH);
        contenedor.add(splitPane, BorderLayout.CENTER);
        setContentPane(contenedor);
    }

    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 214, 223)),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Placa"), gbc);

        gbc.gridx = 1;
        panel.add(txtPlaca, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("Tipo"), gbc);

        gbc.gridx = 3;
        panel.add(cmbTipo, gbc);

        JButton btnRegistrarIngreso = new JButton("Registrar ingreso");
        btnRegistrarIngreso.addActionListener(e -> registrarIngreso());
        gbc.gridx = 4;
        panel.add(btnRegistrarIngreso, gbc);

        JButton btnRegistrarSalida = new JButton("Registrar salida");
        btnRegistrarSalida.addActionListener(e -> registrarSalidaSeleccionada());
        gbc.gridx = 5;
        panel.add(btnRegistrarSalida, gbc);

        JButton btnEliminarHistorial = new JButton("Limpiar historial");
        btnEliminarHistorial.addActionListener(e -> limpiarHistorial());
        gbc.gridx = 6;
        panel.add(btnEliminarHistorial, gbc);

        return panel;
    }

    private JPanel crearPanelEstado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblResumen.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblEstado, BorderLayout.WEST);
        panel.add(lblResumen, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelTablaActivos() {
        configurarTabla(tablaActivos);
        tablaActivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        panel.add(new JLabel("Vehiculos actualmente en el parqueo"), BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaActivos), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelTablaHistorial() {
        configurarTabla(tablaHistorial);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);
        panel.add(new JLabel("Historial de ingresos y salidas"), BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        return panel;
    }

    private void configurarTabla(JTable tabla) {
        tabla.setRowHeight(24);
        tabla.setFillsViewportHeight(true);
        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(219, 228, 239));
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void registrarIngreso() {
        ResultadoOperacion resultado = parqueoService.registrarIngreso(txtPlaca.getText(), (String) cmbTipo.getSelectedItem());
        mostrarResultado(resultado);
        if (resultado.isExitoso()) {
            txtPlaca.setText("");
            cmbTipo.setSelectedIndex(0);
            cargarTablas();
        }
    }

    private void registrarSalidaSeleccionada() {
        int filaSeleccionada = tablaActivos.getSelectedRow();
        Registro registro = modeloActivos.getRegistroEn(filaSeleccionada);
        String placa = registro == null ? "" : registro.getVehiculo().getPlaca();
        ResultadoOperacion resultado = parqueoService.registrarSalida(placa);
        mostrarResultado(resultado);
        if (resultado.isExitoso()) {
            cargarTablas();
        }
    }

    private void limpiarHistorial() {
        ResultadoOperacion resultado = parqueoService.eliminarHistorial();
        mostrarResultado(resultado);
        if (resultado.isExitoso()) {
            cargarTablas();
        }
    }

    private void cargarTablas() {
        List<Registro> activos = parqueoService.obtenerRegistrosActivos();
        List<Registro> historial = parqueoService.obtenerHistorial();
        modeloActivos.setRegistros(activos);
        modeloHistorial.setRegistros(historial);
        lblResumen.setText("Activos: " + activos.size() + " | Historial: " + historial.size() + " | Tarifa: CRC 500/h");
    }

    private void mostrarResultado(ResultadoOperacion resultado) {
        lblEstado.setText(resultado.getMensaje());
        lblEstado.setForeground(resultado.isExitoso() ? new Color(0, 120, 70) : new Color(170, 40, 40));
    }

    public static void mostrar() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            // Si falla el Look and Feel del sistema, Swing usa el predeterminado.
        }
        SwingUtilities.invokeLater(() -> new PrincipalFrame().setVisible(true));
    }
}
