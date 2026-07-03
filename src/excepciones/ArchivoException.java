package excepciones;

/**
 *
 * @author nero2
 */
public class ArchivoException  extends Exception{
    
    public ArchivoException(String mensaje) {
        super(mensaje);
    }
    
    public ArchivoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
}
