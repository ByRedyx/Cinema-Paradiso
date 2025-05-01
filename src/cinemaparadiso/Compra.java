package cinemaparadiso;

import java.io.Serializable;

public class Compra implements Serializable {
    private int[][] butaques;
    private String nom;
    private String dni;

    public Compra() {
    }
    
    public Compra(int[][] butaques, String nom, String dni) {
        this.butaques = butaques;
        this.nom = nom;
        this.dni = dni;
    }

    public int[][] getButaques() {
        return butaques;
    }

    public void setButaques(int[][] butaques) {
        this.butaques = butaques;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDNI() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
    
}
