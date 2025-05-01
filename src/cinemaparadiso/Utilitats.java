package cinemaparadiso;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * @author Andreu Niso Sevilla
 */

public class Utilitats {
    // Posiciona el programa al centre de la pantalla
    public static void centrarPantalla(Window finestra) {
        Dimension tamanyPantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (tamanyPantalla.width - finestra.getWidth()) / 2;
        int y = (tamanyPantalla.height - finestra.getHeight()) / 2;
        finestra.setLocation(x, y);
    }
}
