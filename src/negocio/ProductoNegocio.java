package negocio;
/**
 *
 * @author nero2
 */
import excepciones.DatoInvalidoException;
import excepciones.ProductoDuplicadoException;
import modelo.Producto;
import repositorio.ProductoRepositorio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ProductoNegocio {

    private final ProductoRepositorio repositorio;
    private final Stack<String> historial;

    public ProductoNegocio() {
        this.repositorio = new ProductoRepositorio();
        this.historial = new Stack<>();
    }

    //  Operaciones CRUD 

    public Producto agregar(Producto producto) throws DatoInvalidoException, ProductoDuplicadoException {
        validar(producto);
        if (repositorio.existeCodigo(producto.getCodigo())) {
            throw new ProductoDuplicadoException(
                    "Ya existe un producto registrado con el código: " + producto.getCodigo());
        }
        Producto guardado = repositorio.agregar(producto);
        historial.push("Producto registrado: " + guardado.getNombre());
        return guardado;
    }

    public void editar(Producto producto) throws DatoInvalidoException, ProductoDuplicadoException {
        validar(producto);
        Producto existente = repositorio.buscarPorCodigo(producto.getCodigo());
        if (existente != null && existente.getId() != producto.getId()) {
            throw new ProductoDuplicadoException(
                    "Ya existe otro producto con el código: " + producto.getCodigo());
        }
        boolean actualizado = repositorio.editar(producto);
        if (!actualizado) {
            throw new DatoInvalidoException("No se encontró el producto a editar (ID " + producto.getId() + ").");
        }
        historial.push("Producto editado: " + producto.getNombre());
    }

    public void eliminar(int id, String nombreReferencia) throws DatoInvalidoException {
        boolean eliminado = repositorio.eliminar(id);
        if (!eliminado) {
            throw new DatoInvalidoException("No se encontró el producto a eliminar.");
        }
        historial.push("Producto eliminado: " + nombreReferencia);
    }

    public List<Producto> listar() {
        return repositorio.listar();
    }

    //  Búsqueda y filtrado 

    public List<Producto> buscar(String texto) {
        String filtro = texto == null ? "" : texto.toLowerCase().trim();
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : repositorio.listar()) {
            if (p.getNombre().toLowerCase().contains(filtro) || p.getCodigo().toLowerCase().contains(filtro)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Producto> filtrarPorCategoria(String categoria) {
        if (categoria == null || categoria.equalsIgnoreCase("Todas")) {
            return repositorio.listar();
        }
        List<Producto> resultado = new ArrayList<>();
        for (Producto p : repositorio.listar()) {
            if (p.getCategoria().equalsIgnoreCase(categoria)) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // Ordenamientos 

    public List<Producto> ordenarPorNombre(List<Producto> lista) {
        List<Producto> copia = new ArrayList<>(lista);
        Collections.sort(copia, Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER));
        return copia;
    }

    public List<Producto> ordenarPorPrecio(List<Producto> lista) {
        List<Producto> copia = new ArrayList<>(lista);
        Collections.sort(copia, Comparator.comparingDouble(Producto::getPrecio));
        return copia;
    }

    public List<Producto> ordenarPorCantidad(List<Producto> lista) {
        List<Producto> copia = new ArrayList<>(lista);
        Collections.sort(copia, Comparator.comparingInt(Producto::getCantidad));
        return copia;
    }

    // - Historial (Stack<String>) 

    public void registrarExportacion() {
        historial.push("Inventario exportado correctamente");
    }

    public void registrarErrorExportacion(String motivo) {
        historial.push("Error al exportar inventario: " + motivo);
    }

    public List<String> obtenerHistorial() {
        List<String> copia = new ArrayList<>(historial);
        Collections.reverse(copia);
        return copia;
    }

    //  Estadísticas

    public int totalProductos() {
        return repositorio.listar().size();
    }

    public int totalDisponibles() {
        int contador = 0;
        for (Producto p : repositorio.listar()) {
            if (p.isDisponible()) contador++;
        }
        return contador;
    }

    public int totalNoDisponibles() {
        return totalProductos() - totalDisponibles();
    }

    public int totalUnidadesAlmacenadas() {
        int total = 0;
        for (Producto p : repositorio.listar()) {
            total += p.getCantidad();
        }
        return total;
    }

    public Producto productoMayorPrecio() {
        Producto mayor = null;
        for (Producto p : repositorio.listar()) {
            if (mayor == null || p.getPrecio() > mayor.getPrecio()) {
                mayor = p;
            }
        }
        return mayor;
    }

    public Producto productoMenorPrecio() {
        Producto menor = null;
        for (Producto p : repositorio.listar()) {
            if (menor == null || p.getPrecio() < menor.getPrecio()) {
                menor = p;
            }
        }
        return menor;
    }

    public Map<String, Integer> productosPorCategoria() {
        return repositorio.getConteoPorCategoria();
    }

    public double valorTotalInventario() {
        double total = 0;
        for (Producto p : repositorio.listar()) {
            total += p.getCantidad() * p.getPrecio();
        }
        return total;
    }

    //  Validaciones 
    private void validar(Producto p) throws DatoInvalidoException {
        if (p.getCodigo() == null || p.getCodigo().trim().isEmpty()) {
            throw new DatoInvalidoException("El código es obligatorio.");
        }
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            throw new DatoInvalidoException("El nombre es obligatorio.");
        }
        if (p.getNombre().trim().length() < 3) {
            throw new DatoInvalidoException("El nombre debe tener mínimo tres caracteres.");
        }
        if (p.getCategoria() == null || p.getCategoria().trim().isEmpty()) {
            throw new DatoInvalidoException("La categoría es obligatoria.");
        }
        if (p.getCantidad() < 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor o igual que cero.");
        }
        if (p.getPrecio() <= 0) {
            throw new DatoInvalidoException("El precio debe ser mayor que cero.");
        }
    }
}