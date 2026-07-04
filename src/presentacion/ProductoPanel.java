package presentacion;

/**
 *
 * @author nero2
 */

import excepciones.DatoInvalidoException;
import excepciones.ProductoDuplicadoException;
import modelo.Producto;
import negocio.ProductoNegocio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ProductoPanel extends JPanel {

    private final ProductoNegocio negocio;
    private final MainFrame mainFrame;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JComboBox<String> comboCategoria;
    private JTextField txtCantidad;
    private JTextField txtPrecio;
    private JCheckBox chkDisponible;
    private JRadioButton radioNacional;
    private JRadioButton radioImportado;
    private JTextArea txtDescripcion;

    private JButton btnGuardar;
    private JButton btnEditar;
    private JButton btnLimpiar;

    private Integer idEnEdicion = null; // null => modo "nuevo"

    public ProductoPanel(ProductoNegocio negocio, MainFrame mainFrame) {
        this.negocio = negocio;
        this.mainFrame = mainFrame;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        txtCodigo = new JTextField(15);
        agregarFila(formulario, gbc, fila++, "Código:", txtCodigo);

        txtNombre = new JTextField(15);
        agregarFila(formulario, gbc, fila++, "Nombre:", txtNombre);

        comboCategoria = new JComboBox<>(new String[]{"Electrónica", "Oficina", "Hogar", "Alimentos", "Otros"});
        agregarFila(formulario, gbc, fila++, "Categoría:", comboCategoria);

        txtCantidad = new JTextField(15);
        agregarFila(formulario, gbc, fila++, "Cantidad:", txtCantidad);

        txtPrecio = new JTextField(15);
        agregarFila(formulario, gbc, fila++, "Precio:", txtPrecio);

        chkDisponible = new JCheckBox("Disponible");
        gbc.gridx = 1;
        gbc.gridy = fila++;
        formulario.add(chkDisponible, gbc);

        radioNacional = new JRadioButton("Nacional", true);
        radioImportado = new JRadioButton("Importado");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(radioNacional);
        grupoTipo.add(radioImportado);
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelTipo.add(new JLabel("Tipo de producto:"));
        panelTipo.add(radioNacional);
        panelTipo.add(radioImportado);
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 2;
        formulario.add(panelTipo, gbc);
        gbc.gridwidth = 1;

        txtDescripcion = new JTextArea(4, 15);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        gbc.gridx = 0;
        gbc.gridy = fila;
        formulario.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        formulario.add(scrollDescripcion, gbc);
        fila++;

        add(formulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnGuardar = new JButton("Guardar");
        btnEditar = new JButton("Editar");
        btnLimpiar = new JButton("Limpiar campos");
        btnEditar.setEnabled(false);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnLimpiar);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(this::onGuardar);
        btnEditar.addActionListener(this::onEditar);
        btnLimpiar.addActionListener(e -> limpiarCampos());
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent componente) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    private void onGuardar(ActionEvent e) {
        try {
            Producto producto = capturarProducto();
            negocio.agregar(producto);
            JOptionPane.showMessageDialog(this, "Producto guardado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            mainFrame.refrescarTodo();
        } catch (DatoInvalidoException | ProductoDuplicadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onEditar(ActionEvent e) {
        if (idEnEdicion == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un producto desde la pestaña 'Lista de productos' para editarlo.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Producto producto = capturarProducto();
            producto.setId(idEnEdicion);
            negocio.editar(producto);
            JOptionPane.showMessageDialog(this, "Producto editado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            mainFrame.refrescarTodo();
        } catch (DatoInvalidoException | ProductoDuplicadoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Producto capturarProducto() throws DatoInvalidoException {
        int cantidad;
        double precio;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException ex) {
            throw new DatoInvalidoException("La cantidad debe ser un valor numérico entero.", ex);
        }
        try {
            precio = Double.parseDouble(txtPrecio.getText().trim());
        } catch (NumberFormatException ex) {
            throw new DatoInvalidoException("El precio debe ser un valor numérico.", ex);
        }

        String tipo = radioNacional.isSelected() ? "Nacional" : "Importado";

        return new Producto(
                0,
                txtCodigo.getText().trim(),
                txtNombre.getText().trim(),
                (String) comboCategoria.getSelectedItem(),
                cantidad,
                precio,
                chkDisponible.isSelected(),
                txtDescripcion.getText().trim(),
                tipo
        );
    }

    public void cargarParaEdicion(Producto p) {
        idEnEdicion = p.getId();
        txtCodigo.setText(p.getCodigo());
        txtNombre.setText(p.getNombre());
        comboCategoria.setSelectedItem(p.getCategoria());
        txtCantidad.setText(String.valueOf(p.getCantidad()));
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        chkDisponible.setSelected(p.isDisponible());
        if ("Importado".equalsIgnoreCase(p.getTipo())) {
            radioImportado.setSelected(true);
        } else {
            radioNacional.setSelected(true);
        }
        txtDescripcion.setText(p.getDescripcion());
        btnEditar.setEnabled(true);
    }

    private void limpiarCampos() {
        idEnEdicion = null;
        txtCodigo.setText("");
        txtNombre.setText("");
        comboCategoria.setSelectedIndex(0);
        txtCantidad.setText("");
        txtPrecio.setText("");
        chkDisponible.setSelected(false);
        radioNacional.setSelected(true);
        txtDescripcion.setText("");
        btnEditar.setEnabled(false);
    }
}
