package presentacion;

import javax.swing.*;
import java.awt.*;

public class EstiloUI {

    // Paleta de Colores //

    public static final Color COLOR_PRIMARIO = new Color(0x2E7D32);
    public static final Color COLOR_PRIMARIO_CLARO = new Color(0x66BB6A);
    public static final Color COLOR_ACENTO = new Color(0xFF8F00);
    public static final Color COLOR_FONDO = new Color(0xF5F7F5);
    public static final Color COLOR_FONDO_ALT = new Color(0xE8F0E9);
    public static final Color COLOR_TEXTO = new Color(0x212121);
    public static final Color COLOR_PELIGRO = new Color(0xC62828);


    // Fuentes //

    public static final Font FUENTE_TITULO = new Font("Segoe IU", Font.BOLD, 16);
    public static final Font FUENTE_NORMAL = new Font("Segoe IU", Font.PLAIN, 13);
    public static final Font FUENTE_BOTON = new Font("Segoe IU", Font.BOLD, 12);

    /** Aplica el Look and Feel Nimbus con la paleta de colores del sistema. */

    public static void aplicarLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel((info.getClassName()));
                    break;
                }
            }
            UIManager.put("control", COLOR_FONDO);
            UIManager.put("info", Color.WHITE);
            UIManager.put("nimbusBase", COLOR_PRIMARIO);
            UIManager.put("nimbusBlueGrey", COLOR_FONDO_ALT);
            UIManager.put("nimbusFocus", COLOR_ACENTO);
            UIManager.put("nimbusSelectionBackground", COLOR_PRIMARIO_CLARO);
            UIManager.put("text", COLOR_TEXTO);
            UIManager.put("Table.alternateRowColor", COLOR_FONDO_ALT);
            UIManager.put("defaultFont", FUENTE_NORMAL);
        } catch (Exception e) {
            // Si Nimbus no está disponible, la aplicación sigue con el L&F por defecto.
        }
    }

    /** Da estilo a un botón "principal" (ac ciones positivas: Guardar, Nuevo, etc.). */

    public static void estilizarBotonPrimario(JButton boton) {
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(COLOR_PRIMARIO);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));

    }

    /** Da estilo a un botón "secundario" (Editar, Limpiar, Ordenar, etc.). */

    public static void estilizarBotonSecundario(JButton boton) {
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(COLOR_FONDO_ALT);
        boton.setForeground(COLOR_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8,16,8,16));
    }

    /** Da estilo a un botón de "peligro" (Eliminar). */

    public static void estilizarBotonPeligro(JButton boton) {
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(COLOR_FONDO_ALT);
        boton.setForeground(COLOR_TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
    }

    /** Da estilo a un JLabel usado como título de sección. */

    public static void estilizarTitulo(JLabel label) {
        label.setFont(FUENTE_TITULO);
        label.setForeground(COLOR_PRIMARIO);
    }

}