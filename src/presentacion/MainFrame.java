package presentacion;

import excepciones.ArchivoException;
import excepciones.DatoInvalidoException;
import modelo.Producto;
import negocio.ProductoNegocio;
import util.ArchivoUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {

    private final ProductoNegocio negocio;

    private ProductoPanel productoPanel;
    private EstadisticaPanel estadisticaPanel;
    private JTabbedPane tabbedPane;

    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JComboBox<String> comboFiltroCategoria;

    private static final String[] COLUMNAS =
            {"ID", "Código", "Nombre", "Categoría", "Cantidad", "Precio", "Disponible"};

    public MainFrame() {
        this.negocio = new ProductoNegocio();

        setTitle("Sistema de Control de Inventario");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setJMenuBar(crearMenuBar());
        add(crearToolBar(), BorderLayout.NORTH);
        add(crearTabbedPane(), BorderLayout.CENTER);
    }

  
    // JMenuBar
  
    private JMenuBar crearMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemNuevo = new JMenuItem("Nuevo producto");
        JMenuItem itemExportar = new JMenuItem("Exportar inventario");
        JMenuItem itemSalir = new JMenuItem("Salir");

        itemNuevo.addActionListener(e -> accionNuevo());
        itemExportar.addActionListener(e -> accionExportar());
        itemSalir.addActionListener(e -> System.exit(0));

        menuArchivo.add(itemNuevo);
        menuArchivo.add(itemExportar);
        menuArchivo.addSeparator();
        menuArchivo.add(itemSalir);

        JMenu menuHerramientas = new JMenu("Herramientas");
        JMenuItem itemOrdenar = new JMenuItem("Ordenar productos");
        JMenuItem itemEstadisticas = new JMenuItem("Ver estadísticas");
        JMenuItem itemHistorial = new JMenuItem("Ver historial");

        itemOrdenar.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        itemEstadisticas.addActionListener(e -> {
            estadisticaPanel.actualizar();
            tabbedPane.setSelectedIndex(2);
        });
        itemHistorial.addActionListener(e -> accionVerHistorial());

        menuHerramientas.add(itemOrdenar);
        menuHerramientas.add(itemEstadisticas);
        menuHerramientas.add(itemHistorial);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca del sistema");
        itemAcerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Sistema de Control de Inventario\niNGENIERO ORTIZ T\nProyecto de Reposición",
                "Acerca del sistema", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemAcerca);

        menuBar.add(menuArchivo);
        menuBar.add(menuHerramientas);
        menuBar.add(menuAyuda);
        return menuBar;
    }

 
    // JToolBar
   
    private JToolBar crearToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton btnNuevo = new JButton("Nuevo");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnOrdenar = new JButton("Ordenar");
        JButton btnExportar = new JButton("Exportar");

        btnNuevo.addActionListener(e -> accionNuevo());
        btnGuardar.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        btnEditar.addActionListener(e -> editarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnOrdenar.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        btnExportar.addActionListener(e -> accionExportar());

        toolBar.add(btnNuevo);
        toolBar.add(btnGuardar);
        toolBar.add(btnEditar);
        toolBar.add(btnEliminar);
        toolBar.add(btnOrdenar);
        toolBar.add(btnExportar);
        return toolBar;
    }


    // JTabbedPane
  
    private JTabbedPane crearTabbedPane() {
        tabbedPane = new JTabbedPane();

        productoPanel = new ProductoPanel(negocio, this);
        estadisticaPanel = new EstadisticaPanel(negocio);

        tabbedPane.addTab("Registro de productos", productoPanel);
        tabbedPane.addTab("Lista de productos", crearPanelLista());
        tabbedPane.addTab("Estadísticas", estadisticaPanel);

        return tabbedPane;
    }

    private JPanel crearPanelLista() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));

        txtBuscar = new JTextField(15);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> refrescarTabla(negocio.buscar(txtBuscar.getText())));

        comboFiltroCategoria = new JComboBox<>(
                new String[]{"Todas", "Electrónica", "Oficina", "Hogar", "Alimentos", "Otros"});
        comboFiltroCategoria.addActionListener(e ->
                refrescarTabla(negocio.filtrarPorCategoria((String) comboFiltroCategoria.getSelectedItem())));

        JButton btnOrdenNombre = new JButton("Ordenar por nombre");
        JButton btnOrdenPrecio = new JButton("Ordenar por precio");
        JButton btnOrdenCantidad = new JButton("Ordenar por cantidad");

        btnOrdenNombre.addActionListener(e -> refrescarTabla(negocio.ordenarPorNombre(negocio.listar())));
        btnOrdenPrecio.addActionListener(e -> refrescarTabla(negocio.ordenarPorPrecio(negocio.listar())));
        btnOrdenCantidad.addActionListener(e -> refrescarTabla(negocio.ordenarPorCantidad(negocio.listar())));

        panelSuperior.add(new JLabel("Buscar (nombre/código):"));
        panelSuperior.add(txtBuscar);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(new JLabel("Categoría:"));
        panelSuperior.add(comboFiltroCategoria);
        panelSuperior.add(btnOrdenNombre);
        panelSuperior.add(btnOrdenPrecio);
        panelSuperior.add(btnOrdenCantidad);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnEditarSel = new JButton("Editar producto seleccionado");
        JButton btnEliminarSel = new JButton("Eliminar producto seleccionado");
        btnEditarSel.addActionListener(e -> editarSeleccionado());
        btnEliminarSel.addActionListener(e -> eliminarSeleccionado());
        panelInferior.add(btnEditarSel);
        panelInferior.add(btnEliminarSel);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(scrollTabla, BorderLayout.CENTER);
        panel.add(panelInferior, BorderLayout.SOUTH);

        refrescarTabla(negocio.listar());
        return panel;
    }

    
    // Acciones

    private void accionNuevo() {
        tabbedPane.setSelectedIndex(0);
    }

    private void accionExportar() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar inventario");
        fileChooser.setSelectedFile(new File("inventario.csv"));
        int seleccion = fileChooser.showSaveDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                ArchivoUtil.exportarCSV(archivo, negocio.listar());
                negocio.registrarExportacion();
                JOptionPane.showMessageDialog(this,
                        "Inventario exportado correctamente en:\n" + archivo.getAbsolutePath(),
                        "Exportación exitosa", JOptionPane.INFORMATION_MESSAGE);
            } catch (ArchivoException ex) {
                negocio.registrarErrorExportacion(ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de exportación",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void accionVerHistorial() {
        JTextArea areaHistorial = new JTextArea(15, 40);
        areaHistorial.setEditable(false);
        List<String> historial = negocio.obtenerHistorial();
        if (historial.isEmpty()) {
            areaHistorial.setText("Aún no se han registrado acciones.");
        } else {
            for (String accion : historial) {
                areaHistorial.append("- " + accion + "\n");
            }
        }
        JScrollPane scroll = new JScrollPane(areaHistorial);

        JDialog dialogo = new JDialog(this, "Historial de acciones", true);
        dialogo.getContentPane().add(scroll);
        dialogo.pack();
        dialogo.setLocationRelativeTo(this);
        dialogo.setVisible(true);
    }

    private void editarSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        Producto producto = buscarPorIdEnListaActual(id);
        if (producto != null) {
            productoPanel.cargarParaEdicion(producto);
            tabbedPane.setSelectedIndex(0);
        }
    }

    private void eliminarSeleccionado() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        String nombre = (String) modeloTabla.getValueAt(fila, 2);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de que desea eliminar el producto \"" + nombre + "\"?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                negocio.eliminar(id, nombre);
                refrescarTodo();
                JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (DatoInvalidoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Producto buscarPorIdEnListaActual(int id) {
        for (Producto p : negocio.listar()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

   
    // Refresco de vistas
   
    private void refrescarTabla(List<Producto> productos) {
        modeloTabla.setRowCount(0);
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                    p.getId(), p.getCodigo(), p.getNombre(), p.getCategoria(),
                    p.getCantidad(), p.getPrecio(), p.isDisponible() ? "Sí" : "No"
            });
        }
    }

    public void refrescarTodo() {
        refrescarTabla(negocio.listar());
        estadisticaPanel.actualizar();
    }


    // Main
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}