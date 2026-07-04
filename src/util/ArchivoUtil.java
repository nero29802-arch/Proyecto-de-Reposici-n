package util;

import excepciones.ArchivoException;
import modelo.Producto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author nero2
 */
public class ArchivoUtil {
    
    public static void exportarCSV(File archivo, List<Producto> productos) throws ArchivoException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo))) {
            writer.write("ID,Código,Nombre,Categoría,Cantidad,Precio,Disponible");
            writer.newLine();
            for (Producto p : productos) {
                writer.write(p.getId() + "," +
                        p.getCodigo() + "," +
                        p.getNombre() + "," +
                        p.getCategoria() + "," +
                        p.getCantidad() + "," +
                        p.getPrecio() + "," +
                        p.isDisponible());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ArchivoException("No se pudo exportar el inventario al archivo: " + archivo.getName(), e);
        }
    }
}
