package repositorio;

/**
 *
 * @author nero2
 */

import modelo.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductoRepositorio {

    private List<Producto> productos;
    private Set<String> codigos;
    private Map<String, Integer> productosPorCategoria;
    private int siguienteId;

    public ProductoRepositorio() {
        this.productos = new ArrayList<>();
        this.codigos = new HashSet<>();
        this.productosPorCategoria = new HashMap<>();
        this.siguienteId = 1;
    }

    public Producto agregar(Producto producto) {
        producto.setId(siguienteId++);
        productos.add(producto);
        codigos.add(producto.getCodigo().toUpperCase());
        productosPorCategoria.merge(producto.getCategoria(), 1, Integer::sum);
        return producto;
    }

    public List<Producto> listar() {
        return new ArrayList<>(productos);
    }

    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public boolean editar(Producto actualizado) {
        for (int i = 0; i < productos.size(); i++) {
            Producto actual = productos.get(i);
            if (actual.getId() == actualizado.getId()) {
                if (!actual.getCategoria().equalsIgnoreCase(actualizado.getCategoria())) {
                    decrementarCategoria(actual.getCategoria());
                    productosPorCategoria.merge(actualizado.getCategoria(), 1, Integer::sum);
                }
                if (!actual.getCodigo().equalsIgnoreCase(actualizado.getCodigo())) {
                    codigos.remove(actual.getCodigo().toUpperCase());
                    codigos.add(actualizado.getCodigo().toUpperCase());
                }
                productos.set(i, actualizado);
                return true;
            }
        }
        return false;
    }

    public boolean eliminar(int id) {
        Iterator<Producto> it = productos.iterator();
        while (it.hasNext()) {
            Producto p = it.next();
            if (p.getId() == id) {
                it.remove();
                codigos.remove(p.getCodigo().toUpperCase());
                decrementarCategoria(p.getCategoria());
                return true;
            }
        }
        return false;
    }

    public boolean existeCodigo(String codigo) {
        return codigos.contains(codigo.toUpperCase());
    }

    public Map<String, Integer> getConteoPorCategoria() {
        return new HashMap<>(productosPorCategoria);
    }

    private void decrementarCategoria(String categoria) {
        Integer conteo = productosPorCategoria.get(categoria);
        if (conteo != null) {
            if (conteo <= 1) {
                productosPorCategoria.remove(categoria);
            } else {
                productosPorCategoria.put(categoria, conteo - 1);
            }
        }
    }
}
