package presentacion;

/**
 *
 * @author nero2
 */

import modelo.Producto;
import negocio.ProductoNegocio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class EstadisticaPanel extends JPanel {

    private final ProductoNegocio negocio;

    private JLabel lblTotalProductos;
    private JLabel lblDisponibles;
    private JLabel lblNoDisponibles;
    private JLabel lblUnidades;
    private JLabel lblMayorPrecio;
    private JLabel lblMenorPrecio;
    private JLabel lblValorTotal;

    private DefaultTableModel modeloCategorias;
    private JTable tablaCategorias;

    public EstadisticaPanel(ProductoNegocio negocio) {
        this.negocio = negocio;
        construirInterfaz();
        actualizar();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelResumen = new JPanel(new GridLayout(0, 1, 5, 5));
        panelResumen.setBorder(BorderFactory.createTitledBorder("Resumen general"));

        lblTotalProductos = new JLabel();
        lblDisponibles = new JLabel();
        lblNoDisponibles = new JLabel();
        lblUnidades = new JLabel();
        lblMayorPrecio = new JLabel();
        lblMenorPrecio = new JLabel();
        lblValorTotal = new JLabel();

        panelResumen.add(lblTotalProductos);
        panelResumen.add(lblDisponibles);
        panelResumen.add(lblNoDisponibles);
        panelResumen.add(lblUnidades);
        panelResumen.add(lblMayorPrecio);
        panelResumen.add(lblMenorPrecio);
        panelResumen.add(lblValorTotal);

        modeloCategorias = new DefaultTableModel(new Object[]{"Categoría", "Cantidad de productos"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaCategorias = new JTable(modeloCategorias);
        JScrollPane scrollCategorias = new JScrollPane(tablaCategorias);
        scrollCategorias.setBorder(BorderFactory.createTitledBorder("Productos por categoría"));

        add(panelResumen, BorderLayout.NORTH);
        add(scrollCategorias, BorderLayout.CENTER);

        JButton btnActualizar = new JButton("Actualizar estadísticas");
        btnActualizar.addActionListener(e -> actualizar());
        add(btnActualizar, BorderLayout.SOUTH);
    }

    public void actualizar() {
        lblTotalProductos.setText("Cantidad total de productos: " + negocio.totalProductos());
        lblDisponibles.setText("Cantidad de productos disponibles: " + negocio.totalDisponibles());
        lblNoDisponibles.setText("Cantidad de productos no disponibles: " + negocio.totalNoDisponibles());
        lblUnidades.setText("Cantidad de unidades almacenadas: " + negocio.totalUnidadesAlmacenadas());

        Producto mayor = negocio.productoMayorPrecio();
        Producto menor = negocio.productoMenorPrecio();
        lblMayorPrecio.setText("Producto con mayor precio: " +
                (mayor != null ? mayor.getNombre() + " (₡" + mayor.getPrecio() + ")" : "N/A"));
        lblMenorPrecio.setText("Producto con menor precio: " +
                (menor != null ? menor.getNombre() + " (₡" + menor.getPrecio() + ")" : "N/A"));

        lblValorTotal.setText(String.format("Valor total del inventario: ₡%.2f", negocio.valorTotalInventario()));

        modeloCategorias.setRowCount(0);
        Map<String, Integer> conteo = negocio.productosPorCategoria();
        for (Map.Entry<String, Integer> entry : conteo.entrySet()) {
            modeloCategorias.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
    }
}
