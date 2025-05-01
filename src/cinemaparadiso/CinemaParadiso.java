package cinemaparadiso;

/**
 * @author Andreu Niso Sevilla
 */

//Classe principal que inicia l'aplicació.
public class CinemaParadiso {
    public static void main(String[] args) {
        Menu mainWin = new Menu();
        mainWin.setSize(400, 330);
        
        Utilitats.centrarPantalla(mainWin);
    } 
}