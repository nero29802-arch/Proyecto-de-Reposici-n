package excepciones;

/**
 *
 * @author nero2
 */
public class ProductoDuplicadoException extends Exception {
    
    public ProductoDuplicadoException( String mensaje) {
        super(mensaje);
    }
    
    public ProductoDuplicadoException(String mensaje,  Throwable causa) {
        super(mensaje, causa);
    }
    
}
